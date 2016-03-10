package com.iillyyaa2033.nmud.abstractserver;

import org.keplerproject.luajava.LuaException;

public abstract class AbstractClient extends Thread {
	
	abstract void comms(String message);
	
	public abstract String read();
	
	public abstract boolean send(String line);
	
	public abstract boolean append(String line);
	
	abstract void initLua() throws LuaException;
	
	public abstract String doFunction(String scriptName, String funcName, String[] args);
	
	public abstract String getDescription();
	
	public abstract boolean close();
}
