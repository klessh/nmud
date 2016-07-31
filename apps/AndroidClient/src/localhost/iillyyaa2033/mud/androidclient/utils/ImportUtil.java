package localhost.iillyyaa2033.mud.androidclient.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.io.IOException;

public class ImportUtil{
	
	public static HashMap<String, String> loadUsecommands(){
		File work = new File(GlobalValues.datapath, "scripts/usercommands.txt");
		
		if (!work.exists()) {
			/* core.send(core.LEVEL_DEBUG_IMPORTER, "Importer: путь скриптов не существует.\n\nПроверь правильность:" + work.getAbsolutePath());
*/			return null;
		}
		
		HashMap<String, String> cmds = new HashMap<String, String>();
		
		String currName = "";
		StringBuffer currscript = new StringBuffer();
		try {
			InputStream inputstream = new FileInputStream(work);
			InputStreamReader reader = new InputStreamReader(inputstream, "UTF-8");
			BufferedReader bufferedreader = new BufferedReader(reader);
			
			String buffer = null;
			while ((buffer = bufferedreader.readLine()) != null) {
				if(buffer.startsWith("##### ") && buffer.endsWith(" #####")){
					cmds.put(currName, currscript.toString());
					currscript = new StringBuffer();
					currName = buffer.substring(6,buffer.length()-6);
				} else {
					currscript.append(buffer+"\n");
				}
			}
			cmds.put(currName, currscript.toString());
		} catch (FileNotFoundException e) {
			
		} catch (UnsupportedEncodingException e) {
			
		} catch (IOException e){
			
		}
		
		return cmds;
	}
}
