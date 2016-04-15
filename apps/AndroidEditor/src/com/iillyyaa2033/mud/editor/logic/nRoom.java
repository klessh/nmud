package com.iillyyaa2033.mud.editor.logic;
import java.util.ArrayList;

public class nRoom extends nObject{
	
	public int[] walls; // хм... что же это?
	public ArrayList<nObject> objects;
	
	public nRoom(nObject obj, int[] walls){
		super(obj.id,new int[]{obj.x1,obj.y1,obj.x2,obj.y2},obj.name);
		
		this.walls = walls;
	}
	
	public nRoom(int id, int[] coords, String name, int[] walls){
		super(id, coords, name);
		
		this.walls = walls;
		objects = new ArrayList<nObject>();
	}
}
