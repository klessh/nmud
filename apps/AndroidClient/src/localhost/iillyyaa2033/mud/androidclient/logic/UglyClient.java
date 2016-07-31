package localhost.iillyyaa2033.mud.androidclient.logic;

import android.preference.PreferenceManager;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;
import java.util.StringTokenizer;
import localhost.iillyyaa2033.mud.androidclient.activity.MainActivity;
import localhost.iillyyaa2033.mud.androidclient.exceptions.MudException;
import localhost.iillyyaa2033.mud.androidclient.logic.described.DescribedObject;
import localhost.iillyyaa2033.mud.androidclient.logic.described.DescribedZone;
import localhost.iillyyaa2033.mud.androidclient.logic.dictionary.Dictionary;
import localhost.iillyyaa2033.mud.androidclient.logic.dictionary.Word;
import localhost.iillyyaa2033.mud.androidclient.logic.graph.Graph;
import localhost.iillyyaa2033.mud.androidclient.logic.graph.GraphUtils;
import localhost.iillyyaa2033.mud.androidclient.logic.model.Material;
import localhost.iillyyaa2033.mud.androidclient.logic.model.WorldObject;
import org.keplerproject.luajava.LuaException;
import org.keplerproject.luajava.LuaState;
import org.keplerproject.luajava.LuaStateFactory;
import localhost.iillyyaa2033.mud.androidclient.utils.GlobalValues;

public class UglyClient extends Thread {

	public static final int LEVEL_CLIENT = 0, 
	LEVEL_DEBUG = 1, 
	LEVEL_DEBUG_IMPORTER = 2, 
	LEVEL_DEBUG_DESCR = 3,
	LEVEL_DEBUG_SCRIPTS = 4;

	public MainActivity activity;
	public Importer importer;
	private LuaState L;

	private HashMap<String, String> scriptsmap;
	private ArrayList<String> scriptsnames;

	public boolean debug = true, debug_importer = false, debug_descr = false, debug_scripts = false, debug_graph = false;
	private boolean canScripts = false, isScriptRunning = false, updRequested = false;
	private boolean deadend = false;

	public String cmdstr;

	ArrayList<String> report;

	ArrayList<WorldObject> objects;
	ArrayList<DescribedObject> objs;
	static ArrayList<DescribedZone> zones;
	static HashMap<Integer, Integer[]> links;

	int pos_x = 0, pos_y = 0;

	public UglyClient(MainActivity activity) {
		this.activity = activity;
		debug = PreferenceManager.getDefaultSharedPreferences(activity).getBoolean("FLAG_DEBUG", true);
		debug_importer = PreferenceManager.getDefaultSharedPreferences(activity).getBoolean("FLAG_DEBUG_IMPORTER", false);
		debug_descr = PreferenceManager.getDefaultSharedPreferences(activity).getBoolean("FLAG_DEBUG_DESCR", false);
		debug_scripts = PreferenceManager.getDefaultSharedPreferences(activity).getBoolean("FLAG_DEBUG_SCRIPTS", false);
		importer = new Importer(this);
		report = new ArrayList<String>();

		filling();
	}

	void filling() {
		objects = new ArrayList<WorldObject>();
		objects.add(new WorldObject("room", 1, 1, 18, 18, new Word(1, 1)));
		objects.add(new WorldObject("obj", 5, 5, 6, 6, new Word(1, 2), new Material(new Word(Dictionary.CH_R.ADJECTIVE, 5), "дерево")));
		objects.add(new WorldObject("obj", 7, 7, 9, 9, new Word(1, 3), new Material(new Word(Dictionary.CH_R.ADJECTIVE, 6), "картон")));
		objects.add(new WorldObject("obj", 16, 2, 17, 3, new Word(1, 4), new Material(new Word(Dictionary.CH_R.ADJECTIVE, 7), "стекло")));

		objs = new ArrayList<DescribedObject>();
		zones = new ArrayList<DescribedZone>();
		links = new HashMap<Integer,Integer[]>();	// связь между id комнаты и id объектов в ней


		for (int i = 0; i < objects.size(); i++) {
			WorldObject obj = objects.get(i);

			if (obj.type == null) {
				send(LEVEL_DEBUG_DESCR, "Object haven't any type: " + Dictionary.getWord(obj.name));
			} else if (obj.type.toLowerCase().equals("zone") || obj.type.toLowerCase().equals("room")) {
				DescribedZone object = new DescribedZone(i, obj);
				object.area = getDistance(object.x, object.y, object.x, object.y2) * getDistance(object.x, object.y, object.x2, object.y);
				zones.add(object);
				send(LEVEL_DEBUG_DESCR, "Added zone " + i + " " + Dictionary.getWord(object.name) + " " + object.x + ":" + object.y + " " + object.x2 + ":" + object.y2);
			} else {
				DescribedObject object = new DescribedObject(i, obj);
				object.area = getDistance(object.x, object.y, object.x, object.y2) * getDistance(object.x, object.y, object.x2, object.y);
				objs.add(object);
				send(LEVEL_DEBUG_DESCR, "Added object " + i + " " + Dictionary.getWord(object.name) + " " + object.x + ":" + object.y + " " + object.x2 + ":" + object.y2);
			}
		}

		// заполняем связи зона - ее элементы
		for (DescribedObject obj : objs) {
			for (DescribedZone zone : zones) {
				if (obj.x >= zone.x && obj.y >= zone.y && obj.x2 <= zone.x2 && obj.y2 <= zone.y2) {
					Integer[] ids;

					if (links.get(zone.id) == null) ids = new Integer[1];
					else ids = Arrays.copyOf(links.get(zone.id), links.get(zone.id).length + 1);

					ids[ids.length - 1] = obj.id;
					links.put(zone.id, ids);
					obj.dirCode = getDirectionCode(obj, zone);

					send(LEVEL_DEBUG_DESCR, "Added link " + zone.id + "->" + obj.id + " with dircode " + obj.dirCode);

					break;
				}
			}
		}
	}

	@Override
	public void run() {

		try { 
			sleep(1000);
		} catch (InterruptedException e) {
			
		}

		update();

		if (scriptsmap == null) {
			canScripts = false;
		} else {
			try {
				initLua();
				canScripts = true;
			} catch (Throwable t) {
				canScripts = false;
				send(t.toString());
			}
		}

		doFunction("onServerStarted", "on", null);

		doFunction("onClientConnected", "on", null);

		String cmd = "";
		while ((!(cmd = read()).equals("")) && (!deadend)) {
			if (updRequested) update();

			comms(cmd);
		}

		send("Finishing.");
		close();
	}

	public void requestUpdate() {
		updRequested = true;
	}


	public synchronized void update() {
		debug = PreferenceManager.getDefaultSharedPreferences(activity).getBoolean("FLAG_DEBUG", true);
		debug_importer = PreferenceManager.getDefaultSharedPreferences(activity).getBoolean("FLAG_DEBUG_IMPORTER", false);
		debug_descr = PreferenceManager.getDefaultSharedPreferences(activity).getBoolean("FLAG_DEBUG_DESCR", false);
		debug_scripts = PreferenceManager.getDefaultSharedPreferences(activity).getBoolean("FLAG_DEBUG_SCRIPTS", false);

		GlobalValues.datapath = PreferenceManager.getDefaultSharedPreferences(activity).getString("DATAPATH", "");

		if (!(new File(GlobalValues.datapath)).exists()) {
			// TODO: data must be extracted before db update
			importer.extractContent(activity);
			importer.unzip(activity.getCacheDir() + "/content-base.zip", GlobalValues.datapath);
		}

		updateScripts();

		// TODO: init dict here
		updRequested = false;
	}


	public void updateScripts() {
		scriptsmap = importer.importChatScripts();
		scriptsnames = new ArrayList<String>();

		if (scriptsmap != null) {
			Set<String> set = scriptsmap.keySet();
			for (String s : set) {
				scriptsnames.add(s);
			}

			Collections.sort(scriptsnames);
		}
	}


	public void addScript(String name, String script) {
		scriptsmap.put(name, script);
	}


	public void save() {
		// TODO: Implement this method
	}


	public void saveScripts() {
		// TODO: Implement this method
	}


	public void initLua() throws LuaException {
		L = LuaStateFactory.newLuaState();
		L.openLibs();

		L.pushJavaObject(UglyClient.this);	
		L.setGlobal("client");
		L.pushJavaObject(UglyClient.this);
		L.setGlobal("server");
	}

	private String[] haventcmd = {"Команды не существует.","Нет такой команды.","Похоже, такой команды нет.",
		"Во славу Ктулху!", "Протеряли все полимеры.","Команду съели.","Ничего не случилось","Команда отсутствует."};


	public void comms(String message) {
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
		send(LEVEL_DEBUG_SCRIPTS, "Script " + scriptName + ":" + funcName + " finished. Time: " + (time_end - time_start) + " ms.");

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


	// DESCRIPTION METHODS
	public String getDescription() {
		return getDescription(0, 0, 0);
	}


	public String getDescription(int x0, int y0, int deg) {
		// TODO: положение и описание объектов

		ArrayList<Graph> graphs = new ArrayList<Graph>();

		// Версия со сложением графов


//	/*
		for (DescribedObject obj : objs) {
			Graph current = describePosition(obj);

			/*			for (int i = 0; i < graphs.size(); i++) {
			 Graph other = graphs.get(i);
			 if (GraphUtils.canAppendAuto(3, current, other)) {
			 GraphUtils.appendAuto(3, current, other);
			 } else { 	*/
			graphs.add(current);
			/*			}
			 break;
			 }

			 if (graphs.size() == 0) graphs.add(current);*/
		}

//		 */

		/*
		 // Версия с выбором по dirCode
		 HashMap<Integer, Integer[]> map = new HashMap<Integer, Integer[]>();
		 for (int i = 0; i < objs.size(); i++) {
		 DescribedObject bj = objs.get(i);
		 Integer[] newList;
		 if (map.containsKey(bj.dirCode)) {
		 newList = Arrays.copyOf(map.get(bj.dirCode), map.get(bj.dirCode).length + 1);
		 map.remove(bj.dirCode);
		 } else {
		 newList = new Integer[1];
		 }
		 newList[newList.length - 1] = i;
		 map.put(bj.dirCode, newList);
		 }

		 Integer[] keys = new Integer[map.size()];
		 keys = map.keySet().toArray(keys);

		 for (int key : keys) {
		 Graph subject = describeObjectsWithArrayOfIds(map.get(key));
		 int areId = subject.add(0, new Word(Dictionary.CH_R.VERB, 9, 1));
		 Graph place = getDirectionGraph(key,zones.get(0));
		 subject.append(areId,GraphUtils.rootId,place);
		 graphs.add(subject);
		 }
		 //		 */

		String r = "";

		for (Graph gr : graphs) {
			r += GraphUtils.graphToText(gr) + "\n";
		}

		if (debug_graph) {System.out.println("\n\n#### GRAPHS ####");
			for (Graph gr : graphs) {
				r += printGraph(gr) + "\n";
			}
		}

		return r;
	}


	static int getZoneOf(int x, int y) throws MudException {
		// TODO: рассчеты для многоугольных зон
		int id = -1, deepness = -1;

		for (DescribedZone zone : zones) {
			if (zone.x < x && zone.x2 > x && zone.y < y && zone.y2 > y && zone.deepness > deepness) {
				id = zone.id;
			}
		}

		if (id == -1) throw new MudException();
		return id;
	}

	//
	//	ЦИФРЫ -> ГРАФ
	//
	Graph describePosition(DescribedObject object) {
		Word ARE = new Word(Dictionary.CH_R.VERB, 9, 1);

		Graph result = new Graph();
		int nameId= result.add(GraphUtils.rootId, object.name);		// starting with object's name

		int areId = result.add(nameId, ARE); 		// obj's name -> are
		result.add(nameId, object.material.color);	// obj's name -> color
		Graph place = getDirectionGraph(getDirectionCode(object, zones.get(0)), zones.get(0));	// are -> place
		result.append(GraphUtils.rootId, areId, place);
		return result;
	}

	int getDirectionCode(DescribedObject object, DescribedZone zone) {
		int stepX = getDistance(zone.x, zone.y, zone.x2, zone.y) / 5;
		int stepY = getDistance(zone.x, zone.y, zone.x, zone.y2) / 5;

		int code = 0;

		if (object.yc < (zone.y + stepY)) code = 1000;
		else if (object.yc > (zone.y + stepY * 4)) code = 3000;
		else code = 2000;

		if (object.xc < (zone.x + stepX)) code += 10;
		else if (object.xc > (zone.x + stepX * 4)) code += 30;
		else code += 20;

		return code;
	}

	static Graph getDirectionGraph(int code, DescribedZone zone) {

		Graph result = new Graph();
		Word DOWN = new Word(Dictionary.CH_R.ADJECTIVE, 1);
		Word TOP  = new Word(Dictionary.CH_R.ADJECTIVE, 4);
		Word LEFT = new Word(Dictionary.CH_R.ADJECTIVE, 2);
		Word RIGHT= new Word(Dictionary.CH_R.ADJECTIVE, 3);
		Word CORNER=new Word(Dictionary.CH_R.NOUN, 5);
		Word WALL = new Word(Dictionary.CH_R.NOUN, 6);
		Word CENTER=new Word(Dictionary.CH_R.NOUN, 7);

		Word IN = new Word(Dictionary.CH_R.PREPOSITION, 1);
		Word NEAR = new Word(Dictionary.CH_R.PREPOSITION, 2);

		int prepId = -1;
		int wordId = -1;

		switch (code) {
			case DirCode.LEFT_DOWN_CORNER:
				prepId = result.add(GraphUtils.rootId, IN);
				wordId = result.add(prepId, CORNER);
				result.add(wordId, LEFT);
				result.add(wordId, DOWN);
				break;
			case DirCode.DOWN_WALL:
				prepId = result.add(GraphUtils.rootId, NEAR);
				wordId = result.add(prepId, WALL);
				result.add(wordId, DOWN);
				break;
			case DirCode.RIGHT_DOWN_CORNER:	
				prepId = result.add(GraphUtils.rootId, IN);
				wordId = result.add(prepId, CORNER);
				result.add(wordId, RIGHT);
				result.add(wordId, DOWN);
				break;
			case DirCode.LEFT_WALL:
				prepId = result.add(GraphUtils.rootId, NEAR);
				wordId = result.add(prepId, WALL);
				result.add(wordId, LEFT);
				break;
			case DirCode.CENTER:	
				prepId = result.add(GraphUtils.rootId, NEAR);
				wordId = result.add(prepId, CENTER);
				break;
			case DirCode.RIGHT_WALL:	
				prepId = result.add(GraphUtils.rootId, NEAR);
				wordId = result.add(prepId, WALL);
				result.add(wordId, RIGHT);
				break;
			case DirCode.LEFT_TOP_CORNER:
				prepId = result.add(GraphUtils.rootId, IN);
				wordId = result.add(prepId, CORNER);
				result.add(wordId, LEFT);
				result.add(wordId, TOP);
				break;
			case DirCode.TOP_WALL:	
				prepId = result.add(GraphUtils.rootId, NEAR);
				wordId = result.add(prepId, WALL);
				result.add(wordId, TOP);
				break;
			case DirCode.RIGHT_TOP_CORNER:	
				prepId = result.add(GraphUtils.rootId, IN);
				wordId = result.add(prepId, CORNER);
				result.add(wordId, RIGHT);
				result.add(wordId, TOP);
				break;
		}

		result.add(wordId, zone.name);

		return result;
	}

	Graph describeObjectsWithArrayOfIds(Integer[] array) {
		Graph gr = new Graph();

		int lastId = GraphUtils.rootId;
		for (Integer i : array) {
			lastId = gr.add(lastId, objs.get(i).name);
		}
		return gr;
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

	static String printGraph(Graph gr) {
		String divider = " ", res = "";
		for (int i =0; i < gr.nodes.length; i++) {
			res += gr.nodes[i] + divider;
		}
		res += "\n";

		for (int i = 0; i < gr.links.length; i++) {
			res += gr.links[i] + divider + gr.links[++i] + divider + gr.links[++i];
		}

		return res;
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
			deadend = true;
			return false;
		}

		try {
			if (canScripts) L.close();

			wait(250);
			activity.finish();
		} catch (Exception e) {
			send("Ошибка закрытия." + e.toString());
		}
		return true;
	}

	//
	//	ПРОЧЕЕ
	//
	class DirCode {
		final static int LEFT_DOWN_CORNER 	= 1010;
		final static int DOWN_WALL 			= 1020;
		final static int RIGHT_DOWN_CORNER 	= 1030;
		final static int LEFT_WALL 			= 2010;
		final static int CENTER 			= 2020;
		final static int RIGHT_WALL 		= 2030;
		final static int LEFT_TOP_CORNER	= 3010;
		final static int TOP_WALL 			= 3020;
		final static int RIGHT_TOP_CORNER 	= 3030;
	}
}
