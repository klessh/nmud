package localhost.iillyyaa2033.mud.androidclient.clientserver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;
import localhost.iillyyaa2033.mud.androidclient.activity.MainActivity;
import localhost.iillyyaa2033.mud.androidclient.utils.GlobalValues;
import org.keplerproject.luajava.LuaState;
import org.keplerproject.luajava.LuaStateFactory;
import localhost.iillyyaa2033.mud.androidclient.utils.ScriptsStorage;
import localhost.iillyyaa2033.mud.androidclient.exceptions.NoSuchCommandException;

// В этом классе держим только клиентскую часть
public class LocalClient extends Thread {
	
	private boolean isScriptRunning = false;
	private boolean deadend = false;

	public LuaState L;
	public MainActivity activity;

	// TODO: решить, что делать с cmdstr
	public volatile String cmdstr;	// Сюда MainActivity вбивает команду

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
				//		L.pushJavaObject(UglyClient.this);
				//		L.setGlobal("server");
			} catch (Throwable t) {
				send(t.toString());
			}
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
				L.getGlobal("main");

				if (L.isNil(-1)) {
					send(Level.DEBUG_SCRIPTS, "У польз. скрипта " + cmdName + " нет функции main.");
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
	
	// Scripting interface
	public String getDescription(){
		return "В течение всей зимы 1927-28 годов официальные представители федерального правительства проводили довольно необычное и строго секретное изучение состояния находящегося в Массачусетсе старого иннсмаутского порта. Широкая общественность впервые узнала обо всем этом лишь в феврале, когда была проведена широкая серия облав и арестов, за которыми последовало полное уничтожение -- посредством осуществленных с соблюдением необходимых мер безопасности взрывов и поджогов -- громадного числа полуразвалившихся, пришедших в почти полную негодность и, по всей видимости, опустевших зданий, стоявших вдоль береговой линии. Не отличающиеся повышенным любопытством граждане отнеслись к данной акции всего лишь как к очередной, пусть даже и достаточно массированной, но все же совершенно спонтанной попытке властей поставить заслон на пути контрабандной поставки в страну спиртных напитков. Более же любознательные люди обратили внимание на небывало широкие масштабы проведенных арестов, многочисленность задействованных в них сотрудников полиции, а также на обстановку секретности, в которой проходил вывоз арестованных.";
	}
}
