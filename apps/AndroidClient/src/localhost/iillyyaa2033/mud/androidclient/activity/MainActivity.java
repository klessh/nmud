package localhost.iillyyaa2033.mud.androidclient.activity;

import android.app.Activity;
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
import android.content.Intent;

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
		
		list = (ListView) findViewById(R.id.main_ListView);
  		textfield = (EditText) findViewById(R.id.main_EditText);
		button = (ImageButton) findViewById(R.id.main_Button);

		list.setDividerHeight(0);

		adapter = new ArrayAdapter<String>(this, R.layout.main_listitem);
		list.setAdapter(adapter);

		button.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1) {
					doWork();
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
	
	void doWork(){
		String command = textfield.getText().toString();

		if(command.equals("")) return;

		switch(command){
			case "scripts":
	//			core.listScripts();
				break;
			default:
				core.cmdstr = (command);
		}
		textfield.setText("");
	}

	public void addItem(String text){
		adapter.add(text);
		adapter.notifyDataSetChanged();
		list.setSelection(adapter.getCount());
	}
	
	public void appendToItem(String text){
		int size = adapter.getCount();
		String lastitm = adapter.getItem(size - 1);
		String newitm = lastitm + text;
		adapter.remove(lastitm);
		addItem(newitm);
	}
	
	public void startPreferences(){
		startActivityForResult(new Intent(MainActivity.this,PreferencesActivity.class),0);
	}
}
