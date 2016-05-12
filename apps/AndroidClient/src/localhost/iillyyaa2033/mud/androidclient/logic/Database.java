package localhost.iillyyaa2033.mud.androidclient.logic;

import android.preference.PreferenceManager;
import com.iillyyaa2033.nmud.abstractserver.model.ChunkManager;
import com.iillyyaa2033.nmud.abstractserver.model.WorldObject;
import java.util.ArrayList;

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
