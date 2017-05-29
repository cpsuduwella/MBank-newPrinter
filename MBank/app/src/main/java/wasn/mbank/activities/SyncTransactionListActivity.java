package wasn.mbank.activities;

import java.util.ArrayList;
import java.util.HashMap;

import wasn.mbank.application.MBankApplication;
import wasn.mbank.pojos.Transaction;
import wasn.mbank.threads.Syncer;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleAdapter;

/**
 * activity class for transaction list
 * @author eranga
 */
public class SyncTransactionListActivity extends Activity{

	//application object instance
	private MBankApplication application;
	
	//list view
	private ListView transaction;
	
	//connecting dialog id
	public static final int CONNECTING_DIALOG_ID=0;
	
	//syncing dialog id
	public static final int SYNCING_DIALOG_ID=1;
	
	/**
	 * call when create
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	
		super.onCreate(savedInstanceState);
		
		//set layout
		setContentView(R.layout.sync_transaction_list_layout);
		
		//initialize application object
		application=(MBankApplication)getApplication();
		
		//set list
		transaction=(ListView)findViewById(R.id.sync_transaction_list);
		
		//to hold clients 
		ArrayList<HashMap<String, String>> transactionList = new ArrayList<HashMap<String, String>>();
        
		//to hold one client
        HashMap<String, String> transactionMap;
        
        //get transaction list from database
        ArrayList<Transaction> transactions=new ArrayList<Transaction>();
        
        //get transaction list from database
        transactions=application.getMbankData().getUnsyncedTrnasactionData();
        
        //iterate over transactions
        for(int i=0;i<transactions.size();i++){
        	
        	//create map
        	transactionMap=new HashMap<String, String>();
        	
        	/*
        	 * fill map
        	 */
        	transactionMap.put("name", transactions.get(i).getClinetName());
        	transactionMap.put("time", transactions.get(i).getTransactionTime());
        	transactionMap.put("amount", transactions.get(i).getTransactionAmount());
        	transactionMap.put("image", String.valueOf(R.drawable.dollar2));
        	
        	//add map to transaction list
        	transactionList.add(transactionMap);
        	
        	System.out.println("added to list");
        	
        }       
        
        //adapter 
        SimpleAdapter adpter=new SimpleAdapter(this.getBaseContext(), 
        									   transactionList, 
        									   R.layout.sync_transaction_list_row_layout,
        									   new String[] {"image", "name", "time", "amount"},
        									   new int[] {R.id.sync_transaction_list_icon, R.id.sync_transaction_list_client_name, R.id.sync_transactino_list_client_account_no,R.id.sync_transactino_list_transaction_amount});
        
        //set adapter
        transaction.setAdapter(adpter);
        
	}
	
	/**
	 * when create menu
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// inflate menu define in menu.xml
		getMenuInflater().inflate(R.menu.sync_transaction_menu, menu);

		return true;

	}
	
	/**
	 * when select a menu item
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		/*
		 * select submit
		 */
		if(item.getItemId()==R.id.syncTransactionMenuItemSync){
			
			//show connecting dialog
			showDialog(SYNCING_DIALOG_ID);
			
			//sync data to server
			new Syncer(SyncTransactionListActivity.this).execute("sync");
			
		}
		
		/*
		 * select home 
		 */
		else if(item.getItemId()==R.id.syncTransactionMenuItemHome){

			//clear current client in application object
			application.setClient(null);
			
			//clear current transaction in application object
			application.setTransaction(null);
			
			//set transaction state to 0
			application.setTransactionState(0);
			
			//back to MBankActivity
			startActivity(new Intent(SyncTransactionListActivity.this,MBankActivity.class));
			
		}
	
		return true;
		
	}
	
	/**
	 * when create dialog
	 */
	@Override
	protected Dialog onCreateDialog(int id) {
	
		//create dialog
		ProgressDialog dialog=new ProgressDialog(SyncTransactionListActivity.this);
		
		//set message
		dialog.setMessage("Please wait ... ");
		
		if(id==CONNECTING_DIALOG_ID){
			
			//set title
			dialog.setTitle("Connecting");
			
			return dialog;
			
		}
		
		else if(id==SYNCING_DIALOG_ID){
			
			//set title
			dialog.setTitle("Syncing");
			
			return dialog;
			
		}
		
		return null;
		
	}
	
	/**
	 * when task finish - close dialog
	 */
	public void closeConnectingDialog(int dialogId){
		
		//close dialog
		dismissDialog(dialogId);
		
		//back to MBankActivity
		startActivity(new Intent(SyncTransactionListActivity.this,MBankActivity.class));
		
	}
	
	/**
     * when press back
     */
	@Override
	public void onBackPressed() {
		
		super.onBackPressed();
		
		//back to MBankActivity
		startActivity(new Intent(SyncTransactionListActivity.this,MBankActivity.class));
		
	}
	
}
