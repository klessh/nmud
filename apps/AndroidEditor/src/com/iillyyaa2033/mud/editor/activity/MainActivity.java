package com.iillyyaa2033.mud.editor.activity;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import com.iillyyaa2033.mud.editor.fragment.MainListFragment;
import com.iillyyaa2033.mud.editor.R;
import android.app.Fragment;
import android.widget.ListAdapter;
import android.widget.ArrayAdapter;

public class MainActivity extends FragmentActivity {

	MainListFragment list;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		list = new MainListFragment();
		getFragmentManager().beginTransaction().add(R.id.activity_mainf_frame, list).commit();
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1);
		list.setListAdapter(adapter);
		
		adapter.add("Editor");
		adapter.add("Preferences");
		adapter.add("Help");
		
	}
}
