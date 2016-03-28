package com.iillyyaa2033.mud.editor.logic;
import java.util.ArrayList;

public class nRoom extends nObject{
	
	public int[] walls;
	public ArrayList<nObject> objects;
	
	public nRoom(int id, int[] coords, String[][] qualitys, int[] walls){
		super(id, coords, qualitys);
		
		this.walls = walls;
		objects = new ArrayList<nObject>();
	}
}
