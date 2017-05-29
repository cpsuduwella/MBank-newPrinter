package wasn.mbank.activities;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import wasn.mbank.application.MBankApplication;
import wasn.mbank.pojos.DaySummary;
import wasn.mbank.pojos.Transaction;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;

/**
 * activity class for display all transactions
 * @author eranga
 */
public class AllTransactionListActivity extends Activity{

	//application object instance
	private MBankApplication application;
	
	//list view
	private ListView transaction;
	
	/**
	 * call when create
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	
		super.onCreate(savedInstanceState);
		
		//set layout
		setContentView(R.layout.all_transaction_list_layout);
		
		//initialize application object
		application=(MBankApplication)getApplication();
		
		//set list
		transaction=(ListView)findViewById(R.id.all_transaction_list);
		
		//to hold clients 
		ArrayList<HashMap<String, String>> transactionList = new ArrayList<HashMap<String, String>>();
        
		//to hold one client
        HashMap<String, String> transactionMap;
        
        //get transaction list from database
        ArrayList<Transaction> transactions=new ArrayList<Transaction>();
        
        //get transaction list from database
        transactions=application.getMbankData().getTrnasactionData();
        
        //day summary object
        DaySummary daySummary=null;
        
        //deposit count
        int depositCount=0;
        
        //withdraw count
        int withdrawCount=0;
        
        //total deposit amount
        double depositAmount=0;
        
        //total withdraw amount
        double withdrawAmount=0;
        
        //iterate over transactions
        for(int i=0;i<transactions.size();i++){
        	
        	//create map
        	transactionMap=new HashMap<String, String>();
        	
        	//deposit
        	if(transactions.get(i).getTransactionType().equals("Deposit")){
        		
        		//increase count
        		depositCount++;
        		
        		try{
        			
        			//transaction amount
        			double transactionAmount=Double.parseDouble(transactions.get(i).getTransactionAmount());
        		
        			//add deposit amount
        			depositAmount=depositAmount+transactionAmount;
        		
        		}catch(Exception e){
        			
        		}
        		
        	}
        	
        	//withdraw
        	else{
        		
        		//increase
        		withdrawCount++;
        		
        		try{
        			
        			//transaction amount
        			double transactionAmount=Double.parseDouble(transactions.get(i).getTransactionAmount());
        		
        			//add withdraw amount
        			withdrawAmount=withdrawAmount+transactionAmount;
        		
        		}catch(Exception e){
        			
        		}
        		
        	}
        	
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
        
        // decimal format #.## format
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        
        //format
		String formatedDepositAmount=decimalFormat.format(depositAmount);
		
		//format
		String formatedWithdrawAmount=decimalFormat.format(withdrawAmount);
		
        //create day summary object
        daySummary=new DaySummary(depositCount, withdrawCount, formatedDepositAmount, formatedWithdrawAmount);
        
        
        //set day summary in application object
        application.setDaySummary(daySummary);
        
        //adapter
        SimpleAdapter adpter=new SimpleAdapter(this.getBaseContext(), 
        									   transactionList, 
        									   R.layout.all_transaction_list_row_layout,
        									   new String[] {"image", "name", "time", "amount"},
        									   new int[] {R.id.all_transaction_list_icon, R.id.all_transaction_list_client_name, R.id.all_transactino_list_client_account_no,R.id.all_transactino_list_transaction_amount});
        
        //set adapter
        transaction.setAdapter(adpter);
        
        //set on click listener
        transaction.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,long arg3) {
				
				//get selected list item
				HashMap<String, String> map = (HashMap<String, String>) transaction.getItemAtPosition(position);
				
				//get client account no
				String transactionTime=map.get("time");
				
				//get transaction object from database
				Transaction selectedTransaction=application.getMbankData().searchTransactionByTimr(transactionTime);
				
				//set transaction object in application
				application.setTransaction(selectedTransaction);
							
				/*
				 * set transaction state to 2
				 * it reprint transaction 
				 */
				application.setTransactionState(2);
				
				//start transaction details activity
				if(selectedTransaction!=null){
										
					startActivity(new Intent(AllTransactionListActivity.this,TransactionDetailsActivity.class));
							
				}
				
				else{
					
					System.out.println("null");
					
				}
						
			}
			
        });
        		
	}
	
	/**
	 * when create menu
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// inflate menu define in menu.xml
		getMenuInflater().inflate(R.menu.all_transaction_menu, menu);

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
		if(item.getItemId()==R.id.allTransactionMenuItemPrint){
			
																
			
		}
		
		/*
		 * select home 
		 */
		else if(item.getItemId()==R.id.allTransactionMenuItemHome){

			//clear current client in application object
			application.setClient(null);
			
			//clear current transaction in application object
			application.setTransaction(null);
			
			//set transaction state to 0
			application.setTransactionState(0);
			
			//back to MBankActivity
			startActivity(new Intent(AllTransactionListActivity.this,MBankActivity.class));
			
		}
	
		return true;
		
	}
	
	/**
     * when press back
     */
	@Override
	public void onBackPressed() {
		
		super.onBackPressed();
		
		//back to MBankActivity
		startActivity(new Intent(AllTransactionListActivity.this,MBankActivity.class));
		
	}
	
}
