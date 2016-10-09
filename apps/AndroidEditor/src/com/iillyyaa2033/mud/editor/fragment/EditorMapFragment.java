package com.iillyyaa2033.mud.editor.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.iillyyaa2033.mud.editor.view.MapEditorView;
import java.util.ArrayList;
import com.iillyyaa2033.mud.editor.logic.Database;
import android.widget.ListView;
import com.iillyyaa2033.mud.editor.R;
import android.widget.ArrayAdapter;
import localhost.iillyyaa2033.mud.androidclient.logic.model.WorldObject;
import localhost.iillyyaa2033.mud.androidclient.logic.model.Zone;
import localhost.iillyyaa2033.mud.androidclient.logic.model.World;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.Adapter;

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
					mapview.selectObject(adapter.getItem(p3));
				}
			});
		return root;
	}
}
