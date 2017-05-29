package wasn.mbank.activities;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * activity class for preference 
 * @author eranga
 *
 */
public class PrefsActivity extends PreferenceActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);		
		
		//get created preferences in Prefs.xml to here
		addPreferencesFromResource(R.xml.prefs);
		
	}
	
}
