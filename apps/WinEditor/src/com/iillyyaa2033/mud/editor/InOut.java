package com.iillyyaa2033.mud.editor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.StringTokenizer;

import com.iillyyaa2033.nmud.abstractserver.model.WorldObject;

public class InOut {
	
	Main main;
	File workfile;
	
	public InOut(Main m){
		main = m;
	}
	
	public synchronized ArrayList<WorldObject> load(){
		ArrayList<WorldObject> map = new ArrayList<WorldObject>();

		File workfile = new File(main.path);

		if (!workfile.exists()) {
			System.out.println("Importer: epic fail");
			return null;
		}

		try {
			InputStream inputstream = new FileInputStream(workfile);
			InputStreamReader reader = new InputStreamReader(inputstream, "UTF-8");
			BufferedReader bufferedreader = new BufferedReader(reader);

			String buffer = "";
			StringTokenizer token;

			while ((buffer = bufferedreader.readLine()) != null) {
				token = new StringTokenizer(buffer, "|");
				if (token.countTokens() == 5) {
					WorldObject obj = new WorldObject();
					obj.x = (new Integer(token.nextToken()))*5;
					obj.y = (new Integer(token.nextToken()))*5;
					obj.x2 = (new Integer(token.nextToken()))*5;
					obj.y2 = (new Integer(token.nextToken()))*5;
					obj.name = token.nextToken();

					map.add(obj);
				}
			}

			bufferedreader.close();
			return map;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public synchronized void save(ArrayList<WorldObject> data){
		File workfile = new File(main.path);

		if (!workfile.exists()) {
			System.out.println("Importer: epic fail");
			return;
		}
		
		try {
			OutputStream outputstream = new FileOutputStream(workfile);
			OutputStreamWriter writer = new OutputStreamWriter(outputstream, "UTF-8");
		
			String d = "|";
			
			for(WorldObject o : data){
				writer.write(o.x/5 + d + o.y/5 + d+o.x2/5 + d + o.y2/5 + d + o.name+"\n");
			}
			
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public synchronized void upload(){
		// TODO upload to server, do later
	}
}
