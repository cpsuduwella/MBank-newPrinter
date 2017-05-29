package wasn.mbank.activities;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

/**
 * class relates to transaction tab activity 
 * @author eranga
 */
public class MBankTransactionActivity extends TabActivity{

	//tab host
	TabHost tabHost;
	
	/** 
	 * call when create activity. 
	 */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        
    	super.onCreate(savedInstanceState);
    	
    	//set layout
        setContentView(R.layout.mbank_transaction_tab_layout);
		
        //get tab host
        tabHost=getTabHost();

        //tab for transaction
        TabSpec transactionspec=tabHost.newTabSpec("Transaction");
        //setting title and icon for transaction tab
        transactionspec.setIndicator("Transaction", getResources().getDrawable(R.drawable.transaction));
        //create new intent and set content of transaction tab
        Intent transactionIntent=new Intent(this,TransactionActivity.class);
        transactionspec.setContent(transactionIntent);
        
        //tab for search
        TabSpec searchspec=tabHost.newTabSpec("Search");
        //setting title and icon for search tab
        searchspec.setIndicator("Search", getResources().getDrawable(R.drawable.searchdd));
        //create new intent and set content of search tab
        Intent searchIntent=new Intent(this,SearchActivity.class);
        searchspec.setContent(searchIntent);
        
        //add tabspecs for tab host
        tabHost.addTab(transactionspec);
        tabHost.addTab(searchspec);
        
    }
	
}
