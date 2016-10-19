package localhost.iillyyaa2033.mud.androidclient.logic.model;

import java.io.Serializable;
import java.util.ArrayList;
import localhost.iillyyaa2033.mud.androidclient.exceptions.IncorrectPositionException;

public class World implements Serializable{
	
	public World(){};
	
	public ArrayList<Zone> meta = new ArrayList<Zone>();
	public ArrayList<WorldObject> objects = new ArrayList<WorldObject>();
	
	public void tick(){
		for(Zone z : meta){
			z.tick();
		}
		
		for(WorldObject o : objects){
			o.tick();
		}
	}
	
	public WorldObject[] searchById(String id){
		ArrayList<WorldObject> obj = new ArrayList<WorldObject>();
		
		for(WorldObject o : objects){
			if(o.getId()!=null) if(o.getId().equals(id)) obj.add(o);
		}
		
		WorldObject[] array = new WorldObject[obj.size()];
		array = obj.toArray(array);
		return array;
	}
	
	public void autoAddToZone(WorldObject object){
		for(Zone z : meta){
			try {
				z.addObject(object);
				return;
			} catch (IncorrectPositionException e) {
				
			}
		}
		objects.add(object);
	}
	
	public void remove(WorldObject obj){
		
	}
}
