package com.iillyyaa2033.mud.editor.mapeditor;
import java.util.ArrayList;

public class nObject{
	public int id;
	public int x1, x2, y1, y2, xc, yc;
	public String[][] qualitys;
	
	public nObject(int id, int[] coords, String[][] qualitys){
		this.id = id;
		setCoords(coords);
		this.qualitys = qualitys;
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
