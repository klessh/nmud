import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Main{
	
	static boolean debug_descr = true;
	static ArrayList<WorldObject> objects;
	
	public static void main(String[] args){
		objects = new ArrayList<WorldObject>();
		objects.add(new WorldObject("room",1,1,18,18,"Кладовка"));
		objects.add(new WorldObject("obj",5,5,6,6,"Столб"));
		objects.add(new WorldObject("obj",7,7,9,9,"Коробка"));
		objects.add(new WorldObject("obj",8,4,11,7,"Колодец"));
	//	System.out.println(getDescription(0,0,0)+"\n\n");
		System.out.println(altDescription(10,10,0));
	}
	
	static ArrayList<DescribedObject> objs;
	static ArrayList<DescribedZone> zones;
	static HashMap<Integer, Integer[]> links;
	
	static String altDescription(int x0, int y0, int deg){
		objs = new ArrayList<DescribedObject>();
		zones = new ArrayList<DescribedZone>();
		links = new HashMap<Integer,Integer[]>();	// связь между id комнаты и id объектов в ней
		
		for(int i = 0; i < objects.size(); i++){
			WorldObject obj = objects.get(i);
			
			if(obj.type == null){
				System.out.println("Object haven't type "+obj.name);
			}else if (obj.type.toLowerCase().equals("zone") || obj.type.toLowerCase().equals("room")){
				DescribedZone object = new DescribedZone(i,obj);
				object.area = getDistance(object.x,object.y,object.x,object.y2) * getDistance(object.x,object.y,object.x2,object.y);
				zones.add(object);
				System.out.println("Added zone "+i);
			} else {
				DescribedObject object = new DescribedObject(i,obj);
				object.area = getDistance(object.x,object.y,object.x,object.y2) * getDistance(object.x,object.y,object.x2,object.y);
				objs.add(object);
				System.out.println("Added object "+i);
			}
		}
		
		// заполняем связи зона - ее элементы
		for(DescribedObject obj : objs){
			for(DescribedZone zone : zones){
				if(obj.x >= zone.x && obj.y >= zone.y && obj.x2 <= zone.x2 && obj.y2 <= zone.y2){
					Integer[] ids;
					
					if(links.get(zone.id) == null) ids = new Integer[1];
					else ids = Arrays.copyOf(links.get(zone.id),links.get(zone.id).length+1);
					
					ids[ids.length-1] = obj.id;
					links.put(zone.id,ids);
					System.out.println("Added link "+zone.id+"->"+obj.id);
					break;
				}
			}
		}
		
		String result = "\n\n";
		result += "YOU_ARE_IN ";
		
		int whereAmI;
		
		try{
			whereAmI = getZoneOf(x0,y0);
			result += zones.get(whereAmI).name;
		} catch(MudException e){
			result += "UNKNOWN_ZONE";
			return result;
		}
		result += ". THERE_ARE ";
		
		String[] names = new String[links.get(whereAmI).length];
		int i = 0;
		for(int j : links.get(whereAmI)){
			names[i] = objs.get(j-1).name;
			i++;
		}
		result += describeArray(names, true) + ".";
		
		
		for(int objId : links.get(whereAmI)){
			result += "\n\t";
			result += describeObjectInZone(objId-1, whereAmI);
		}
		
		return result;
	}
	
	// перебираем все описанные зоны, чтобы получить ту, в которой сейчас игрок
	static int getZoneOf(int x, int y) throws MudException{
		// TODO: рассчеты для многоугольных зон
		int id = -1, deepness = -1;
		
		for(DescribedZone zone : zones){
			if(zone.x < x && zone.x2 > x && zone.y < y && zone.y2 > y && zone.deepness > deepness){
				id = zone.id;
			}
		}
		
		if(id == -1) throw new MudException();
		return id;
	}
	
	static String describeArray(Object[] list, boolean toLowerCase){
		StringBuilder sb = new StringBuilder();
		
		sb.append(toLowerCase ? list[0].toString().toLowerCase() : list[0].toString());
		
		for(int i = 1; i < list.length - 1; i++){
			sb.append(", ");
			sb.append(toLowerCase ? list[i].toString().toLowerCase() : list[i].toString());
		}
		
		sb.append(" AND ");
		sb.append(toLowerCase ? list[list.length-1].toString().toLowerCase() : list[list.length-1].toString());
		return sb.toString();
	}
	
	static <T> String describeArrayList(ArrayList<T> list){
		StringBuilder sb = new StringBuilder();
		
		for(int i = 0; i < list.size() - 1; i++){
			sb.append(list.get(i).toString());
		}
		sb.append(" AND ");
		sb.append(list.get(list.size()-1));
		sb.append(".");
		return sb.toString();
	}
	
	static String describeObject(int index){
		String result = "";
		DescribedObject object = objs.get(index);
		result += object.name;
		return result;
	}
	
	static String describeZone(int index){
		return "";
	}
	
	static String describeObjectInZone(int objId, int zoneId){
		DescribedObject object = objs.get(objId);
		DescribedZone zone = zones.get(zoneId);
		
		String result = "";
		result += object.name;
		result += " LOCATED_IN ";
		
		return result;
	}
	
	static StringBuilder recursiveDescr(StringBuilder sb, HashMap<Integer,DescribedObject> objsm, int[] pie, int deep, DescribedObject starter){
		if(deep > 10) return sb;
		if(!starter.included) return sb;
		deep++;

		DescribedObject left2 = objsm.get(starter.obj_left2);
		DescribedObject left = objsm.get(starter.obj_left);
		DescribedObject right = objsm.get(starter.obj_right);
		DescribedObject right2 = objsm.get(starter.obj_right2);

		sb.append(left.included ? "Слева от "+starter.name.toLowerCase()+"а находится "+left.name.toLowerCase()+"; " : "");
		sb.append(right.included ? "Справа от "+starter.name.toLowerCase()+"а находится "+right.name.toLowerCase()+"; " : "");
		starter.included = false;

		if(left.included) recursiveDescr(sb,objsm,pie,deep,left);
		if(right.included) recursiveDescr(sb,objsm,pie,deep,right);

		return sb;
	}

	static private int getDistance(int x1, int y1, int x2, int y2) {
		// расстояние между точками x и у.
		double dist = Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
		return new Double(dist).intValue();
	}

	static private int getDegree(int x_B, int y_B, int x_C, int y_C) {
		// треуг ABC, где А - север, В - центр, С - точка
		int x_A = x_B, y_A = y_B + 10;

		double l_AB = Math.sqrt((x_B - x_A) * (x_B - x_A) + (y_B - y_A) * (y_B - y_A));
		double l_BC = Math.sqrt((x_C - x_B) * (x_C - x_B) + (y_C - y_B) * (y_C - y_B));
		double l_AC = Math.sqrt((x_C - x_A) * (x_C - x_A) + (y_C - y_A) * (y_C - y_A));

		double arccos = Math.acos((l_AB * l_AB + l_BC * l_BC - l_AC * l_AC) / (2 * l_AB * l_BC));
		if (x_B > x_C) {
			arccos = Math.PI + (Math.PI - arccos);
		}
		//	return new Double(arccos*1000).intValue();
		return new Double(Math.toDegrees(arccos)).intValue();
	}

	String compareObjs(int angle, DescribedObject obj, int[] pie) {
		StringBuilder sb = new StringBuilder();

		int mark = 0;

		int fr = ((angle + 45) < 360 ? angle + 45 : (360 - (angle + 45)) * -1);
		int br = ((fr + 90) < 360 ? fr + 90 : (360 - (fr + 90)) * -1);
		int bl = ((br + 90) < 360 ? br + 90 : (360 - (br + 90)) * -1);
		int fl = ((bl + 90) < 360 ? bl + 90 : (360 - (bl + 90)) * -1);

		if (waka(fl, fr, obj)) mark = 1;
		else if (waka(fr, br, obj)) mark = 2;
		else if (waka(br, bl, obj)) mark = 3;
		else if (waka(bl, fl, obj)) mark = 4;
		else mark = 5;

		String[] straight = {"Перед вами","Прямо перед вами"};
		String[] right = {"Справа","Справа от вас","По правую руку"};
		String[] left = {"Слева","Слева от вас"};
		String[] back = {"Позади","Позади вас"};
		String[] verb = {"виднеется","находится"};

		switch (mark) {
			case 1:
				sb.append(straight[(int)(Math.random() * (straight.length))]);
				//	sb.append("Впереди ");
				break;
			case 2:
				sb.append(right[(int)(Math.random() * (right.length))]);
				break;
			case 3:
				sb.append(back[(int)(Math.random() * back.length)]);
				break;
			case 4:
				sb.append(left[(int)(Math.random() * left.length)]);
				break;
			case 5:
			default:
				send(0, "Пофэйлили марку.");
		}
		sb.append(" ").append(verb[(int) (Math.random() * verb.length)]).append(" ").append(obj.name.toLowerCase());

		return sb.toString();
	}

	boolean waka(int l, int r, DescribedObject obj) {

		if (l < r) {
			if (obj.deg_center >= l && obj.deg_center <= r)
				return true;
		} else {
			if (obj.deg_center >= l) return true;
			if (obj.deg_center <= r) return true;
		}

		return false;
	}
	
	void send(int i, String s){
		System.out.println(s);
	}
}

class WorldObject {

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
	
	public WorldObject(String type, int x, int y, int x2, int y2, String name){
		id = -1;
		this.type = type;
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

class DescribedObject extends WorldObject{
	// TODO: многоугольник
	public int id;
	public int xc, yc, distance, area;
	public int deg_min, deg_max, deg_center;
	public int priority;
	public int obj_left, obj_left2, obj_right, obj_right2;
	public boolean included = false, fullview = true;

	public DescribedObject(int id, WorldObject obj){
		this.id = id;
		super.name = obj.name;
		super.x = obj.x;
		super.y=obj.y;
		super.x2=obj.x2;
		super.y2=obj.y2;
	}
}

class DescribedZone extends WorldObject{
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
	}
}

class MudException extends Exception{}
