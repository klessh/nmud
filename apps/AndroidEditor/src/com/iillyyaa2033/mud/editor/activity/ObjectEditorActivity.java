package com.iillyyaa2033.mud.editor.activity;
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.content.Intent;
import android.widget.EditText;
import com.iillyyaa2033.mud.editor.logic.nRoom;
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
		for(nRoom room : Database.rooms){
			if(room.id == roomId){
				room = assignData(room);
			}
		}
		
		super.onPause();
	}
	
	nRoom assignData(nRoom old){
		StringTokenizer token = new StringTokenizer(view.getText().toString(),"\n");
		while(token.hasMoreTokens()){
			String text = token.nextToken();
			if(text.startsWith("name:")){
				old.name = text.substring(5);
			}
		}
		return old;
	}
}
