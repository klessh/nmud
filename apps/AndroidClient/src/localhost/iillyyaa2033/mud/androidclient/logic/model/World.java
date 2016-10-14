package localhost.iillyyaa2033.mud.androidclient.logic.model;

import java.util.ArrayList;
import java.util.Arrays;
import localhost.iillyyaa2033.mud.androidclient.exceptions.IncorrectPositionException;

public class World{
	
	public static ArrayList<Zone> zones = new ArrayList<Zone>();
	public static ArrayList<WorldObject> uncat = new ArrayList<WorldObject>();
	
	public static WorldObject[] searchById(String id){
		ArrayList<WorldObject> obj = new ArrayList<WorldObject>();
		
		for(Zone z : zones){
			if(z.getId()!=null) if(z.getId().equals(id)) obj.add(z);
			
			for(WorldObject o : z.objects){
				if(o.getId()!=null) if(o.getId().equals(id)) obj.add(o);
			}
		}
		
		WorldObject[] array = new WorldObject[obj.size()];
		array = obj.toArray(array);
		return array;
	}
	
	public static void autoAddToZone(WorldObject object){
		for(Zone z : zones){
			try {
				z.addObject(object);
				return;
			} catch (IncorrectPositionException e) {
				
			}
		}
		uncat.add(object);
	}
}
