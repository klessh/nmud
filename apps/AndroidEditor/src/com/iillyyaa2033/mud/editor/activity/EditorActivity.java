package com.iillyyaa2033.mud.editor.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import com.iillyyaa2033.mud.editor.R;
import com.iillyyaa2033.mud.editor.fragment.DictionaryFragment;
import com.iillyyaa2033.mud.editor.fragment.EditorMapFragment;
import com.iillyyaa2033.mud.editor.fragment.GraphVisFrag;
import com.iillyyaa2033.mud.editor.fragment.UsercommandsFragment;
import com.iillyyaa2033.mud.editor.logic.Database;
import com.iillyyaa2033.mud.editor.logic.Loader;
import java.io.FileNotFoundException;
import localhost.iillyyaa2033.mud.androidclient.logic.model.World;
import localhost.iillyyaa2033.mud.androidclient.utils.GlobalValues;
import localhost.iillyyaa2033.mud.androidclient.utils.ImportUtil;
import android.preference.PreferenceManager;

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
		
		try {
			GlobalValues.datapath = PreferenceManager.getDefaultSharedPreferences(this).getString("PREF_ROOT","/storage/emulated/0/AppProjects/MyGITHUB/nmud/content-ru");
			World.zones = ImportUtil.importZones();
		} catch (FileNotFoundException e) {
			Toast.makeText(this,e.toString(),Toast.LENGTH_LONG).show();
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	//	menu.add(0,0,0,"Load map").setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
	//	menu.add(1,1,1,"Save map").setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		menu.add(2,2,2,"Prefs").setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
			case 0:
				// load map
				break;
			case 1:
				// save map
			case 2:
				startActivity(new Intent(this,Preferences.class));
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
