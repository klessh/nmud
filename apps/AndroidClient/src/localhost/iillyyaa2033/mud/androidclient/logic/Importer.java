package localhost.iillyyaa2033.mud.androidclient.logic;

import android.content.Context;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;
import localhost.iillyyaa2033.mud.androidclient.logic.model.WorldObject;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

public class Importer {

	Core core;
	
	public Importer(Core c) {
		core = c;
	}

	public synchronized HashMap<String, String> importChatScripts() {

		File work = new File(core.db.datapath, "scripts");
		if (!work.exists()) {
			/**/ core.send(core.LEVEL_DEBUG_IMPORTER, "Importer: путь скриптов не существует.\n\nПроверь правильность:" + work.getAbsolutePath());
			return null;
		}

		File[] list = work.listFiles(new FilenameFilter() {

				@Override
				public boolean accept(File dir, String name) {
					if (name.endsWith(".lua")) return true;
					return false;
				}
			});

		HashMap<String, String> cmds = new HashMap<String, String>();

		if (list == null || list.length < 1) {
			core.send(core.LEVEL_DEBUG_IMPORTER, "Improter: список луа-файлов " + (list == null ? "null" : "равен " + list.length) + ". Путь - " + work.getAbsolutePath());
			return cmds;
		}

		StringBuffer currscript = new StringBuffer();

		for (File currFile : list)
			try {
				InputStream inputstream = new FileInputStream(currFile);
				InputStreamReader reader = new InputStreamReader(inputstream, "UTF-8");
				BufferedReader bufferedreader = new BufferedReader(reader);

				String buffer = "";

				while ((buffer = bufferedreader.readLine()) != null) {
					currscript.append(buffer);
					currscript.append("\n");
				}

				String key = currFile.getName().substring(0, currFile.getName().length() - 4);
				String value = currscript.toString();
				cmds.put(key, value);

				
					if (value.contains("server:")) core.send(core.LEVEL_DEBUG_IMPORTER, "Скрипт \"" + key + "\" использует доступ к серверу.");
					if (value.contains("client:close")) core.send(core.LEVEL_DEBUG_IMPORTER, "Скрипт \"" + key + "\" может закрывать клиенты.");
				

				currscript.setLength(0);
				bufferedreader.close();
			} catch (Exception e) {
				core.send("Importer: ошибка ридеров" + e);
				return null;
			}
		return cmds;
	}

	public HashMap<String, String> importUsers() {

		File workfile = new File(core.db.datapath, "users.csv");
		if (!workfile.exists()) {
			core.send(core.LEVEL_DEBUG_IMPORTER, "Importer: нету файла с акками");
			return null;
		}

		try {
			InputStream inputstream = new FileInputStream(workfile);
			InputStreamReader reader = new InputStreamReader(inputstream, "UTF-8");
			BufferedReader bufferedreader = new BufferedReader(reader);

			String buffer = "";
			StringTokenizer token;
			HashMap<String, String> users = new HashMap<String, String>();

			while ((buffer = bufferedreader.readLine()) != null) {
				token = new StringTokenizer(buffer, ",");
				if (token.countTokens() <= 2) {
					String usr = token.nextToken();
					String pwd = token.nextToken();
					users.put(usr, pwd);
				}
			}

			bufferedreader.close();
			return users;
		} catch (Exception e) {
			core.send("Importer: ошибка импорта акков" + e);
			return null;
		}
	}

	public void saveUsers(HashMap<String, String> users) {
		// TODO: Implement this method
	}
	
	public ArrayList<WorldObject> importObjects() {
		File workfile = new File(core.db.datapath, "/maps/testmap.txt");

		if (!workfile.exists()) {
			core.send(core.LEVEL_DEBUG_IMPORTER,"Importer: нету файла с акками");
			return null;
		}

		try {
			InputStream inputstream = new FileInputStream(workfile);
			InputStreamReader reader = new InputStreamReader(inputstream, "UTF-8");
			BufferedReader bufferedreader = new BufferedReader(reader);

			String buffer = "";
			StringTokenizer token;
			ArrayList<WorldObject> objs = new ArrayList<WorldObject>();

			while ((buffer = bufferedreader.readLine()) != null) {
				token = new StringTokenizer(buffer, "|");
				if (token.countTokens() == 5) {
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
		} catch (Exception e) {
			core.send("Importer: ошибка импорта акков" + e);
			return null;
		}
	}

	
	String[][] forms = new String[][]{
		new String[]{"",	"а",	"у",	"",		"ом",	"е"},
		new String[]{"а",	"и",	"е",	"у",	"ей",	"е"},
		new String[]{"а",	"ы",	"е",	"у",	"ой",	"е"},
		new String[]{"й",	"я",	"ю",	"й",	"ем",	"е"}
	};
	
	public HashMap<Integer, String[]> importWords(String endfilename){
		
		File workfile = new File(core.db.datapath, "/dict/"+endfilename+".txt");

		if (!workfile.exists()) {
			core.send(core.LEVEL_DEBUG_IMPORTER,"Importer: нету файла с со словарем ("+workfile.getAbsolutePath()+").");
			return null;
		}
		
		try {
			InputStream inputstream = new FileInputStream(workfile);
			InputStreamReader reader = new InputStreamReader(inputstream, "UTF-8");
			BufferedReader bufferedreader = new BufferedReader(reader);

			String buffer = "";
			StringTokenizer token;
			HashMap<Integer, String[]> objs = new HashMap<Integer, String[]>();

			while ((buffer = bufferedreader.readLine()) != null) {
				token = new StringTokenizer(buffer, "|");
				
				if (token.countTokens() == 3) {
					int id = new Integer(token.nextToken());
					String base = token.nextToken();
					int end = new Integer(token.nextToken());
					String[] word = new String[6];
					
					core.send(core.LEVEL_DEBUG_IMPORTER,""+id);
					
					for(int i = 0; i < 6; i++){
						word[i] = base+forms[end][i];
						if(core.debug_importer) core.append(" "+word[i]);
					}
					objs.put(id,word);
				} else {
					core.send(core.LEVEL_DEBUG_IMPORTER, "Malformed line; "+token.countTokens());
				}
			}

			bufferedreader.close();
			return objs;
		} catch (Exception e) {
			core.send("Importer: ошибка импорта акков" + e);
			return null;
		}
	}
	
	public void extractContent(Context c) {
		File f = new File(c.getCacheDir() + "/content-base.zip");
		
		core.send(core.LEVEL_DEBUG_IMPORTER, "Importing content... ");
		
		if (!f.exists()){
			try {

				InputStream is = c.getAssets().open("content-base.zip");
				int size = is.available();
				byte[] buffer = new byte[size];
				is.read(buffer);
				is.close();
				
				FileOutputStream fos = new FileOutputStream(f);
				fos.write(buffer);
				fos.close();
				core.append(core.LEVEL_DEBUG_IMPORTER, "Successfully.");
			} catch (Exception e) {
				core.append(core.LEVEL_DEBUG_IMPORTER, "Failed.");
				core.send(core.LEVEL_DEBUG_IMPORTER,e.getMessage());
			}
		}
	}

	public void unzip(String source, String destination) {

		try {
			ZipFile zipFile = new ZipFile(source);
			zipFile.setFileNameCharset(core.db.encoding_contentarchive);
			if (zipFile.isEncrypted()) {
				core.send(core.LEVEL_DEBUG_IMPORTER, "Архив зашифрован. Распаковка отменена.");
				return;
			}
			zipFile.extractAll(destination);
		} catch (ZipException e) {
			core.send(core.LEVEL_DEBUG_IMPORTER, e.getMessage());
		}
	}

	void saveReport(ArrayList<String> report) {
		long time = System.currentTimeMillis() / 1000;
		File workfile = new File(core.db.datapath, "/report_" + time + ".txt");
		try {
			workfile.createNewFile();
			OutputStream stream = new FileOutputStream(workfile);
			OutputStreamWriter writer = new OutputStreamWriter(stream);
			for (String s : report) {
				writer.write(s + "\n");
			}
			writer.flush();
			writer.close();
			stream.close();
		} catch (IOException e) {}

	}
}
