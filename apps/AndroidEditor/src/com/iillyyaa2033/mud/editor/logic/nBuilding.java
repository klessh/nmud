package com.iillyyaa2033.mud.editor.logic;
import java.util.ArrayList;

public class nBuilding extends nObject{
	
	ArrayList<nRoom> rooms;
	ArrayList<nDoor> inDoors;
	
	public nBuilding(int id, int[] coords, String[][] qualitys, 
					ArrayList<nDoor> inDoors,ArrayList<nRoom> rooms){
		super(id,coords,qualitys);
		this.rooms = rooms;
		this.inDoors = inDoors;
	}	
}
