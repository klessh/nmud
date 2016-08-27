package localhost.iillyyaa2033.mud.androidclient.logic.model;

import java.util.ArrayList;
import localhost.iillyyaa2033.mud.androidclient.exceptions.IncorrectPositionException;

public class Zone extends WorldObject{
	
	ArrayList<WorldObject> objects;
	
	public void addObject(WorldObject obj) throws IncorrectPositionException{
		if(obj.x < this.x || obj.y < this.y || obj.x2 > this.x2 || obj.y2 > this.y2)
			throw new IncorrectPositionException();
		else
			objects.add(obj);
	}
}
