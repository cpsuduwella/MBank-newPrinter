package wasn.mbank.activities;

import java.util.ArrayList;

import wasn.mbank.application.MBankApplication;
import wasn.mbank.pojos.DaySummary;
import wasn.mbank.pojos.Transaction;
import wasn.mbank.threads.PrinterConnector;
import wasn.mbank.threads.SummaryPrinter;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

/**
 * activity class relates to day summary
 * @author eranga
 */
public class DaySummaryActivity extends Activity{				

	//deposit count text						
	TextView depositCount;					
	
	//deposit value								
	TextView depositAmount;								
	
	//withdraw count									
	TextView withdrawCount;										
	
	//withdraw value text										
	TextView withdrawAmount;										
	
	//application object instance										
	private MBankApplication application;
	
	//connecting dialog id
	public static final int CONNECTING_DIALOG_ID=0;
	
	//printing dialog id
	public static final int PRINTING_DIALOG_ID=1;
	
	/**
	 * call when create
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
			
		super.onCreate(savedInstanceState);
			
		//set layout
		setContentView(R.layout.day_summary_layout);
		
		//initialize application object
		application=(MBankApplication)getApplication();
		
		/**
		 * initialize
		 */
		depositCount=(TextView) findViewById(R.id.deposit_count_value);
		depositAmount=(TextView) findViewById(R.id.deposit_amount_value);
		withdrawCount=(TextView) findViewById(R.id.withdrawal_count_value);
		withdrawAmount=(TextView) findViewById(R.id.withdrawal_amount_value);
		
		/*
		 * set texts
		 */
		if(application.getDaySummary()!=null){
		
			//get day summary object
			DaySummary daySummary=application.getDaySummary();
			
			depositCount.setText(""+daySummary.getDepositCount());
			depositAmount.setText(daySummary.getDepositAmount());
			withdrawCount.setText(""+daySummary.getWithdrawCount());
			withdrawAmount.setText(daySummary.getWithdrawAmount());
			
		}
		
	}
	
	/**
	 * when create menu
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// inflate menu define in menu.xml
		getMenuInflater().inflate(R.menu.day_summary_menu, menu);

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
		if(item.getItemId()==R.id.daySummaryMenuItemPrint){
			
			//get unsynced transaction list from database
			ArrayList<Transaction> transactionList=application.getMbankData().getUnsyncedTrnasactionData();
			
			//have unsycned transactions
			if(transactionList.size()>0){
				
				//can't print day summary
				displayMessageDialog("Available unsynced transactions.Please sync transaction befor print day summary", 0);
				
			}
			
			//no unsynced transactions 
			else{
				
				displayMessageDialog("All the transactions will be delete after printing the summary. Are you sure to print day summary?", 1);
				
			}
			
		}
		
		/*
		 * select home 
		 */
		else if(item.getItemId()==R.id.daySummaryMenuItemHome){

			//clear current client in application object
			application.setClient(null);
			
			//clear current transaction in application object
			application.setTransaction(null);
			
			//set transaction state to 0
			application.setTransactionState(0);
			
			//back to MBankActivity
			startActivity(new Intent(DaySummaryActivity.this,MBankActivity.class));
			
		}
	
		return true;
		
	}
	
	/**
	 * when create dialog
	 */
	@Override
	protected Dialog onCreateDialog(int id) {
	
		//create dialog
		ProgressDialog dialog=new ProgressDialog(DaySummaryActivity.this);
		
		//set message
		dialog.setMessage("Please wait ... ");
		
		if(id==CONNECTING_DIALOG_ID){
			
			//set title
			dialog.setTitle("Connecting");
			
			return dialog;
			
		}
		
		else if(id==PRINTING_DIALOG_ID){
			
			//set title
			dialog.setTitle("Printing");
			
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
		
		//clear current client in application object
		application.setClient(null);
		
		//clear current transaction in application object
		application.setTransaction(null);
		
		//set day summary object to null
		application.setDaySummary(null);
		
		//summary print success
		if(application.getDaySummaryPrintState()==1){
		
			//delete all transaction from database
			application.getMbankData().deleteTransactionData();
		
			//back to MBankActivity
			startActivity(new Intent(DaySummaryActivity.this,MBankActivity.class));
		
		}
		
		else{
			
			//stay here
			
		}
		
	}
	
	/**
	 * create message dialogs to inform messages
	 * @param message - message to display
	 * @param state - indicate can or cannot print 
	 * 				  0 - cannot print
	 * 				  1 - can print
	 */
	public void displayMessageDialog(String message,final int state){
		
		/*
		 * alert builder
		 */
		AlertDialog.Builder alertBuilder= new AlertDialog.Builder(this);
		alertBuilder.setTitle("Warning");
		alertBuilder.setMessage(message);	
		alertBuilder.setCancelable(false);
		
		/*
		 * ok button
		 */
		alertBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
			
				dialog.cancel();
				
				//can print day summary
				if(state==1){
					
					//display dialog
					showDialog(PRINTING_DIALOG_ID);
					
					//print receipt
					new SummaryPrinter(DaySummaryActivity.this).execute();
					
				}			
				
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
     * when press back
     */
	@Override
	public void onBackPressed() {
		
		super.onBackPressed();
		
		//back to MBankActivity
		startActivity(new Intent(DaySummaryActivity.this,MBankActivity.class));
		
	}
	
}
