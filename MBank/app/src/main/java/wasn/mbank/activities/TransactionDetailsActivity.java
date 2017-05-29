package wasn.mbank.activities;

import java.util.ArrayList;
import java.util.HashMap;

import wasn.mbank.application.MBankApplication;
import wasn.mbank.pojos.Transaction;
import wasn.mbank.threads.PrinterConnector;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

/**
 * activity class relates to transaction details
 * @author eranga
 */
public class TransactionDetailsActivity extends Activity {
				
	//name text			
	TextView nameText;				
	
	//nic text
	TextView nicText;
	
	//account no
	TextView accountNoText;
	
	//transaction type text
	TextView transactionTypeText;
	
	//previous balance
	TextView previousBalanceText;
	
	//transaction amount text
	TextView transactionAmountText;
	
	//balance amount
	TextView balanceAmountText;
	
	//transaction time text
	TextView transactionTimeText;
	
	//receipt id
	TextView receiptIdText;
	
	//application object instance
	private MBankApplication application;
	
	//list view
	private ListView transactionDetail;
	
	//downloading dialog id
	public static final int PRINTING_DIALOG_ID=10;
	
	/**
	 * call when create
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
						
		super.onCreate(savedInstanceState);
						
		//set layout			
		setContentView(R.layout.transaction_details_layout);
		//setContentView(R.layout.transaction_detail_layout);
							
		//initialize application object	
		application=(MBankApplication)getApplication();
									
		//set list
		transactionDetail=(ListView)findViewById(R.id.transaction_detail_list);
							
		//to hold details
		ArrayList<HashMap<String, String>> detailsList = new ArrayList<HashMap<String, String>>();
        
		//to hold one client attribute
        HashMap<String, String> attributeMap;
		
		/*
		 * initialize
		 */
		nameText=(TextView) findViewById(R.id.transaction_client_name_value);
		nicText=(TextView) findViewById(R.id.transaction_client_nic_value);
		accountNoText=(TextView) findViewById(R.id.transaction_account_no_value);
		transactionTypeText=(TextView) findViewById(R.id.transaction_type_value);
		previousBalanceText=(TextView) findViewById(R.id.transaction_previous_balance_value);
		transactionAmountText=(TextView) findViewById(R.id.transaction_amount_value);
		balanceAmountText=(TextView) findViewById(R.id.transaction_balance_amount_value);
		transactionTimeText=(TextView) findViewById(R.id.transaction_time_value);
		receiptIdText=(TextView) findViewById(R.id.transaction_receiptid_value);
		
		/*
		 * set texts
		 */
		if(application.getTransaction()!=null){
			
			//get transaction from application
			Transaction transaction=application.getTransaction();
	        
			nameText.setText(transaction.getClinetName());
			nicText.setText(transaction.getClinetNic());
			accountNoText.setText(transaction.getClientAccountNo());
			transactionTypeText.setText(transaction.getTransactionType());
			previousBalanceText.setText(transaction.getPreviousBalance());
			transactionAmountText.setText(transaction.getTransactionAmount());
			balanceAmountText.setText(transaction.getCurrentBalance());
			transactionTimeText.setText(transaction.getTransactionTime());
			receiptIdText.setText(transaction.getReceiptId());
			
		}
		
	}
	
	/**
	 * when create menu
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// inflate menu define in menu.xml
		getMenuInflater().inflate(R.menu.transaction_detail_menu, menu);

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
		if(item.getItemId()==R.id.transactionDetailMenuItemPrint){
			
			//get transaction
			Transaction transction=application.getTransaction();
			
			//transaction
			if (application.getTransactionState()==1){
				
				//save transaction in database/if success
				if(application.getMbankData().insertTransaction(transction)){
					
					//get attributes
					String accountNo=transction.getClientAccountNo();
					String balanceAmount=transction.getCurrentBalance();
					
					//try{
			
						//get receipt receipt no
						int receiptNo=Integer.parseInt(application.getMbankData().getReceiptNo());
						
						//next receipt no
						String nextReceiptNo=Integer.toString(receiptNo+1);
						
						//set next receipt no
						application.getMbankData().setReceiptNo(nextReceiptNo);
					
						//update client balance
						application.getMbankData().updateClientBalance(accountNo, balanceAmount);
						
					//}catch(Exception e){
						
						
						
					//}
									
					//show connecting dialog
					showDialog(PRINTING_DIALOG_ID);
					
					//start async task and print a receipt
					new PrinterConnector(TransactionDetailsActivity.this).execute();
					
				}
				
				//fail
				else{
					
					displayMessageDialog("Error", "Cannot save transaction");
					
				}
			
			}
			
			//reprint transaction
			else if(application.getTransactionState()==2){
				
				//show connecting dialog
				showDialog(PRINTING_DIALOG_ID);
				
				//start async task and print a receipt
				new PrinterConnector(TransactionDetailsActivity.this).execute();
				
			}
			
		}
		
		/*
		 * select home 
		 */
		else if(item.getItemId()==R.id.transactionDetailMenuItemHome){
			
			//clear current client in application object
			application.setClient(null);
			
			//clear current transaction in application object
			application.setTransaction(null);
			
			//set transaction state to 0
			application.setTransactionState(0);
			
			//back to MBankActivity
			startActivity(new Intent(TransactionDetailsActivity.this,MBankActivity.class));
			
		}
		
		return true;
		
	}
	
	/**
	 * create message dialogs to inform messages
	 * @param title - alert title
	 * @param message - message to display
	 */
	public void displayMessageDialog(String title,String message){
		
		/*
		 * alert builder
		 */
		AlertDialog.Builder alertBuilder= new AlertDialog.Builder(this);
		alertBuilder.setTitle(title);
		alertBuilder.setMessage(message);
		alertBuilder.setCancelable(false);
		
		/*
		 * ok button
		 */
		alertBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
			
				//cancel dialog
				dialog.cancel();
				
				//start printing thread
				
					
			}
							
		});
								
		/*
		 * cancel button
		 */
        alertBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				dialog.cancel();
				
			}
			
		});
		
		//display alert
        AlertDialog alert=alertBuilder.create();
        alertBuilder.show();
		
	}
	
	/**
	 * when create dialog
	 */
	@Override
	protected Dialog onCreateDialog(int id) {
	
		//create dialog
		ProgressDialog dialog=new ProgressDialog(TransactionDetailsActivity.this);
		
		//set message
		dialog.setMessage("Please wait ... ");
		
		if(id==PRINTING_DIALOG_ID){
			
			//set title
			dialog.setTitle("Printing");
			
			return dialog;
			
		}
		
		return null;
		
	}
	
	/**
	 * when task finish - close dialog 
	 */
	public void closePrintingDialog(int dialogId){
		
		//close dialog
		dismissDialog(dialogId);
		
		//clear current client in application object
		application.setClient(null);
		
		//clear current transaction in application object
		application.setTransaction(null);
		
		//transaction
		if(application.getTransactionState()==1){
			
			//go to transaction activity
			startActivity(new Intent(TransactionDetailsActivity.this,MBankTransactionActivity.class));
			
		}
		
		//reprint transaction
		else if(application.getTransactionState()==2){
			
			//go to MBankSummaryActivity
			startActivity(new Intent(TransactionDetailsActivity.this,MBankSummaryActivity.class));
			
		}
		
		//set transaction state to 0
		application.setTransactionState(0);
		
	}
	
}