package com.iillyyaa2033.mud.editor.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import com.iillyyaa2033.mud.editor.R;
import com.iillyyaa2033.mud.editor.fragment.DictionaryFragment;
import com.iillyyaa2033.mud.editor.fragment.EditorMapFragment;
import com.iillyyaa2033.mud.editor.fragment.GraphVisFrag;
import com.iillyyaa2033.mud.editor.fragment.UsercommandsFragment;
import com.iillyyaa2033.mud.editor.logic.Database;
import com.iillyyaa2033.mud.editor.logic.Loader;
import java.io.IOException;

public class EditorActivity extends Activity implements ActionBar.TabListener {
	
	EditorMapFragment map;
	UsercommandsFragment uce;
	DictionaryFragment dictionary;
	GraphVisFrag grvf;
	ActionBar actionBar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mapeditor);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		
		actionBar = getActionBar();
        actionBar.setHomeButtonEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
	//	actionBar.setHideOffset(100);
		
		map = new EditorMapFragment();
		uce = new UsercommandsFragment();
		grvf = new GraphVisFrag();
		dictionary = new DictionaryFragment();
		
		actionBar.addTab(actionBar.newTab().setText("MapEditor").setTabListener(this));
		actionBar.addTab(actionBar.newTab().setText("ScriptsEditor").setTabListener(this));
		actionBar.addTab(actionBar.newTab().setText("UserCommandsEditor").setTabListener(this));
		actionBar.addTab(actionBar.newTab().setText("Dictionary").setTabListener(this));
		actionBar.addTab(actionBar.newTab().setText("Graph Vis").setTabListener(this));

		Database.uploadDict(Loader.loadDictionary());
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	//	menu.add(0,0,0,"Load map").setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
	//	menu.add(1,1,1,"Save map").setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
			case 0:
				try {
					Database.rooms = Loader.loadMap(this,PreferenceManager.getDefaultSharedPreferences(this).getString("PREF_ROOT", "/storage/emulated/0/Download/nmud/content-ru")+"/maps/");
					map.updateFromDB();
				} catch (Exception e) {
					new AlertDialog.Builder(this).setMessage(e.toString()).show();
				}
				break;
			case 1:
				try {
					Loader.saveMap(PreferenceManager.getDefaultSharedPreferences(this).getString("PREF_ROOT", "/storage/emulated/0/Download/nmud/content-ru")+"/maps/test.txt", map.get());
				} catch (IOException e) {
					new AlertDialog.Builder(this).setMessage(e.toString()).show();
				}
		}
		return super.onOptionsItemSelected(item);
	}
	
	
	
	@Override
	public void onTabSelected(ActionBar.Tab p1, FragmentTransaction p2) {
		selectTab(p1.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab p1, FragmentTransaction p2) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab p1, FragmentTransaction p2) {
		
	}
	
	void selectTab(int pos){
		actionBar.setSelectedNavigationItem(pos);
		
		switch(pos){
			case 0:
				getFragmentManager().beginTransaction().replace(R.id.activity_mapeditor_map,map).commit();
				break;
			case 2:
				getFragmentManager().beginTransaction().replace(R.id.activity_mapeditor_map,uce).commit();
				break;	
			case 3:
				getFragmentManager().beginTransaction().replace(R.id.activity_mapeditor_map,dictionary).commit();
				break;
			case 4:
				getFragmentManager().beginTransaction().replace(R.id.activity_mapeditor_map,grvf).commit();
				break;
			default:
				getFragmentManager().beginTransaction().replace(R.id.activity_mapeditor_map,new Fragment()).commit();
		}
	}
}
