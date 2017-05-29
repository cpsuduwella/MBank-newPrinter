package wasn.mbank.threads;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import wasn.mbank.activities.R;
import wasn.mbank.activities.TransactionDetailsActivity;
import wasn.mbank.application.MBankApplication;
import wasn.mbank.pojos.Transaction;
import woyou.aidlservice.jiuiv5.ICallback;
import woyou.aidlservice.jiuiv5.IWoyouService;

import static java.lang.Thread.sleep;

/**
 * connect to printer via bluetooth and print receipts
 * @author eranga
 */
public class PrinterConnector extends AsyncTask<String, String, String>{

	//transaction details activity
	TransactionDetailsActivity transactionDetailsActivity;
	
	//application object
	MBankApplication application;

	private static final String TAG = "PrinterTestDemo";

	private IWoyouService woyouService;

	private ICallback callback = null;

	private ServiceConnection connService = new ServiceConnection() {

		@Override
		public void onServiceDisconnected(ComponentName name) {
			woyouService = null;
		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			woyouService = IWoyouService.Stub.asInterface(service);
//            setButtonEnable(true);
		}
	};

	private final int MSG_TEST = 1;
	private long printCount = 0;

	@SuppressLint("HandlerLeak")
	Handler handler=new Handler(){
		@Override
		public void handleMessage(Message msg){
			if(msg.what == MSG_TEST){
				long mm = MemInfo.getmem_UNUSED(application.getBaseContext());
				if( mm < 100){
					handler.sendEmptyMessageDelayed(MSG_TEST, 20000);
				}else{
					handler.sendEmptyMessageDelayed(MSG_TEST, 800);
				}
				Log.i(TAG,"testAll: " + printCount + " Memory: " + mm);
			}
		}
	};

	private void test(){
		ThreadPoolManager.getInstance().executeTask(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					woyouService.printerSelfChecking(null);
					woyouService.printText(" printed: " + printCount + " bills.\n\n\n\n", null);
					printCount++;
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}});
	}

	/**
					 * constructor
	 */
	public PrinterConnector(TransactionDetailsActivity activity) {
	
		this.transactionDetailsActivity=activity;
		
		application=(MBankApplication)transactionDetailsActivity.getApplication();

		new BitmapUtils(this.application);

//		info = (TextView) findViewById(R.id.info);

		callback = new ICallback.Stub() {

			@Override
			public void onRunResult(final boolean success) throws RemoteException {
			}

			@Override
			public void onReturnString(final String value) throws RemoteException {
				Log.i(TAG,"printlength:" + value + "\n");
			}

			@Override
			public void onRaiseException(int code, final String msg) throws RemoteException {
				Log.i(TAG,"onRaiseException: " + msg);
//				runOnUiThread(new Runnable(){
//					@Override
//					public void run() {
//						info.append("onRaiseException = " + msg + "\n");
//					}});

			}
		};

		Intent intent=new Intent();
		intent.setPackage("woyou.aidlservice.jiuiv5");
		intent.setAction("woyou.aidlservice.jiuiv5.IWoyouService");
	    this.application.startService(intent);
		this.application.bindService(intent, connService, Context.BIND_AUTO_CREATE);
	}
	
	/**
	 * run in background
	 */
	@Override
	protected String doInBackground(String... params) {
 
		//print state
		int state=0;
		
		//sleep for while
		try{
			
			sleep(1000);

			state=configureAndPrint();
			
		}catch(Exception e){
			
		}
		
		//return
		return Integer.toString(state);
		
	}
	
	/**
	 * connect to printer and print receipt
	 * @return print state
	 */
	public int configureAndPrint(){
		
		//printing state
		int printState=0;

		//real transaction
		if(application.getTransactionState()==1){
			//print receipt
			receiptType = "C";
			printState = reprintReceipt();

			//empty for loop for accurate use
			for(long i=0;i<20000;i++){
			}
			try {
				Thread.sleep(2500);
			}
			catch(Exception e){}
			receiptType = "A";
			printState = reprintReceipt();
		}
//				//reprint transaction
		else if(application.getTransactionState()==2){
			//reprint receipt
			receiptType = "R";
			printState = reprintReceipt();
		}

		//return 
		return printState;
		
	}

	String receiptType="";

	public int reprintReceipt(){

		//printing state
		int printState=1;

		ThreadPoolManager.getInstance().executeTask(new Runnable(){

			@Override
			public void run() {
				Bitmap mBitmap;
				mBitmap = BitmapFactory.decodeResource(application.getResources(), R.raw.logo_english);
				mBitmap = BitmapUtils.zoomBitmap(mBitmap, 197, 163);
				try {

					//////////
					//get current transaction
					Transaction transaction=application.getTransaction();

					//printing receipt
					String rType="";

					//customer copy
					if(receiptType.equals("C")){
						rType = "(Customer copy)\n";
					}

					//agent copy
					else if(receiptType.equals("A")){
						rType = "(Agent copy)\n";
					}

					//reprint copy
					else if(receiptType.equals("R")){
						rType = "(Reprint Agent/Customer copy)\n";
					}

					String name   = transaction.getClinetName();
					String nic    = transaction.getClinetNic();
					String accNo  = transaction.getClientAccountNo();
					String type   = transaction.getTransactionType();
					String time   = transaction.getTransactionTime();
					String amount = transaction.getTransactionAmount();
					String balance= transaction.getCurrentBalance();
					String recNo  = transaction.getReceiptId();

					//alignment 0--align left , 1--align center, 2--align right
					woyouService.setAlignment(1, callback);
					woyouService.printBitmap(mBitmap, callback);

					woyouService.lineWrap(1, callback);
					woyouService.setAlignment(1, callback);
					woyouService.printTextWithFont("SANASA Development Bank\n", "", 28, callback);
					woyouService.printTextWithFont("Colombo - 02\n", "", 24, callback);
					woyouService.printTextWithFont("Tel: 011 2393759\n", "", 24, callback);
					woyouService.printTextWithFont(rType, "", 24, callback);
					woyouService.printTextWithFont("(Receipt No : " + recNo + ")\n", "", 20, callback);

					woyouService.lineWrap(1, callback);
//					woyouService.setFontSize(24, callback);
					woyouService.setAlignment(0, callback);

					woyouService.printTextWithFont("Name   : " + name + "\n", "", 24, callback);
					woyouService.printTextWithFont("NIC No : " + nic + "\n", "", 24, callback);
					woyouService.printTextWithFont("Acc No : " + accNo + "\n\n", "", 24, callback);
					woyouService.printTextWithFont("Transaction:\n", "", 26, callback);
					woyouService.printTextWithFont(" Type  : " + type + "\n", "", 24, callback);
					woyouService.printTextWithFont(" Time  : " + time + "\n", "", 24, callback);
					woyouService.printTextWithFont(" Amount: " + amount + "\n", "", 24, callback);
					woyouService.printTextWithFont("Balance: " + balance + "\n\n\n", "", 24, callback);
					;
					woyouService.printTextWithFont("..............    ..............\n", "", 24, callback);
					woyouService.printTextWithFont("   Customer           Agent\n", "", 24, callback);

					String[] s = woyouService.getServiceVersion().split("\\.");

					Log.i("PrinterTestDemo","version = " + woyouService.getServiceVersion()+ "\nlength="+s.length);

					woyouService.lineWrap(4, callback);
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}});

		try {
			Thread.sleep(500);
		}
		catch(Exception e){}

		return printState;
	}

	/**
	 * call when doInBackground is finish
	 */
	@Override
	protected void onPostExecute(String result){
					
		super.onPostExecute(result);
		
		//message to display
		String message="";
		
		if(result.equals("1")){
			//transaction
			if(application.getTransactionState()==1){
				//set message
				message="transaction success";
			}
			//re printing transaction
			else if(application.getTransactionState()==2){
				//set message									
				message="printing success";
			}
		}								
		
		//print fail
		else if(result.equals("0")){
			//set message				
			message="printing fail";
		}
		
		//bluetooth not available
		else if(result.equals("-1")){
			//set message				
			message="bluetooth not available";
		}
		
		//bluetooth not enable
		else if(result.equals("-2")){
			//set message				
			message="bluetooth not enabled";
		}
		
		//cannot connecto to printer
		else if(result.equals("-3")){
			//set message				
			message="cannot connect to printer";
		}
		
		//other
		else{
			message = "print fail";
		}
		
		//display toast
		Toast.makeText(transactionDetailsActivity, message, Toast.LENGTH_LONG ).show();
		
		//close dialog
		transactionDetailsActivity.closePrintingDialog(transactionDetailsActivity.PRINTING_DIALOG_ID);
		
	}

}