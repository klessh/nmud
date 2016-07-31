package localhost.iillyyaa2033.descrstest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import localhost.iillyyaa2033.descrstest.described.DescribedObject;
import localhost.iillyyaa2033.descrstest.described.DescribedZone;
import localhost.iillyyaa2033.descrstest.dictionary.Dictionary;
import localhost.iillyyaa2033.descrstest.dictionary.Word;
import localhost.iillyyaa2033.descrstest.graph.Graph;
import localhost.iillyyaa2033.descrstest.graph.GraphUtils;
import localhost.iillyyaa2033.descrstest.model.Material;
import localhost.iillyyaa2033.descrstest.model.WorldObject;

public class Main {

	static boolean debug_descr = !true;
	static boolean debug_graph = true;
	static ArrayList<WorldObject> objects;
	static Dictionary dict;

	static ArrayList<DescribedObject> objs;
	static ArrayList<DescribedZone> zones;
	static HashMap<Integer, Integer[]> links;

	static long start = 0, last = 0;

	public static void main(String[] args) {
		start = last = System.currentTimeMillis();

		dict = new Dictionary();

		objects = new ArrayList<WorldObject>();
		objects.add(new WorldObject("room", 1, 1, 18, 18, new Word(1, 1)));
		objects.add(new WorldObject("obj", 5, 5, 6, 6, new Word(1, 2), new Material(new Word(Dictionary.CH_R.ADJECTIVE, 5), "дерево")));
		objects.add(new WorldObject("obj", 7, 7, 9, 9, new Word(1, 3), new Material(new Word(Dictionary.CH_R.ADJECTIVE, 6), "картон")));
		objects.add(new WorldObject("obj", 16, 2, 17, 3, new Word(1, 4), new Material(new Word(Dictionary.CH_R.ADJECTIVE, 7), "стекло")));

		objs = new ArrayList<DescribedObject>();
		zones = new ArrayList<DescribedZone>();
		links = new HashMap<Integer,Integer[]>();	// связь между id комнаты и id объектов в ней


		for (int i = 0; i < objects.size(); i++) {
			WorldObject obj = objects.get(i);

			if (obj.type == null) {
				log("Object haven't any type: " + dict.getWord(obj.name));
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
					obj.dirCode = getDirectionCode(obj, zone);

					log("Added link " + zone.id + "->" + obj.id + " with dircode " + obj.dirCode);

					break;
				}
			}
		}
		logTime();
		altDescription(10, 10, 0);
		logTime();
	}

	// DESCRIPTION METHODS

	static void altDescription(int x0, int y0, int deg) {
		// TODO: положение и описание объектов

		ArrayList<Graph> graphs = new ArrayList<Graph>();

		// Версия со сложением графов


//	/*
		for (DescribedObject obj : objs) {
			Graph current = describePosition(obj);

			for (int i = 0; i < graphs.size(); i++) {
				Graph other = graphs.get(i);
				if (GraphUtils.canAppendAuto(3, current, other)) {
					GraphUtils.appendAuto(3, current, other);
				} else { 
					graphs.add(current);
				}
				break;
			}

			if (graphs.size() == 0) graphs.add(current);
		}

//		 */

		/*
		 // Версия с выбором по dirCode
		 HashMap<Integer, Integer[]> map = new HashMap<Integer, Integer[]>();
		 for (int i = 0; i < objs.size(); i++) {
		 DescribedObject bj = objs.get(i);
		 Integer[] newList;
		 if (map.containsKey(bj.dirCode)) {
		 newList = Arrays.copyOf(map.get(bj.dirCode), map.get(bj.dirCode).length + 1);
		 map.remove(bj.dirCode);
		 } else {
		 newList = new Integer[1];
		 }
		 newList[newList.length - 1] = i;
		 map.put(bj.dirCode, newList);
		 }

		 Integer[] keys = new Integer[map.size()];
		 keys = map.keySet().toArray(keys);

		 for (int key : keys) {
		 Graph subject = describeObjectsWithArrayOfIds(map.get(key));
		 int areId = subject.add(0, new Word(Dictionary.CH_R.VERB, 9, 1));
		 Graph place = getDirectionGraph(key,zones.get(0));
		 subject.append(areId,GraphUtils.rootId,place);
		 graphs.add(subject);
		 }
		 //		 */

		for (Graph gr : graphs) {
			System.out.println(GraphUtils.graphToText(gr));
		}

		if (debug_graph) {System.out.println("\n\n#### GRAPHS ####");
			for (Graph gr : graphs) {
				printGraph(gr);
				System.out.println();
			}
		}
	}


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

	//
	//	ЦИФРЫ -> ГРАФ
	//
	static Graph describePosition(DescribedObject object) {
		Word ARE = new Word(Dictionary.CH_R.VERB, 9, 1);

		Graph result = new Graph();
		int nameId= result.add(GraphUtils.rootId, object.name);		// starting with object's name

		int areId = result.add(nameId, ARE); 		// obj's name -> are
		result.add(nameId, object.material.color);	// obj's name -> color
		Graph place = getDirectionGraph(getDirectionCode(object, zones.get(0)), zones.get(0));	// are -> place
		result.append(GraphUtils.rootId, areId, place);
		return result;
	}

	static int getDirectionCode(DescribedObject object, DescribedZone zone) {
		int stepX = getDistance(zone.x, zone.y, zone.x2, zone.y) / 5;
		int stepY = getDistance(zone.x, zone.y, zone.x, zone.y2) / 5;

		int code = 0;

		if (object.yc < (zone.y + stepY)) code = 1000;
		else if (object.yc > (zone.y + stepY * 4)) code = 3000;
		else code = 2000;

		if (object.xc < (zone.x + stepX)) code += 10;
		else if (object.xc > (zone.x + stepX * 4)) code += 30;
		else code += 20;

		return code;
	}

	static Graph getDirectionGraph(int code, DescribedZone zone) {

		Graph result = new Graph();
		Word DOWN = new Word(Dictionary.CH_R.ADJECTIVE, 1);
		Word TOP  = new Word(Dictionary.CH_R.ADJECTIVE, 4);
		Word LEFT = new Word(Dictionary.CH_R.ADJECTIVE, 2);
		Word RIGHT= new Word(Dictionary.CH_R.ADJECTIVE, 3);
		Word CORNER=new Word(Dictionary.CH_R.NOUN, 5);
		Word WALL = new Word(Dictionary.CH_R.NOUN, 6);
		Word CENTER=new Word(Dictionary.CH_R.NOUN, 7);

		Word IN = new Word(Dictionary.CH_R.PREPOSITION, 1);
		Word NEAR = new Word(Dictionary.CH_R.PREPOSITION, 2);

		int prepId = -1;
		int wordId = -1;

		switch (code) {
			case DirCode.LEFT_DOWN_CORNER:
				prepId = result.add(GraphUtils.rootId, IN);
				wordId = result.add(prepId, CORNER);
				result.add(wordId, LEFT);
				result.add(wordId, DOWN);
				break;
			case DirCode.DOWN_WALL:
				prepId = result.add(GraphUtils.rootId, NEAR);
				wordId = result.add(prepId, WALL);
				result.add(wordId, DOWN);
				break;
			case DirCode.RIGHT_DOWN_CORNER:	
				prepId = result.add(GraphUtils.rootId, IN);
				wordId = result.add(prepId, CORNER);
				result.add(wordId, RIGHT);
				result.add(wordId, DOWN);
				break;
			case DirCode.LEFT_WALL:
				prepId = result.add(GraphUtils.rootId, NEAR);
				wordId = result.add(prepId, WALL);
				result.add(wordId, LEFT);
				break;
			case DirCode.CENTER:	
				prepId = result.add(GraphUtils.rootId, NEAR);
				wordId = result.add(prepId, CENTER);
				break;
			case DirCode.RIGHT_WALL:	
				prepId = result.add(GraphUtils.rootId, NEAR);
				wordId = result.add(prepId, WALL);
				result.add(wordId, RIGHT);
				break;
			case DirCode.LEFT_TOP_CORNER:
				prepId = result.add(GraphUtils.rootId, IN);
				wordId = result.add(prepId, CORNER);
				result.add(wordId, LEFT);
				result.add(wordId, TOP);
				break;
			case DirCode.TOP_WALL:	
				prepId = result.add(GraphUtils.rootId, NEAR);
				wordId = result.add(prepId, WALL);
				result.add(wordId, TOP);
				break;
			case DirCode.RIGHT_TOP_CORNER:	
				prepId = result.add(GraphUtils.rootId, IN);
				wordId = result.add(prepId, CORNER);
				result.add(wordId, RIGHT);
				result.add(wordId, TOP);
				break;
		}

		result.add(wordId, zone.name);

		return result;
	}

	static Graph describeObjectsWithArrayOfIds(Integer[] array) {
		Graph gr = new Graph();

		int lastId = GraphUtils.rootId;
		for (Integer i : array) {
			lastId = gr.add(lastId, objs.get(i).name);
		}
		return gr;
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

	static void log(String message) {
		if (debug_descr) System.out.println("# " + message);
	}

	static void logTime() {
		long current = System.currentTimeMillis();
		System.out.println("# Last step: " + (current - last) + "ms; in total: " + (current - start) + "ms.");
		last = System.currentTimeMillis();
	}

	static void printGraph(Graph gr) {
		String divider = " ";
		for (int i =0; i < gr.words.length; i++) {
			System.out.print(gr.words[i] + divider);
		}
		System.out.println();

		for (int i = 0; i < gr.links.length; i++) {
			System.out.println(gr.links[i] + divider + gr.links[++i] + divider + gr.links[++i]);
		}
	}

	//
	//	ПРОЧЕЕ
	//
	class DirCode {
		final static int LEFT_DOWN_CORNER 	= 1010;
		final static int DOWN_WALL 			= 1020;
		final static int RIGHT_DOWN_CORNER 	= 1030;
		final static int LEFT_WALL 			= 2010;
		final static int CENTER 			= 2020;
		final static int RIGHT_WALL 		= 2030;
		final static int LEFT_TOP_CORNER	= 3010;
		final static int TOP_WALL 			= 3020;
		final static int RIGHT_TOP_CORNER 	= 3030;
	}
}

class MudException extends Exception {}
