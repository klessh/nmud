package com.iillyyaa2033.mud.editor.activity;
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.content.Intent;
import android.widget.EditText;

public class ObjectEditorActivity extends Activity {

	EditText view;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		view = new EditText(this);
		setContentView(view);
		
		int roomId = getIntent().getIntExtra("room_id",-1);
		if(roomId >-1){
			StringBuilder builder = new StringBuilder();
			builder.append("id:"+roomId);
			builder.append("\nname:untamed");
			view.setText(builder.toString());
		}
	}
}
