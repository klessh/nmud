package com.iillyyaa2033.nmud.abstractserver;

public interface AbstractCore{
	
	/* Start core */
	public abstract void run();
	
	/* Update all from hard disk */
	public abstract void update();
	
	/* Update only scripts */
	public abstract void updateScripts();
	
	/* Save all to hard disk */
	public abstract void save();
	
	/* Add script in core's scrtips list */
	public abstract void addScript(String name, String scripts);
	
	/* Return list of all scritps as string */
	public abstract String listScripts();
	
	/* Save all scripts */
	public abstract void saveScripts();
	
	/* Close core */
	public abstract boolean close();
}
