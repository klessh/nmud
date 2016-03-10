package com.iillyyaa2033.nmud.abstractserver;

public abstract class AbstractCore extends Thread{
	
	public abstract void start();
	
	public abstract void update();
	
	public abstract void updateScripts();
	
	public abstract void save();
	
	public abstract void addScript(String name, String scripts);
	
	public abstract String listScripts();
	
	public abstract void saveScripts();
}
