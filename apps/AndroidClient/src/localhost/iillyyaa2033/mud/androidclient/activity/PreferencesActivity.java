package localhost.iillyyaa2033.mud.androidclient.activity;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import localhost.iillyyaa2033.mud.androidclient.R;

public class PreferencesActivity extends PreferenceActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
	}
	
}
