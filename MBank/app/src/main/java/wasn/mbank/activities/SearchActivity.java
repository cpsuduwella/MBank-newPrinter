package wasn.mbank.activities;

import java.util.ArrayList;

import wasn.mbank.application.MBankApplication;
import wasn.mbank.pojos.Client;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

/**
 * activity class related to search
 * @author eranga
 */
public class SearchActivity extends Activity {

	//name text
	EditText nameText;
	
	//nic text
	EditText nicText;
	
	//address text
	EditText addressText;
	
	//application object instance
	private MBankApplication application;
	
	/**
	 * call when create
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
			
		super.onCreate(savedInstanceState);
			
		//set layout
		setContentView(R.layout.search_layout);
		
		//initialize application object
		application=(MBankApplication)getApplication();
		
		/*
		 * initialize 
		 */
		nameText=(EditText) findViewById(R.id.name_text);
		nicText=(EditText) findViewById(R.id.nic_text);
		addressText=(EditText) findViewById(R.id.address_text);
		
	}
	
	/**
	 * when create menu
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// inflate menu define in menu.xml
		getMenuInflater().inflate(R.menu.search_menu, menu);

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
		if(item.getItemId()==R.id.searchMenuItemSubmit){
			
			//get name
			String name = nameText.getText().toString().trim();
			
			//get nic
			String nic = nicText.getText().toString().trim();
			
			//get address
			String address = addressText.getText().toString().trim();
			
			//argument to model class method 
			String parameter=null;
			
			//client list
			ArrayList<Client> clients=new ArrayList<Client>();
			
			//new intent
			Intent searchResultIntent=new Intent(SearchActivity.this, SearchResultActivity.class);
			
			//empty fields
			if(name.equals("") && nic.equals("")){
				
				parameter=null;
				
				//get all client list
				clients=application.getMbankData().searchClientBySearchParameters(parameter);
				
				//have matching clients
				if(clients.size()>0){
					
					//set client list in application object
					application.setClients(clients);
					
					//start search result activity
					startActivity(searchResultIntent);
					
				}
				
				//no matching clients
				else{
					
					//display message
					displayMessageDialog("No matching clients");
					
				}					
				
			}
			
			//given name 
			else if(!name.equals("")){
				
				parameter="name like " + "'%" + name + "%'" ;
				
				//get all client list
				clients=application.getMbankData().searchClientBySearchParameters(parameter);
				
				//have matching clients
				if(clients.size()>0){
					
					//set client list in application object
					application.setClients(clients);
					
					//start search result activity
					startActivity(searchResultIntent);
					
				}
				
				//no matching clients
				else{
					
					//display message
					displayMessageDialog("No matching clients");
					
				}
				
			}
			
			//given nic
			else if(!nic.equals("")){
				
				parameter="nic like " + "'%" + nic + "%'" ;
				
				//get all client list
				clients=application.getMbankData().searchClientBySearchParameters(parameter);
				
				//have matching clients
				if(clients.size()>0){
					
					//set client list in application object
					application.setClients(clients);
					
					//start search result activity
					startActivity(searchResultIntent);
					
				}
				
				//no matching clients
				else{
					
					//display message
					displayMessageDialog("No matching clients");
					
				}
				
			}
			
			//other
			else{
				
				//display message
				displayMessageDialog("No matching clients");
				
			}
			
		}
		
		/*
		 * select cear 
		 */
		else if(item.getItemId()==R.id.searchMenuItemClear){
		
			//clear edit text contents
			nameText.setText("");
			nicText.setText("");
			addressText.setText("");
			
		}
		
		/*
		 * select home 
		 */
		else if(item.getItemId()==R.id.searchMenuItemHome){
			
			//clear edit text contents
			nameText.setText("");
			nicText.setText("");
			addressText.setText("");
			
			//set client list
			//application.setClients(null);
			
			//clear current client in application object
			application.setClient(null);
			
			//clear current transaction in application object
			application.setTransaction(null);
			
			//set transaction state to 0
			application.setTransactionState(0);
			
			//back to MBankActivity
			startActivity(new Intent(SearchActivity.this,MBankActivity.class));
			
		}
			
		return true;
		
	}
	
	/**
	 * create message dialogs to inform messages
	 * @param message - message to display
	 */
	public void displayMessageDialog(String message){
		
		/*
		 * alert builder
		 */
		AlertDialog.Builder alertBuilder= new AlertDialog.Builder(this);
		alertBuilder.setTitle("Result");
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
		
		//back to MBankActivity
		startActivity(new Intent(SearchActivity.this,MBankActivity.class));
		
	}		
	
}