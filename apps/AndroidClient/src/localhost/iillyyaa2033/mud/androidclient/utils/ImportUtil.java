package localhost.iillyyaa2033.mud.androidclient.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import localhost.iillyyaa2033.mud.androidclient.logic.dictionary.WordScript;
import localhost.iillyyaa2033.mud.androidclient.logic.model.World;

public class ImportUtil {

	public static HashMap<String, String> loadUsercommands() throws FileNotFoundException {
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
				if (buffer.startsWith("##### ") && buffer.endsWith(" #####")) {
					if (!currName.equals(""))
						cmds.put(currName, currscript.toString());

					currscript = new StringBuffer();
					currName = buffer.substring(6, buffer.length() - 6);
					n = false;
				} else {
					currscript.append((n ? "\n" : "") + buffer);
					if (!n) n = !n;
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
		} catch (IOException e) {
			e(e);
		}

		return cmds;
	}

	public static ArrayList<WordScript> importWords() {
		return null;
	}

	public static void importToWorld(File file){

		try {
			ObjectInputStream reader = new ObjectInputStream(new FileInputStream(file));
			try {
				World world;
				world = (World) reader.readObject();
				if(world != null) WorldHolder.setInstance(world);
			} catch (ClassNotFoundException e) {
				e(e);
			}
			reader.close();
		} catch (IOException e) {
			e(e);
		}
	}

	private static void e(Throwable e) {
		ExceptionsStorage.exceptions.add(e);
	}
	
	static void e(String e){
		ExceptionsStorage.exceptions.add(new Exception(e));
	}
}
