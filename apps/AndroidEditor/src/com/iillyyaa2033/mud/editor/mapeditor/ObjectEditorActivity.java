package com.iillyyaa2033.mud.editor.mapeditor;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import java.io.IOException;
import android.text.method.QwertyKeyListener;

public class ObjectEditorActivity extends Activity{

	private String[] map; // S[] name, S[] qualitys
	public static final int REQUEST_ADDING_OBJECT = 0, REQUEST_ADDING_BUILDING = 1, 
							REQUEST_ADDING_ROOM =2;
	private int request_code;
	private String[] pad = new String[]{"Изменит", "Родит", "Дат", "Винит","Творит","Творог"};
	private String[][] qualitys;
	private Loader loader;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		loader = new Loader(this);
		ListView view = new ListView(this);
		
		Intent intent = getIntent();
		request_code = intent.getIntExtra("object_type",REQUEST_ADDING_OBJECT);
		
		switch(request_code){
			case REQUEST_ADDING_OBJECT:
				map = initAsObject();
				break;
			case REQUEST_ADDING_BUILDING:
			case REQUEST_ADDING_ROOM:
				map = initAsRoom();
				break;
		}
		qualitys = new String[map.length][6];
		
		try{
			qualitys = loader.loadNametable(intent.getStringExtra("nametable"), qualitys);
		}catch (NumberFormatException e){}catch (IOException e){}
		
		ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, map);
		view.setAdapter(adapter);
		view.setOnItemClickListener(new OnItemClickListener(){

				@Override
				public void onItemClick(AdapterView<?> p1, View p2, int p3, long p4){
					showEditingDialog(p3,0);
				}
			});
		setContentView(view);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		menu.add(0,1,1,"Accept and close").setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		switch(item.getItemId()){
			case 1:
				Intent i = new Intent();
				i.putExtra("name",qualitys[0][0]);
				try{ loader.saveNametable(qualitys);
				} catch (IOException e){}
				
				this.setResult(Activity.RESULT_OK, i);
				this.finish();
				break;
		}
		return true;
	}
	
	void showEditingDialog(final int quality, final int step){
		if(step > 5) return;
		
		final EditText view = new EditText(this);
		view.setHint(pad[step]);
		if(quality < qualitys.length){
			if(qualitys[quality][step] != null) 
				view.setText(qualitys[quality][step]);
		}
		
		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		dialog.setTitle("Editing quality named "+map[quality].toLowerCase());
		dialog.setView(view);
		dialog.setCancelable(false);
		dialog.setNegativeButton("Cancel",null);
		dialog.setPositiveButton("Next", new DialogInterface.OnClickListener(){

				@Override
				public void onClick(DialogInterface p1, int p2){
					qualitys[quality][step] = view.getText().toString();
					
					if(step < 5){
						showEditingDialog(quality, step+1);
					}
				}
			});
		dialog.show();
	}
	
	String[] initAsObject(){
		String[] map = new String[]{"Имя", "Цвет", "Размер", "Возраст", "Чистота", "Влажность", "Материал", "Привлекательность"};
		return map;
	}
	
	String[] initAsRoom(){
		String[] map = new String[]{"Имя","Размер","Уют","Освещенность","Теплота"};
		return map;
	}
}
