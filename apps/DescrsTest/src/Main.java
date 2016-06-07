import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Main {

	static boolean debug_descr = true;
	static ArrayList<WorldObject> objects;
	static Dictionary dict;
	
	public static void main(String[] args) {
		dict = new Dictionary();
		
		objects = new ArrayList<WorldObject>();
		objects.add(new WorldObject("room", 1, 1, 18, 18, 0));
		objects.add(new WorldObject("obj", 5, 5, 6, 6, 1, new Material("черный","дерево")));
		objects.add(new WorldObject("obj", 7, 7, 9, 9, 2, new Material("серая","картон")));
		objects.add(new WorldObject("obj", 8, 4, 11, 7, 3, new Material("прозрачный","стекло")));

		System.out.println(altDescription(10, 10, 0));
	}

	static ArrayList<DescribedObject> objs;
	static ArrayList<DescribedZone> zones;
	static HashMap<Integer, Integer[]> links;

	static String altDescription(int x0, int y0, int deg) {
		objs = new ArrayList<DescribedObject>();
		zones = new ArrayList<DescribedZone>();
		links = new HashMap<Integer,Integer[]>();	// связь между id комнаты и id объектов в ней

		for (int i = 0; i < objects.size(); i++) {
			WorldObject obj = objects.get(i);

			if (obj.type == null) {
				log("Object haven't type " + obj.name);
			} else if (obj.type.toLowerCase().equals("zone") || obj.type.toLowerCase().equals("room")) {
				DescribedZone object = new DescribedZone(i, obj);
				object.area = getDistance(object.x, object.y, object.x, object.y2) * getDistance(object.x, object.y, object.x2, object.y);
				zones.add(object);
				log("Added zone " + i + " " + object.name + " " + object.x + ":" + object.y + " " + object.x2 + ":" + object.y2);
			} else {
				DescribedObject object = new DescribedObject(i, obj);
				object.area = getDistance(object.x, object.y, object.x, object.y2) * getDistance(object.x, object.y, object.x2, object.y);
				objs.add(object);
				log("Added object " + i + " " + object.name + " " + object.x + ":" + object.y + " " + object.x2 + ":" + object.y2);
			}
		}

		// заполняем связи зона - ее элементы
		for (DescribedObject obj : objs) {
			for (DescribedZone zone : zones) {
				if (obj.x >= zone.x && obj.y >= zone.y && obj.x2 <= zone.x2 && obj.y2 <= zone.y2) {
					Integer[] ids;

					if (links.get(zone.id) == null) ids = new Integer[1];
					else ids = Arrays.copyOf(links.get(zone.id), links.get(zone.id).length + 1);

					ids[ids.length - 1] = obj.id;
					links.put(zone.id, ids);
					log("Added link " + zone.id + "->" + obj.id);
					break;
				}
			}
		}

		String result = "\n\n";
		result += "YOU_ARE_IN ";

		int whereAmI;

		try {
			whereAmI = getZoneOf(x0, y0);
			result += getTextualKeyKode(getPositionCode(x0,y0,zones.get(whereAmI)));
			result += " OF ";
			result += zones.get(whereAmI).name;
		} catch (MudException e) {
			result += "UNKNOWN_ZONE";
			return result;
		}
		result += ". THERE_ARE ";

		String[] names = new String[links.get(whereAmI).length];
		int i = 0;
		for (int j : links.get(whereAmI)) {
			names[i] = dict.getNounForm(objs.get(j - 1).name,0);
			i++;
		}
		result += describeArray(names, true) + ".";


		for (int objId : links.get(whereAmI)) {
			result += "\n\t";
			result += describeObjectInZone(objId - 1, whereAmI);
			result += ". ";
			result += describeObject(objId - 1);
			result += ". ";
		}

		return result;
	}

	static String toNativeLang(int noun, int verb){
		return "";
	}
	
	static String toNativeLang(Integer noun, Integer[] nounAttrs, Integer verb, Integer[] verbAttrs){
		int nounType = noun/10;	// часть речи подлежащего
		int nounGen; // род подлежащего
		int nounCou; // число подлежащего
		String formedNoun;	// просклоненное подлежащее
		
		if(nounType == 0){
			nounCou = (int) noun/100;
			formedNoun = dict.getNounForm((int)noun/1000, 0);
		} else{
			nounCou = (int) noun/100;
			formedNoun = dict.getNounForm((int)noun/1000, 0);
		}
		
		return formedNoun;
	}
	
	// перебираем все описанные зоны, чтобы получить ту, в которой сейчас игрок
	static int getZoneOf(int x, int y) throws MudException {
		// TODO: рассчеты для многоугольных зон
		int id = -1, deepness = -1;

		for (DescribedZone zone : zones) {
			if (zone.x < x && zone.x2 > x && zone.y < y && zone.y2 > y && zone.deepness > deepness) {
				id = zone.id;
			}
		}

		if (id == -1) throw new MudException();
		return id;
	}

	static String describeArray(Object[] list, boolean toLowerCase) {
		StringBuilder sb = new StringBuilder();

		sb.append(toLowerCase ? list[0].toString().toLowerCase() : list[0].toString());

		for (int i = 1; i < list.length - 1; i++) {
			sb.append(", ");
			sb.append(toLowerCase ? list[i].toString().toLowerCase() : list[i].toString());
		}

		sb.append(" AND ");
		sb.append(toLowerCase ? list[list.length - 1].toString().toLowerCase() : list[list.length - 1].toString());
		return sb.toString();
	}

	static String describeObject(int index) {
		String result = "";
		DescribedObject object = objs.get(index);
		result += object.name;
		
		try{
			result += " IS " + object.material.color;
			result += " AND";
		} catch (NullPointerException e){
			log("describeObject(), NPE");
		}
		
		try{
			result += " MADE_FROM " + object.material.madeFrom;
		} catch (NullPointerException e){
			log("describeObject(), NPE");
		}
		return result;
	}

	static String describeZone(int index) {
		return "";
	}

	static String describeObjectInZone(int objId, int zoneId) {
		// todo: принимать на вход порядок слов и прочий кастомайз
		DescribedObject object = objs.get(objId);
		DescribedZone zone = zones.get(zoneId);


		String result = "";
		result += object.name;
		result += " LOCATED_NEAR ";
		result += getTextualKeyKode(getPositionCode(object, zone));

		return result;
	}

	static int getPositionCode(int x, int y, DescribedZone zone) {
		int stepX = getDistance(zone.x, zone.y, zone.x2, zone.y) / 5;
		int stepY = getDistance(zone.x, zone.y, zone.x, zone.y2) / 5;

		int result = 0;
		if (x < zone.x + stepX) result = 1000;
		else if (x > zone.x + stepX * 2) result = 3000;
		else result = 2000;

		if (y < zone.y + stepY) result += 10;
		else if (y > zone.y + stepY * 2) result += 30;
		else result += 20;
		return result;
	}
	
	static int getPositionCode(DescribedObject object, DescribedZone zone) {
		int stepX = getDistance(zone.x, zone.y, zone.x2, zone.y) / 5;
		int stepY = getDistance(zone.x, zone.y, zone.x, zone.y2) / 5;

		int result = 0;
		if (object.xc < zone.x + stepX) result = 1000;
		else if (object.xc > zone.x + stepX * 2) result = 3000;
		else result = 2000;

		if (object.yc < zone.y + stepY) result += 10;
		else if (object.yc > zone.y + stepY * 2) result += 30;
		else result += 20;
		return result;
	}

	static String getTextualKeyKode(int code){
		String result = "";
		// TODO: тут неправильно. Шурик, это не наш метод!
		// Как правильно - пока не знаю.
		switch (code) {
			case 1010:
				result += "DOWN_LEFT_CORNER";
				break;
			case 1020:
				result += "DOWN_WALL";
				break;
			case 1030:
				result += "DOWN_RIGHT_CORNER";
				break;
			case 2010:
				result += "LEFT_WALL";
				break;
			case 2020:
				result += "CENTER";
				break;
			case 2030:
				result += "RIGHT_WALL";
				break;
			case 3010:
				result += "TOP_LEFT_CORNER";
				break;
			case 3020:
				result += "TOP_WALL";
				break;
			case 3030:
				result += "TOP_RIGHT_CORNER";
				break;
			default:
				result += "ERROR";
		}
		return result;
	}
	
	static StringBuilder recursiveDescr(StringBuilder sb, HashMap<Integer,DescribedObject> objsm, int[] pie, int deep, DescribedObject starter) {
		if (deep > 10) return sb;
		if (!starter.included) return sb;
		deep++;

		DescribedObject left2 = objsm.get(starter.obj_left2);
		DescribedObject left = objsm.get(starter.obj_left);
		DescribedObject right = objsm.get(starter.obj_right);
		DescribedObject right2 = objsm.get(starter.obj_right2);

	//	sb.append(left.included ? "Слева от " + starter.name.toLowerCase() + "а находится " + left.name.toLowerCase() + "; " : "");
	//	sb.append(right.included ? "Справа от " + starter.name.toLowerCase() + "а находится " + right.name.toLowerCase() + "; " : "");
		starter.included = false;

		if (left.included) recursiveDescr(sb, objsm, pie, deep, left);
		if (right.included) recursiveDescr(sb, objsm, pie, deep, right);

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
	//	sb.append(" ").append(verb[(int) (Math.random() * verb.length)]).append(" ").append(obj.name.toLowerCase());

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

	void send(int i, String s) {
		System.out.println(s);
	}
	
	static void log(String message){
		if(debug_descr) System.out.println("# " + message);
	}
}

class MudException extends Exception {}

