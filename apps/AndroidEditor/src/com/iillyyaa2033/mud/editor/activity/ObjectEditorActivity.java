package com.iillyyaa2033.mud.editor.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.iillyyaa2033.mud.editor.R;
import java.util.ArrayList;
import localhost.iillyyaa2033.mud.androidclient.logic.model.World;
import localhost.iillyyaa2033.mud.androidclient.logic.model.WorldObject;
import java.util.Arrays;
import java.util.Comparator;
import android.widget.TextView;
import android.app.AlertDialog;

public class ObjectEditorActivity extends Activity {

	WorldObject object;

	ArrayAdapter<String> adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_objedit);

		String id = getIntent().getStringExtra("object");
		if (id != null) {
			WorldObject[] o = World.searchById(id);
			if (o.length > 0) object = o[0];
		}

		if (object != null) {
			getActionBar().setTitle(object.toString());
		} else {
			getActionBar().setTitle("New object");
			object = new WorldObject();
		}

		initializeParamsList();
	}

	void initializeParamsList() {
		ListView list = (ListView) findViewById(R.id.activity_objedit_list);

		TextView footer = new TextView(this);
		footer.setText("Новый параметр");
		footer.setPadding(24,24,24,24);
		list.addFooterView(footer);
		
		ArrayList<String> params = new ArrayList<String>();
		for (String key : object.params.keySet()) {
			params.add("[" + key + "] " + object.params.get(key));
		}
		
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, params);
		adapter.sort(new Comparator<String>(){

				@Override
				public int compare(String p1, String p2) {
					return p1.compareToIgnoreCase(p2);
				}
			});
		list.setAdapter(adapter);
		list.setOnItemClickListener(new OnItemClickListener(){

				@Override
				public void onItemClick(AdapterView<?> p1, View p2, int p3, long p4) {
					if(p3 < adapter.getCount()) paramDialog(adapter.getItem(p3));
					else paramDialog(null);
				}
			});
	}
	
	void paramDialog(String which){
		// get tag & data
		// start dialog
		// update adapter after finish
	}
}
