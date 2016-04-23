package com.iillyyaa2033.mud.editor.fragment;

import android.app.ListFragment;
import android.content.Intent;
import android.view.View;
import android.widget.ListView;
import com.iillyyaa2033.mud.editor.activity.EditorActivity;
import com.iillyyaa2033.mud.editor.activity.Preferences;

public class MainListFragment extends ListFragment {

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		
		switch(position){
			case 0:
				startActivity(new Intent(getActivity(),EditorActivity.class));
				break;
			case 1:
				startActivity(new Intent(getActivity(),Preferences.class));
				break;
		}
	}
}
