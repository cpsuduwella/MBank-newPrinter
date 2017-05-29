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

import wasn.mbank.activities.DaySummaryActivity;
import wasn.mbank.activities.R;
import wasn.mbank.application.MBankApplication;
import wasn.mbank.pojos.DaySummary;
import woyou.aidlservice.jiuiv5.ICallback;
import woyou.aidlservice.jiuiv5.IWoyouService;

import static java.lang.Thread.sleep;

/**
 * connect to printer and print day summary
 * @author eranga
 */
public class SummaryPrinter extends AsyncTask<String, String, String>{

	//daysummary activity
	DaySummaryActivity daySummaryActivity;
	
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
	public SummaryPrinter(DaySummaryActivity activity) {
	
		this.daySummaryActivity=activity;
		
		application=(MBankApplication)daySummaryActivity.getApplication();

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
//				application.this.runOnUiThread(new Runnable(){
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

		//print summary slip
		printDaySummary();

		//set print state 1 - successful
		printState=1;
		//return 
		return printState;
		
	}

	/**
	 * print day summary
	 * @return print state
	 */
	public int printDaySummary(){
		
		//printing state
		int printState=1;

		ThreadPoolManager.getInstance().executeTask(new Runnable(){

			@Override
			public void run() {
			Bitmap mBitmap;
			mBitmap = BitmapFactory.decodeResource(application.getResources(), R.raw.logo_english);
			mBitmap = BitmapUtils.zoomBitmap(mBitmap, 197, 163);
			try {

				//get day summary object
				DaySummary daySummary=application.getDaySummary();

				//printing receipt
				String rType="(Day summary)";

				String depositCount   ="Deposit count   : "+daySummary.getDepositCount()+"\n";
				String depositAmount  ="Deposit amount  : "+daySummary.getDepositAmount()+"\n";
				String withdrawCount  ="Withdraw count  : "+daySummary.getWithdrawCount()+"\n";
				String withdrawAmount ="Withdraw amount : "+daySummary.getWithdrawAmount()+"\n\n\n";

				//alignment 0--align left , 1--align center, 2--align right
				woyouService.setAlignment(1, callback);
				woyouService.printBitmap(mBitmap, callback);

				woyouService.lineWrap(1, callback);
				woyouService.setAlignment(1, callback);
				woyouService.printTextWithFont("SANASA Development Bank\n", "", 28, callback);
				woyouService.printTextWithFont("Colombo - 02\n", "", 24, callback);
				woyouService.printTextWithFont("Tel: 011 2393759\n", "", 24, callback);
				woyouService.printTextWithFont(rType, "", 24, callback);

				woyouService.lineWrap(1, callback);
				woyouService.setAlignment(0, callback);

				woyouService.printTextWithFont(depositCount, "", 24, callback);
				woyouService.printTextWithFont(depositAmount, "", 24, callback);
				woyouService.printTextWithFont(withdrawCount, "", 24, callback);
				woyouService.printTextWithFont(withdrawAmount, "", 26, callback);
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
		
			//set summary printing state
			application.setDaySummaryPrintState(1);
			
			//set message									
			message="printing success";			
			
		}								
		
		//print fail
		else if(result.equals("0")){																													
						
			//set summary printing state
			application.setDaySummaryPrintState(0);
			
			//set message				
			message="printing fail";			
							
		}						
		
		//bluetooth not available
		else if(result.equals("-1")){									
			
			//set summary printing state
			application.setDaySummaryPrintState(0);
			
			//set message				
			message="bluetooth not available";			
							
		}
		
		//bluetooth not enable
		else if(result.equals("-2")){									
			
			//set summary printing state
			application.setDaySummaryPrintState(0);
			
			//set message				
			message="bluetooth not enable";			
							
		}
		
		//cannot connecto to printer
		else if(result.equals("-3")){									
			
			//set summary printing state
			application.setDaySummaryPrintState(0);
			
			//set message				
			message="cannot connect to printer";			
							
		}
		
		//other
		else{
			
			//set summary printing state
			application.setDaySummaryPrintState(0);
			
			//set message
			message = "print fail";
			
		}
		
		//display toast
		Toast.makeText(daySummaryActivity, message, Toast.LENGTH_LONG ).show();
		
		//close dialog
		daySummaryActivity.closeConnectingDialog(daySummaryActivity.PRINTING_DIALOG_ID);
		
	}
	
}
