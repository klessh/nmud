package com.iillyyaa2033.mud.editor.logic;

import java.util.ArrayList;

public class nBuilding extends nObject{
	
	public ArrayList<nRoom> rooms;
	
	public nBuilding(int id, int[] coords, String name){
		super(id,coords,name);
		rooms = new ArrayList<nRoom>();
	}
	
	public nBuilding(int id, int[] coords, String name, ArrayList<nRoom> rooms){
		super(id,coords,name);
		this.rooms = rooms;
	}	
}
