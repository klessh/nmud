package com.iillyyaa2033.mud.editor.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;
import com.iillyyaa2033.mud.editor.logic.Database;
import java.util.StringTokenizer;
import android.widget.TextView;
import localhost.iillyyaa2033.mud.androidclient.logic.model.WorldObject;
import localhost.iillyyaa2033.mud.androidclient.logic.model.World;
import com.iillyyaa2033.mud.editor.R;

public class ObjectEditorActivity extends Activity {
	
	WorldObject object;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_objedit);
		
		String id = getIntent().getStringExtra("object");
		if(id!=null){
			WorldObject[] o = World.searchById(id);
			if(o.length>0) object = o[0];
		}
		
		if(object != null){
			getActionBar().setTitle(object.toString());
		}
	}
}
