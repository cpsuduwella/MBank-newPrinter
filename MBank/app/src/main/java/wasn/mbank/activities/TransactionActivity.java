package wasn.mbank.activities;

import wasn.mbank.application.MBankApplication;
import wasn.mbank.pojos.Client;
import wasn.mbank.utils.DepositProcessor;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.RadioButton;

/**
 * activity class relates to transaction 
 * @author eranga
 */
public class TransactionActivity extends Activity{

	//application object instance
	private MBankApplication application;
	
	//account no text
	EditText accountNoText;
	
	//transaction amount text
	EditText transactionAmountText;
	
	//deposit radio button
	RadioButton depositRadioButton;
	
	//withdraw radio button
	RadioButton withdrawRadioButton;
	
	/**
	 * call when create
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
			
		super.onCreate(savedInstanceState);
			
		//set layout
		setContentView(R.layout.transaction_layout);
		
        /*
		 * initialize
		 */
		accountNoText=(EditText) findViewById(R.id.account_no_text);
		transactionAmountText=(EditText) findViewById(R.id.transaction_amount_text);
		depositRadioButton=(RadioButton) findViewById(R.id.transction_type_deposit);
		withdrawRadioButton=(RadioButton) findViewById(R.id.transction_type_withdraw);
		
		//initialize application object
		application=(MBankApplication)getApplication();
		
		//set account no
		if(application.getClient()!=null){
			
			accountNoText.setText(application.getClient().getAccountNo());
			
		}
		
	}
	
    /**
	 * when create menu
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// inflate menu define in menu.xml
		getMenuInflater().inflate(R.menu.transaction_menu, menu);

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
		if(item.getItemId()==R.id.transactionMenuItemSubmit){
			
			String accountNo=accountNoText.getText().toString();
			String amount=transactionAmountText.getText().toString();
			
			/*
			 * Missing fields
			 */
			if(accountNo.equals("") || amount.equals("")){
				
				//display dialog
				displayMessageDialog("Warning","Missing some fields");
				
			}
			
			/*
			 * have all fields
			 */
			else{
			
				/*
				 * transaction amount 0
				 */
				if(Double.parseDouble(amount)==0){
					
					//display dialog
					displayMessageDialog("Error","Amount cannot be 0");
					
					//clear text field
					transactionAmountText.setText("");
					
				}
				
				/*
				 * valid amount
				 */
				else{
					
					/*
					 * check deposit
					 */
					if(depositRadioButton.isChecked()==true){
						
						//get corresponding client from database
						Client client=application.getMbankData().searchClientByAccountNo(accountNo);
						
						//have client
						if(client!=null){
							
							//process transaction
							if(new DepositProcessor(application).processDeposit(amount,client)){
									
								//set transaction state
								application.setTransactionState(1);
								
								//start TransactionDetailsActivity
								startActivity(new Intent(TransactionActivity.this,TransactionDetailsActivity.class));
								
							}
							
							//fail processing
							else{
								
								displayMessageDialog("Error","Fail process transaction");
								
							}
							
						}
						
						//no such client invalid details
						else{
							
							displayMessageDialog("Error","Invalid account no");
							
						}
						
					}
					
					/*
					 * check withdraw
					 */
					else if(withdrawRadioButton.isChecked()){
						
						System.out.println("withdraw");
						
					}
				
				}
				
			}
						
		}
		
		/*
		 * select clear
		 */
		else if(item.getItemId()==R.id.transactionMenuItemClear){
			
			//clear edit text contents
			accountNoText.setText("");
			transactionAmountText.setText("");
			
		}
		
		/*
		 * select home 
		 */
		else if(item.getItemId()==R.id.transactionMenuItemHome){
			
			//clear edit text contents
			accountNoText.setText("");
			
			transactionAmountText.setText("");
			
			//clear current client in application object
			application.setClient(null);
			
			//clear current transaction in application object
			application.setTransaction(null);
			
			//set transaction state to 0
			application.setTransactionState(0);
			
			//back to MBankActivity
			startActivity(new Intent(TransactionActivity.this,MBankActivity.class));
			
		}
		
		return true;
		
	}

	@Override
	protected void onDestroy() {
		
		super.onDestroy();
		
		//set client in application object to null
		application.setClient(null);
		
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
			
				dialog.cancel();
					
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
		
		//clear current client in application object
		application.setClient(null);
		
		//clear current transaction in application object
		application.setTransaction(null);
		
		//set transaction state to 0
		application.setTransactionState(0);
		
		//back to MBankActivity
		startActivity(new Intent(TransactionActivity.this,MBankActivity.class));
		
	}
	
}