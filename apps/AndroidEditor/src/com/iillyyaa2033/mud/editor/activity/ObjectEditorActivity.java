package com.iillyyaa2033.mud.editor.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.iillyyaa2033.mud.editor.R;
import java.util.ArrayList;
import java.util.Comparator;
import localhost.iillyyaa2033.mud.androidclient.logic.model.World;
import localhost.iillyyaa2033.mud.androidclient.logic.model.WorldObject;
import localhost.iillyyaa2033.mud.androidclient.utils.WorldHolder;

public class ObjectEditorActivity extends Activity {

	WorldObject object;
	boolean newelyCreated = false;
	
	ArrayAdapter<String> adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_objedit);

		String id = getIntent().getStringExtra("object");
		if (id != null) {
			WorldObject[] o = WorldHolder.getInstance().searchById(id);
			if (o.length > 0) object = o[0];
		}

		if (object != null) {
			getActionBar().setTitle(object.toString());
		} else {
			getActionBar().setTitle("New object");
			object = new WorldObject();
			newelyCreated = true;
		}

		TextView info = (TextView)findViewById(R.id.activity_objedit_info);
		info.setText("Стоим с гномом, открыв рот и выпучив глаза. Перед нами возвышается наспех сделанная избушка на пнях-ножках. Убей, не помню, откуда у меня взялась такая идея. Что-то зубастое и мелкое ржет в глубине сознания.\nНамекаю Биву, что теперь это наш транспорт.\nДалеко посылают, отказываются лезть внутрь.\nЯ и сам боюсь: дверь в двух метрах над землей, проем в форме рта с обломками зубов-веток, оно еще скрипит и воет. Н-да-а-а-а…\nКороче, я полез! Ежели что — не поминайте лихом. Бив, кажется, благословил меня напоследок. Хмуро обернулся. Мне ласково улыбнулись и махнули вслед. Ну-ну.");
		
		initializeParamsList();
	}

	@Override
	protected void onStop() {
		super.onStop();
		if(newelyCreated){
			WorldHolder.getInstance().autoAddToZone(object);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0,0,0,"Del");
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
			case 0:
				
		}
		return true;
	}
	
	void initializeParamsList() {
		ListView list = (ListView) findViewById(R.id.activity_objedit_list);

		TextView footer = new TextView(this);
		footer.setText("Новый параметр");
		footer.setPadding(24, 24, 24, 24);
		list.addFooterView(footer);

		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
		updateAdapter();
		list.setAdapter(adapter);
		list.setOnItemClickListener(new OnItemClickListener(){

				@Override
				public void onItemClick(AdapterView<?> p1, View p2, int p3, long p4) {
					if (p3 < adapter.getCount()) paramDialog(adapter.getItem(p3));
					else paramDialog(null);
				}
			});
		list.setOnItemLongClickListener(new OnItemLongClickListener(){

				@Override
				public boolean onItemLongClick(AdapterView<?> p1, View p2, int p3, long p4) {
					Toast.makeText(ObjectEditorActivity.this,"Deleting will be here",Toast.LENGTH_SHORT).show();
					return true;
				}
			});
	}

	void paramDialog(String which) {
		String tag = null;
		String value = null;

		if (which != null) {
			int ind = which.indexOf("]");
			tag = which.substring(which.indexOf("[") + 1, ind);
			value = which.substring(ind + 2);
		}

		switch (tag) {
			default:
				dialogParamDefault(tag, value);
		}
	}

	void dialogParamDefault(final String tag, String value) {
		final LinearLayout ll = new LinearLayout(this);
		ll.setOrientation(LinearLayout.VERTICAL);
		
		final EditText tg = new EditText(this);
		if(tag == null){
			tg.setHint("tag");
			ll.addView(tg);
		}
		
		final EditText val = new EditText(this);
		val.setText(value);
		val.setHint("value");
		ll.addView(val);
		
		AlertDialog.Builder ab = new AlertDialog.Builder(this);
		ab.setTitle("" + tag);
		ab.setView(ll);
		ab.setPositiveButton("ok", new DialogInterface.OnClickListener(){

				@Override
				public void onClick(DialogInterface p1, int p2) {
					object.params.put((tag==null?tg.getText().toString():tag), val.getText().toString());
					updateAdapter();
				}
			});
		ab.show();
	}

	void updateAdapter() {
		adapter.clear();

		ArrayList<String> params = new ArrayList<String>();
		for (String key : object.params.keySet()) {
			params.add("[" + key + "] " + object.params.get(key));
		}

		adapter.addAll(params);
		adapter.sort(new Comparator<String>(){

				@Override
				public int compare(String p1, String p2) {
					return p1.compareToIgnoreCase(p2);
				}
			});
	}
}
