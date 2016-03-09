package localhost.iillyyaa2033.mud.androidclient.logic;

import java.util.ArrayList;
import localhost.iillyyaa2033.mud.androidclient.logic.model.ChunkManager;
import localhost.iillyyaa2033.mud.androidclient.logic.model.WorldObject;

public class Database{
	
	public ArrayList<WorldObject> objects;
	ArrayList<ChunkManager> chunks;
	
	public Database(Core c){
		objects = c.importer.importObjects();
	}
	
	public int getChunkId(int x, int y){
		return 1;
	}
	
	public WorldObject getObjectById(){
		return null;
	}
}
