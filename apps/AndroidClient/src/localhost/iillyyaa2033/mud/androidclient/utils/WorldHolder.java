package localhost.iillyyaa2033.mud.androidclient.utils;

import localhost.iillyyaa2033.mud.androidclient.logic.model.World;

public class WorldHolder{
	
	private static World _world = null;
	public static World getInstance(){
		if(_world == null) _world = new World();
		return _world;
	}
	public static void setInstance(World world){
		_world = world;
	}
}
