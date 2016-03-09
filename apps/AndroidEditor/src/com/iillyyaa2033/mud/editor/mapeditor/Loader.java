package com.iillyyaa2033.mud.editor.mapeditor;

import android.preference.PreferenceManager;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.StringTokenizer;
import android.content.Context;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class Loader{
	
	Context c;
	Loader l;
	
	public Loader(Context c){
		this.c = c;
		if(l == null) l = this;
	}
	
	String[][] loadNametable(String nameofnametable, String[][] qualitys) throws IOException{
		if(nameofnametable == null) return qualitys;

		File f = new File(PreferenceManager.getDefaultSharedPreferences(c).getString("PREF_MAP_ROOT_NAMETABLES","/storage/emulated/0/Download/")+nameofnametable.toLowerCase()+".nametab");

		if(!f.exists()) return qualitys;

		InputStream inputstream = new FileInputStream(f);
		InputStreamReader reader = new InputStreamReader(inputstream, "UTF-8");
		BufferedReader bufferedreader = new BufferedReader(reader);

		String line = "";
		String[][] str = new String[qualitys.length][];
		int i = 0;
		while (((line = bufferedreader.readLine()) != null) && i < qualitys.length){
			String delims = "|];";
			StringTokenizer token = new StringTokenizer(line,delims,false);
			String[] substr = new String[token.countTokens()];

			int y = 0;
			while(token.hasMoreTokens()){
				substr[y] = token.nextToken();
				y++;
			}
			str[i] = substr;
			i++;
		}
		return str;
	}
	
	void saveNametable(String[][] nametable) throws IOException{
		if(nametable == null) return;
		
		File f = new File(PreferenceManager.getDefaultSharedPreferences(c).getString("PREF_MAP_ROOT_NAMETABLES","/storage/emulated/0/Download/")+nametable[0][0].toLowerCase()+".nametab");
		if(!f.exists()) f.createNewFile();

		OutputStream outputstream = new FileOutputStream(f);
		OutputStreamWriter writer = new OutputStreamWriter(outputstream, "UTF-8");
//		writer.write("["+nametable[0][0]+"]\n");
		for(int i = 0; i<nametable.length; i++){
			for(int y = 0; y < nametable[i].length; y++){
				writer.write(nametable[i][y]+"|");
			}
			writer.write("\n");
		}
		writer.flush();
	}
	
	ArrayList<nObject> loadMap(String f2load) throws Exception{
		File f = new File(f2load);

		if(!f.canRead()) return null;

		InputStream inputstream = new FileInputStream(f2load);
		InputStreamReader reader = new InputStreamReader(inputstream, "UTF-8");
		BufferedReader bufferedreader = new BufferedReader(reader);

		ArrayList<nObject> objects = new ArrayList<nObject>();

		String line = "";
		int todo = 0;
		while ((line = bufferedreader.readLine()) != null){
			String delims = " ];";
			StringTokenizer token = new StringTokenizer(line,delims,false);
			String firstword;

			if ((firstword = token.nextToken()).equals("[Objects")){
				todo = 1; 
			}else{
				switch (todo){
					case 1:
						int[] coords = new int[]{Integer.valueOf(token.nextToken()),Integer.valueOf(token.nextToken()),Integer.valueOf(token.nextToken()),Integer.valueOf(token.nextToken())};
						String name = token.nextToken();
						nObject obj = new nObject(Integer.valueOf(firstword),coords, loadNametable(name, new String[8][]));
						objects.add(obj);
						break;
					default:
						break;
				}
			}
		}
		return objects;
	}

	void saveMap(String f2save, MapEditorView map) throws IOException{
		File f = new File(f2save);

		if(!f.canRead()) return;

		OutputStream outputstream = new FileOutputStream(f);
		OutputStreamWriter writer = new OutputStreamWriter(outputstream, "UTF-8");
		writer.write("[Objects]\n");
		for(nObject o: map.objs){
			StringBuilder sb = new StringBuilder();
			sb.append(o.id+";");
			sb.append(o.x1+";"); 
			sb.append(o.y1+";"); 
			sb.append(o.x2+";"); 
			sb.append(o.y2+";");
			sb.append(o.qualitys[0][0]);
			sb.append("\n");
			writer.write(sb.toString());
		}
		writer.flush();
	}
	
	nBuilding loadBuilding(String path){
		return null;
	}
	
	void saveBuilding(nBuilding building) throws IOException{
		File f = new File(PreferenceManager.getDefaultSharedPreferences(c).getString("PREF_MAP_ROOT_BUILDINGS","/storage/emulated/0/Download/")+building.qualitys[0][0].toLowerCase()+".nametab");
		if(!f.exists()) f.createNewFile();

		OutputStream outputstream = new FileOutputStream(f);
		OutputStreamWriter writer = new OutputStreamWriter(outputstream, "UTF-8");
		writer.write("[Building]");
		
		
		writer.write("[Rooms]");
		
		writer.write("[Doors]");
		
		writer.flush();
	}
}
