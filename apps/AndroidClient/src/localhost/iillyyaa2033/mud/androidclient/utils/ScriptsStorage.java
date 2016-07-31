package localhost.iillyyaa2033.mud.androidclient.utils;

import java.util.ArrayList;
import java.util.HashMap;
import localhost.iillyyaa2033.mud.androidclient.exceptions.NoSuchCommandException;
import java.util.Arrays;

public class ScriptsStorage{
	
	public static String[] commandsNames;
	private static HashMap<String, String> commands;
	
	public static void putCommands(HashMap<String, String> _commands){
		commands = _commands;
		commandsNames = new String[commands.size()];
		commandsNames = commands.keySet().toArray(commandsNames);
	}
	
	public static boolean haveCommand(String name){
		for(String s : commandsNames){
			if(s.equals(name)) return true;
		}
		return false;
	}
	
	public static String getCommand(String name) throws NoSuchCommandException{
		
		if(!haveCommand(name)){
			for(String curr : commandsNames){
				if(curr.startsWith(name)) return commands.get(curr);
			}
			
			throw new NoSuchCommandException();
		} else {
			return commands.get(name);
		}
	}
}
