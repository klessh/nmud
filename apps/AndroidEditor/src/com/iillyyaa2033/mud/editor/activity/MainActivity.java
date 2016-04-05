package com.iillyyaa2033.mud.editor.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.ArrayAdapter;
import com.iillyyaa2033.mud.editor.R;
import com.iillyyaa2033.mud.editor.fragment.MainListFragment;

public class MainActivity extends FragmentActivity {

	MainListFragment list;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		list = new MainListFragment();
		getFragmentManager().beginTransaction().add(R.id.activity_mainf_frame, list).commit();
	}

	@Override
	protected void onStart() {
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1);
		list.setListAdapter(adapter);

		adapter.add("Editor");
		adapter.add("Preferences");
		adapter.add("Help");
		super.onStart();
	}
	
	
}
