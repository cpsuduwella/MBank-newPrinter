package wasn.mbank.threads;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import android.os.AsyncTask;
import android.widget.Toast;
import wasn.mbank.activities.MBankActivity;
import wasn.mbank.application.MBankApplication;

/**
 * connect to mbank server and authenticate user
 * @author eranga
 */
public class Logger extends AsyncTask<String, String, String>{

	//login state
	public int loginState;
	
	//activity object, singleton
	MBankActivity mbankAcivity;
	
	//application object instance
	MBankApplication application;
	
	/**
	 * constructor
	 */
	public Logger(MBankActivity activity) {
	
		this.mbankAcivity=activity;
		
		//initialize application object
		application=(MBankApplication)mbankAcivity.getApplication();
		
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
		
		//username
		String username=params[0];
		
		//password
		String passowrd=params[1];
			
		//authenticate user
		loginState=authenticateUser(username,passowrd);
		
		//return
		return Integer.toString(loginState);				
		
	}
	
	/**
	 * authenticate user by connecting to server
	 * @return
	 */
	public int authenticateUser(String username,String password){
		
		//connect to server and get input stream
		InputStream inputStream=getStreamConntion(username,password);
		
		//response from server
		int state=0;
		
		try{
			
			//get server response
			state=processServerResponse(inputStream);
			
			//login success
			if(state>0){
				
				try{
					
					//login success set logged state to 1
					application.getMbankData().setLoginState("1");
					
					//set branch id in MBank data
					application.getMbankData().setBranchId(Integer.toString(state));
					
				}
				catch(Exception e){
					
					//internal database error
					state=-3;
					
				}
				
			}
			
			else{
				
				//login success set logged state to 0
				application.getMbankData().setLoginState("0");
				
				//set branch id to 0 MBank data
				application.getMbankData().setBranchId("0");
				
			}
			
		}
		
		//error
		catch(Exception e){
		
			//error
			state=-2;
			
		}
		
		//close stream
		finally{
			
			try{
				
				inputStream.close();
				
			}catch(Exception e){
				
			}
			
		}
		
		//return
		return state;
		
	}
	
	/**
	 * create stream connection with server
	 */
	public InputStream getStreamConntion(String username,String password){
		
		//input stream
		InputStream inputStream=null;

		//server url here localhost
		//String urlString="http://10.22.137.65:8080/MBank_Server/LoginServlet?"+
		String urlString="http://10.100.31.5:8080/MBank_Server/LoginServlet?" +
		//String urlString="http://10.0.2.2:8080/MBank_Server/LoginServlet?" +
         		   		 "username="+username+"&" +				
         		   		 "password="+password;			
		
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
            	loginState=-2;
            				
            }
            		
        //error in connection    
        }catch(Exception e){
        				
            //error in data connection
        	loginState=-2;
        				
            System.out.println("Error in network call " + e);
            		
        }				
        
        //return
        return inputStream;
		
	}
	
	/**
	 * get server response and login state/ branch id
	 * @param inputStream
	 * @throws IOException
	 */
	public int processServerResponse(InputStream inputStream) throws IOException{
		
		//to store branch id
		int serverResponse=0;
		
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

                	//get server response
                	serverResponse=Integer.parseInt(buffer.toString().trim());	
                	
                }
                
                //break continue
                break;
                
            }
            
            //append character to buffer
            buffer.append((char) readingCharacter);
        	
        }
		
        //close stream
        inputStream.close();
        
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
		
		if(result.equals("0")){
			
			//set message
			message="login fail";
			
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
			message="Mobile database error";
			
		}
		
		else{
				
			//set message
			message="Login success";
				
		}
		
		//close dialog
		mbankAcivity.closeConnectingDialog(mbankAcivity.CONNECTING_DIALOG_ID);
		
		//display toast
		Toast.makeText(mbankAcivity, message, Toast.LENGTH_LONG ).show();
		
	}
	
}
