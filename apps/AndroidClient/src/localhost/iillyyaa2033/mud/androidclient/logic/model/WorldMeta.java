package localhost.iillyyaa2033.mud.androidclient.logic.model;

import java.io.Serializable;
import java.util.HashMap;

public class WorldMeta implements Serializable{
	// WorldMeta
	// - нода, которая может содержать еще 4*4 подноды;
	// - предоставляет различную инфу
	// - используется для физики, ии, описаний
	
	public int level = -1; 			// уровень вложенности, -1 == не определен
	public int[] position = {0,0};	// центр ноды
	public WorldMeta[] submeta = new WorldMeta[16];
	public HashMap<String, String> info = new HashMap<String, String>();
	
	public void tick(){
		// do smth
		for(WorldMeta m : submeta){
			if(m != null){
				m.tick();
			}
		}
	}
}
