package localhost.iillyyaa2033.mud.androidclient.logic.model;

import java.io.Serializable;
import java.util.ArrayList;
import localhost.iillyyaa2033.mud.androidclient.exceptions.IncorrectPositionException;

public class World implements Serializable{
	
	public World(){};
	
	public ArrayList<Zone> nonmeta = new ArrayList<Zone>();
	public ArrayList<WorldMeta> meta = new ArrayList<WorldMeta>();
	public ArrayList<WorldObject> objects = new ArrayList<WorldObject>();
	
	public void tick(){
		for(WorldMeta m : meta){
			m.tick();
		}
		
		for(Zone z : nonmeta){
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
	
	public WorldMeta getMetaFor(int x, int y, int maxLvl){
		// TODO: meta here!
		return null;
	}
	
	public void add(WorldObject object){
		objects.add(object);
	}
	
	public void remove(WorldObject obj){
		objects.remove(obj);
	}
}
