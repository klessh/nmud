package com.iillyyaa2033.mud.editor.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.iillyyaa2033.mud.editor.R;
import com.iillyyaa2033.mud.editor.view.MapView;
import java.io.File;
import localhost.iillyyaa2033.mud.androidclient.logic.model.WorldObject;
import localhost.iillyyaa2033.mud.androidclient.utils.ExportUtil;
import localhost.iillyyaa2033.mud.androidclient.utils.GlobalValues;
import localhost.iillyyaa2033.mud.androidclient.utils.ImportUtil;
import localhost.iillyyaa2033.mud.androidclient.utils.WorldHolder;

public class MapEditorActivity extends Activity{
	
	MapView mapview;
	ListView list;
	ArrayAdapter<WorldObject> adapter;

	int currentLayer = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.fragment_mapeditor);
		
		mapview = (MapView) findViewById(R.id.map);

		adapter = new ArrayAdapter<WorldObject>(this,android.R.layout.simple_list_item_1);
		
		list = (ListView) findViewById(R.id.list);
		list.setAdapter(adapter);
		list.setOnItemClickListener(new OnItemClickListener(){

				@Override
				public void onItemClick(AdapterView<?> p1, View p2, int p3, long p4) {
					if(p3 < adapter.getCount()) mapview.selectObject(adapter.getItem(p3));
					else startAdd();
				}
			});
		list.setOnItemLongClickListener(new OnItemLongClickListener(){

				@Override
				public boolean onItemLongClick(AdapterView<?> p1, View p2, int p3, long p4) {
					if(p3 < adapter.getCount()) startEdit(adapter.getItem(p3));
					else startAdd();
					return true;
				}
			});

		TextView text = new TextView(this);
		text.setText("Добавить новый объект");
		text.setPadding(20,20,20,20);
		list.addFooterView(text);

		Button btnObj = (Button) findViewById(R.id.layerObjects);
		Button btnMeta = (Button) findViewById(R.id.layerMeta);
		Button btnAi = (Button) findViewById(R.id.layerAi);

		btnObj.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1) {
					chooseLayer(0);
				}
			});
		btnMeta.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1) {
					chooseLayer(1);
				}
			});
		btnAi.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1) {
					chooseLayer(2);
				}
			});
	}
	
	@Override
	public void onResume() {
		super.onResume();
		reloadAdapter();
	}

	@Override
	protected void onStop() {
		saveTheWorld(true);
		super.onStop();
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		reloadAdapter();
	}

	void startAdd(){
		startActivityForResult(new Intent(this,ObjectEditorActivity.class),0);
	}

	void startEdit(WorldObject o){
		Intent i = new Intent(this,ObjectEditorActivity.class);
		i.putExtra("object",o.getId());
		startActivityForResult(i,0);
	}

	void reloadAdapter(){
		adapter.clear();
		for (WorldObject object: WorldHolder.getInstance().objects) {
			adapter.add(object);
		}
		adapter.notifyDataSetChanged();
	}

	void chooseLayer(int which){
	//	Toast.makeText(this,""+which,Toast.LENGTH_SHORT).show();
	}
	
	void saveTheWorld(boolean autosave) {
	 if (autosave) {

	 }
	 ExportUtil.saveFromWorld();
	 Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
	 }
}
