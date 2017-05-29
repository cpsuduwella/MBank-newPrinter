package wasn.mbank.utils;

import wasn.mbank.pojos.Client;

/**
 * define string splitting methods to create POJOs
 * @author eranga
 */
public class RecodeTokaniser {

	/**
     * split recode and set splited values to client attributes
     * @param record
     * @return client
     */
    public Client splitRecord(String clientRecord){
    	
    	//client pbject
    	Client client=null;
    	
    	//to hold splitting attributes
		String[] temp;
		 
		//delimiter
		String delimiter = ",";
		
		try{
			
			//given string will be split by the argument delimiter provided. */
			temp = clientRecord.split(delimiter);
			
			//client id at 0
			String clientId=temp[0];
			
			//client name at 1
			String clientName=temp[1];
			
			//client nic at 2
			String nic=temp[2];
			
			//client account no at 3
			String accountNo=temp[3];
			
			//balance amount at 4
			String balanceAmount=temp[4];
			
			//birth date at 5
			String birthDate=temp[5];
			
			//previous transaction at 6
			String previoudTransaction=temp[6];
			
			//create client object
			client=new Client(clientId, clientName, nic, birthDate,accountNo, balanceAmount, previoudTransaction);
			
		}
		
		//some thing going wrong
		catch(Exception e){
		
			client=null;
			
		}
		
    	//return
    	return client;
    	
    }
    
}