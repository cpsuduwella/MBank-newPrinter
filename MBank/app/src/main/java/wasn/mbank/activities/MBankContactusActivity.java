package wasn.mbank.activities;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

/**
 * contact us activity/tabbed activity
 * @author eranga
 */
public class MBankContactusActivity extends TabActivity{

	// tab host to hold tabs
	TabHost tabHost;

	/**
	 * when create activity
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.mbank_contactus_layout);

		// initialize tabHost
		tabHost = getTabHost();

		// tab for contact info
		TabSpec infospec = tabHost.newTabSpec("Contact info");
		// setting title and icon for power tab
		infospec.setIndicator("Contact info",getResources().getDrawable(R.drawable.dialog_inf));
		// create new intent and set content of power tab
		Intent contactInfoIntent = new Intent(MBankContactusActivity.this, ContactInfoActivity.class);
		infospec.setContent(contactInfoIntent);

		// tab for map
		TabSpec mappec = tabHost.newTabSpec("Location");
		// setting title and icon for power tab
		mappec.setIndicator("Location",getResources().getDrawable(R.drawable.home_icon));
		// create new intent and set content of power tab
		Intent mapIntent = new Intent(MBankContactusActivity.this,ContactLocationActivity.class);
		mappec.setContent(mapIntent);

		// add tab specs for tab host
		tabHost.addTab(infospec);
		tabHost.addTab(mappec);

	}
	
}
