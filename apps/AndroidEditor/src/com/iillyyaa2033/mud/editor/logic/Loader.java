package com.iillyyaa2033.mud.editor.logic;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.preference.PreferenceManager;
import com.iillyyaa2033.mud.editor.view.MapView;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.StringTokenizer;
import android.util.Log;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

public class Loader {

	/*	public String[][] loadNametable(String nameofnametable, String[][] qualitys) throws IOException {
	 if (nameofnametable == null) return qualitys;

	 File f = new File(PreferenceManager.getDefaultSharedPreferences(c).getString("PREF_MAP_ROOT_NAMETABLES", "/storage/emulated/0/Download/") + nameofnametable.toLowerCase() + ".nametab");

	 if (!f.exists()) return qualitys;

	 InputStream inputstream = new FileInputStream(f);
	 InputStreamReader reader = new InputStreamReader(inputstream, "UTF-8");
	 BufferedReader bufferedreader = new BufferedReader(reader);

	 String line = "";
	 String[][] str = new String[qualitys.length][];
	 int i = 0;
	 while (((line = bufferedreader.readLine()) != null) && i < qualitys.length) {
	 String delims = "|];";
	 StringTokenizer token = new StringTokenizer(line, delims, false);
	 String[] substr = new String[token.countTokens()];

	 int y = 0;
	 while (token.hasMoreTokens()) {
	 substr[y] = token.nextToken();
	 y++;
	 }
	 str[i] = substr;
	 i++;
	 }
	 return str;
	 }

	 public void saveNametable(String[][] nametable) throws IOException {
	 if (nametable == null) return;

	 File f = new File(PreferenceManager.getDefaultSharedPreferences(c).getString("PREF_MAP_ROOT_NAMETABLES", "/storage/emulated/0/Download/") + nametable[0][0].toLowerCase() + ".nametab");
	 if (!f.exists()) f.createNewFile();

	 OutputStream outputstream = new FileOutputStream(f);
	 OutputStreamWriter writer = new OutputStreamWriter(outputstream, "UTF-8");
	 //		writer.write("["+nametable[0][0]+"]\n");
	 for (int i = 0; i < nametable.length; i++) {
	 for (int y = 0; y < nametable[i].length; y++) {
	 writer.write(nametable[i][y] + "|");
	 }
	 writer.write("\n");
	 }
	 writer.flush();
	 }	*/

	public static HashMap<String,String> loadDictionary() {

		HashMap<String,String> mapa = new HashMap<String,String>();

		try {
			InputStream inputstream = new FileInputStream(new File("/storage/emulated/0/AppProjects/nmud/content-ru/dict/ru.txt"));
			InputStreamReader reader = new InputStreamReader(inputstream, "UTF-8");
			BufferedReader bufferedreader = new BufferedReader(reader);
			
			String line = "";
			String key = null, value = null;
			while ((line = bufferedreader.readLine()) != null) {
				if(line.startsWith("### ") && line.endsWith(" ###")){
					if(value != null){
						mapa.put(key,value);
						value = null;
					}
					
					key = line.substring(4,line.length()-4);
				} else {
					if(value == null) value = line;
					else value += "\n"+line;
				}
			}
			if(value != null)mapa.put(key,value);
		} catch (UnsupportedEncodingException e) {
			Log.e("nmud-edit","",e);
		} catch (IOException e) {
			Log.e("nmud-edit","",e);
		}

		return mapa;
	}
}
