package com.iillyyaa2033.mud.editor.mapeditor;

import android.widget.ArrayAdapter;
import android.content.Context;
import java.util.ArrayList;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.TextView;
import android.util.Log;

public class MapEditorAdapter extends ArrayAdapter{
	
	ArrayList<nObject> objects;
	Context c;
	
	public MapEditorAdapter(Context c, int layout){
		super(c,layout);
		objects = new ArrayList<nObject>();
		this.c = c;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		if(convertView == null){
			LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(android.R.layout.simple_list_item_1, null);
		}
		TextView title = (TextView) convertView.findViewById(android.R.id.text1);
		title.setText(objects.get(position).qualitys[0][0]);
		return convertView;
	}

	@Override
	public int getCount(){
		return objects.size();
	}
	
	@Override
	public nObject getItem(int position){
		return objects.get(position);
	}

	@Override
	public int getPosition(nObject item){
		return objects.indexOf(item);
	}

	@Override
	public long getItemId(int position){
		return position;
	}

	@Override
	public void add(nObject object){
		objects.add(object);
		notifyDataSetChanged();
	}

	public void addAlll(ArrayList<nObject> list){
		objects.addAll(list);
		notifyDataSetChanged();
	}
	
	public void rm(int position){
		objects.remove(position);
		notifyDataSetChanged();
	}
}
