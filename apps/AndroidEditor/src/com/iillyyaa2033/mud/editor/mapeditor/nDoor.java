package com.iillyyaa2033.mud.editor.mapeditor;
import java.util.ArrayList;

public class nDoor extends nObject{
	int x3, y3, x4, y4, xc2, yc2;
	
	public nDoor(int id, int[] coords1, int[] coords2, String[][] qualitys){
		super(id, coords1, qualitys);
		x3 = coords2[0];
		y3 = coords2[1];
		x4 = coords2[2];
		y4 = coords2[3];
		xc2 = x4-((x4-x3)/2);
		yc2 = y4-((y4-y3)/2);
	}
}
