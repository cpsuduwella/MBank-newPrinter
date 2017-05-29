package wasn.mbank.model;

import java.util.ArrayList;

import wasn.mbank.pojos.Client;
import wasn.mbank.pojos.Transaction;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * database class that handle all data base related things
 * @author eranga
 */
public class MBankData {

	//context
	Context context;
	
	//DBHelper object,DBHelper using only in this class
	DBHelper dbHelper;
	
	/**
	 * constructor
	 */
	public MBankData(Context context){
		
		//set context
		this.context=context;
		
		//create db helper
		dbHelper=new DBHelper();
		
	}
	
	/**
	 * clean all (destructor)
	 */
	public void close(){
		
		//close DB helper
		dbHelper.close();
		
	}
	
	/**
	 * get isLogged attribute from application_data table
	 * @return loginState - login state with server
	 */
	public String getLoginState(){
		
		//login sate with server
		String loginState="0";
		
		//get database
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		
		// create cursor for get app data   ( table name		     columns   		select		 	 	select args  		  groupby   having  orderby )	
		Cursor appDataCursor = db.query(DBHelper.TABLE_NAME_APPDATA , null  , "attribute_name=?", new String[]{"isLogged"},  null   , null ,  null   );
		
		//has elements in cursor
		if(appDataCursor.moveToNext()){
			
			//last record
			appDataCursor.moveToLast();
			
			//get login status
			loginState=appDataCursor.getString(2);
			
		}
		
		//close cursor
		appDataCursor.close();
		
		//close database
		db.close();
		
		//return
		return loginState;
		
	}
	
	/**
	 * update isLogged attribute in applicationData
	 * @param isLogged - determine logged or not (1 or 0)
	 */
	public void setLoginState(String isLogged){
		
		//open database
        SQLiteDatabase db=dbHelper.getWritableDatabase();	
        
        //create content values
        ContentValues appDataValues =new ContentValues();
    	
    	//application data - isLogged = 0						
    	appDataValues.put("attribute_value", isLogged);
    		
		//update application data									
		db.update(DBHelper.TABLE_NAME_APPDATA, appDataValues, "attribute_name=?",new String[]{"isLogged"});
		
		//close database
		db.close();
		
	}
	
	/**
	 * get isDownloded attribute from application_data table
	 * @return downloadStates - download state
	 */
	public String getDownloadSate(){
		
		//download state with server
		String downloadState="0";
		
		//get database
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		
		// create cursor for get app data   ( table name		     columns   		select		 	 	select args  		  groupby   having  orderby )	
		Cursor appDataCursor = db.query(DBHelper.TABLE_NAME_APPDATA , null  , "attribute_name=?", new String[]{"isDownloaded"},  null   , null ,  null   );
		
		//has elements in cursor
		if(appDataCursor.moveToNext()){
			
			//last record
			appDataCursor.moveToLast();
			
			//get download status
			downloadState=appDataCursor.getString(2);
			
		}
		
		//close cursor
		appDataCursor.close();
		
		//close database
		db.close();
		
		//return
		return downloadState;
		
	}
	
	/**			
	 * update isDownloaded attribute in applicationData			
	 * @param isDownloaded - determine data downloded or not (1 or 0)			
	 */						
	public void setDownloadState(String isDownloaded){				
		
		//open database
        SQLiteDatabase db=dbHelper.getWritableDatabase();	
        
        //create content values
        ContentValues appDataValues =new ContentValues();
    	
    	//application data - isDownloaded = 0						
    	appDataValues.put("attribute_value", isDownloaded);
    		
		//update application data									
		db.update(DBHelper.TABLE_NAME_APPDATA, appDataValues, "attribute_name=?",new String[]{"isDownloaded"});
		
		//close database
		db.close();
		
	}
	
	/**
	 * update branchId attribute in applicationData
	 * @param receipt no
	 */
	public void setBranchId(String branchId){
		
		//open database
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        
        //create content values
        ContentValues appDataValues =new ContentValues();
    			
    	appDataValues.put("attribute_value", branchId);
    		
		//update application data			
		db.update(DBHelper.TABLE_NAME_APPDATA, appDataValues, "attribute_name=?",new String[]{"branchId"});
		
		//close database
		db.close();
		
	}
	
	/**
	 * get branch id attribute from application_data table
	 * @return branch id
	 */
	public String getBranchId(){
		
		//branch id
		String branchId="0";
		
		//get database
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		
		// create cursor for get app data   ( table name		     columns   		select		 	 	select args  		  groupby   having  orderby )	
		Cursor appDataCursor = db.query(DBHelper.TABLE_NAME_APPDATA , null  , "attribute_name=?", new String[]{"branchId"},  null   , null ,  null   );
		
		//has elements in cursor
		if(appDataCursor.moveToNext()){
			
			//last record
			appDataCursor.moveToLast();
			
			//get receiptNo
			branchId=appDataCursor.getString(2);
			
		}
		
		//close cursor
		appDataCursor.close();
		
		//close database
		db.close();
		
		//return
		return branchId;
		
	}
	
	/**
	 * update receiptNo attribute in applicationData
	 * @param next receipt no
	 */
	public void setReceiptNo(String receiptNo){
		
		//open database
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        
        //create content values
        ContentValues appDataValues =new ContentValues();
    	
    	//application data - isDownloaded = 0			
    	appDataValues.put("attribute_value", receiptNo);
    		
		//update application data			
		db.update(DBHelper.TABLE_NAME_APPDATA, appDataValues, "attribute_name=?",new String[]{"receiptNo"});
		
		//close database
		db.close();
		
	}
	
	/**
	 * get receiptNo attribute from application_data table
	 * @return next receipt no
	 */
	public String getReceiptNo(){
		
		//receipt no state with server
		String receiptNo="0";
		
		//get database
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		
		// create cursor for get app data   ( table name		     columns   		select		 	 	select args  		  groupby   having  orderby )	
		Cursor appDataCursor = db.query(DBHelper.TABLE_NAME_APPDATA , null  , "attribute_name=?", new String[]{"receiptNo"},  null   , null ,  null   );
		
		//has elements in cursor
		if(appDataCursor.moveToNext()){
			
			//last record
			appDataCursor.moveToLast();
			
			//get receiptNo
			receiptNo=appDataCursor.getString(2);
			
		}
		
		//close cursor
		appDataCursor.close();
		
		//close database
		db.close();
		
		//return
		return receiptNo;
		
	}
	
	/**
	 * insert client details in to client table
	 * @param clients - list of client
	 * @return storedRecordCount - stored record count
	 */
	public int insetClientData(ArrayList<Client> clients){
		
		//stored record count
		int storedRecordCount=0;
		
		//open database
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        
        //delete all records in client table
        db.delete(DBHelper.TABLE_NAME_CLIENT, null, null);
        
        //create content values for client record
        ContentValues clientValues =new ContentValues();
        
        //iterate over client list
        for(int i=0;i<clients.size();i++){
        	
        	//fill content value
        	clientValues.put("id", clients.get(i).getId());
        	clientValues.put("name", clients.get(i).getName());
        	clientValues.put("nic", clients.get(i).getNic());
        	clientValues.put("birth_date", clients.get(i).getBirthDate());
        	clientValues.put("account_no", clients.get(i).getAccountNo());
        	clientValues.put("balance_amount", clients.get(i).getBalanceAmount());
        	clientValues.put("previous_transaction", clients.get(i).getPreviousTransaction());
        	
        	//throw an exception if insert error
        	try{

        		//insert power data into power table															
        		db.insertOrThrow(DBHelper.TABLE_NAME_CLIENT, null, clientValues);
        		
        		//increase
        		storedRecordCount++;
        		
        	//error
        	}catch(Exception e){
        		
        		System.out.println("Error in Client inserting " + e);
        		
        	}
        	
        }
        
        //close database
        db.close();
        
        //return
        return storedRecordCount;
		
	}
	
	/**
	 * get all client data from database
	 * @return clients - list of client
	 */
	public ArrayList<Client> getClientData(){
		
		//list to hold client data
		ArrayList<Client> clients=new ArrayList<Client>();
		
		//open database
        SQLiteDatabase db=dbHelper.getWritableDatabase();

		// create a cursor to get client data
		Cursor clientCursor = db.query(DBHelper.TABLE_NAME_CLIENT, null, null, null,null, null, null);
		
		//until has elements
		while(clientCursor.moveToNext()){
			
			/*
			 * get client data
			 */
			String clientId=clientCursor.getString(0);
			String clientName=clientCursor.getString(1);
			String clientNic=clientCursor.getString(2);
			String birthDate=clientCursor.getString(3);
			String accountNo=clientCursor.getString(4);
			String balanceAmount=clientCursor.getString(5);
			String previousTransaction=clientCursor.getString(6);
			
			//new client object
			Client client=new Client(clientId, clientName, clientNic, birthDate, accountNo, balanceAmount, previousTransaction);
			
			//add to list
			clients.add(client);
			
		}
		
		//close cursor
		clientCursor.close();
		
		//close db
		db.close();
		
		//return
		return clients;
		
	}
	
	/**
	 * get required client data from database by searching
	 * @param parameter - parameter to where clause
	 * @return clients - list of client
	 */
	public ArrayList<Client> searchClientBySearchParameters(String parameter){
		
		//list to hold client data
		ArrayList<Client> clients=new ArrayList<Client>();
		
		//open database
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        
        Cursor clientCursor = db.query(DBHelper.TABLE_NAME_CLIENT, null, parameter, null,null, null, null);
		
		//until has elements
		while(clientCursor.moveToNext()){
			
			/*
			 * get client data
			 */
			String clientId=clientCursor.getString(0);
			String clientName=clientCursor.getString(1);
			String clientNic=clientCursor.getString(2);
			String birthDate=clientCursor.getString(3);
			String accountNo=clientCursor.getString(4);
			String balanceAmount=clientCursor.getString(5);
			String previousTransaction=clientCursor.getString(6);
			
			//new client object
			Client client=new Client(clientId, clientName, clientNic, birthDate, accountNo, balanceAmount, previousTransaction);
			
			//add to list
			clients.add(client);
			
		}
		
		//close cursor
		clientCursor.close();
		
		//close db
		db.close();
		
		//return
		return clients;
		
	}
	
	/**
	 * get required client data from database by searching
	 * @param accountNo - parameter to where clause
	 * @return client - matching client
	 */
	public Client searchClientByAccountNo(String accountNo){
		
		//client object
		Client client=null;
		
		//open database
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        
        Cursor clientCursor = db.query(DBHelper.TABLE_NAME_CLIENT, null, "account_no=?" ,new String[]{accountNo},null, null, null);
        
        //has elements in cursor
		if(clientCursor.moveToNext()){
			
			//last record
			clientCursor.moveToFirst();
			
			/*
			 * get attributes
			 */
			String clientId=clientCursor.getString(0);
			String clientName=clientCursor.getString(1);
			String clientNic=clientCursor.getString(2);
			String clientBirthDate=clientCursor.getString(3);
			String balanceAmount=clientCursor.getString(5);
			String previousTransaction=clientCursor.getString(6);
			
			//create client object
			client=new Client(clientId, clientName, clientNic, clientBirthDate, accountNo, balanceAmount, previousTransaction);
			
		}
		
		//close cursor
		clientCursor.close();
		
		//close db
		db.close();
		
		//return
		return client;
		
	}
	
	/**
	 * update client balance of corresponding client
	 * @param accountNo
	 * @param balance
	 */
	public void updateClientBalance(String accoutnNo,String balance){
		
		//open database
        SQLiteDatabase db=dbHelper.getWritableDatabase();	
        
        //create content values
        ContentValues clientValues =new ContentValues();
    	
    	//put new balance balance amount 						
        clientValues.put("balance_amount", balance);
    		
		//update balance amount data									
		db.update(DBHelper.TABLE_NAME_CLIENT, clientValues, "account_no=?",new String[]{accoutnNo});
		
		//close database
		db.close();
		
	}
	
	/**
	 * insert transaction details in to transaction_deatail table
	 * @param Transaction 
	 * @return insetStatus
	 */
	public boolean insertTransaction(Transaction transaction){
				
		// successful or fail transaction insertion
		boolean insetStatus=true;
		
		//open database
        SQLiteDatabase db=dbHelper.getWritableDatabase();	
        
        //create content values for transaction record
        ContentValues transactionValues =new ContentValues();
                	
    	//fill content value
    	transactionValues.put("branch_id", transaction.getBranchId());
    	transactionValues.put("client_id", transaction.getClientId());
    	transactionValues.put("client_name", transaction.getClinetName());
    	transactionValues.put("client_nic", transaction.getClinetNic());
    	transactionValues.put("account_no", transaction.getClientAccountNo());
    	transactionValues.put("previous_balance", transaction.getPreviousBalance());
    	transactionValues.put("transaction_amount", transaction.getTransactionAmount());
    	transactionValues.put("current_balance", transaction.getCurrentBalance());
    	transactionValues.put("transaction_time", transaction.getTransactionTime());
    	transactionValues.put("transaction_type", transaction.getTransactionType());
    	transactionValues.put("check_no", transaction.getCheckNo());
    	transactionValues.put("description", transaction.getDescription());
    	transactionValues.put("receipt_id", transaction.getReceiptId());
    	transactionValues.put("synced_state", "0");
    	
    	//throw an exception if insert error
    	try{

    		//insert data															
    		db.insertOrThrow(DBHelper.TABLE_NAME_TRANSACTION_DETAIL, null, transactionValues);
    		
    	//error	
    	}catch(Exception e){
    		
    		//set false
    		insetStatus=false;
    		
    		System.out.println("Error in Transaction inserting " + e);
    		
    	}
        
        //close database
        db.close();
        
        //return
		return insetStatus;
        
	}
	
	/**
	 * get all transaction details from database
	 */
	public ArrayList<Transaction> getTrnasactionData(){
		
		//list to hold transaction data
		ArrayList<Transaction> transactions=new ArrayList<Transaction>();
		
		//open database
        SQLiteDatabase db=dbHelper.getWritableDatabase();

		// create a cursor to get transaction data
		Cursor transactionCursor = db.query(DBHelper.TABLE_NAME_TRANSACTION_DETAIL, null, null, null,null, null, null);
		
		//until has elements
		while(transactionCursor.moveToNext()){
			
			/*
			 * get transaction data
			 */
			String branchId=transactionCursor.getString(1);
			String clientId=transactionCursor.getString(2);
			String clientName=transactionCursor.getString(3);
			String clientNic=transactionCursor.getString(4);
			String accoutNo=transactionCursor.getString(5);
			String previousBalance=transactionCursor.getString(6);
			String transactionAmount=transactionCursor.getString(7);
			String currentBalance=transactionCursor.getString(8);
			String transactionTime=transactionCursor.getString(9);
			String transactionType=transactionCursor.getString(10);
			String checkNo=transactionCursor.getString(11);
			String description=transactionCursor.getString(12);
			String receiptId=transactionCursor.getString(13);
			
			//new transaction object
			Transaction transaction=new Transaction(branchId,
													clientName,
													clientNic,
													accoutNo,
													previousBalance, 
													transactionAmount, 
													currentBalance,
													transactionTime,
													receiptId,
													clientId,
													transactionType, 
													checkNo,
													description);
			
			//add to list
			transactions.add(transaction);
			
		}
		
		//close cursor
		transactionCursor.close();
		
		//close db
		db.close();
		
		//return
		return transactions;
		
	}
	
	/**
	 * get unsynced transaction details from database
	 */
	public ArrayList<Transaction> getUnsyncedTrnasactionData(){
		
		//list to hold transaction data
		ArrayList<Transaction> transactions=new ArrayList<Transaction>();
		
		//open database
        SQLiteDatabase db=dbHelper.getWritableDatabase();

		// create a cursor to get transaction data
		Cursor transactionCursor = db.query(DBHelper.TABLE_NAME_TRANSACTION_DETAIL, null, "synced_state=?" ,new String[]{"0"},null, null, null);
		
		//until has elements
		while(transactionCursor.moveToNext()){
			
			/*
			 * get transaction data
			 */
			String branchId=transactionCursor.getString(1);
			String clientId=transactionCursor.getString(2);
			String clientName=transactionCursor.getString(3);
			String clientNic=transactionCursor.getString(4);
			String accoutNo=transactionCursor.getString(5);
			String previousBalance=transactionCursor.getString(6);
			String transactionAmount=transactionCursor.getString(7);
			String currentBalance=transactionCursor.getString(8);
			String transactionTime=transactionCursor.getString(9);
			String transactionType=transactionCursor.getString(10);
			String checkNo=transactionCursor.getString(11);
			String description=transactionCursor.getString(12);
			String receiptId=transactionCursor.getString(13);
			
			//new transaction object
			Transaction transaction=new Transaction(branchId,
													clientName,
													clientNic,
													accoutNo,
													previousBalance, 
													transactionAmount, 
													currentBalance,
													transactionTime,
													receiptId,
													clientId,
													transactionType, 
													checkNo,
													description);
			
			//add to list
			transactions.add(transaction);
			
		}
		
		//close cursor
		transactionCursor.close();
		
		//close db
		db.close();
		
		//return
		return transactions;
		
	}
	
	/**
	 * update unsynced transaction state  
	 */
	public void updateTransactionState(){
		
		//open database
        SQLiteDatabase db=dbHelper.getWritableDatabase();	
        
        //create content values
        ContentValues updateValues =new ContentValues();
    	
    	//set synced state  						
        updateValues.put("synced_state", "1");
    		
		//update balance amount data									
		db.update(DBHelper.TABLE_NAME_TRANSACTION_DETAIL, updateValues, "synced_state=?",new String[]{"0"});
		
		//close database
		db.close();
		
	}
	
	/**
	 * get required transaction from database by searching
	 * @param time - transaction time
	 * @return transaction - matching transaction
	 */
	public Transaction searchTransactionByTimr(String time){
		
		//transaction object
		Transaction transaction=null;
		
		//open database
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        
        Cursor transactionCursor = db.query(DBHelper.TABLE_NAME_TRANSACTION_DETAIL, null, "transaction_time=?" ,new String[]{time},null, null, null);
        
        //has elements in cursor
		if(transactionCursor.moveToNext()){
			
			//last record
			transactionCursor.moveToFirst();
			
			/*
			 * get attributes
			 */
			/*
			 * get transaction data
			 */
			String branchId=transactionCursor.getString(1);
			String clientId=transactionCursor.getString(2);
			String clientName=transactionCursor.getString(3);
			String clientNic=transactionCursor.getString(4);
			String accoutNo=transactionCursor.getString(5);
			String previousBalance=transactionCursor.getString(6);
			String transactionAmount=transactionCursor.getString(7);
			String currentBalance=transactionCursor.getString(8);
			String transactionTime=transactionCursor.getString(9);
			String transactionType=transactionCursor.getString(10);
			String checkNo=transactionCursor.getString(11);
			String description=transactionCursor.getString(12);
			String receiptId=transactionCursor.getString(13);
			
			//new transaction object
			transaction=new Transaction(branchId,
										clientName,
										clientNic,
										accoutNo,
										previousBalance, 
										transactionAmount, 
										currentBalance,
										transactionTime,
										receiptId,
										clientId,
										transactionType,
										checkNo,
										description);
			
		}
		
		//close cursor
		transactionCursor.close();
		
		//close db
		db.close();
		
		//return
		return transaction;
		
	}
	
	/**
	 * delete all transaction details from database
	 */
	public void deleteTransactionData(){
		
		//open database
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        
        try{
        
            //delete
        	db.delete(DBHelper.TABLE_NAME_TRANSACTION_DETAIL, null, null);
        	
        }catch(Exception e){
        	
        }
		
	}
	
	/**
	 * inner class class for SQLite database operations (helper class for create,open and upgrade database) 
	 */
	private class DBHelper extends SQLiteOpenHelper {

		// constants
		public static final String DB_NAME = "mbankdb";
		public static final String TABLE_NAME_CLIENT = "client";
		public static final String TABLE_NAME_TRANSACTION_DETAIL = "transaction_detail";
		public static final String TABLE_NAME_APPDATA = "application_data";
		
		//DB version
		public static final int DB_VERSION = 30;

		/**
		 * constructor
		 * @param context - context
		 */				
		public DBHelper() {
							
			super(context, DB_NAME, null, DB_VERSION);

		}

		/**
		 * call once n only once
		 */
		@Override
		public void onCreate(SQLiteDatabase db) {

			// create table for client
			//String sqlClient = "create table client (id INTEGER PRIMARY KEY AUTOINCREMENT, location TEXT, type TEXT, state int, time TEXT, voltage TEXT, current TEXT, line_voltage TEXT )";
			String sqlClient = "create table client (id TEXT, " +
													 "name TEXT, " +
													 "nic TEXT, " +
													 "birth_date TEXT, " +
													 "account_no TEXT PRIMARY KEY, " +
													 "balance_amount TEXT, " +
													 "previous_transaction TEXT)";
			
			// create table for transaction
			String sqlTransaction="create table transaction_detail (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
															 "branch_id TEXT, " +
															 "client_id TEXT, " +
															 "client_name TEXT, " +
															 "client_nic TEXT, " +
															 "account_no TEXT, " +
															 "previous_balance TEXT, " +
															 "transaction_amount TEXT, " +
															 "current_balance TEXT, " +
															 "transaction_time TEXT, " +
															 "transaction_type TEXT, " +
															 "check_no TEXT, " +
															 "description TEXT, " +
															 "receipt_id TEXT UNIQUE, " +
															 "synced_state TEXT)";
			
			//create table for store application attributes
			String sqlApplicationData="create table application_data (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
																	  "attribute_name TEXT, " +
																	  "attribute_value TEXT )";
	    	
			//execute sql
			db.execSQL(sqlClient);
			db.execSQL(sqlTransaction);
			db.execSQL(sqlApplicationData);
			
			/*
			 * add initial application data to DB
			 */
			//content values
	        ContentValues loginValues =new ContentValues();
								
	        //application data login states				
	        loginValues.put("attribute_name", "isLogged");
	        loginValues.put("attribute_value", "0");
	    	
	    	//content values
	    	ContentValues downlodValues =new ContentValues();
			
	        //application data login states
	    	downlodValues.put("attribute_name", "isDownloaded");
	    	downlodValues.put("attribute_value", "0");
	    	
	    	//content values
	    	ContentValues branchValues=new ContentValues();
	    	
	    	//application data receipt no
	    	branchValues.put("attribute_name", "branchId");
	    	branchValues.put("attribute_value", "0");
	    	
	    	//content values
	    	ContentValues receiptValues=new ContentValues();
	    	
	    	//application data receipt no
	    	receiptValues.put("attribute_name", "receiptNo");
	    	receiptValues.put("attribute_value", "1");
	   
	    	//insert application data to application_data table
	    	try {
	    	
	    		//insert fail throw exception
	        	db.insertOrThrow(DBHelper.TABLE_NAME_APPDATA, null, loginValues);
	        	db.insertOrThrow(DBHelper.TABLE_NAME_APPDATA, null, downlodValues);
	        	db.insertOrThrow(DBHelper.TABLE_NAME_APPDATA, null, branchValues);
	        	db.insertOrThrow(DBHelper.TABLE_NAME_APPDATA, null, receiptValues);
	            
	        } catch (Exception e) {
	        	
	            //catch code
	        	System.out.println("Error " + e);
	        	
	        }
	        
	        System.out.println("oncreate");
	        
		}

		/**
		 * call when upgrade the database, normally changing the DB_VERSION
		 */
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

			//drop tables
			db.execSQL("drop table if exists "+ TABLE_NAME_CLIENT);
			db.execSQL("drop table if exists "+ TABLE_NAME_TRANSACTION_DETAIL);
			db.execSQL("drop table if exists "+ TABLE_NAME_APPDATA);				
			
			//create databases again
			this.onCreate(db);
			
		}

	}
	
}
