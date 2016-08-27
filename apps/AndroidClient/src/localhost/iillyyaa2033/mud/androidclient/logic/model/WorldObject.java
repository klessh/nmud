package localhost.iillyyaa2033.mud.androidclient.logic.model;

import localhost.iillyyaa2033.mud.androidclient.logic.dictionary.Word;
import java.util.HashMap;

public class WorldObject {

	public int id;
	public int x,y,x2,y2;
	public Word name;
	public Material material;
	
	public HashMap<String, String> params;
	
	public WorldObject(){
		id = -1;
		params = new HashMap<String, String>();
	}

	public WorldObject(int x, int y, int x2, int y2, Word name){
		id = -1;
		this.x = x;
		this.y = y;
		this.x2 = x2;
		this.y2 = y2;
		this.name = name;
		params = new HashMap<String, String>();
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
