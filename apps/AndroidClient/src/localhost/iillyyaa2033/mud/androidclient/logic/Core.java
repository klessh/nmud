package localhost.iillyyaa2033.mud.androidclient.logic;

import android.preference.PreferenceManager;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Set;
import java.util.StringTokenizer;
import localhost.iillyyaa2033.mud.androidclient.activity.MainActivity;
import localhost.iillyyaa2033.mud.androidclient.logic.model.DescribedWorldObject;
import localhost.iillyyaa2033.mud.androidclient.logic.model.WorldObject;
import org.keplerproject.luajava.LuaState;
import org.keplerproject.luajava.LuaStateFactory;

public class Core extends Thread {

	public static final int LEVEL_CLIENT = 0, 
							LEVEL_DEBUG = 1, 
							LEVEL_DEBUG_IMPORTER = 2, 
							LEVEL_DEBUG_DESCR = 3,
							LEVEL_DEBUG_SCRIPTS = 4;

	public MainActivity activity;
	public Importer importer;
	public Database db;
	public Dictionary dict;
	private LuaState L;

	private HashMap<String, String> scriptsmap;
	private ArrayList<String> scriptsnames;

	public boolean debug = true, debug_importer = false, debug_descr = false, debug_scripts = false;
	private boolean canScripts = false;
	private boolean isScriptRunning = false;
	private boolean updRequested = false;

	public String cmdstr;

	ArrayList<String> report;

	int pos_x = 0, pos_y = 0;

	public Core(MainActivity activity) {
		this.activity = activity;
		debug = PreferenceManager.getDefaultSharedPreferences(activity).getBoolean("FLAG_DEBUG", true);
		debug_importer = PreferenceManager.getDefaultSharedPreferences(activity).getBoolean("FLAG_DEBUG_IMPORTER", false);
		debug_descr = PreferenceManager.getDefaultSharedPreferences(activity).getBoolean("FLAG_DEBUG_DESCR", false);
		debug_scripts = PreferenceManager.getDefaultSharedPreferences(activity).getBoolean("FLAG_DEBUG_SCRIPTS", false);
		db = new Database(this);
		importer = new Importer(this);
		dict = new Dictionary(this);
		report = new ArrayList<String>();
	}

	@Override
	public void run() {

		try { sleep(1000);
		} catch (InterruptedException e) {}

		update();

		if (scriptsmap == null) {
			canScripts = false;
		} else {
			try{
			L = LuaStateFactory.newLuaState();
			L.openLibs();

			L.pushJavaObject(Core.this);	
			L.setGlobal("client");
			L.pushJavaObject(Core.this);
			L.setGlobal("server");

			canScripts = true;
			} catch(Throwable t){
				canScripts = false;
				send(t.toString());
			}
		}

		doFunction("onServerStarted", "on", null);

		doFunction("onClientConnected", "on", null);

		String cmd = "";
		while (!(cmd = read()).equals("") && !Thread.currentThread().isInterrupted()) {
			if (updRequested) update();
			comms(cmd);
		}

		if (interrupted()) {
			send("Finish.");
			close();
		}
	}

	public void requestUpdate() {
		updRequested = true;
	}

	public synchronized void update() {
		debug = PreferenceManager.getDefaultSharedPreferences(activity).getBoolean("FLAG_DEBUG", true);
		debug_importer = PreferenceManager.getDefaultSharedPreferences(activity).getBoolean("FLAG_DEBUG_IMPORTER", false);
		debug_descr = PreferenceManager.getDefaultSharedPreferences(activity).getBoolean("FLAG_DEBUG_DESCR", false);
		debug_scripts = PreferenceManager.getDefaultSharedPreferences(activity).getBoolean("FLAG_DEBUG_SCRIPTS", false);
		
		db.update();

		if (!(new File(db.datapath)).exists()) {
			// TODO: data must be extracted before db update
			importer.extractContent(activity);
			importer.unzip(activity.getCacheDir() + "/content-base.zip", db.datapath);
		}

		scriptsmap = importer.importChatScripts();
		scriptsnames = new ArrayList<String>();

		if (scriptsmap != null) {
			Set<String> set = scriptsmap.keySet();
			for (String s : set) {
				scriptsnames.add(s);
			}

			Collections.sort(scriptsnames);
		}

		dict.update();
		updRequested = false;
	}


	private String[] haventcmd = {"Команды не существует.","Нет такой команды.","Похоже, такой команды нет.",
		"Во славу Ктулху!", "Протеряли все полимеры.","Команду съели.","Ничего не случилось","Команда отсутствует."};

	private synchronized void comms(String message) {
		message = message.toLowerCase();

		StringTokenizer token = new StringTokenizer(message);
		int size = token.countTokens();

		if (size == 0) return;

		String cmd = token.nextToken();
		String[] args = new String[size - 1];

		for (int i = 0; i < size - 1; i++) {
			args[i] = token.nextToken();
		}

		if (scriptsmap != null) {
			if (scriptsmap.containsKey(cmd)) {
				send(doFunction(cmd, "chat", args));
			} else {
				String fullCmd = getFullCommand(cmd);
				if (fullCmd == null) {
					report.add(message);
					int random = (int)Math.floor(Math.random() * haventcmd.length);
					send(haventcmd[random]);
				} else {
					send(doFunction(fullCmd, "chat", args));
				}
			}
		} else {
			send("Скрипты не загружены.");
		}
	}

	public String getFullCommand(String shortCmd) {
		for (String s : scriptsnames) {
			if (s.startsWith(shortCmd)) {
				return s;
			}
		}
		return null;
	}

	public synchronized String read() {
		while (cmdstr == null) {
			try {
				sleep(100);
			} catch (InterruptedException e) {

			}
		}
		String cmd = cmdstr + "";
		cmdstr = null;
		return cmd;
	}

	public synchronized boolean send(int level, String line) {
		switch (level) {
			case LEVEL_CLIENT:
				return send(line);
			case LEVEL_DEBUG:
				if (debug) return send("# " + line);
				else return true;
			case LEVEL_DEBUG_IMPORTER:
				if (debug_importer) return send("# " + line);
				else return true;
			case LEVEL_DEBUG_DESCR:
				if (debug_descr) return send("# " + line);
				else return true;
			case LEVEL_DEBUG_SCRIPTS:
				if (debug_scripts) return send("# " + line);
				else return true;
			default:
				return send(line);
		}
	}

	public String line2snd;
	public synchronized boolean send(String line) {
		if (line == null) return true;
		if (line.equals("")) return true;
		line2snd = line;
		activity.handler.post(new Runnable(){

				@Override
				public void run() {
					activity.addItem(line2snd);
				}
			});
		try {
			sleep(100);
		} catch (InterruptedException e) {}
		return true;
	}

	public synchronized boolean append(int level, String line) {
		switch (level) {
			case LEVEL_CLIENT:
				return append(line);
			case LEVEL_DEBUG:
				if (debug) return append("# " + line);
				else return true;
			case LEVEL_DEBUG_IMPORTER:
				if (debug_importer) return append("# " + line);
				else return true;
			case LEVEL_DEBUG_DESCR:
				if (debug_descr) return append("# " + line);
				else return true;
			case LEVEL_DEBUG_SCRIPTS:
				if (debug_scripts) return append("# " + line);
				else return true;
			default:
				return append(line);
		}
	}

	public String line2append;
	public synchronized boolean append(String line) {
		if (line == null) return true;
		if (line.equals("")) return true;
		line2append = line;
		activity.handler.post(new Runnable(){

				@Override
				public void run() {
					activity.appendToItem(line2append);
				}
			});
		try {
			sleep(100);
		} catch (InterruptedException e) {}
		return true;
	}

	public synchronized void send2others(String text) {	// Отправляем сообщение всем, кроме его автора
		// Just nothing
	}

	public void saveReport() {
		if (report.size() == 0) return;
		importer.saveReport(report);
		report.clear();
	}

	public void startPreferences() {
		activity.handler.post(new Runnable(){

				@Override
				public void run() {
					activity.startPreferences();
				}
			});
	}

	public String doFunction(String script, String func) {
		return doFunction(script, func, null);
	}

	public String doFunction(String scriptName, String funcName, String[] args) {
		long time_start = System.currentTimeMillis();
		
		if (!canScripts) return "Скрипты не работают.";

		if (!scriptsmap.containsKey(scriptName)) {
			send(LEVEL_DEBUG_SCRIPTS, "Команды " + scriptName + " не существует.");
			return "";
		}
		String res = null;

		isScriptRunning = true;

		try {
			L.setTop(0);
			int ok = L.LdoString(scriptsmap.get(scriptName));
			if (ok == 0) {
				L.getGlobal(funcName);

				if (L.isNil(-1)) {
					send(LEVEL_DEBUG_SCRIPTS, "У скрипта " + scriptName + " нет функции " + funcName + ".");
				} else if (args == null) {
					L.pcall(0, 1, -2);
				} else {
					for (int i = 0; i < args.length; i++) {
						L.pushString(args[i]);
					}
					L.pcall(args.length, 1, -2 - args.length);
				}
				res = L.toString(-1);
			} else {
				send(LEVEL_DEBUG_SCRIPTS, "При выполнении " + scriptName + ":" + funcName + "() произошла досадная ошибка: " + errorReason(ok));
			}
		} catch (Exception e) {
			send(LEVEL_DEBUG, "Произошла серьезная ошибка:\n" + e.toString());
			res = "Internal error";
		}
		isScriptRunning = false;
		
		long time_end = System.currentTimeMillis();
		send(LEVEL_DEBUG_SCRIPTS,"Script "+scriptName+":"+funcName+" finished. Time: "+(time_end-time_start)+" ms.");
		
		return res;
	}

	private String errorReason(int error) {	// Перевод ошибки в словсеный режим
		switch (error) {
			case 1: return "Yield error";
			case 2: return "Ошибка при выполнении";
			case 3: return "Синтаксическая ошибка";
			case 4: return "Память закончилась"; // (Out of memory)
		}
		return "Ошибка с номером " + error + " не опознана";
	}


	public String moveTo(String x, String y) {
		pos_x = Integer.parseInt(x);
		pos_y = Integer.parseInt(y);
		return "Вы ТПшнулись на (" + pos_x + ":" + pos_y + ").";
	}

	public String getDescription() {
		return getDescription(pos_x, pos_y, 0);
	}

	public String getDescription(int x0, int y0, int deg) {

		/* PREPARING  */
		StringBuilder sb = new StringBuilder();
		ArrayList<DescribedWorldObject> objs = new ArrayList<DescribedWorldObject>();
		HashMap<Integer, DescribedWorldObject> objsm = new HashMap<Integer,DescribedWorldObject>();

		DescribedWorldObject horizon = new DescribedWorldObject(0, new WorldObject(0, 0, 0, 0, "Горeзонт"));
		horizon.included = false;
		objsm.put(0, horizon);

		if (debug_descr) sb.append("Точка зрения: " + "(" + x0 + "," + y0 + "); Объектов: " + db.objects.size() + "\n");

		
		/* CREATING DESCRIBEDWORLDOBJECT AND FILLING IT TO ARRAYLIST */
		int idhlp = 0;
		for (WorldObject o : db.objects) {

			DescribedWorldObject obj = new DescribedWorldObject(++idhlp, o);

			obj.xc = (obj.x + obj.x2) / 2;
			obj.yc = (obj.y + obj.y2) / 2;

			obj.distance = getDistance(x0, y0, obj.xc, obj.yc);

			int deg00 = getDegree(x0, y0, o.x, o.y);
			int deg01 = getDegree(x0, y0, o.x, o.y2);
			int deg10 = getDegree(x0, y0, o.x2, o.y);
			int deg11 = getDegree(x0, y0, o.x2, o.y2);
			obj.deg_min = Math.min(Math.min(deg00, deg01), Math.min(deg10, deg11));
			obj.deg_max = Math.max(Math.max(deg00, deg01), Math.max(deg10, deg11));

			obj.deg_center = getDegree(x0, y0, obj.xc, obj.yc);

			if (obj.deg_center < obj.deg_min || obj.deg_center > obj.deg_max) {
				int _max = obj.deg_max;
				obj.deg_max = obj.deg_min;
				obj.deg_min = _max;
			}

			obj.area = getDistance(obj.x, obj.y, obj.x2, obj.y) * getDistance(obj.x, obj.y, obj.x, obj.y2);

			objs.add(obj);
			objsm.put(obj.id, obj);
		}

		Collections.sort(objs, new Comparator<DescribedWorldObject>(){

				@Override
				public int compare(DescribedWorldObject p1, DescribedWorldObject p2) {
					if (p1.distance <  p2.distance) return -1;
					if (p1.distance == p2.distance) return 0;
					if (p1.distance >  p2.distance) return 1;
					return 0;
				}
			});

		/*  ADDING INFO TO PIE */
		int[] pie = new int[360];
		for (DescribedWorldObject obj : objs) {
			if (obj.deg_min < obj.deg_max) {
				for (int i = obj.deg_min; i < obj.deg_max; i++) {
					if (pie[i] == 0) pie[i] = obj.id;
					else if (obj.fullview) obj.fullview = false;
				}
			} else {
				for (int i = obj.deg_min; i < 360; i++) {
					if (pie[i] == 0) pie[i] = obj.id;
					else if (obj.fullview) obj.fullview = false;
				}

				for (int i = 0; i < obj.deg_max; i++) {
					if (pie[i] == 0) pie[i] = obj.id;
					else if (obj.fullview) obj.fullview = false;
				}
			}
			
			// TODO: приоритет еще должен зависеть от отклонения от центра сегмента зрения
			double priority = /*obj.area */ (1 / (double) obj.distance) * 100 * (obj.fullview ? 1000: 1);
			obj.priority = new Double(priority).intValue();
		}

		objs = new ArrayList<DescribedWorldObject>();


		/* ADDING OTHER INFO */
		int objbefore = pie[0], objbefore_before = pie[0];
		for (int i = 0; i < pie.length; i++) {
			if (debug_descr)		sb.append(pie[i] + " ");

			if (pie[i] != objbefore) {
				objsm.get(objbefore).obj_right = pie[i];
				objsm.get(pie[i]).obj_left = objbefore;
				objsm.get(pie[i]).included = true;

				objsm.get(pie[i]).obj_left2 = objbefore_before;
				objsm.get(objbefore_before).obj_right2 = pie[i];

				objbefore_before = objbefore;
				objbefore = pie[i];
			}
		}

		if (debug_descr) sb.append("\n\n\n");

		if (pie[pie.length - 1] != pie[0])
			objsm.get(pie[pie.length - 1]).obj_right = pie[0];
		
		int _zone = 0; // forward - right - backward - left
		int _zonepointer = 0;
		int[][] pie2 = new int[4][90];
		
		int[] starters = new int[4];	// Начальные объекты
		
		for(int i = deg; i<360; i++){
			pie2[_zone][_zonepointer] = pie[i];
			if(debug_descr) sb.append(" "+pie[i]);
			if(objsm.get(pie[i]).priority > objsm.get(starters[_zone]).priority) starters[_zone] = pie[i];
			_zonepointer++;
			if(_zonepointer == 90){
				_zone++;
				_zonepointer = 0;
				if(debug_descr) sb.append("\n");
			}
		}
		
		for(int i = 0; i<deg; i++){
			pie2[_zone][_zonepointer] = pie[i];
			if(debug_descr) sb.append(" "+pie[i]);
			if(objsm.get(pie[i]).priority > objsm.get(starters[_zone]).priority) starters[_zone] = pie[i];
			_zonepointer++;
			
			if(_zonepointer == 90){
				_zone++;
				_zonepointer = 0;
				if(debug_descr) sb.append("\n");
			}
		}
		
		sb.append("Впереди "+objsm.get(starters[0]).name);
		sb.append("\nСправа "+objsm.get(starters[1]).name);
		sb.append("\nПозади "+objsm.get(starters[2]).name);
		sb.append("\nСлева "+objsm.get(starters[3]).name);
	//	recursiveDescr(sb,objsm,pie,0,objsm.get(starters[0]));
		
		return sb.toString();
	}

	StringBuilder recursiveDescr(StringBuilder sb, HashMap<Integer,DescribedWorldObject> objsm, int[] pie, int deep, DescribedWorldObject starter){
		if(deep > 10) return sb;
		if(!starter.included) return sb;
		deep++;

		DescribedWorldObject left2 = objsm.get(starter.obj_left2);
		DescribedWorldObject left = objsm.get(starter.obj_left);
		DescribedWorldObject right = objsm.get(starter.obj_right);
		DescribedWorldObject right2 = objsm.get(starter.obj_right2);

		sb.append(left.included ? "Слева от "+starter.name.toLowerCase()+"а находится "+left.name.toLowerCase()+"; " : "");
		sb.append(right.included ? "Справа от "+starter.name.toLowerCase()+"а находится "+right.name.toLowerCase()+"; " : "");
		starter.included = false;

		if(left.included) recursiveDescr(sb,objsm,pie,deep,left);
		if(right.included) recursiveDescr(sb,objsm,pie,deep,right);

		return sb;
	}
	
	private int getDistance(int x1, int y1, int x2, int y2) {
		double dist = Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
		return new Double(dist).intValue();
	}

	private int getDegree(int x_B, int y_B, int x_C, int y_C) {
		// треуг ABC, где А - север, В - центр, С - точка
		int x_A = x_B, y_A = y_B + 10;

		double l_AB = Math.sqrt((x_B - x_A) * (x_B - x_A) + (y_B - y_A) * (y_B - y_A));
		double l_BC = Math.sqrt((x_C - x_B) * (x_C - x_B) + (y_C - y_B) * (y_C - y_B));
		double l_AC = Math.sqrt((x_C - x_A) * (x_C - x_A) + (y_C - y_A) * (y_C - y_A));

		double arccos = Math.acos((l_AB * l_AB + l_BC * l_BC - l_AC * l_AC) / (2 * l_AB * l_BC));
		if (x_B > x_C) {
			arccos = Math.PI + (Math.PI - arccos);
		}
		//	return new Double(arccos*1000).intValue();
		return new Double(Math.toDegrees(arccos)).intValue();
	}

	String compareObjs(int angle, DescribedWorldObject obj, int[] pie) {
		StringBuilder sb = new StringBuilder();

		int mark = 0;

		int fr = ((angle + 45) < 360 ? angle + 45 : (360 - (angle + 45)) * -1);
		int br = ((fr + 90) < 360 ? fr + 90 : (360 - (fr + 90)) * -1);
		int bl = ((br + 90) < 360 ? br + 90 : (360 - (br + 90)) * -1);
		int fl = ((bl + 90) < 360 ? bl + 90 : (360 - (bl + 90)) * -1);

		if (waka(fl, fr, obj)) mark = 1;
		else if (waka(fr, br, obj)) mark = 2;
		else if (waka(br, bl, obj)) mark = 3;
		else if (waka(bl, fl, obj)) mark = 4;
		else mark = 5;

		String[] straight = {"Перед вами","Прямо перед вами"};
		String[] right = {"Справа","Справа от вас","По правую руку"};
		String[] left = {"Слева","Слева от вас"};
		String[] back = {"Позади","Позади вас"};
		String[] verb = {"виднеется","находится"};

		switch (mark) {
			case 1:
				sb.append(straight[(int)(Math.random() * (straight.length))]);
				//	sb.append("Впереди ");
				break;
			case 2:
				sb.append(right[(int)(Math.random() * (right.length))]);
				break;
			case 3:
				sb.append(back[(int)(Math.random() * back.length)]);
				break;
			case 4:
				sb.append(left[(int)(Math.random() * left.length)]);
				break;
			case 5:
			default:
				send(LEVEL_DEBUG_DESCR, "Пофэйлили марку.");
		}
		sb.append(" ").append(verb[(int) (Math.random() * verb.length)]).append(" ").append(obj.name.toLowerCase());

		return sb.toString();
	}

	boolean waka(int l, int r, DescribedWorldObject obj) {

		if (l < r) {
			if (obj.deg_center >= l && obj.deg_center <= r)
				return true;
		} else {
			if (obj.deg_center >= l) return true;
			if (obj.deg_center <= r) return true;
		}

		return false;
	}

	String compareObjs(DescribedWorldObject one, DescribedWorldObject another) {
		StringBuilder sb = new StringBuilder();
		int distance = Math.max(one.deg_center, another.deg_center) - Math.min(one.deg_center, another.deg_center);
		if (distance > 180) distance = distance - 360;
	 	sb.append("Dist " + one.name + " to " + another.name + " is " + distance);
		return sb.toString();
	}

	String compareObjs(DescribedWorldObject one, DescribedWorldObject two, DescribedWorldObject three) {
		// А между В и С
		return "";
	}
	
	public String listScripts() {
		StringBuilder sb = new StringBuilder();

		for (String s : scriptsnames) {
			sb.append(s + "\n");
		}
		sb.append("Всего скриптов: " + scriptsnames.size());
		return sb.toString();
	}

	public synchronized boolean close() {
		if (isScriptRunning) {
			Thread.currentThread().interrupt();
			return false;
		}

		try {
			if (canScripts) L.close();
			Thread.currentThread().interrupt();
			send("Socket closed");
			wait(3000);
			activity.finish();
		} catch (Exception e) {
			send("Ошибка закрытия." + e.toString());
		}
		return true;
	}
}
