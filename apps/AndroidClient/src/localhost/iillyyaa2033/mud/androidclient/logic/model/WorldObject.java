package localhost.iillyyaa2033.mud.androidclient.logic.model;

public class WorldObject{
	// TODO: stable  ids;
	public int id;
	public int x,y,x2,y2;
	public String name;
	
	public WorldObject(){}
	
	public WorldObject(int x, int y, int x2, int y2, String name){
		this.x = x;
		this.y = y;
		this.x2 = x2;
		this.y2 = y2;
		this.name = name;
	}
}
