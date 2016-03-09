package com.iillyyaa2033.mud.editor;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.iillyyaa2033.mud.editor.luaeditor.LuaEditorActivity;
import com.iillyyaa2033.mud.editor.mapeditor.MapEditorActivity;

public class MainActivity extends Activity{
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		int i = android.R.style.Theme_Holo_Dialog_NoActionBar_MinWidth;
		
		ListView lv = (ListView) findViewById(R.id.mainListView);
		String[] activities = {"Редактор карт","Редактор скриптов","Настройки","Справка"};
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,activities);
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(new OnItemClickListener(){

				@Override
				public void onItemClick(AdapterView<?> p1, View p2, int p3, long p4) {
					switch(p3){
						case 0:
							startActivity(new Intent(MainActivity.this, MapEditorActivity.class));
							break;
						case 1:
							startActivity(new Intent(MainActivity.this, LuaEditorActivity.class));
							break;
						case 2:
							startActivity(new Intent(MainActivity.this, Preferences.class));
							break;
						case 3:
							startActivity(new Intent(MainActivity.this, HelpActivity.class));
							break;
					}
				}
		});
    }
}
