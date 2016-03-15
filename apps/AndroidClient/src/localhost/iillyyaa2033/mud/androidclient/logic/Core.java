package localhost.iillyyaa2033.mud.androidclient.logic;

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

	public static final int LEVEL_CLIENT = 0, LEVEL_DEBUG = 1;

	private MainActivity activity;
	public Importer importer;
	public Database db;
	private LuaState L;

	private HashMap<String, String> scriptsmap;
	private ArrayList<String> scriptsnames;

	public boolean debug = true;
	private boolean canScripts = false;
	private boolean isScriptRunning = false;

	public String cmdstr;

	ArrayList<String> report;

	int pos_x = 0, pos_y = 0;
	
	public Core(MainActivity activity) {
		this.activity = activity;
		importer = new Importer(this);
		db = new Database(this);
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
			L = LuaStateFactory.newLuaState();
			L.openLibs();

			L.pushJavaObject(Core.this);	
			L.setGlobal("client");
			L.pushJavaObject(Core.this);
			L.setGlobal("server");

			canScripts = true;
		}

		doFunction("onServerStarted", "on", null);

		doFunction("onClientConnected", "on", null);

		String cmd = "";
		while (!(cmd = read()).equals("")) {
			comms(cmd);
		}
	}

	public synchronized void update() {
		if(!(new File(db.datapath)).exists()){
			importer.extractContent(activity);
			importer.unzip(activity.getCacheDir() + "/content-ru.zip",db.datapath);
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
		db.update();
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
				if (debug) return send("# "+line);
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
		if (!canScripts) return "Скрипты не работают.";

		if (!scriptsmap.containsKey(scriptName)) {
			send(LEVEL_DEBUG, "Команды " + scriptName + " не существует.");
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
					send(LEVEL_DEBUG, "У скрипта " + scriptName + " нет функции " + funcName + ".");
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
				send(LEVEL_DEBUG, "При выполнении " + scriptName + ":" + funcName + "() произошла досадная ошибка: " + errorReason(ok));
			}
		} catch (Exception e) {
			send(LEVEL_DEBUG, "Произошла серьезная ошибка:\n" + e.toString());
			res = "Internal error";
		}
		isScriptRunning = false;
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

	
	public String moveTo(String x, String y){
		pos_x = Integer.parseInt(x);
		pos_y = Integer.parseInt(y);
		return "Вы ТПшнулись на ("+pos_x+":"+pos_y+").";
	}
	
	public String getDescription() {
		return getDescription(pos_x, pos_y);
	}

	public String getDescription(int x0, int y0) {

		StringBuilder sb = new StringBuilder();
		ArrayList<DescribedWorldObject> objs = new ArrayList<DescribedWorldObject>();
		HashMap<Integer, DescribedWorldObject> objsm = new HashMap<Integer,DescribedWorldObject>();

		DescribedWorldObject horizon = new DescribedWorldObject(0, new WorldObject(0, 0, 0, 0, "Горизонт"));
		objsm.put(0, horizon);

		sb.append("Точка зрения: " + "(" + x0 + "," + y0 + "); Объектов: " + db.objects.size() + "\n");

		int object_with_biggest_priority = 0;

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
			double priority = /*obj.area */ (1 / (double) obj.distance) * 100;
			obj.priority = new Double(priority).intValue();

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

		int[] pie = new int[360];
		for (DescribedWorldObject obj : objs) {
			if (obj.deg_min < obj.deg_max) {
				for (int i = obj.deg_min; i < obj.deg_max; i++) {
					if (pie[i] == 0) pie[i] = obj.id;
				}
			} else {
				for (int i = obj.deg_min; i < 360; i++) {
					if (pie[i] == 0) pie[i] = obj.id;
				}

				for (int i = 0; i < obj.deg_max; i++) {
					if (pie[i] == 0) pie[i] = obj.id;
				}
			}
		}

		objs = null;

		// TODO: вангую багу, что objbefore_before может быть неправилен для крайних объектов
		int objbefore = pie[0], objbefore_before = pie[0];
		for (int i = 0; i < 360; i++) {
			//	sb.append(pie[i] + " ");
			if (pie[i] != objbefore) {
				objsm.get(objbefore).obj_right = pie[i];
				objsm.get(pie[i]).obj_left = objbefore;
				objsm.get(pie[i]).included = true;

				objsm.get(pie[i]).obj_left2 = objbefore_before;
				objsm.get(objbefore_before).obj_right2 = pie[i];

				objbefore_before = objbefore;
				objbefore = pie[i];
			}

			if (objsm.get(pie[i]).priority > objsm.get(object_with_biggest_priority).priority)
				object_with_biggest_priority = pie[i];
		}

		if (pie[359] != pie[0]) {
			objsm.get(pie[359]).obj_right = pie[0];
			objsm.get(pie[0]).obj_left = pie[0];

		}

		sb.append(compareObjs(0, objsm.get(object_with_biggest_priority), pie));
		objsm.get(object_with_biggest_priority).included = false;

		sb.append("\nВокруг себя вы видите следующее: ");
		for (int i = 1; i < objsm.size(); i++) {
			DescribedWorldObject obj = objsm.get(i);
			if (obj.included)
				sb.append(obj.name.toLowerCase() + ", ");
			/*	sb.append("\n\n" + obj.id + " «" + obj.name + "»  {" + obj.x + ":" + obj.y + " | " + obj.x2 + ":" + obj.y2 + "} " +
			 "\n\tЦентр и расст: {" + obj.xc + ":" + obj.yc + "} " + obj.distance +
			 "\n\tРад мин/макс: " + obj.deg_min + "/" + obj.deg_max +
			 "\n\tПриоритет: " + obj.priority +
			 "\n\tСлева/справа 1: " + obj.obj_left + " / " + obj.obj_right +
			 "\n\tСлева/справа 2: " + obj.obj_left2 + " / " + obj.obj_right2
			 );	*/
		}	

		sb.append("землю и голубое небо.");

		return sb.toString();
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

		int fr = ((angle + 15) < 360 ? angle + 15 : (360 - (angle + 15)) * -1);
		int br = ((fr + 120) < 360 ? fr + 120 : (360 - (fr + 120)) * -1);
		int bl = ((br + 90) < 360 ? br + 90 : (360 - (br + 90)) * -1);
		int fl = ((bl + 120) < 360 ? bl + 120 : (360 - (bl + 120)) * -1);
		
		if(waka(fl,fr,obj)) mark = 1;
		if(waka(fr,br,obj)) mark = 2;
		if(waka(br,bl,obj)) mark = 3;
		if(waka(bl,fl,obj)) mark = 4;
		
		String[] straight = {"Перед вами"};
		String[] right = {"Справа","Справа от вас","По правую руку"};
		String[] left = {"Слева","Слева от вас"};
		String[] back = {"Позади","Позади вас"};
		String[] verb = {"виднеется","находится"};
		
		switch(mark){
			case 1:
				sb.append(straight[(int)(Math.random()*straight.length)]);
				break;
			case 2:
				sb.append(right[(int)(Math.random()*(right.length))]);
				break;
			case 3:
				sb.append(back[(int)(Math.random()*back.length)]);
				break;
			case 4:
				sb.append(left[(int)(Math.random()*left.length)]);
				break;
		}
		sb.append(" ").append(verb[(int) (Math.random()*verb.length)]).append(" ").append(obj.name.toLowerCase());
		
		return sb.toString();
	}

	boolean waka(int l, int r, DescribedWorldObject obj) {
		
		if (l < r) {
			if(obj.deg_center > l && obj.deg_center < r)
				return true;
		} else {
			if(obj.deg_center >= l) return true;
			if(obj.deg_center <= r) return true;
		}

		return false;
	}

	String compareObjs(DescribedWorldObject one, DescribedWorldObject another) {
		// A справа/слева от В
		// А перед/позади В
		return "";
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

	public synchronized boolean close() {	// Закрытые сокета и lua
		if (isScriptRunning) return false;

		try {						// пытаемся...
			if (canScripts) L.close();
			Thread.currentThread().interrupt();
			send("Socket closed");
		} catch (Exception e) {									// А при ошибке...
			send("Ошибка закрытия." + e.toString());		// спамим в лог.
		}
		return true;
	}
}
