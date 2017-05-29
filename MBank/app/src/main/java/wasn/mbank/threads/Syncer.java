package wasn.mbank.threads;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import android.os.AsyncTask;
import android.widget.Toast;
import wasn.mbank.activities.SyncTransactionListActivity;
import wasn.mbank.application.MBankApplication;
import wasn.mbank.pojos.Transaction;

/**
 * connect to MBank server and sync data
 * @author eranga
 */
public class Syncer extends AsyncTask<String, String, String>{

	//activity object, singleton
	SyncTransactionListActivity syncTransactionAcivity;
	
	//application object instance
	MBankApplication application;
	
	//synced record count
	public int syncedRecordCount=0;
	
	/**
	 * constructor
	 */
	public Syncer(SyncTransactionListActivity activity) {
	
		this.syncTransactionAcivity=activity;
		
		//initialize application object
		application=(MBankApplication)syncTransactionAcivity.getApplication();
		
	}
	
	/**
	 * run in background
	 */
	@Override
	protected String doInBackground(String... params) {
		
		//sleep for while
		try{
			
			Thread.sleep(1000);
			
		}catch(Exception e){

		}
		
		//get sync type
		String syncType=params[0];
		
		//sync data to server
		int syncState=syncDataToServer(syncType);
		
		//return
		return Integer.toString(syncState);
		
	}
	
	/**
	 * authenticate user by connecting to server
	 * @return
	 */
	public int syncDataToServer(String syncType){
		
		//sync state
		int syncState=0;
		
		//new url
		URL url=null;
		
		//outpu stream
		OutputStream outputStream=null;
		
		try {
			
			//to TransactionServlet
			//url = new URL("http://10.22.137.65:8080/MBank_Server/TransactionServlet");
			url = new URL("http://10.100.31.5:8080/MBank_Server/TransactionServlet");
			//url = new URL("http://10.100.19.29:8080/MBank_Server/TransactionServlet");
			
			//new connection
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			
			//connection properties
			connection.setDoOutput(true);
			connection.setRequestMethod("POST");
			
			//get output stream
			outputStream=connection.getOutputStream();
			
			//get unsynced transaction list from database
			ArrayList<Transaction> transactionList=application.getMbankData().getUnsyncedTrnasactionData();
			
			//initially send transaction count
            String recordCount="sync"+" "+transactionList.size()+"\n";
            
            //initially send record count
            outputStream.write(recordCount.getBytes());
            
			//iterate over transaction list
			for(int i=0;i<transactionList.size();i++){
				
				//get transaction
				Transaction transaction=transactionList.get(i);
				
				//sending record consists \n
                String sendingRecord=transaction.getTransactionTime().replace(" ", "  ")+","+
                                     transaction.getClientAccountNo()+","+
                                     transaction.getClientId()+","+
                                     transaction.getTransactionType()+","+
                                     transaction.getTransactionAmount()+","+
                                     "empty"+","+
                                     transaction.getBranchId()+","+
                                     transaction.getReceiptId()+","+
                                     "empty"+"\n";					
				
                System.out.println(sendingRecord);
                
                //send record
                outputStream.write(sendingRecord.getBytes());
                
                //increase synced record count
                syncedRecordCount++;
                
			}
			
			//flush data
			outputStream.flush();
			
			//get server response
			syncState=processServerResponse(connection);
			
		}
		
		//error in sync
		catch(Exception e){
			
			System.out.println(e);
			
			syncState=-7;
			
		}
		
		//close connections
		finally{
			
			try{
				
				if(outputStream!=null){
			
					//close
					outputStream.close();
					
				}
			
			}catch(Exception e){
				
			}
			
		}
		
		//return
		return syncState;
		
	}
	
	
	/**
	 * get server response and sync state
	 * @param connection
	 * @throws IOException
	 */
	public int processServerResponse(HttpURLConnection connection) throws IOException{
		
		//get input stream
		InputStream inputStream=connection.getInputStream();
		
		//to store branch id
		int serverResponse=0;
		
		//to store reading data
        StringBuffer buffer=new StringBuffer();

        //reading character
        int readingCharacter;

        //read character by character
        while ((readingCharacter = inputStream.read()) != -1){
            
            //append character to buffer
            buffer.append((char) readingCharacter);
        	
        }				
        
        try{
        
        	//get server response
        	serverResponse=Integer.parseInt(buffer.toString().trim());
        	
        	System.out.println(serverResponse);
        	
        }
        
        //error / connection
        catch(Exception e){
        	
        	System.out.println(e);
        	
        	serverResponse=-7;
        	
        }
        
		finally{
			
			try{
				
				//close stream
				inputStream.close();
				
			}catch(Exception e){
				
			}
			
		}
        
        //return
        return serverResponse;
        
	}
	
	/**
	 * call when doInBackground is finish
	 */
	@Override
	protected void onPostExecute(String result){
					
		super.onPostExecute(result);
		
		//message to display
		String message;
		
		if(Integer.parseInt(result)>0){
			
			message="Synced " + syncedRecordCount + " records ";
			
			//update transaction state
			application.getMbankData().updateTransactionState();
			
		}
		
		else if(result.equals("0")){
			
			//set message
			message="No data to sync";
			
		}
		
		else if(result.equals("-1")){
			
			//set message
			message="Server error";
			
		}
		
		else if(result.equals("-2")){
			
			//set message
			message="Duplicating records in server";
			
		}
		
		else if(result.equals("-3")){
			
			//set message
			message="Mobile database error";
			
		}
		
		else if (result.equals("-4")){
				
			//set message
			message="Missed some data";
				
		}
		
		else if (result.equals("-5")){
			
			//set message
			message="Error in synced records";
				
		}
		
		else if (result.equals("-6")){
			
			//set message
			message="Error in sync type";
				
		}
		
		else if (result.equals("-7")){
			
			//set message
			message="Connection error";
				
		}
		
		else{
			
			message="Sync error";
			
		}
		
		//close dialog
		syncTransactionAcivity.closeConnectingDialog(syncTransactionAcivity.SYNCING_DIALOG_ID);
		
		//display toast
		Toast.makeText(syncTransactionAcivity, message, Toast.LENGTH_LONG ).show();
		
	}
	
}