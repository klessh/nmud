package com.iillyyaa2033.mud.editor.mapeditor;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

public class BuildingEditorActivity extends Activity{
	
	Loader loader = new Loader(this);
	nBuilding building;
	public static final int REQUEST_ADDING_OBJECT = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		BuildingEditorView view = new BuildingEditorView(this);
		view.parent = this;
		setContentView(view);
		
		building = loader.loadBuilding(getIntent().getStringExtra("path"));
	}
	
	void objEdit(){
		startActivityForResult(new Intent(this, ObjectEditorActivity.class).putExtra("object_type",REQUEST_ADDING_OBJECT),REQUEST_ADDING_OBJECT);
	}
	
/*	@Override
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
		}
	}	*/
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		menu.add(0,1,1,"Accept and close").setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		setResult(Activity.RESULT_OK);
		finish();
		return true;
	}
}
