package com.iillyyaa2033.nmud.abstractserver;

public interface AbstractDatabase {

	public abstract void addUser(String name, String password);
	
	public abstract void updateUsers();
	
	public abstract void saveUsers();
}
