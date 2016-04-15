package com.iillyyaa2033.mud.editor.logic;


public class nObject{
	public int id;
	public int x1, x2, y1, y2, xc, yc;
	public String name;
	
	public nObject(int id, int[] coords, String name){
		this.id = id;
		setCoords(coords);
		this.name = name;
	}
	
	public void setCoords(int coords[]){
		x1 = coords[0];
		y1 = coords[1];
		x2 = coords[2];
		y2 = coords[3];
		xc = x2-((x2-x1)/2);
		yc = y2-((y2-y1)/2);
	}
}
