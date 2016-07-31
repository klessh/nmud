package localhost.iillyyaa2033.mud.androidclient.logic.described;

import localhost.iillyyaa2033.mud.androidclient.logic.model.WorldObject;

public class DescribedObject extends WorldObject {
	// TODO: многоугольник
	// TODO: стереть лишние переменные
	public int id;
	public int xc, yc, distance, area;
	public int deg_min, deg_max, deg_center;
	public int priority;
	public int obj_left, obj_left2, obj_right, obj_right2;
	public boolean included = false, fullview = true;

	public int dirCode;
	
	public DescribedObject(int id, WorldObject obj) {
		this.id = id;
		super.name = obj.name;
		super.x = obj.x;
		super.y = obj.y;
		super.x2 = obj.x2;
		super.y2 = obj.y2;
		super.material = obj.material;
		xc = x + (x2 - x) / 2;
		yc = y + (y2 - y) / 2;
	}

	@Override
	public String toString() {
		return name.toString();
	}
	
	
}

