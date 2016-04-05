package com.iillyyaa2033.mud.editor.activity;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.ArrayAdapter;
import com.iillyyaa2033.mud.editor.R;
import com.iillyyaa2033.mud.editor.fragment.MapEditorMapFragment;
import com.iillyyaa2033.mud.editor.fragment.EditorListFragment;
import android.app.Fragment;

public class EditorActivity extends FragmentActivity implements ActionBar.TabListener {
	
	EditorListFragment list;
	ArrayAdapter<String> adapter;
	MapEditorMapFragment map;
	ActionBar actionBar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mapeditor);
		
		actionBar = getActionBar();
        actionBar.setHomeButtonEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		
		map = new MapEditorMapFragment();
		actionBar.addTab(actionBar.newTab().setText("MapEditor").setTabListener(this));
		actionBar.addTab(actionBar.newTab().setText("ScriptsEditor").setTabListener(this));
		actionBar.addTab(actionBar.newTab().setText("Dictionary").setTabListener(this));
		
		
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_activated_1);
		adapter.add("aaaaa");
		adapter.add("bbbbb");
		adapter.add("ccccc");
		list = new EditorListFragment();
		list.setListAdapter(adapter);
		getFragmentManager().beginTransaction().add(R.id.activity_mapeditor_objects, list).commit();
		super.onPostCreate(savedInstanceState);
	}
	
	@Override
	public void onTabSelected(ActionBar.Tab p1, FragmentTransaction p2) {
		selectTab(p1.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab p1, FragmentTransaction p2) {
		list.setSelection(1);
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
			default:
				getFragmentManager().beginTransaction().replace(R.id.activity_mapeditor_map,new Fragment()).commit();
		}
	}
}
