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
import android.widget.TabHost;
import com.iillyyaa2033.mud.editor.logic.Database;
import android.content.Intent;
import com.iillyyaa2033.mud.editor.activity.DictionaryEditorActivity;

public class DictionaryFragment extends Fragment{
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		ListView view = new ListView(getActivity());
		view.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,Database.dictNames));
		view.setOnItemClickListener(new OnItemClickListener(){

				@Override
				public void onItemClick(AdapterView<?> p1, View p2, int p3, long p4) {
					Intent i = new Intent(getActivity(),DictionaryEditorActivity.class);
					i.putExtra("wID",p3);
					startActivity(i);
				}
			});
		return view;
	}
	
	
}
