package wasn.mbank.activities;

import java.util.ArrayList;
import java.util.HashMap;

import wasn.mbank.application.MBankApplication;
import wasn.mbank.pojos.Client;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;

/**
 * activity class for search result 
 * @author eranga
 */
public class SearchResultActivity extends Activity{

	//application object instance
	private MBankApplication application;
	
	//list view
	private ListView searchResult;
	
	/**
	 * call when create
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	
		super.onCreate(savedInstanceState);
		
		//set layout
		setContentView(R.layout.search_result_layout);
		
		//initialize application object
		application=(MBankApplication)getApplication();
		
		//set list
		searchResult=(ListView)findViewById(R.id.search_result_list);
		
		//to hold clients 
		ArrayList<HashMap<String, String>> clientList = new ArrayList<HashMap<String, String>>();
        
		//to hold one client
        HashMap<String, String> clientMap;
        
        //list of matching clients
        ArrayList<Client> clients=new ArrayList<Client>();
        
        //get matching clients
        clients=application.getClients();
        
        //have matching clients
        if(clients!=null){
        
	        //iterate over clients
	        for(int i=0;i<clients.size();i++){
	        	
	        	//create map
	        	clientMap=new HashMap<String, String>();
	        	
	        	/*
	        	 * fill map
	        	 */
	        	clientMap.put("name", clients.get(i).getName());
	        	clientMap.put("accountNo", clients.get(i).getAccountNo());
	        	clientMap.put("nic", clients.get(i).getNic());
	        	clientMap.put("image", String.valueOf(R.drawable.user_info));
	        	
	        	//add map to client list
	        	clientList.add(clientMap);
	        	
	        }
        
        }
        
        //adapter 
        SimpleAdapter adpter=new SimpleAdapter(this.getBaseContext(), 
        									   clientList, 
        									   R.layout.search_result_row_layout,
        									   new String[] {"image", "name", "accountNo", "nic"},
        									   new int[] {R.id.client_icon, R.id.client_name, R.id.client_account_no,R.id.client_nic});
	        
        //set adapter
        searchResult.setAdapter(adpter);
        
        //set on click listener
        searchResult.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,long arg3) {
				
				//get selected list item
				HashMap<String, String> map = (HashMap<String, String>) searchResult.getItemAtPosition(position);
				
				//get client account no
				String accountNo=map.get("accountNo");
				
				//get client object from database
				Client client=application.getMbankData().searchClientByAccountNo(accountNo);
				
				//set selected client object in application
				application.setClient(client);
				
				//start client details activity
				if(client!=null){
												
					startActivity(new Intent(SearchResultActivity.this,ClientDetailsActivity.class));
										
				}
				
			}
        	
        });
        
	}
	
}