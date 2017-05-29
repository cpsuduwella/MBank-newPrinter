package wasn.mbank.threads;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import wasn.mbank.activities.MBankActivity;
import wasn.mbank.application.MBankApplication;
import wasn.mbank.model.MBankData;
import wasn.mbank.pojos.Client;
import wasn.mbank.utils.RecodeTokaniser;
import android.os.AsyncTask;
import android.widget.Toast;

/**
 * Connect to MBank_Server and download client data
 * @author eranga
 */
public class Downloader extends AsyncTask<String, String, String>{

	//handshake code
	public int handShake;
	
	//data downloading state
	public int downloadingState;
	
	//activity object, singleton
	MBankActivity mbankAcivity;
	
	//application object instance
	MBankApplication application;
	
	//database class object
	MBankData sensorData;
	
	/**
	 * constructor
	 */
	public Downloader(MBankActivity activity) {
	
		this.mbankAcivity=activity;
		
		//initialize application object
		application=(MBankApplication)mbankAcivity.getApplication();
		
		//initialize mbank database object
		application.getMbankData();
		
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
		
		String branchId=params[0];
		
		//dowaload data from server
		int result=downloadDataFromServer(branchId);
		
		//return 
		return Integer.toString(result);
		
	}
	
	public int downloadDataFromServer(String branchId){
		
		//list of clients
		ArrayList<Client> clients=new ArrayList<Client>();
		
		//connect to server and get input stream
		InputStream inputStream=getStreamConntion(branchId);
		
		try{
		
			//get client list from server
			clients=processServerResponse(inputStream);
			
			//no downloading data
			if(handShake==0){
				
				downloadingState=0;
				
			}
			
			//server error
			else if(handShake==-1){
				
				downloadingState=-1;
				
			}
			
			//data available
			else{
				
				//data lost while transmission
				if(handShake!=clients.size()){
					
					downloadingState=-3;
					
				}
				
				//no error
				else{
					
					downloadingState=1;
					
				}
				
				//store downloaded client data in database
				int storedRecordCount=application.getMbankData().insetClientData(clients);
				
				//database error
				if(clients.size()!=storedRecordCount){
					
					downloadingState=-4;
					
				}
				
				//stored data in database
				if(storedRecordCount>0){
					
					//data downloded,set isDownloaded state to 1
					application.getMbankData().setDownloadState("1");
				
				}
				
			}
			
		}catch(Exception e){
			
			//error in network call
			downloadingState=-2;
			
		}
		
		//return
		return downloadingState;
		
	}
	
	/**
	 * create stream connection with server
	 */
	public InputStream getStreamConntion(String branchId){
		
		//input stream
		InputStream inputStream=null;

		//server url here localhost
		//String urlString="http://10.22.137.65:8080/MBank_Server/SinkServlet?branchId="+branchId;
		String urlString="http://10.100.31.5:8080/MBank_Server/SinkServlet?branchId="+branchId;
		
        try {
        	
        	//create url object
        	URL url= new URL(urlString);																		
        	
        	//open connection
        	URLConnection urlConnection=url.openConnection();					
        	
        	//cast
        	HttpURLConnection httpUrlConnection=(HttpURLConnection)urlConnection;					
        				
        	//set requesting method
        	httpUrlConnection.setRequestMethod("GET");
        					
        	//set attributes
        	httpUrlConnection.setDoInput(true);				
        	httpUrlConnection.setDoOutput(true);					
        				
        	//connect
        	httpUrlConnection.connect();
        				
        	//get response code
            int responseCode = httpUrlConnection.getResponseCode();
            				
            //connection is OK
            if (responseCode == HttpURLConnection.HTTP_OK) {
           
            	//get input stream from connection
            	inputStream = httpUrlConnection.getInputStream();
            					
            }
            			
            else{
            
            	//error in network connection
            	downloadingState=-2;
            				
            }
            		
        //error in connection    
        }catch(Exception e){
        				
            //error in data connection
        	downloadingState=-2;
        				
            System.out.println("Error in network call " + e);
            		
        }				
        
        //return
        return inputStream;
		
	}
	
	/**
	 * get server response and generate client list
	 * @param inputStream
	 * @throws IOException
	 */
	public ArrayList<Client> processServerResponse(InputStream inputStream) throws IOException{
		
		//to store client details
		ArrayList<Client> clients=new ArrayList<Client>();
		
		//to store reading data
        StringBuffer buffer=new StringBuffer();                

        //reading character
        int readingCharacter;

        //keep track with receiving record count
        int i=0;

        //read character by character
        while ((readingCharacter = inputStream.read()) != -1){
        	
        	//if new line character found
            if(readingCharacter=='\n'){

            	//increase
                i++;

                //initial line contains record count,or error code
                if(i==1){

                	//get hand shake value
                	handShake=Integer.parseInt(buffer.toString().trim());	
                	
                }
                
                //rest of the lines
                else{

                    //split buffer content and create client object
                    Client client=new RecodeTokaniser().splitRecord(buffer.toString().trim());

                    //no error in client record
                    if(client!=null){
                    	
                    	//add client object to vector
                    	clients.add(client);
                    	
                    }
                    
                }

                //new buffer
                buffer=new StringBuffer();
                
                //loop continue
                continue;
                
            }
            
            //append character to buffer
            buffer.append((char) readingCharacter);
        	
        }
		
        //return
        return clients;
        
	}
	
	/**
	 * call when doInBackground is finish
	 */
	@Override
	protected void onPostExecute(String result){
					
		super.onPostExecute(result);
		
		//message to display
		String message;
		
		if(result.equals("0")){
			
			//set message
			message="No data to download";
			
		}
		
		else if(result.equals("-1")){
			
			//set message
			message="Server error";
			
		}
		
		else if(result.equals("-2")){
			
			//set message
			message="Connection error";
			
		}
		
		else if(result.equals("-3")){
			
			//set message
			message="Data lost while downloading";
			
		}
		
		else if(result.equals("-4")){
			
			//set message
			message="Database inset error";
			
		}
		
		else{
				
			//set message
			message="Data downloaded";
				
		}
		
		//close dialog
		mbankAcivity.closeConnectingDialog(mbankAcivity.DOWNLOADING_DIALOG_ID);
		
		//display toast
		Toast.makeText(mbankAcivity, message, Toast.LENGTH_LONG ).show();
		
	}

}