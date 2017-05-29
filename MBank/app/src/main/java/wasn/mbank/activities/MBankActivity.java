package wasn.mbank.activities;

import java.util.ArrayList;
												
import wasn.mbank.application.MBankApplication;
import wasn.mbank.pojos.Transaction;
import wasn.mbank.threads.Downloader;
import wasn.mbank.threads.Logger;
import wasn.mbank.utils.SHA1;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class MBankActivity extends Activity {
	
	//to hold grid view elements
	private MBankGridViewAdapter mbankGridViewAdapter;
	
	//application object instance
	private MBankApplication application;
	
	//hold grid view element texts
	private ArrayList<String> texts;
	
	//to hold grid view element image ids
	private ArrayList<Integer> images;
	
	//grid view
	private GridView gridView;
	
	//connecting dialog id
	public static final int CONNECTING_DIALOG_ID=0;
	
	//downloading dialog id
	public static final int DOWNLOADING_DIALOG_ID=1;
	
    /**
     * call when create
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.mbank_layout);
		//startActivity(new Intent(MBankActivity.this,TransactionActivity.class));
        
        //initialize application object
		application=(MBankApplication)getApplication();
        
		//set logged state to 0
		//application.getMbankData().setLoginState("1");
		
		//set client
		application.setClient(null);
		
		//set transaction
		application.setTransaction(null);
		
		//set client list
		application.setClients(null);
		
        //create grid view lists
		createLists();
        
        //initialize adapter
        mbankGridViewAdapter=new MBankGridViewAdapter(this, texts, images);
        
        //set adapter to grid view
        gridView=(GridView)findViewById(R.id.mbankGridView);
        gridView.setAdapter(mbankGridViewAdapter);
        
        //implement On Item click listener
        gridView.setOnItemClickListener(new OnItemClickListener(){
        	
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,long arg3) {
			
				//get selected item
				String selection=mbankGridViewAdapter.getItem(position);
				
				//select Connect
				if(selection.equals("Login")){
					
					//show connecting dialog
					showDialog(CONNECTING_DIALOG_ID);
					
					//get user name from shared preference
					String username=application.getPrefences().getString("username", "");
					
					//get user name from shared preference
					String password=SHA1.encode(application.getPrefences().getString("password", ""));
					
					//connect to MBank server and send login request
					new Logger(MBankActivity.this).execute(username,password);
					
				}
				
				//select Search
				if(selection.equals("Download")){
					
					//get login state
					String loginState=application.getMbankData().getLoginState();
					
					//login fail or still not logged to system
					if(loginState.equals("0")){
						
						//display toast
						//Toast.makeText(MBankActivity.this, "Need to login to the system", Toast.LENGTH_SHORT ).show();
						
						//display warning message
						displayMessageDialog("Please login to the system");
						
					}
					
					//logged to system
					else{
						
						//show downloading dialog
						showDialog(DOWNLOADING_DIALOG_ID);
						
						//get branch id from database
						String branchId=application.getMbankData().getBranchId();
						
						//connect to MBank server and send downloading request
						new Downloader(MBankActivity.this).execute(branchId);
						
					}
					
				}
				
				//select transaction
				if(selection.equals("Transaction")){
					
					//get login state
					String loginState=application.getMbankData().getLoginState();
					
					System.out.println(loginState);
					
					//login fail or still not logged to system
					if(loginState.equals("0")){
						
						//display warning message
						displayMessageDialog("Please login to the system");
						
					}
					
					//logged to system
					else{
						
						//get download state
						String downloadState=application.getMbankData().getDownloadSate();
						
						System.out.println(downloadState);
						
						//no downloaded data
						if(downloadState.equals("0")){
							
							//display warning message
							displayMessageDialog("Please download the data");
							
						}
						
						//data downloded
						else{
						
							//start Transaction activity
							startActivity(new Intent(MBankActivity.this,MBankTransactionActivity.class));
							
						}
						
					}
					
				}
				
				//select summary
				if(selection.equals("Sync")){
					
					//get login state
					String loginState=application.getMbankData().getLoginState();
					
					//login fail or still not logged to system
					if(loginState.equals("0")){
						
						//display warning message
						displayMessageDialog("Please login to the system");
						
					}
					
					//logged to system
					else{
						
						//get transaction list												
						ArrayList<Transaction> transactions=new ArrayList<Transaction>();
						transactions=application.getMbankData().getUnsyncedTrnasactionData();
						
						//if transaction count in transaction table > 0
						if(transactions.size()>0){
						
							//start Transaction activity
							startActivity(new Intent(MBankActivity.this,SyncTransactionListActivity.class));
							
						}
						
						else{
							
							//else display message
							displayMessageDialog("No transactions to sync");
							
						}
						
					}
					
				}
				
				//select summary
				if(selection.equals("Summary")){
					
					//get login state
					String loginState=application.getMbankData().getLoginState();
					
					//login fail or still not logged to system
					if(loginState.equals("0")){
						
						//display warning message
						displayMessageDialog("Please login to the system");
						
					}
					
					//logged to system
					else{
						
						//get transaction list
						ArrayList<Transaction> transactions=new ArrayList<Transaction>();
						transactions=application.getMbankData().getTrnasactionData();
						
						//if transaction count in transaction table > 0
						if(transactions.size()>0){
						
							//start Summary activity
							startActivity(new Intent(MBankActivity.this,MBankSummaryActivity.class));
							
						}
						
						else{
							
							//else display message
							displayMessageDialog("No available transactions");
							
						}
						
					}
					
				}
				
				//select About us
				if(selection.equals("About us")){
					
					//start about us activity
					startActivity(new Intent(MBankActivity.this,MBankAboutusActivity.class));
					
				}
				
				//select Contact us
				if(selection.equals("Contact us")){
					
					//start contact us activity
					startActivity(new Intent(MBankActivity.this,MBankContactusActivity.class));
					
				}
				
			}
			
		});
        
    }
    
    /**
	 * fill texts list and image list 
	 */
	public void createLists(){
		
		//initialize texts
		texts=new ArrayList<String>();
		
		//add elements to list
		texts.add("Login");
		texts.add("Download");
		texts.add("Transaction");
		texts.add("Sync");
		texts.add("Summary");
		texts.add("Contact us");
		
		//initialize images
		images=new ArrayList<Integer>();
		
		//add elements to list
		/*images.add(R.drawable.login);
		images.add(R.drawable.download_icon);
		images.add(R.drawable.mobile_icon);
		images.add(R.drawable.hotsync1);
		images.add(R.drawable.icon_chat);
		images.add(R.drawable.contact_icon);*/
	
		images.add(R.drawable.login_new);
		images.add(R.drawable.aaa);
		images.add(R.drawable.sample);
		images.add(R.drawable.sync);
		images.add(R.drawable.quarterly_updateeee);
		images.add(R.drawable.contact);
		
	}
	
	/**
	 * when create menu
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// inflate menu define in menu.xml
		getMenuInflater().inflate(R.menu.mbank_menu, menu);

		return true;

	}
	
	/**
	 * when select a menu item
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {

			case R.id.mbankMenuItemPrefs:
				//display prefs activity
				startActivity(new Intent(MBankActivity.this, PrefsActivity.class));
				break;
				
			case R.id.mbankMenuItemLogout:
				//log out
				application.getMbankData().setLoginState("0");
				
				break;	

		}

		return true;

	}				
	
	/**
	 * when create dialog
	 */
	@Override
	protected Dialog onCreateDialog(int id) {
	
		//create dialog
		ProgressDialog dialog=new ProgressDialog(MBankActivity.this);
		
		//set message
		dialog.setMessage("Please wait ... ");
		
		if(id==CONNECTING_DIALOG_ID){
			
			//set title
			dialog.setTitle("Connecting");
			
			return dialog;
			
		}
		
		else if(id==DOWNLOADING_DIALOG_ID){
			
			//set title
			dialog.setTitle("Downloading");
			
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
		
	}
	
	/**
	 * call when finish activity
	 */
	@Override
	protected void onDestroy() {
	
		super.onDestroy();
		
		//close database connections
		application.getMbankData().close();
		
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
		
		//super.onBackPressed();
		
		//back to MBankActivity
		//startActivity(new Intent(TransactionActivity.this,MBankActivity.class));
		
	}

}