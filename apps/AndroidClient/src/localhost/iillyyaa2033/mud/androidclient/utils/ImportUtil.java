package localhost.iillyyaa2033.mud.androidclient.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import localhost.iillyyaa2033.mud.androidclient.logic.model.Zone;
import localhost.iillyyaa2033.mud.androidclient.logic.dictionary.WordScript;
import localhost.iillyyaa2033.mud.androidclient.logic.model.WorldObject;

public class ImportUtil{
	
	public static HashMap<String, String> loadUsercommands() throws FileNotFoundException{
		File work = new File(GlobalValues.datapath, "scripts/usercommands.txt");
		
		if (!work.exists()) {
			throw new FileNotFoundException("ImportUtil - loadUsercommands - 404");
		}
		
		HashMap<String, String> cmds = new HashMap<String, String>();
		
		try {
			InputStream inputstream = new FileInputStream(work);
			InputStreamReader reader = new InputStreamReader(inputstream, GlobalValues.encoding);
			BufferedReader bufferedreader = new BufferedReader(reader);
			
			String buffer = null;
			String currName = "";
			StringBuffer currscript = new StringBuffer();
			boolean n = false;
			while ((buffer = bufferedreader.readLine()) != null) {
				if(buffer.startsWith("##### ") && buffer.endsWith(" #####")){
					if(!currName.equals(""))
						cmds.put(currName, currscript.toString());
						
					currscript = new StringBuffer();
					currName = buffer.substring(6,buffer.length()-6);
					n = false;
				} else {
					currscript.append((n ? "\n" : "")+buffer);
					if(!n) n = !n;
				}
			}
			cmds.put(currName, currscript.toString());
			
			bufferedreader.close();
			reader.close();
			inputstream.close();
		} catch (FileNotFoundException e) {
			e(e);
		} catch (UnsupportedEncodingException e) {
			e(e);
		} catch (IOException e){
			e(e);
		}
		
		return cmds;
	}
	
	public static void saveUsercommands(HashMap<String, String> cmds) throws FileNotFoundException{
		File work = new File(GlobalValues.datapath, "scripts/usercommands.txt");

		if (!work.exists()) {
			throw new FileNotFoundException("ImportUtil - saveUsercommands - 404");
		}
		
		try {
			OutputStream stream = new FileOutputStream(work);
			OutputStreamWriter writer = new OutputStreamWriter(stream, GlobalValues.encoding);
			BufferedWriter bufferedwriter = new BufferedWriter(writer);
			
			Set<String> set = cmds.keySet();
			for(String cmd : set){
				writer.write("##### "+cmd+" #####\n");
				writer.write(cmds.get(cmd)+"\n");
			}
			
			bufferedwriter.close();
			writer.close();
			stream.close();
		} catch (UnsupportedEncodingException e) {
			e(e);
		} catch (IOException e) {
			e(e);
		}
	}
	
	public static ArrayList<WordScript> importWords(){
		return null;
	}
	
	public static ArrayList<Zone> importZones() throws FileNotFoundException{
		File work = new File(GlobalValues.datapath, "maps/main.txt");

		if (!work.exists()) {
			throw new FileNotFoundException("ImportUtil - importZones - 404");
		}

		ArrayList<Zone> zones = new ArrayList<Zone>();
		
		try {
			InputStream inputstream = new FileInputStream(work);
			InputStreamReader reader = new InputStreamReader(inputstream, GlobalValues.encoding);
			BufferedReader bufferedreader = new BufferedReader(reader);
			
			String line = "";
			ArrayList<String> lines = new ArrayList<String>();
			
			while((line = bufferedreader.readLine()) != null){
				if(line.equals("####")){
					mapWork(zones,lines);
					lines = new ArrayList<String>();
				} else {
					lines.add(line);
				}
			}
			mapWork(zones,lines);
		}catch(IOException e){
			e(e);
		}
		return zones;
	}
	
	private static void mapWork(ArrayList<Zone> zones, ArrayList<String> lines){
		
		Zone currentZone = null;
		WorldObject currentObject;
		
		if(lines.get(0).startsWith("zone")){
			currentZone = new Zone();
			currentObject = currentZone;
			zones.add(currentZone);
		} else {
			currentObject = new WorldObject();
		}
		
		for(int i = 0; i < lines.size(); i++){
			String line = lines.get(i);
			int div = line.indexOf(": ");
			String key = line.substring(0,div);
			String val = line.substring(div+2);
			currentObject.params.put(key,val);
		}
		
		if(currentZone == null && currentObject != null){
			try{
				String bigId = currentObject.params.get("object");
		//		String objId = bigId.substring(bigId.indexOf('-'));
				String zonId = "1";//bigId.substring(0,bigId.indexOf('-'));
				for(Zone zone : zones){
					if(zone.params.get("zone").equals(zonId))
						zone.addObject(currentObject);
				}
			} catch (Exception e){
				e(e);
			}
		}
	}
	
	private static void e(Throwable e){
		ExceptionsStorage.exceptions.add(e);
	}
}
