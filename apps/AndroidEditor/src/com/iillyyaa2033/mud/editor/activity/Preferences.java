package com.iillyyaa2033.mud.editor.activity;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import com.iillyyaa2033.mud.editor.R;

public class Preferences extends PreferenceActivity{
	
	@Override public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);  
		addPreferencesFromResource(R.xml.preferences);
	}
}
