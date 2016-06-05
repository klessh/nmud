public class DescribedZone extends WorldObject{
	// TODO: многоугольник
	public int id;
	public int xc, yc, distance, area;
	public int deepness;	// глубина вложенности зоны

	public DescribedZone(int id, WorldObject parent){
		this.id  =  id;
		super.name = parent.name;
		super.x  =  parent.x;
		super.y  =  parent.y;
		super.x2 = parent.x2;
		super.y2 = parent.y2;
		xc = x + (x2-x)/2;
		yc = y + (y2-y)/2;
	}
}
