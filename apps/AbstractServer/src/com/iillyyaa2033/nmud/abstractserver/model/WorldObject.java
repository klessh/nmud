package com.iillyyaa2033.nmud.abstractserver.model;

public class WorldObject {

	public String type;
	public int id;
	public int x,y,x2,y2;
	public String name;
	
	public WorldObject(){
		id = -1;
	}
	
	public WorldObject(int x, int y, int x2, int y2, String name){
		id = -1;
		this.x = x;
		this.y = y;
		this.x2 = x2;
		this.y2 = y2;
		this.name = name;
	}
	
	public int getId(){
		return id;
	}
	
	public void setId(int id){
		this.id = id;
	}

	public void replaceUsingCenter(int x, int y){
		int wx = (x2 - this.x)/2;
		int wy = (y2 -this.y)/2;
		this.x = x - wx;
		this.y = y - wy;
		this.x2 = x + wx;
		this.y2 = y + wy;
	}
}
