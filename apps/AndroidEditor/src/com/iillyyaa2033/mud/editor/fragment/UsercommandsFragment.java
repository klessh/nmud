package com.iillyyaa2033.mud.editor.fragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import java.util.Comparator;
import java.util.HashMap;
import localhost.iillyyaa2033.mud.androidclient.utils.ImportUtil;

public class UsercommandsFragment extends Fragment{
	
	HashMap<String, String> map;
	ArrayAdapter<String> adapter;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		map = ImportUtil.loadUsecommands();
		
		LinearLayout ll = new LinearLayout(getActivity());
		ll.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));
		ListView view = new ListView(getActivity());
		view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));
	//	view.set;
		ll.addView(view);
		adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1);
		adapter.addAll(map.keySet());
		adapter.sort(new Comparator<String>(){

				@Override
				public int compare(String p1, String p2) {
					return p1.compareTo(p2);
				}
				
			});
		view.setAdapter(adapter);
		view.setOnItemClickListener(new OnItemClickListener(){

				@Override
				public void onItemClick(AdapterView<?> p1, View p2, int p3, long p4) {
					String key = adapter.getItem(p3);
					dialogue(key,true);
					
				}
			});
		view.setOnItemLongClickListener(new OnItemLongClickListener(){

				@Override
				public boolean onItemLongClick(AdapterView<?> p1, View p2, int p3, long p4) {
					String key = adapter.getItem(p3);
					dialogue(key,false);
					return true;
				}
				
			
		});
		return ll;
	}
	
	void dialogue(final String key, final boolean isPreview){
		String val = map.get(key);
		
		final TextView edit;
		
		if(isPreview){
			edit = new TextView(getActivity());
		}else{
			edit = new EditText(getActivity());
		}
		edit.setText(val);
		
		AlertDialog.Builder ab = new AlertDialog.Builder(getActivity());
		ab.setView(edit);
		ab.setPositiveButton("^_______^", new AlertDialog.OnClickListener(){

				@Override
				public void onClick(DialogInterface p1, int p2) {
					if(!isPreview){
						map.remove(key);
						map.put(key,edit.getText().toString());
					}
				}
			});
		ab.show();
	}
}
