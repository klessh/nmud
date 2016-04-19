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

public class EditorMapFragment extends Fragment {

	MapEditorView mapview;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		LinearLayout ll = new LinearLayout(getActivity());
		mapview = new MapEditorView(getActivity());
		ll.addView(mapview);
		return ll;
	}

	@Override
	public void onStart() {
		updateFromDB();
		super.onStart();
	}

	@Override
	public void onResume() {
		updateFromDB();
		super.onResume();
	}

	@Override
	public void onPause() {
		saveToDB();
		super.onPause();
	}

	@Override
	public void onStop() {
		// TODO: Implement this method
		super.onStop();
	}
	
	public MapEditorView get(){
		// TODO: get
		return mapview;
	}
	
	public void updateFromDB(){
		mapview.rooms = Database.rooms;
	}
	
	void saveToDB(){
		Database.rooms = mapview.rooms;
	}
}
