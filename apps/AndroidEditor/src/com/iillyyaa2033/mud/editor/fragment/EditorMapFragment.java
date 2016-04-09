package com.iillyyaa2033.mud.editor.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.iillyyaa2033.mud.editor.view.MapEditorView;
import java.util.ArrayList;

public class EditorMapFragment extends Fragment {

	MapEditorView mapview;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		LinearLayout ll = new LinearLayout(getActivity());
		mapview = new MapEditorView(getActivity());
		ll.addView(mapview);
		return ll;
	}
	
	public void add(String obj){
		
	}
	
	public ArrayList get(){
		// TODO: get
		return null;
	}
	
	
}
