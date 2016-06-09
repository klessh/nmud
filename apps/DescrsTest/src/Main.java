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
		objects.add(new WorldObject("room", 1, 1, 18, 18, 101001));
		objects.add(new WorldObject("obj", 5, 5, 6, 6, 101002, new Material("черный","дерево")));
		objects.add(new WorldObject("obj", 7, 7, 9, 9, 101003, new Material("серая","картон")));
		objects.add(new WorldObject("obj", 8, 4, 11, 7, 101004, new Material("прозрачный","стекло")));

		System.out.println(altDescription(10, 10, 0));
	}

	static ArrayList<DescribedObject> objs;
	static ArrayList<DescribedZone> zones;
	static HashMap<Integer, Integer[]> links;

	static String altDescription(int x0, int y0, int deg) {
		objs = new ArrayList<DescribedObject>();
		zones = new ArrayList<DescribedZone>();
		links = new HashMap<Integer,Integer[]>();	// связь между id комнаты и id объектов в ней

		fillDescribed();
		
		String result = "\n\n";
		result += "YOU_ARE_IN ";

		int whereAmI;

		try {
			whereAmI = getZoneOf(x0, y0);
			result += toCollocation(getTextualDirection(getPositionCode(x0,y0,zones.get(whereAmI))));
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
			names[i] = dict.getWord(objs.get(j - 1).name);
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

	// заполняем массивы objs, zones, links
	static void fillDescribed(){
		for (int i = 0; i < objects.size(); i++) {
			WorldObject obj = objects.get(i);

			if (obj.type == null) {
				log("Object haven't type " + dict.getWord(obj.name));
			} else if (obj.type.toLowerCase().equals("zone") || obj.type.toLowerCase().equals("room")) {
				DescribedZone object = new DescribedZone(i, obj);
				object.area = getDistance(object.x, object.y, object.x, object.y2) * getDistance(object.x, object.y, object.x2, object.y);
				zones.add(object);
				log("Added zone " + i + " " + dict.getWord(object.name) + " " + object.x + ":" + object.y + " " + object.x2 + ":" + object.y2);
			} else {
				DescribedObject object = new DescribedObject(i, obj);
				object.area = getDistance(object.x, object.y, object.x, object.y2) * getDistance(object.x, object.y, object.x2, object.y);
				objs.add(object);
				log("Added object " + i + " " + dict.getWord(object.name) + " " + object.x + ":" + object.y + " " + object.x2 + ":" + object.y2);
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

	// возвращает массив со словами, опред местоположение в зоне
	static int[] getTextualDirection(int code){
		// TODO: тут неправильно. Шурик, это не наш метод!
		// Как правильно - пока не знаю.
		switch (code) {
			case 1010:
//				result += "DOWN_LEFT_CORNER";
				return new int[]{102001,102002,101025};
			case 1020:
//				result += "DOWN_WALL";
				return new int[]{102001,101026};
			case 1030:
//				result += "DOWN_RIGHT_CORNER";
				return new int[]{102001,102003,101025};
			case 2010:
//				result += "LEFT_WALL";
				return new int[]{102002,101026};
			case 2020:
//				result += "CENTER";
				return new int[]{101027};
			case 2030:
//				result += "RIGHT_WALL";
				return new int[]{102003,101026};
			case 3010:
//				result += "TOP_LEFT_CORNER";
				return new int[]{102004,102002,101025};
			case 3020:
//				result += "TOP_WALL";
				return new int[]{102004,101026};
			case 3030:
//				result += "TOP_RIGHT_CORNER";
				return new int[]{102004,102003,101025};
			default:
				return new int[]{0};
		}
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
		result += getTextualDirection(getPositionCode(object, zone));

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


	static String toNativeLang(int noun, int verb){
		return "";
	}
	
	// строит фразу из id слов
	static String toNativeLang(Integer noun, Integer[] nounAttrs, Integer verb, Integer[] verbAttrs){
		String formedNoun = "";	// просклоненное подлежащее
		
		return formedNoun;
	}
	
	static String toCollocation(int... ids){
		StringBuilder sb = new StringBuilder();
		for(int i : ids){
			sb.append(dict.getWord(i)).append(" ");
		}
		return sb.toString();
	}

	//
	//	ГЕОМЕТРИЧЕСКИЕ МЕТОДЫ
	//
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


	//
	//	МЕТОДЫ ВВОДА-ВВЫВОДА
	//
	void send(int i, String s) {
		System.out.println(s);
	}
	
	static void log(String message){
		if(debug_descr) System.out.println("# " + message);
	}
}

class MudException extends Exception {}
