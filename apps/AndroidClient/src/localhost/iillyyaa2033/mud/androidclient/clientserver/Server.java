package localhost.iillyyaa2033.mud.androidclient.clientserver;

import java.io.FileNotFoundException;
import localhost.iillyyaa2033.mud.androidclient.utils.ExceptionsStorage;
import localhost.iillyyaa2033.mud.androidclient.utils.GlobalValues;
import localhost.iillyyaa2033.mud.androidclient.utils.ImportUtil;
import localhost.iillyyaa2033.mud.androidclient.utils.ScriptsStorage;
import org.keplerproject.luajava.LuaState;
import org.keplerproject.luajava.LuaStateFactory;
import localhost.iillyyaa2033.mud.androidclient.logic.model.World;
import localhost.iillyyaa2033.mud.androidclient.logic.dictionary.Dictionary;
import localhost.iillyyaa2033.mud.androidclient.exceptions.MudException;
import localhost.iillyyaa2033.mud.androidclient.logic.dictionary.WordScript;
import java.util.ArrayList;

public class Server extends Thread {
// Принимаем клиенты, выполняем работу с ФС
	
	LuaState L;

	public Server() {
		setDaemon(true);
		init();
		start();
	}

	@Override
	public void run() {
		super.run();

		doFunction("onServerStarted", "on", null);

		//	doFunction("onClientConnected", "on", null);
	}

	public synchronized void init(){
		try {
			ScriptsStorage.putCommands(ImportUtil.loadUsercommands());
		} catch (FileNotFoundException e) {
			GlobalValues.canScripts = false;
			ExceptionsStorage.exceptions.add(e);
		}

		if (GlobalValues.canScripts) {
			try {
				L = LuaStateFactory.newLuaState();
				L.openLibs();
			} catch (Throwable t) {
				ExceptionsStorage.addException(t);
			}
		}

		ArrayList<WordScript> a = ImportUtil.importWords();
		Dictionary.initializeDictionary(a);

		try {
			World.zones = ImportUtil.importZones();
		} catch (FileNotFoundException e) {
			// TODO: correct handling
			ExceptionsStorage.addException(e);
		}
	}
	
	public void doFunction(String script, String function, String[] args) {
		// TODO: Implement this method
	}


}
