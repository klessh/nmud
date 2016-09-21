package localhost.iillyyaa2033.mud.androidclient.clientserver;

import java.util.ArrayList;
import java.util.StringTokenizer;
import localhost.iillyyaa2033.mud.androidclient.activity.MainActivity;
import localhost.iillyyaa2033.mud.androidclient.exceptions.NoSuchCommandException;
import localhost.iillyyaa2033.mud.androidclient.logic.model.Creature;
import localhost.iillyyaa2033.mud.androidclient.logic.model.World;
import localhost.iillyyaa2033.mud.androidclient.logic.model.Zone;
import localhost.iillyyaa2033.mud.androidclient.utils.DescriptionFactory;
import localhost.iillyyaa2033.mud.androidclient.utils.ExceptionsStorage;
import localhost.iillyyaa2033.mud.androidclient.utils.GlobalValues;
import localhost.iillyyaa2033.mud.androidclient.utils.ScriptsStorage;
import org.keplerproject.luajava.LuaState;
import org.keplerproject.luajava.LuaStateFactory;
import localhost.iillyyaa2033.mud.androidclient.exceptions.IncorrectPositionException;

// В этом классе держим только клиентскую часть
public class LocalClient extends Thread {
	
	private boolean isScriptRunning = false;
	private boolean deadend = false;

	public LuaState L;
	public MainActivity activity;

	// TODO: решить, что делать с cmdstr
	public volatile String cmdstr;	// Сюда MainActivity вбивает команду

	// Логика, которую надо будет вынести в модель, в сервер, или еще куда-нибудь
	Zone currentZone;
	Creature player;
	
	public LocalClient(MainActivity activity) {
		this.activity = activity;
	}

	@Override
	public void run() {
		if (GlobalValues.canScripts) {
			try {
				L = LuaStateFactory.newLuaState();
				L.openLibs();

				L.pushJavaObject(LocalClient.this);	
				L.setGlobal("client");
			} catch (Throwable t) {
				send(t.toString());
			}
		}
		
		if(World.zones.size()>0) currentZone = World.zones.get(0);
		else currentZone = new Zone();
		
		player = new Creature();
		try {
			currentZone.addObject(player);
		} catch (IncorrectPositionException e) {
			ExceptionsStorage.addException(e);
		}

		String cmd = "";
		while ((!(cmd = read()).equals("")) && (!deadend)) {
			comms(cmd);
		}

		send("Finishing.");
		close();
	}

	public synchronized boolean close() {

		if (isScriptRunning) {
			deadend = true;
			return false;
		}

		try {
			if (GlobalValues.canScripts) L.close();

			wait(250);
			activity.finish();
		} catch (Exception e) {
			send("Ошибка закрытия." + e.toString());
		}
		return true;
	}

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

		send(doCommand(cmd, args));
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
			case Level.CLIENT:
				return send(line);
			case Level.DEBUG:
				if (GlobalValues.debug) return send("# " + line);
				else return true;
			case Level.DEBUG_IMPORTER:
				if (GlobalValues.debug_importer) return send("# " + line);
				else return true;
			case Level.DEBUG_DESCR:
				if (GlobalValues.debug_descr) return send("# " + line);
				else return true;
			case Level.DEBUG_SCRIPTS:
				if (GlobalValues.debug_scripts) return send("# " + line);
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
			case Level.CLIENT:
				return append(line);
			case Level.DEBUG:
				if (GlobalValues.debug) return append("# " + line);
				else return true;
			case Level.DEBUG_IMPORTER:
				if (GlobalValues.debug_importer) return append("# " + line);
				else return true;
			case Level.DEBUG_DESCR:
				if (GlobalValues.debug_descr) return append("# " + line);
				else return true;
			case Level.DEBUG_SCRIPTS:
				if (GlobalValues.debug_scripts) return append("# " + line);
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

	// TODO: избавиться от этого массива
	private String[] haventcmd = {"Команды не существует.","Нет такой команды.","Похоже, такой команды нет.",
		"Во славу Ктулху!", "Протеряли все полимеры.","Команду съели.","Ничего не случилось","Команда отсутствует.",
		"Наберите команду \"справка\" для получения списка команд."};
	
	public String doCommand(String cmdName){
		return doCommand(cmdName,null);
	}	
		
	public String doCommand(String cmdName, String[] args){
		return doFunction(cmdName, "main", args);
	}
	
	public String doFunction(String cmdName, String function, String... args){
		long time_start = System.currentTimeMillis();

		if (!GlobalValues.canScripts) return "Скрипты не работают.";
		
		String res = null;

		String command = "";
		
		try {
			command = ScriptsStorage.getCommand(cmdName);
		} catch (NoSuchCommandException e) {
			long time_end = System.currentTimeMillis();
			send(Level.DEBUG_SCRIPTS, "Script " + cmdName + " doesn't exist. Time: " + (time_end - time_start) + " ms.");
			return haventcmd[(int) (Math.random()*haventcmd.length)];
		}

		isScriptRunning = true;
		
		try {
			L.setTop(0);
			int ok = L.LdoString(command);
			if (ok == 0) {
				L.getGlobal(function);

				if (L.isNil(-1)) {
					send(Level.DEBUG_SCRIPTS, "У польз. скрипта " + cmdName + " нет функции "+function + ".");
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
				send(Level.DEBUG_SCRIPTS, "При выполнении " + cmdName +" произошла досадная ошибка: " + errorReason(ok));
			}
		} catch (Exception e) {
			send(Level.DEBUG, "Произошла серьезная ошибка:\n" + e.toString());
			res = "Internal error";
		}
		isScriptRunning = false;

		long time_end = System.currentTimeMillis();
		send(Level.DEBUG_SCRIPTS, "Script " + cmdName + " finished. Time: " + (time_end - time_start) + " ms.");

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
	
	private static class Level{
		static final int CLIENT = 0; 
		static final int DEBUG = 1; 
		static final int DEBUG_IMPORTER = 2;
		static final int DEBUG_DESCR = 3;
		static final int DEBUG_SCRIPTS = 4;
	}
	
	// ==================== //
	// Scripting interfaces //
	// ==================== //
	public String getDescription(){
		DescriptionFactory factory = new DescriptionFactory(currentZone, player);
		try{
			factory.prepare();
		} catch(NullPointerException e){
			ExceptionsStorage.addException(e);
		}
		
		return factory.getSampleDescr()+"\n"+factory.getAutoDescription();
	}
	
	public String getDescriptionOf(String objName){
		DescriptionFactory factory = new DescriptionFactory(currentZone, player);
		try{
			factory.prepare();
		} catch(NullPointerException e){
			ExceptionsStorage.addException(e);
		}
		return factory.getHandDescriptionOf(objName);
	}
	
	public String listScripts(){
		StringBuilder b = new StringBuilder();
		for(String s : ScriptsStorage.commandsNames){
			b.append(s).append("\t\t");
		}
		return b.toString();
	}
	
	public ArrayList<String> listVisibleObjNames(){
		ArrayList<String> l = new ArrayList<String>();
		// TODO: this
		return l;
	}
}
