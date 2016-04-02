package com.iillyyaa2033.mud.editor.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.ArrayAdapter;
import com.iillyyaa2033.mud.editor.R;
import com.iillyyaa2033.mud.editor.fragment.MapEditorListFragment;
import com.iillyyaa2033.mud.editor.fragment.MapEditorMapFragment;

public class MapEditorActivity extends FragmentActivity {

	MapEditorListFragment list;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mapeditor);
		
		list = new MapEditorListFragment();
		getFragmentManager().beginTransaction().add(R.id.activity_mapeditor_objects, list).commit();
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1);
		list.setListAdapter(adapter);
	//	adapter.add("Sample item");
		
		MapEditorMapFragment map = new MapEditorMapFragment();
		getFragmentManager().beginTransaction().add(R.id.activity_mapeditor_map, map).commit();
		
	}
}
