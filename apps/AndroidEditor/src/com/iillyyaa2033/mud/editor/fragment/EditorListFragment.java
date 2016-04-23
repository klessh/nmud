package com.iillyyaa2033.mud.editor.fragment;

import android.app.ListFragment;
import android.view.View;
import android.widget.ListView;

public class EditorListFragment extends ListFragment {
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		setSelection(position);
	}
}
