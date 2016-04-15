package com.iillyyaa2033.mud.editor.logic;

import java.util.ArrayList;

public class nBuilding extends nObject{
	
	ArrayList<nRoom> rooms;
	ArrayList<nDoor> inDoors;
	
	public nBuilding(int id, int[] coords, String name, 
					ArrayList<nDoor> inDoors,ArrayList<nRoom> rooms){
		super(id,coords,name);
		this.rooms = rooms;
		this.inDoors = inDoors;
	}	
}
