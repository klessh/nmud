package com.iillyyaa2033.mud.editor.activity;
import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;
import com.iillyyaa2033.mud.editor.logic.Database;
import java.util.StringTokenizer;

public class ObjectEditorActivity extends Activity {

	EditText view;
	int roomId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		view = new EditText(this);
		setContentView(view);
		
		roomId = getIntent().getIntExtra("room_id",-1);
		if(roomId >-1){
			StringBuilder builder = new StringBuilder();
			builder.append("id:"+roomId);
			builder.append("\nname:untamed");
			view.setText(builder.toString());
		}
	}

	@Override
	protected void onPause() {
		if(roomId<0) return; 
		
		
		super.onPause();
	}
}
