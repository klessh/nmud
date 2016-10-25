package com.iillyyaa2033.mud.editor.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import java.io.File;
import localhost.iillyyaa2033.mud.androidclient.utils.ExceptionsStorage;
import localhost.iillyyaa2033.mud.androidclient.utils.ExportUtil;
import localhost.iillyyaa2033.mud.androidclient.utils.GlobalValues;
import localhost.iillyyaa2033.mud.androidclient.utils.ImportUtil;

public class MainActivity extends Activity {

	final String[] items = {"Map editor","Usercommands editor","Dictionary","Graph visualizer"};
	ListView list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
		list = new ListView(this);
		list.setAdapter(adapter);
		list.setOnItemClickListener(new OnItemClickListener(){

				@Override
				public void onItemClick(AdapterView<?> p1, View p2, int p3, long p4) {
					Intent i = null;
					switch (p3) {
						case 0:	// map
							i = new Intent(MainActivity.this, MapEditorActivity.class);
							break;
						case 1:	// usercommands
							break;
						case 2: // dict
							break;
						case 3: // graph
							break;
					}
					if (i != null) startActivity(i);
				}
			});
		setContentView(list);

		GlobalValues.datapath = PreferenceManager.getDefaultSharedPreferences(this).getString("PREF_ROOT", "/storage/emulated/0/AppProjects/MyGITHUB/nmud/content-ru");
	}

	@Override
	protected void onStart() {
		ImportUtil.importToWorld(new File(GlobalValues.datapath + "/maps/root.bin"));
		super.onStart();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 0, 0, "Log").setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
//		menu.add(1, 1, 1, "Save").setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		menu.add(2, 2, 2, "Prefs").setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case 0:
				String[] items = new String[ExceptionsStorage.exceptions.size()];
				for (int i = 0; i < items.length; i++) {
					items[i] = ExceptionsStorage.exceptions.get(i).toString();
				}
				new AlertDialog.Builder(this).setItems(items, null).show();
				break;
				/*			case 1:
				 saveTheWorld(false);
				 break;	*/
			case 2:
				startActivity(new Intent(this, Preferences.class));
				break;
		}
		return super.onOptionsItemSelected(item);
	}

}
