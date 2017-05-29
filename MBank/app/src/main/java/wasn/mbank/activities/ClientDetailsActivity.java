package wasn.mbank.activities;

import java.util.ArrayList;
import java.util.HashMap;

import wasn.mbank.application.MBankApplication;
import wasn.mbank.pojos.Client;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

/**
 * activity class relates to client details
 * @author eranga
 */
public class ClientDetailsActivity extends Activity {					

	//name text
	TextView nameText;
	
	//nic text
	TextView nicText;
	
	//address text
	TextView birthDateText;
	
	//account no
	TextView accountNoText;
	
	//balance amount
	TextView balanceAmountText;
	
	//previous transaction
	TextView previousTransactionText;
	
	//application object instance
	private MBankApplication application;
	
	//list view
	private ListView clientDetail;
	
	/**
	 * call when create
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
			
		super.onCreate(savedInstanceState);
			
		//set layout
		setContentView(R.layout.client_details_layout);
		//setContentView(R.layout.client_detail_layout);
		
		//initialize application object
		application=(MBankApplication)getApplication();
		
		//set list
		clientDetail=(ListView)findViewById(R.id.client_detail_list);
		
		//to hold details
		ArrayList<HashMap<String, String>> detailsList = new ArrayList<HashMap<String, String>>();
        
		//to hold one client attribute
        HashMap<String, String> attributeMap;
        
		/*
		 * initialize
		 */
		nameText=(TextView) findViewById(R.id.client_name_value);
		nicText=(TextView) findViewById(R.id.client_nic_value);
		birthDateText=(TextView) findViewById(R.id.client_birthdate_value);
		accountNoText=(TextView) findViewById(R.id.client_account_no_value);
		balanceAmountText=(TextView) findViewById(R.id.client_balance_amount_value);
		previousTransactionText=(TextView) findViewById(R.id.client_previous_transaction_value);
		
		/*
		 * set texts
		 */
		if(application.getClient()!=null){
						
			//get client from application
			Client client=application.getClient();
			
			nameText.setText(client.getName());
			nicText.setText(client.getNic());
			birthDateText.setText(client.getBirthDate());
			accountNoText.setText(client.getAccountNo());
			balanceAmountText.setText(client.getBalanceAmount());
			previousTransactionText.setText(client.getPreviousTransaction());
			
		}
		
	}
	
	/**
	 * when create menu
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// inflate menu define in menu.xml
		getMenuInflater().inflate(R.menu.client_detail_menu, menu);

		return true;

	}
	
	/**
	 * when select a menu item
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		/*
		 * select Select menu 
		 */
		if(item.getItemId()==R.id.clientDetailMenuItemSelect){
			
			//back to MBankTransactionActivity
			startActivity(new Intent(ClientDetailsActivity.this,MBankTransactionActivity.class));
			
		}
		
		/*
		 * select Home menu
		 */
		else if(item.getItemId()==R.id.clientDetailItemHome){

			//clear current client in application object
			application.setClient(null);
			
			//clear current transaction in application object
			application.setTransaction(null);
			
			//set transaction state to 0
			application.setTransactionState(0);
			
			//back to MBankActivity
			startActivity(new Intent(ClientDetailsActivity.this,MBankActivity.class));
			
		}
		
		return true;
		
	}
	
}