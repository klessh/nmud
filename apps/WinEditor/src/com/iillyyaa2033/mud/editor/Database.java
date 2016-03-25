package com.iillyyaa2033.mud.editor;

import java.util.HashMap;

import com.iillyyaa2033.nmud.abstractserver.model.WorldObject;

public class Database {
	
	Main main;
	HashMap<Integer, WorldObject> objects;
	// TODO words table
	// TODO materials table
	// TODO scripts table
	
	public Database(Main m){
		main = m;
		
	}
	
	WorldObject getObjectByID(int id){
		return objects.get(id);
	}
	
	String getWordByID(){
		// TODO get word by id
		return "";
	}
	
	void getMaterialByID(int id){
		// TODO get material by id
	}
}
