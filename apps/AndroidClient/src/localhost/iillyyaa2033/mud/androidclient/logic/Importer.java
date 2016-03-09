package localhost.iillyyaa2033.mud.androidclient.logic;

import android.os.Environment;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.logging.Logger;
import localhost.iillyyaa2033.mud.androidclient.logic.model.WorldObject;
import android.text.format.Time;
import java.util.Date;
import java.io.IOException;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import android.content.res.AssetManager;

public class Importer {

	Core core;
	
	public Importer(Core c){
		core = c;
	}
	
	public void saveUsers(HashMap<String, String> users) {
		// TODO: Implement this method
	}
	
	public HashMap<String, String> importChatScripts(){

		String scriptspath = Environment.getExternalStorageDirectory() + "/Download/nMud/scripts/";
//		String scriptspath = "file:///android_asset/nMud/scripts/";
		
		
		File work = new File(scriptspath);
		if(!work.exists()){
			/*if(core.debug)*/ core.send("# Importer: путь скриптов не существует.\n\nПроверь правильность:"+scriptspath);
			return null;
		}

		File[] list = work.listFiles(new FilenameFilter() {

				@Override
				public boolean accept(File dir, String name) {
					if(name.endsWith(".lua")) return true;
					return false;
				}
			});

		HashMap<String, String> cmds = new HashMap<String, String>();

		if(list == null || list.length <1){
			if(core.debug) core.send("# Improter: список луа-файлов "+(list==null ? "null" : "равен "+list.length)+". Путь - "+scriptspath);
			return cmds;
		}

		StringBuffer currscript = new StringBuffer();

		for(File currFile : list)
			try {
				InputStream inputstream = new FileInputStream(currFile);
				InputStreamReader reader = new InputStreamReader(inputstream, "UTF-8");
				BufferedReader bufferedreader = new BufferedReader(reader);

				String buffer = "";

				while((buffer = bufferedreader.readLine()) != null){
					currscript.append(buffer);
					currscript.append("\n");
				}

				String key = currFile.getName().substring(0, currFile.getName().length()-4);
				String value = currscript.toString();
				cmds.put(key, value);

				if(core.debug){
					if(value.contains("server:")) core.send("# Скрипт \""+key+"\" использует доступ к серверу.");
					if(value.contains("client:close")) core.send("# Скрипт \""+key+"\" может закрывать клиенты.");
				}
				
				currscript.setLength(0);
				bufferedreader.close();
			}catch (Exception e) {
				if(core.debug) core.send("Importer: ошибка ридеров"+e);
				return null;
			}
		return cmds;
	}
	
	public HashMap<String, String> importUsers(){

		File workfile = new File(Environment.getExternalStorageDirectory(), "/Download/nMud/users.csv");
		if(!workfile.exists()){
			if(core.debug) core.send("Importer: нету файла с акками");
			return null;
		}

		try {
			InputStream inputstream = new FileInputStream(workfile);
			InputStreamReader reader = new InputStreamReader(inputstream, "UTF-8");
			BufferedReader bufferedreader = new BufferedReader(reader);

			String buffer = "";
			StringTokenizer token;
			HashMap<String, String> users = new HashMap<String, String>();

			while((buffer = bufferedreader.readLine()) != null){
				token = new StringTokenizer(buffer, ",");
				if(token.countTokens()<=2){
					String usr = token.nextToken();
					String pwd = token.nextToken();
					users.put(usr,pwd);
				}
			}

			bufferedreader.close();
			return users;
		}catch (Exception e) {
			if(core.debug) core.send("Importer: ошибка импорта акков"+e);
			return null;
		}
	}
	
	public ArrayList<WorldObject> importObjects(){
		File workfile = new File(Environment.getExternalStorageDirectory(), "/Download/nMud/maps/testmap.txt");
		
		if(!workfile.exists()){
			if(core.debug) core.send("Importer: нету файла с акками");
			return null;
		}

		try {
			InputStream inputstream = new FileInputStream(workfile);
			InputStreamReader reader = new InputStreamReader(inputstream, "UTF-8");
			BufferedReader bufferedreader = new BufferedReader(reader);

			String buffer = "";
			StringTokenizer token;
			ArrayList<WorldObject> objs = new ArrayList<WorldObject>();

			while((buffer = bufferedreader.readLine()) != null){
				token = new StringTokenizer(buffer, "|");
				if(token.countTokens()==5){
					WorldObject obj = new WorldObject();
					obj.x = new Integer(token.nextToken());
					obj.y = new Integer(token.nextToken());
					obj.x2 = new Integer(token.nextToken());
					obj.y2 = new Integer(token.nextToken());
					obj.name = token.nextToken();
					
					objs.add(obj);
				}
			}

			bufferedreader.close();
			return objs;
		}catch (Exception e) {
			if(core.debug) core.send("Importer: ошибка импорта акков"+e);
			return null;
		}
	}
	
	void saveReport(ArrayList<String> report){
		long time = System.currentTimeMillis()/1000;
		File workfile = new File(Environment.getExternalStorageDirectory(), "/Download/nMud/report_"+time+".txt");
		try {
			workfile.createNewFile();
			OutputStream stream = new FileOutputStream(workfile);
			OutputStreamWriter writer = new OutputStreamWriter(stream);
			for(String s : report){
				writer.write(s+"\n");
			}
			writer.flush();
			writer.close();
			stream.close();
		} catch (IOException e) {}

	}
}
