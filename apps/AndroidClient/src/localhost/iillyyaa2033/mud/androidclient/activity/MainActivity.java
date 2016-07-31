package localhost.iillyyaa2033.mud.androidclient.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import localhost.iillyyaa2033.mud.androidclient.R;
import localhost.iillyyaa2033.mud.androidclient.clientserver.LocalClient;
import localhost.iillyyaa2033.mud.androidclient.clientserver.Server;
import localhost.iillyyaa2033.mud.androidclient.utils.GlobalValues;

public class MainActivity extends Activity {

	ListView list;
	EditText textfield;
	ImageButton button;

	ArrayAdapter<String> adapter;

	Server server;
	LocalClient client;
	public Handler handler;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		
		if(prefs.getString("DATAPATH","").equals(""))
			prefs.edit().remove("DATAPATH").putString("DATAPATH",Environment.getExternalStorageDirectory()+"/Download/nMud/").apply();
		
		GlobalValues.datapath = PreferenceManager.getDefaultSharedPreferences(this).getString("DATAPATH","");
		
			
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

					client.cmdstr = (command);
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
			
		GlobalValues.debug = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("FLAG_DEBUG",true);
		GlobalValues.debug_importer = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("FLAG_DEBUG_IMPORTER",false);
		GlobalValues.debug_descr = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("FLAG_DEBUG_DESCR",false);
		GlobalValues.debug_scripts = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("FLAG_DEBUG_SCRIPTS",false);
		GlobalValues.debug_graph = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("FLAG_DEBUG_GRAPH",false);	
			
		handler = new Handler();
		server = new Server();
		client = new LocalClient(this);
		client.setDaemon(true);
		client.start();
	}
	
	@Override
	protected void onStop() {
		super.onStop();
//		client.saveReport();
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
