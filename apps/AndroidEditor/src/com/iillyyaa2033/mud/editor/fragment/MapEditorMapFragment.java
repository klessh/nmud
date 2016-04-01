package com.iillyyaa2033.mud.editor.fragment;

import android.app.Fragment;
import android.view.ViewGroup;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import com.iillyyaa2033.mud.editor.view.MapEditorView;

public class MapEditorMapFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		MapEditorView view = new MapEditorView(getActivity());
		return view;
	}
	
	
}
