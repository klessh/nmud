package localhost.iillyyaa2033.mud.androidclient.logic;

import java.util.HashMap;
import java.util.StringTokenizer;
import localhost.iillyyaa2033.mud.androidclient.activity.MainActivity;
import org.keplerproject.luajava.JavaFunction;
import org.keplerproject.luajava.LuaException;
import org.keplerproject.luajava.LuaState;
import org.keplerproject.luajava.LuaStateFactory;
import localhost.iillyyaa2033.mud.androidclient.logic.model.WorldObject;
import java.util.ArrayList;
import localhost.iillyyaa2033.mud.androidclient.logic.model.DescribedWorldObject;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

public class Core extends Thread {

	private MainActivity activity;
	public Importer importer;
	private Database db;
	private LuaState L;

	private HashMap<String, String> scriptsmap;

	public boolean debug = !false;
	private boolean canSctipts = false;
	private boolean isScriptRunning = false;

	public String cmdstr;

	ArrayList<String> report;

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

		scriptsmap = importer.importChatScripts();

		if (scriptsmap == null) {
			canSctipts = false;
		} else {

			try {
				initLua();
				canSctipts = true;
			} catch (LuaException e) {
				send(e.toString());
				canSctipts = false;
			}
		}

		doFunction("onServerStarted", "on", null);

		doFunction("onClientConnected", "on", null);

		String cmd = "";
		while (!(cmd = read()).equals("")) {
			comms(cmd);
		}
	}

	private String[] haventcmd = {"Команды не существует.","Нет такой команды.","Похоже, такой команды нет.",
		"Во славу Ктулху!", "Протеряли все полимеры.","Команду съели.","Лондон украден летучими мышами",
		"Ничего не случилось", "London was stolen by bats"};

	private synchronized void comms(String message) {	// Обарботка входящией строки
		message = message.toLowerCase();	// Теперь все сообщение набрано строчными буквами

		StringTokenizer token = new StringTokenizer(message);	// Делим сообщение на токены
		int size = token.countTokens();							// определяем количество токенов

		if (size == 0) return;	// Если токенов нет, то завершимся

		String cmd = token.nextToken();		// Возьмем первое слово как команду...
		String[] args = new String[size - 1];	// создадим массив для ея аргументов

		for (int i = 0; i < size - 1; i++) {		// Пройдемся циклом 
			args[i] = token.nextToken();	// занесем все остальный слова сообщения в массив аргументов
		}

		if (scriptsmap != null) {
			if (scriptsmap.containsKey(cmd)) {
				send(doFunction(cmd, "chat", null));
			} else {
				report.add(message);
				int random = (int)Math.floor(Math.random() * haventcmd.length);
				send(haventcmd[random]);
			}

		} else {
			send("Скрипты не загружены.");
		}
	}

	public synchronized String read() {	// Получение входящей строки
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

	private void initLua() throws LuaException {

		L = LuaStateFactory.newLuaState();
		L.openLibs();

		L.pushJavaObject(Core.this);	
		L.setGlobal("client");
		L.pushJavaObject(Core.this);
		L.setGlobal("server");

		JavaFunction print = new JavaFunction(L) {

			@Override
			public int execute() throws LuaException {

				for (int i = 2; i <= L.getTop(); i++) {	
					int type = L.type(i);				
					String stype = L.typeName(type);
					String val = null;
					if (stype.equals("userdata")) {		
						Object obj = L.toJavaObject(i);	
						if (obj != null)				
							val = obj.toString();		
					} else if (stype.equals("boolean")) {			
						val = L.toBoolean(i) ? "true" : "false";	
					} else {
						val = L.toString(i);
					}
					if (val == null)		
						val = stype;		
					send(val);
				}
				return 0;				
			}
		};
		print.register("print");

		JavaFunction redir = new JavaFunction(L){

			@Override
			public int execute() throws LuaException {
				for (int i = 2; i <= L.getTop(); i++) {	
					int type = L.type(i);				
					String stype = L.typeName(type);
					String val = null;
					if (stype.equals("userdata")) {		
						Object obj = L.toJavaObject(i);	
						if (obj != null)				
							val = obj.toString();		
					} else if (stype.equals("boolean")) {			
						val = L.toBoolean(i) ? "true" : "false";	
					} else {
						val = L.toString(i);
					}
					if (val == null)	val = stype;
					send(doFunction(val, "chat", null));	
				}
				return 0;
			}
		};
		redir.register("redir");
	}

	/*	public String doScript(String src) {
	 String res = null;			// Вывод, сейчас пуст

	 L.setTop(0);
	 int ok = L.LloadString(src);			// Загрузим строку и занесем результат
	 isScriptRunning = true;	// Флаг, что луа работает
	 try {										// Попробуем...
	 if (ok == 0) {							// Если все нормально
	 L.getGlobal("debug");
	 L.getField(-1, "traceback");
	 L.remove(-2);
	 L.insert(-2);
	 ok = L.pcall(0, 0, -2);
	 if (ok != 0) {
	 res = "Internal error";
	 }
	 }
	 } catch (Exception e) {
	 res = "Internal error";
	 send(res+errorReason(ok) + ": " + L.toString(-1) + e.toString());
	 }
	 isScriptRunning = false;
	 return res;
	 }*/

	public String doFunction(String scriptName, String funcName, String[] args) {
		if (!canSctipts) return "Скрипты не работают.";

		if (!scriptsmap.containsKey(scriptName)) {
			send("# Команды " + scriptName + " не существует.");
			return "";
		}
		String res = null;

		isScriptRunning = true;

		try {
			L.setTop(0);
			int ok = L.LdoString(scriptsmap.get(scriptName));
			if (ok == 0) {
				L.getGlobal(funcName);
				L.pcall(0, 1, -2);
				res = L.toString(-1);
			} else {
				res = errorReason(ok);
			}
		} catch (Exception e) {
			send(e.toString());
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

	public String getDescription() {
		return getDescription(15, 25);
	}

	public String getDescription(int x0, int y0) {

		StringBuilder sb = new StringBuilder();
		ArrayList<DescribedWorldObject> objs = new ArrayList<DescribedWorldObject>();
		sb.append("Точка зрения: " + "(" + x0 + "," + y0 + ")");
		sb.append("Объектов: " + db.objects.size() + "\n");

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

			obj.area = getDistance(obj.x,obj.y,obj.x2,obj.y) * getDistance(obj.x,obj.y,obj.x,obj.y2);
			double priority = obj.area * (1/(double) obj.distance) * 10;
			obj.priority = new Double(priority).intValue();
			
			objs.add(obj);
			sb.append(obj.id + " «" + obj.name + "»  {" + obj.x + ":" + obj.y + " | " + obj.x2 + ":" + obj.y2 + "} " +
					  "\n\tЦентр и расст: {" + obj.xc + ":" + obj.yc + "} " + obj.distance +
					  "\n\tРад мин/макс: " + obj.deg_min + "/" + obj.deg_max +
					  "\n\tПриоритет: " + obj.priority +
					  "\n\n");
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
		for (int i : pie) {
			sb.append(i + "\t");
		}

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

	public synchronized boolean close() {	// Закрытые сокета и lua
		if (isScriptRunning) return false;

		try {						// пытаемся...
			if (canSctipts) L.close();
			Thread.currentThread().interrupt();
			send("Socket closed");
		} catch (Exception e) {									// А при ошибке...
			send("Ошибка закрытия." + e.toString());		// спамим в лог.
		}
		return true;
	}
}
