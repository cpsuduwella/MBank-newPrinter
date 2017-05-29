package wasn.mbank.activities;

import android.os.Bundle;

/**
 * @author eranga
 * activity class correspond to configuration preferance
 */
public class ConfigurationActivity extends PrefsActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		//get created preferences in Prefs.xml to here
		addPreferencesFromResource(R.xml.configuration);
		
	}
	
}
