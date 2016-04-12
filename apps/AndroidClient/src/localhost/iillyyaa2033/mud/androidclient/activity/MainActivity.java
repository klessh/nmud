package localhost.iillyyaa2033.mud.androidclient.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import localhost.iillyyaa2033.mud.androidclient.R;
import localhost.iillyyaa2033.mud.androidclient.logic.Core;
import android.preference.PreferenceManager;
import android.content.SharedPreferences;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View.OnLongClickListener;

public class MainActivity extends Activity {

	ListView list;
	EditText textfield;
	ImageButton button;

	ArrayAdapter<String> adapter;

	Core core;
	public Handler handler;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		
		if(prefs.getString("DATAPATH","").equals(""))
			prefs.edit().remove("DATAPATH").putString("DATAPATH",Environment.getExternalStorageDirectory()+"/Download/nMud/").apply();
		
		list = (ListView) findViewById(R.id.main_ListView);
  		textfield = (EditText) findViewById(R.id.main_EditText);
		button = (ImageButton) findViewById(R.id.main_Button);

		list.setDividerHeight(0);

		adapter = new ArrayAdapter<String>(this, R.layout.main_listitem);
		list.setAdapter(adapter);

		button.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1) {
					String command = textfield.getText().toString();

					if(command.equals("")) return;

					core.cmdstr = (command);
					textfield.setText("");
				}
			});

		button.setOnLongClickListener(new OnLongClickListener(){

				@Override
				public boolean onLongClick(View p1) {
					startPreferences();
					return false;
				}
			});
			
		handler = new Handler();
		core = new Core(this);
		core.setDaemon(true);
		core.start();
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		core.saveReport();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		switch(requestCode){
			case 0:
				core.requestUpdate();
				break;
		}
	}
	
	public void addItem(String text){
		adapter.add(text);
		adapter.notifyDataSetChanged();
		list.setSelection(adapter.getCount());
	}
	
	public void appendToItem(String text){
		int size = adapter.getCount();
		if(size > 1) {
			String lastitm = adapter.getItem(size - 1);
			String newitm = lastitm + text;
			adapter.remove(lastitm);
			addItem(newitm);
		} else {
			addItem(text);
		}
	}
	
	public void startPreferences(){
		startActivityForResult(new Intent(MainActivity.this,PreferencesActivity.class),0);
	}
}
