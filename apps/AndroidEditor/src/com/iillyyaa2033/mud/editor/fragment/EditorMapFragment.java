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
import com.iillyyaa2033.mud.editor.view.MapEditorView;
import localhost.iillyyaa2033.mud.androidclient.logic.model.World;
import localhost.iillyyaa2033.mud.androidclient.logic.model.WorldObject;
import localhost.iillyyaa2033.mud.androidclient.logic.model.Zone;

public class EditorMapFragment extends Fragment {

	MapEditorView mapview;
	ListView list;
	ArrayAdapter<WorldObject> adapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		adapter = new ArrayAdapter<WorldObject>(getActivity(),android.R.layout.simple_list_item_1);
		for (Zone zone : World.zones) {
			adapter.add(zone);
			for (WorldObject object : zone.objects) {
				adapter.add(object);
			}
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.fragment_mapeditor,container,false);
		mapview = (MapEditorView) root.findViewById(R.id.map);
		
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
		
		return root;
	}

	@Override
	public void onResume() {
		super.onResume();
		adapter.notifyDataSetChanged();
	}
	
	void startAdd(){
		startActivity(new Intent(getActivity(),ObjectEditorActivity.class));
	}
	
	void startEdit(WorldObject o){
		Intent i = new Intent(getActivity(),ObjectEditorActivity.class);
		i.putExtra("object",o.getId());
		startActivity(i);
	}
}
