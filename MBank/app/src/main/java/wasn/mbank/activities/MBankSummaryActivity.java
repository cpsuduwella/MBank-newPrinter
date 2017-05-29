package wasn.mbank.activities;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

/**
 * class relates to summary activity
 * @author eranga
 */
public class MBankSummaryActivity extends TabActivity{

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
        
        //tab for all transaction
        TabSpec transactionspec=tabHost.newTabSpec("AllTransaction");
        //setting title and icon
        transactionspec.setIndicator("All Transactions", getResources().getDrawable(R.drawable.summary));
        //create new intent and set content
        Intent transactionIntent=new Intent(this,AllTransactionListActivity.class);
        transactionspec.setContent(transactionIntent);
        
        //tab for day summary
        TabSpec daysummaryspec=tabHost.newTabSpec("DaySummary");
        //setting title and icon
        daysummaryspec.setIndicator("Day Summary", getResources().getDrawable(R.drawable.statistic1));
        //create new intent and set content of search tab
        Intent searchIntent=new Intent(this,DaySummaryActivity.class);
        daysummaryspec.setContent(searchIntent);
        
        //add tabspecs for tab host
        tabHost.addTab(transactionspec);
        tabHost.addTab(daysummaryspec);
        
    }
	
}
