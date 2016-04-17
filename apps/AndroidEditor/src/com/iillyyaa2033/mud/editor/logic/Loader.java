package com.iillyyaa2033.mud.editor.logic;

import android.content.Context;
import android.preference.PreferenceManager;
import com.iillyyaa2033.mud.editor.view.MapEditorView;
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

public class Loader{
	
	Context c;
	
	public Loader(Context c){
		this.c = c;
	}
	
	public String[][] loadNametable(String nameofnametable, String[][] qualitys) throws IOException{
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
	
	public void saveNametable(String[][] nametable) throws IOException{
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
	
/*	public ArrayList<nRoom> loadMap(String f2load) throws Exception{
		
	} */

	public static void saveMap(String f2save, MapEditorView map) throws IOException{
		File f = new File(f2save);

		if(!f.canRead()) throw new IOException("File not found: \n\t"+f2save);

		OutputStream outputstream = new FileOutputStream(f);
		OutputStreamWriter writer = new OutputStreamWriter(outputstream, "UTF-8");
		
		for(nRoom room: map.rooms){
			StringBuilder sb = new StringBuilder();
			sb.append("[room]");
			sb.append(room.id+"|");
			sb.append(room.x1+"|"); 
			sb.append(((int)map.canvasSize) - room.y2+"|"); 
			sb.append(room.x2+"|"); 
			sb.append(((int)map.canvasSize) - room.y1+"|");
			sb.append(room.name);
			sb.append("\n");
			for(nObject object : room.objects){
				sb.append(object.id+"|");
				sb.append(object.x1+"|"); 
				sb.append(((int)map.canvasSize) - object.y2+"|"); 
				sb.append(object.x2+"|"); 
				sb.append(((int)map.canvasSize)- object.y1+"|");
				sb.append(object.name);
				sb.append("\n");
			}
			writer.write(sb.toString());
		}
		writer.flush();
	}
	
	public nBuilding loadBuilding(String path){
		return null;
	}
	
	void saveBuilding(nBuilding building){
		
	}
}
