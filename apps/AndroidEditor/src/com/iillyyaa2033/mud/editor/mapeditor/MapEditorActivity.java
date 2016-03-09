package com.iillyyaa2033.mud.editor.mapeditor;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import com.iillyyaa2033.mud.editor.FileChooserActivity;
import com.iillyyaa2033.mud.editor.R;
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
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class MapEditorActivity extends Activity{
    
	public static final int REQUEST_ADDING_OBJECT = 0, REQUEST_ADDING_BUILDING = 1, REQUEST_ADDING_ROOM = 2, REQUEST_SAVE_MAPFILE = 10, REQUEST_LOAD_MAPFILE  = 11;
	Loader loader = new Loader(this);
	MapEditorView map;
	MapEditorAdapter adapter;
	nObject workingObject;
	nBuilding workingBuilding;
	
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
		setContentView(R.layout.mapeditor);
		
		final ListView lv = (ListView) findViewById(R.id.mapeditor_listview);
		adapter = new MapEditorAdapter(this, android.R.layout.simple_list_item_1);
		lv.setAdapter(adapter);
		lv.setFastScrollEnabled(true);
		
		map = (MapEditorView) findViewById(R.id.mapeditor_editor);
		map.setAdapter(adapter);
		map.setParent(this);
		
		lv.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> p1, View p2, int p3, long p4){
				map.setSelectionToObject(p3);
			}
		});
		lv.setOnItemLongClickListener(new OnItemLongClickListener(){

			@Override
			public boolean onItemLongClick(AdapterView<?> p1, View p2, final int objectid, long p4){
				AlertDialog.Builder ad = new AlertDialog.Builder(MapEditorActivity.this);
				ad.setItems(new String[]{"Задать размер в клетках","Сменить местоположение и размер","Сменить тип","Edit nametable","Удаление"}, new DialogInterface.OnClickListener(){

						@Override
						public void onClick(DialogInterface p1, int clicked_item){
							switch(clicked_item){
								case 0: // size in cells
									break;
								case 1:
									map.editObject(objectid);
									break;
								case 2: // change type
									break;
								case 3:
									Intent i = new Intent(MapEditorActivity.this, ObjectEditorActivity.class);
									i.putExtra("object_type",REQUEST_ADDING_OBJECT);
									i.putExtra("nametable",map.objs.get(objectid).qualitys[0][0]);
									startActivityForResult(i,REQUEST_ADDING_OBJECT);
									break;
								case 4:
									AlertDialog.Builder adb = new AlertDialog.Builder(MapEditorActivity.this);
									adb.setTitle("Asking");
									adb.setMessage("Are you sure want to delete this obj?");
									adb.setCancelable(false);
									adb.setNegativeButton("cancel",null);
									adb.setPositiveButton("yes", new DialogInterface.OnClickListener(){

										@Override
										public void onClick(DialogInterface p1, int p2){
											map.removeObject(objectid);
										}
									});
									adb.show();
							}
						}
					});
				ad.show();
				return true;
			}
		});
		
    }
	
	public void startBuildingEditor(nObject object){
		workingObject = object;
		startActivityForResult(new Intent(this, ObjectEditorActivity.class).putExtra("object_type",REQUEST_ADDING_BUILDING),REQUEST_ADDING_BUILDING);
	}
	
	public void startObjectEditor(nObject object){
		workingObject = object;
		startActivityForResult(new Intent(this, ObjectEditorActivity.class).putExtra("object_type",REQUEST_ADDING_OBJECT),REQUEST_ADDING_OBJECT);
	}


	
	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		menu.add(0,1,2,"Save map").setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		menu.add(0,2,1,"Load map").setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		switch(item.getItemId()){
			case 1:
				Intent i = new Intent(MapEditorActivity.this, FileChooserActivity.class);
				Bundle b = new Bundle();
				
				b.putInt("requestcode",3);
				b.putString("extension",".mudmap");
				b.putString("target",PreferenceManager.getDefaultSharedPreferences(this).getString("PREF_MAP_ROOT","/storage/emulated/0/Download/"));
				i.putExtras(b);
				startActivityForResult(i,REQUEST_SAVE_MAPFILE);
				break;
			case 2:
				Intent intent = new Intent(MapEditorActivity.this, FileChooserActivity.class);
				Bundle bundle = new Bundle();

				bundle.putInt("requestcode",3);
				bundle.putString("extension",".mudmap");
				bundle.putString("target",PreferenceManager.getDefaultSharedPreferences(this).getString("PREF_MAP_ROOT","/storage/emulated/0/Download/"));
				intent.putExtras(bundle);
				startActivityForResult(intent,REQUEST_LOAD_MAPFILE);
				break;
		}
		return true;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		super.onActivityResult(requestCode, resultCode, data);
		switch(requestCode){
			case REQUEST_ADDING_OBJECT:
				String name = data.getStringExtra("name");
				try{
					workingObject.qualitys = loader.loadNametable(name, new String[8][]);
					map.objs.add(workingObject);
					map.invalidate();
					adapter.add(workingObject);
					adapter.notifyDataSetChanged();
				}catch (NumberFormatException e){}catch (IOException e){}
				break;
			case REQUEST_ADDING_BUILDING:
		//		startActivity(new Intent(this, BuildingEditorActivity.class).putExtra("path",PreferenceManager.getDefaultSharedPreferences(this).getString("PREF_MAP_ROOT","/storage/emulated/0/Download/")+"/buildings/"+workingBuilding.id+".mudmap"));
				startActivity(new Intent(this, BuildingEditorActivity.class).putExtra("path",PreferenceManager.getDefaultSharedPreferences(this).getString("PREF_MAP_ROOT","/storage/emulated/0/Download/")+"/buildings/testbuilding.mudmap"));
				break;
			case REQUEST_SAVE_MAPFILE:
				try{
					loader.saveMap(data.getExtras().getString("file"), map);
				} catch(Exception e){
					Log.e("mud.editor","error",e);
				}
				break;
			case REQUEST_LOAD_MAPFILE:
				try{
					map.objs = loader.loadMap(data.getExtras().getString("file"));
					map.invalidate();
					adapter.addAlll(map.objs);
				} catch(Exception e){
					new AlertDialog.Builder(this).setMessage("Something went wrong, so this is log:\n\n"+e).show();
					Log.e("mud.editor","error",e);
				}
				break;
			default:
				// do nothing
		}
	}
}
