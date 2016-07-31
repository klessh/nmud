package localhost.iillyyaa2033.mud.androidclient.activity;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import localhost.iillyyaa2033.mud.androidclient.R;
import localhost.iillyyaa2033.mud.androidclient.utils.GlobalValues;

public class PreferencesActivity extends PreferenceActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
	}

	@Override
	protected void onStop() {
		super.onStop();
		GlobalValues.debug = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("FLAG_DEBUG",true);
		GlobalValues.debug_importer = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("FLAG_DEBUG_IMPORTER",false);
		GlobalValues.debug_descr = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("FLAG_DEBUG_DESCR",false);
		GlobalValues.debug_scripts = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("FLAG_DEBUG_SCRIPTS",false);
		GlobalValues.debug_graph = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("FLAG_DEBUG_GRAPH",false);	
		
	}
}
