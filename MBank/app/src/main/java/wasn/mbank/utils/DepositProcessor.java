package wasn.mbank.utils;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import wasn.mbank.activities.ClientDetailsActivity;
import wasn.mbank.activities.MBankActivity;
import wasn.mbank.application.MBankApplication;
import wasn.mbank.pojos.Client;
import wasn.mbank.pojos.Transaction;
import android.app.Application;

/**
 * process deposit transactions
 * @author eranga
 */
public class DepositProcessor {

	//application object
	MBankApplication application;
	
	/**
	 * constructor
	 * @param application - application object
	 */
	public DepositProcessor(MBankApplication application){
		
		this.application=application;
		
	}
	
	/**
	 * process transaction according to passing parameters and generate transaction object
	 * @param transactionAmount
	 * @param client
	 * @return isProcessed
	 */
	public boolean processDeposit(String transactionAmount,Client client){
				
		//Transaction																																											
		Transaction transaction=null;										
						
		//transaction validity		
		boolean isProcessed=true;		
						
		try{
		
			//get branch id from data base
			String branchId=application.getMbankData().getBranchId();
			
			System.out.println(branchId);
			
			/*
			 * client attributes
			 */
			String clientName=client.getName();
			String clientNic=client.getNic();
			String accountNo=client.getAccountNo();
			String previousBalance=client.getBalanceAmount();

			//format transaction amount
			String formatedTransactionAmount=transactionAmount+".00";							
			
			/*
			 * format balance amount
			 */
			//balance after deposit
			double balance=(Double.parseDouble(client.getBalanceAmount()))+(Double.parseDouble(transactionAmount));
			
			// decimal format #.## format
            DecimalFormat decimalFormat = new DecimalFormat("0.00");
            
            //format
			String formatedbalanceAmount=decimalFormat.format(balance);
			
			/*
			 * generate transaction time
			 */
			//date format
			String DATE_FORMAT_NOW = "yyyy/MM/dd HH:mm:ss";
			
			//calendar
			Calendar calendar = Calendar.getInstance();
			
			//simple date format
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT_NOW);
			
			//generate transaction time
			String transactionTime=simpleDateFormat.format(calendar.getTime());
			
			/*
			 * generate receipt id
			 */		
			//next receipt no from database
			String receiptNo=application.getMbankData().getReceiptNo();
			
			System.out.println(receiptNo);
			
			//receipt id
			String receiptId="";
			
			//length = 1
			if(branchId.length()==1){
			
				receiptId="00"+branchId + receiptNo;
				
			}
			
			//lenght > 1
			else{
				
				receiptId="000"+branchId+receiptNo;
				
			}
			
			System.out.println(receiptId);
			
			String clientId=client.getId();
			String transactionType="Deposit";
			String checkNo="";
			String description="";
			
			//generate new transaction
			transaction=new Transaction(branchId,
										clientName,
										clientNic,
										accountNo,
										previousBalance,
										formatedTransactionAmount, 
										formatedbalanceAmount,
										transactionTime,
										receiptId,
										clientId,
										transactionType,
										checkNo,
										description);
			
		}
		
		//parsing error
		catch(Exception e){
		
			//set to null
			transaction=null;
			
			//set to false
			isProcessed=false;
			
			System.out.println(e);
			
		}
		
		//set transaction in application object
		application.setTransaction(transaction);
		
		//return
		return isProcessed;
		
	}
	
}