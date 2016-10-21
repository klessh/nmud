package com.iillyyaa2033.mud.editor.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.iillyyaa2033.mud.editor.R;
import com.iillyyaa2033.mud.editor.activity.ObjectEditorActivity;
import com.iillyyaa2033.mud.editor.view.MapView;
import localhost.iillyyaa2033.mud.androidclient.logic.model.WorldObject;
import localhost.iillyyaa2033.mud.androidclient.utils.WorldHolder;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class MapEditorFragment extends Fragment {

	MapView mapview;
	ListView list;
	ArrayAdapter<WorldObject> adapter;

	int currentLayer = 0;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		reloadAdapter();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.fragment_mapeditor,container,false);
		mapview = (MapView) root.findViewById(R.id.map);
		
		list = (ListView) root.findViewById(R.id.list);
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
			
		TextView text = new TextView(getActivity());
		text.setText("Добавить новый объект");
		text.setPadding(20,20,20,20);
		list.addFooterView(text);
		
		Button btnObj = (Button) root.findViewById(R.id.layerObjects);
		Button btnMeta = (Button) root.findViewById(R.id.layerMeta);
		Button btnAi = (Button) root.findViewById(R.id.layerAi);
		
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
		return root;
	}

	@Override
	public void onResume() {
		super.onResume();
		reloadAdapter();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		reloadAdapter();
	}
	
	void startAdd(){
		startActivityForResult(new Intent(getActivity(),ObjectEditorActivity.class),0);
	}
	
	void startEdit(WorldObject o){
		Intent i = new Intent(getActivity(),ObjectEditorActivity.class);
		i.putExtra("object",o.getId());
		startActivityForResult(i,0);
	}
	
	void reloadAdapter(){
		adapter = new ArrayAdapter<WorldObject>(getActivity(),android.R.layout.simple_list_item_1);
		for (WorldObject object: WorldHolder.getInstance().objects) {
			adapter.add(object);
		}
	}
	
	void chooseLayer(int which){
		Toast.makeText(getActivity(),""+which,Toast.LENGTH_SHORT).show();
	}
}
