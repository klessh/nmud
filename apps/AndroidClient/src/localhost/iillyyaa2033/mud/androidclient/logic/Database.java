package localhost.iillyyaa2033.mud.androidclient.logic;

import android.preference.PreferenceManager;
import java.util.ArrayList;
import localhost.iillyyaa2033.mud.androidclient.logic.model.ChunkManager;
import localhost.iillyyaa2033.mud.androidclient.logic.model.WorldObject;

public class Database{
	
	private Core c;
	public ArrayList<WorldObject> objects;
	ArrayList<ChunkManager> chunks;
	
	// TODO: move it anywhere else, coz db need to be updatet after importer extracted data
	public String datapath;
	public String encoding_contentarchive = "UTF-8";
	
	
	public Database(Core c){
		this.c = c;
		datapath = PreferenceManager.getDefaultSharedPreferences(c.activity).getString("DATAPATH","");
	}
	
	public void update(){
		objects = c.importer.importObjects();
	}
	
	public int getChunkId(int x, int y){
		return 1;
	}
	
	public WorldObject getObjectById(){
		return null;
	}
	
}
