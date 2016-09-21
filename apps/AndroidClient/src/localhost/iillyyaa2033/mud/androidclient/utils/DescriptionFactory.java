package localhost.iillyyaa2033.mud.androidclient.utils;

import localhost.iillyyaa2033.mud.androidclient.logic.model.WorldObject;
import localhost.iillyyaa2033.mud.androidclient.logic.model.Zone;
import localhost.iillyyaa2033.mud.androidclient.logic.model.Creature;

public class DescriptionFactory{
	
	Zone currentZone;
	// TODO: player's position here
	
	public DescriptionFactory(Zone z, Creature player){
		currentZone = z;
	}
	
	public void prepare(){
		if(currentZone == null) throw new NullPointerException("DescriptionFactory: current zone is null");
		return;
	}
	
	public String getSampleDescr(){
		String result = "";
		result += currentZone.params.get("string-name").toUpperCase()+". ";
		result += currentZone.params.get("string-descr");
		return result;
	}
	
	public String getAutoDescription(){
		String result = "Авто-описания отключены, выведен список объектов:";
		for(WorldObject obj : currentZone.objects) result += " \t" + obj.params.get("string-name").toLowerCase();
		result += "\n";
		return result;
	}
	
	public String getHandDescriptionOf(String objectName){
		String result = "# Объект не найден";
		objectName = objectName.toLowerCase();
		
		for(WorldObject obj : currentZone.objects){
			if(objectName.equals(obj.params.get("string-name").toLowerCase()))
				result = obj.params.get("string-descr");
		}
		
		return result;
	}
}
