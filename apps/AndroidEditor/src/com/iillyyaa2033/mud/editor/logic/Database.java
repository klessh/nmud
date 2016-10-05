package com.iillyyaa2033.mud.editor.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import localhost.iillyyaa2033.mud.androidclient.logic.model.Zone;
import localhost.iillyyaa2033.mud.androidclient.logic.model.World;

// Прикол в том, что могут быть проблемы с многопоточностью
// TODO: проверить утверждение выше
public class Database{
	
	public static ArrayList<String> dictNames = new ArrayList<String>();
	public static ArrayList<String> dict = new ArrayList<String>();
	public static void uploadDict(HashMap<String,String> _dict){
		 Set<String> keys = _dict.keySet();
		 
		 for(String key : keys){
			 dictNames.add(key);
			 dict.add(_dict.get(key));
		 }
	}
}
