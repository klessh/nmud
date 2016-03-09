package localhost.iillyyaa2033.mud.androidclient.logic.model;

public class DescribedWorldObject extends WorldObject{
	
	public int xc, yc, distance, area;
	public int deg_min, deg_max;
	
	public DescribedWorldObject(WorldObject obj){
		super.name = obj.name;
		super.x = obj.x;
		super.y=obj.y;
		super.x2=obj.x2;
		super.y2=obj.y2;
	}
}
