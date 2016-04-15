package com.iillyyaa2033.mud.editor.logic;

import java.util.ArrayList;

// Прикол в том, что могут быть проблемы с многопоточностью
public class Database{
	
	private Database instance;
	
	private Database(){}
	
	public Database getInstance(){
		if(instance==null) instance = new Database();
		return instance;
	}
	
	public ArrayList<nRoom> rooms = new ArrayList<nRoom>();
	
	// TODO: dict here
}
