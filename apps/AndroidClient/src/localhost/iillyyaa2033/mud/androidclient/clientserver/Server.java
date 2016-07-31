package localhost.iillyyaa2033.mud.androidclient.clientserver;

import localhost.iillyyaa2033.mud.androidclient.clientserver.LocalClient;
import localhost.iillyyaa2033.mud.androidclient.utils.GlobalValues;
import org.keplerproject.luajava.LuaStateFactory;
import org.keplerproject.luajava.LuaState;
import localhost.iillyyaa2033.mud.androidclient.utils.ScriptsStorage;
import localhost.iillyyaa2033.mud.androidclient.utils.ImportUtil;

public class Server extends Thread {
// Принимаем клиенты, выполняем работу с ФС
	LuaState L;

	public Server() {
		setDaemon(true);
		start();
	}

	@Override
	public void run() {
		ScriptsStorage.putCommands(ImportUtil.loadUsecommands());
		
		if (GlobalValues.canScripts) {
			try {
				L = LuaStateFactory.newLuaState();
				L.openLibs();

				//		L.pushJavaObject(LocalClient.this);	
				//		L.setGlobal("client");
				//		L.pushJavaObject(UglyClient.this);
				//		L.setGlobal("server");
			} catch (Throwable t) {
				//		send(t.toString());
			}
		}

		doFunction("onServerStarted", "on", null);

		doFunction("onClientConnected", "on", null);

		super.run();
	}

	public void doFunction(String script, String function, String[] args) {
		// TODO: Implement this method
	}


}
