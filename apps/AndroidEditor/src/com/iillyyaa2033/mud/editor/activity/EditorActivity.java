package com.iillyyaa2033.mud.editor.activity;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import com.iillyyaa2033.mud.editor.R;
import com.iillyyaa2033.mud.editor.fragment.EditorDictionaryFragment;
import com.iillyyaa2033.mud.editor.fragment.EditorMapFragment;
import com.iillyyaa2033.mud.editor.logic.Loader;
import java.io.IOException;
import java.io.File;
import com.iillyyaa2033.mud.editor.logic.Database;

public class EditorActivity extends FragmentActivity implements ActionBar.TabListener {
	
	EditorMapFragment map;
	EditorDictionaryFragment dictionary;
	ActionBar actionBar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mapeditor);
		
		actionBar = getActionBar();
        actionBar.setHomeButtonEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		
		map = new EditorMapFragment();
		
		actionBar.addTab(actionBar.newTab().setText("MapEditor").setTabListener(this));
		actionBar.addTab(actionBar.newTab().setText("ScriptsEditor").setTabListener(this));
		actionBar.addTab(actionBar.newTab().setText("Dictionary").setTabListener(this));
		
		dictionary = new EditorDictionaryFragment();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0,0,0,"Load map").setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		menu.add(1,1,1,"Save map").setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		
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
				getFragmentManager().beginTransaction().replace(R.id.activity_mapeditor_map,dictionary).commit();
				break;
			default:
				getFragmentManager().beginTransaction().replace(R.id.activity_mapeditor_map,new Fragment()).commit();
		}
	}
}
