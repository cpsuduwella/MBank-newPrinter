package wasn.mbank.application;

import java.util.ArrayList;

import wasn.mbank.model.MBankData;
import wasn.mbank.pojos.Client;
import wasn.mbank.pojos.DaySummary;
import wasn.mbank.pojos.Transaction;
import android.app.Application;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.preference.PreferenceManager;

/**
 * application class
 * @author eranga
 */
public class MBankApplication extends Application implements OnSharedPreferenceChangeListener{

	//database class object															
	MBankData mbankData;														
	
	//shared preference			
	SharedPreferences prefences;		
	
	//list of client			
	ArrayList<Client> clients;		
	
	//client object
	Client client;				
	
	//transaction object
	Transaction transaction;
	
	//day summary object
	DaySummary daySummary;

	/*
	 * keep track with transaction state
	 * 0 - non
	 * 1 - print
	 * 2 - reprint
	 */
	int transactionState;
	
	//keep track with print day summary
	int daySummaryState;
	
	/*
	 * day summary printing state
	 * 1 - success
	 * 0 - fail
	 */
	int daySummaryPrintState;
	
	/**
	 * call when create
	 */
	@Override
	public void onCreate() {

		super.onCreate();
		
		//get preference
		prefences=PreferenceManager.getDefaultSharedPreferences(this);
		
		//listener
		prefences.registerOnSharedPreferenceChangeListener(this);
	
		//initialize
		mbankData=new MBankData(this);
		
		//initialize
		clients=new ArrayList<Client>();
		
		//initialize
		client=null;
		
	}

	/**
	 * call when terminate application
	 */
	@Override
	public void onTerminate() {
		
		super.onTerminate();
		
		//clear sensor data
		mbankData.close();
		
	}
	
	/**
	 * get MBankData object
	 * @return mbankData 
	 */
	public MBankData getMbankData(){
		
		//return
		return mbankData;
		
	}

	/**
	 * get client list
	 * @return clients
	 */
	public ArrayList<Client> getClients() {
		
		return clients;
		
	}

	/**
	 * set client list
	 * @param clients
	 */
	public void setClients(ArrayList<Client> clients) {
		
		this.clients = clients;
		
	}

	/**
	 * get client
	 * @return client
	 */
	public Client getClient() {
		
		return client;
		
	}

	/**
	 * set client
	 * @param client
	 */
	public void setClient(Client client) {
		
		this.client = client;
		
	}

	/**
	 * get transaction
	 * @return transaction
	 */
	public Transaction getTransaction() {
		
		return transaction;
		
	}

	/**
	 * set transaction
	 * @param transaction
	 */
	public void setTransaction(Transaction transaction) {
		
		this.transaction = transaction;
		
	}
	
	/**
	 * get re print state
	 * @return
	 */
	public int getTransactionState() {
		
		return transactionState;
		
	}

	/**
	 * set re print state
	 * @param rePrint
	 */
	public void setTransactionState(int transactionState) {
		
		this.transactionState = transactionState;
		
	}

	/**
	 * get day summary
	 * @return day summary object
	 */
	public DaySummary getDaySummary() {
		
		return daySummary;
		
	}

	/**
	 * set day summary
	 * @param daySummary
	 */
	public void setDaySummary(DaySummary daySummary) {
		
		this.daySummary = daySummary;
		
	}

	/**
	 * when change preference
	 * @param sharedPreferences
	 * @param key
	 */
	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		
		
	}
			
	/**
	 * get preference
	 * @return
	 */		
	public SharedPreferences getPrefences() {
					
		return prefences;
					
	}
					
	/**
	 * set preference
	 * @param prefences
	 */
	public void setPrefences(SharedPreferences prefences) {
					
		this.prefences = prefences;
					
	}

	/**
	 * get summary print state
	 * @return
	 */
	public int getDaySummaryPrintState() {
		
		return daySummaryPrintState;
		
	}

	/**
	 * set summary print state
	 * @param daySummaryPrintState
	 */
	public void setDaySummaryPrintState(int daySummaryPrintState) {
		
		this.daySummaryPrintState = daySummaryPrintState;
		
	}																												
		
}