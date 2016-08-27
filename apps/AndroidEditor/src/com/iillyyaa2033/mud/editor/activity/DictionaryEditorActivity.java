package com.iillyyaa2033.mud.editor.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import com.iillyyaa2033.mud.editor.logic.Database;
import android.view.Menu;
import android.view.MenuItem;

public class DictionaryEditorActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		int id = getIntent().getExtras().getInt("wID",-1);
		TextView view = new TextView(this);
		view.setText(Database.dict.get(id));
		setContentView(view);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(1,1,1,"Edit");
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
			case 1:
				
				return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	
}
