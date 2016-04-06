package com.iillyyaa2033.mud.editor.fragment;

import android.app.Fragment;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.TextView;
import android.view.View.OnClickListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.Adapter;
import java.util.ArrayList;

public class EditorDictionaryFragment extends Fragment{
	
	ListView view;
	ArrayList<String> wordlist;
	ArrayAdapter<String> adapter;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = new ListView(getActivity());
		
		TextView header = new TextView(getActivity());
		header.setText("Add word to dictionary");
		header.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1) {
					// TODO: launch add word activity/dialog
				}
			});
		view.addHeaderView(header);
		
		wordlist = new ArrayList<String>();
		wordlist.add("Sample word");
			
		adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, wordlist);
		view.setAdapter(adapter);
		view.setOnItemClickListener(new OnItemClickListener(){

				@Override
				public void onItemClick(AdapterView<?> p1, View p2, int p3, long p4) {
					// TODO: launch wordeditor activity/fragment
				}
			});
		return view;
	}
	
	
}
