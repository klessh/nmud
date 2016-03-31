package com.iillyyaa2033.mud.editor.fragment;

import android.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.ListView;
import android.app.ListFragment;
import android.widget.Toast;
import android.content.Intent;
import com.iillyyaa2033.mud.editor.activity.MapEditorActivity;
import com.iillyyaa2033.mud.editor.activity.Preferences;
import com.iillyyaa2033.mud.editor.activity.HelpActivity;

public class MainListFragment extends ListFragment {

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		
		switch(position){
			case 0:
				startActivity(new Intent(getActivity(),MapEditorActivity.class));
				break;
			case 1:
				startActivity(new Intent(getActivity(),Preferences.class));
				break;
			case 2:
				startActivity(new Intent(getActivity(),HelpActivity.class));
				break;
		}
	}
}
