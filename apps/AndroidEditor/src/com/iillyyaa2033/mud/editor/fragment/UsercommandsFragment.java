package com.iillyyaa2033.mud.editor.fragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import java.io.FileNotFoundException;
import java.util.Comparator;
import java.util.HashMap;
import com.iillyyaa2033.mud.editor.logic.ImportUtil;

public class UsercommandsFragment extends Fragment{
	
	HashMap<String, String> map;
	ArrayAdapter<String> adapter;
	
	boolean canSave = true;
	boolean needSave = false;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		try {
			map = ImportUtil.loadUsercommands();
		} catch (FileNotFoundException e) {
			map = new HashMap<String,String>();
			canSave = false;
		}

		LinearLayout ll = new LinearLayout(getActivity());
		ll.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));
		
		Button addBtn = new Button(getActivity());
		addBtn.setText("New cmd");
		addBtn.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1) {
					final EditText t = new EditText(getActivity());
					new AlertDialog.Builder(getActivity())
						.setTitle("Name?")
						.setView(t)
						.setNegativeButton("Cnl",null)
						.setPositiveButton("Ok", new AlertDialog.OnClickListener(){

							@Override
							public void onClick(DialogInterface p1, int p2) {
								adapter.add(t.getText().toString());
								dialogue(t.getText().toString(),false);
							}
						})
						.show();
				}
			});
		
		ListView view = new ListView(getActivity());
		view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));
		view.addHeaderView(addBtn);
		
		adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1);
		adapter.addAll(map.keySet());
		resort();
		
		view.setAdapter(adapter);
		view.setOnItemClickListener(new OnItemClickListener(){

				@Override
				public void onItemClick(AdapterView<?> p1, View p2, int p3, long p4) {
					String key = adapter.getItem(p3-1);
					dialogue(key,true);
					
				}
			});
		view.setOnItemLongClickListener(new OnItemLongClickListener(){

				@Override
				public boolean onItemLongClick(AdapterView<?> p1, View p2, int p3, long p4) {
					String key = adapter.getItem(p3-1);
					dialogue(key,false);
					return true;
				}
				
			
		});
		
		ll.addView(view);
		return ll;
	}

	@Override
	public void onStop() {
		super.onStop();
		if(canSave && needSave){
			try {
				ImportUtil.saveUsercommands(map);
			} catch (FileNotFoundException e) {
				
			}
		}
	}
	
	void dialogue(final String key, final boolean isPreview){
		String val = map.get(key);
		if(val == null){
			val = "function main(...)\n\tclient:send(\"Auto-generated answer\")\nend";
			map.put(key,val);
			resort();
		}
		
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
						if(!map.get(key).equals(edit.getText().toString())){
							map.remove(key);
							map.put(key,edit.getText().toString());
							needSave = true;
						}
					}
				}
			});
		ab.show();
	}
	
	void resort(){
		adapter.sort(new Comparator<String>(){

				@Override
				public int compare(String p1, String p2) {
					return p1.compareTo(p2);
				}

			});
	}
}
