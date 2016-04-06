package com.iillyyaa2033.mud.editor.fragment;

import android.app.Fragment;
import android.view.ViewGroup;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import com.iillyyaa2033.mud.editor.view.MapEditorView;
import java.util.ArrayList;
import com.iillyyaa2033.mud.editor.R;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.graphics.Color;

public class MapEditorMapFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		LinearLayout ll = new LinearLayout(getActivity());
		ListView listview = new ListView(getActivity());
		ll.addView(listview,300,ll.getHeight());
		MapEditorView mapview = new MapEditorView(getActivity());
		ll.addView(mapview);
		return ll;
	}
	
	public void put(ArrayList data){
		// TODO: put
	}
	
	public ArrayList get(){
		// TODO: get
		return null;
	}
}
