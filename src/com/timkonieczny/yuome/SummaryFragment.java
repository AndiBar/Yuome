package com.timkonieczny.yuome;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.client.ClientProtocolException;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class SummaryFragment extends Fragment implements OnItemClickListener {

	// TODO: PHP-Anbindung...
	protected String notificationString;
	private Thread notificationThread;
	private int width, height;
	public String balance;
	private float debit;
	private float credit;
	ArrayList<HashMap<String, String>> debts_list;
	ArrayList<HashMap<String, String>> credits_list;
	private boolean notLoggedIn = false;
	
	private List<SummaryRowItem> rowItems;

	public SummaryFragment() {
		// Empty constructor required for fragment subclasses
		debit = 0f;
    	credit = 0f;
    	debts_list = new ArrayList<HashMap<String, String>>();
    	credits_list = new ArrayList<HashMap<String, String>>();
    	
    	Thread debt_thread = new DebtThread();
        debt_thread.start();
        
        try {
        	long waitMillis = 10000;
        	while (debt_thread.isAlive()) {
        	   debt_thread.join(waitMillis);
        	}
        } catch (InterruptedException e) {
        }
        Thread credit_thread = new CreditThread();
        credit_thread.start();
        
        try {
        	long waitMillis = 10000;
        	while (credit_thread.isAlive()) {
        	   credit_thread.join(waitMillis);
        	}
        } catch (InterruptedException e) {
        }
	}

	@SuppressLint("NewApi")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		
		
		View rootView = inflater.inflate(R.layout.fragment_summary, container, false);
        
        for(HashMap debt : debts_list){
        	debit = (float) (Math.round((debit + Double.parseDouble(debt.get("balance").toString())) * 100) / 100.);
        }
        Log.d("debit", debit+"");
        
        for(HashMap credt : credits_list){
        	credit = (float) (Math.round((credit + Double.parseDouble(credt.get("balance").toString())) * 100) / 100.);
        }
        Log.d("credit", credit+"");
   
		notificationThread= new Thread(	//TODO: Serverprobleme??
				new Runnable(){
					public void run(){
						try {				//TODO: Wirft Exception
								notificationString=PHPConnector.getNotifications();
								System.out.println("notificationString: "+notificationString);
								
//						        String[] receiptData = notificationString.split(":");
//						        for(String i: receiptData){
//						        	System.out.println("receiptData: "+i);
//						        }
//								
//								ReceiptsFragment.receiptsList = new ArrayList<Receipt>();
////								ReceiptsFragment.receiptsList.add(new Receipt("01.01.2014","Aldi","Erik, Andi","15,63"));
//								
//								for(int i=0;i<receiptData.length;i+=6){
//									ReceiptsFragment.receiptsList.add(new Receipt(receiptData[i], receiptData[i+4],receiptData[i+3],receiptData[i+2],receiptData[i+1]));
//								}
								
							} catch (ClientProtocolException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								notificationString="failed";
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								notificationString="failed";
							}
						}
					}
				);
			notificationThread.start();
			try {
				notificationThread.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			int debugtype = 0;	//Testdaten
			String debugdate = "2014-08-18";
			float debugtotal = 12.95f;
			String debugstore = "REWE";
			String debugowner = "Andreas Helms";
			
			String[][] notifications = new String[][]{
					new String[]{String.valueOf(debugtype),debugowner,debugdate,debugstore,String.valueOf(debugtotal)},	//Testdaten
					new String[]{"1","Erik Harbeck"},
					new String[]{"2","Andreas Helms","5,63","0,00"}
			};
//			String[] notification = new String[]{String.valueOf(debugtype),debugowner,debugdate,debugstore,String.valueOf(debugtotal)};
			
			ListView lv = (ListView) rootView.findViewById(R.id.myList);
	        rowItems = new ArrayList<SummaryRowItem>();

	        //Populate the List
	        SummaryRowItem item=new SummaryRowItem("placeholder", "placeholder");
//	        item.setDebitDrawable(new BitmapDrawable(this.getResources(),leftBitmap));
//	        item.setCreditDrawable(new BitmapDrawable(this.getResources(),rightBitmap));
	        item.setCredit(credit);
	        item.setDebit(debit);
		        if(credit>debit){
		        item.setCreditDrawableHeight(149);
		        item.setDebitDrawableHeight(Math.round(149/credit*debit));
		        Log.d("dp_height",""+Math.round(149/credit*debit));
	        }else if(debit>credit){
	        	item.setDebitDrawableHeight(149);
	        	item.setCreditDrawableHeight(Math.round(149/debit*credit));
	        	Log.d("dp_height",""+Math.round(149/debit*credit));
	        }else{
	        	item.setDebitDrawableHeight(149);
	        	item.setCreditDrawableHeight(149);
	        }
	        Double value = Math.round((credit-debit) * 100) / 100.;
	        
	        item.setTotal(value.toString());
	        rowItems.add(item);
	        item=new SummaryRowItem("placeholder", "placeholder");
	        for (int i = 0; i < notifications.length; i++) {
	        	switch(notifications[i][0]){
	        	case "0":
	        		item = new SummaryRowItem("Neue Rechnung von "+notifications[i][1]+", "+notifications[i][4]+"€", notifications[i][3]+" vom "+notifications[i][2]+".");
	        		break;
	        	case "1":
	        		item = new SummaryRowItem("Neuer Kontakt", notifications[i][1]);
	        		break;
	        	case "2":
	        		item = new SummaryRowItem(notifications[i][1]+" hat "+notifications[i][2]+"€ erhalten.", "Offener Betrag: "+notifications[i][3]+"€.");
	        	}
	            rowItems.add(item);
	        }
	        
	        	        
	        // Set the adapter on the ListView
	        SummaryAdapter adapter = new SummaryAdapter(getActivity(), R.layout.notification_list_row, rowItems, debts_list, credits_list);
	        lv.setAdapter(adapter);
//	        
//	        getActivity().findViewById(R.id.left_text).setOnClickListener(new OnClickListener(){
//	        	public void onClick(View v){
//	        		Intent intent = new Intent(getActivity(), CreditsActivity.class);
//	        		ArrayList parcellist = new ArrayList<Article>();
//	            	Bundle credits = new Bundle();
//	            	for(HashMap<String,String> credit : credits_list){
//	            		Parcelable parcel = new Article(credit.get("ID"),credit.get("balance"),credit.get("username"));
//	            		parcellist.add(parcel);
//	            	}
//	            	credits.putParcelableArrayList("articles", parcellist);
//	              	intent.putExtras(credits);
//		        	startActivity(intent);
//	        	}
//	        });
//	        
//	        getActivity().findViewById(R.id.right_text).setOnClickListener(new OnClickListener(){
//	        	public void onClick(View v){
//	        		Intent intent = new Intent(getActivity(), DebtsActivity.class);
//	        		ArrayList parcellist = new ArrayList<Article>();
//	            	Bundle debts = new Bundle();
//	            	for(HashMap<String,String> debt : debts_list){
//	            		Parcelable parcel = new Article(debt.get("ID"),debt.get("balance"),debt.get("username"));
//	            		parcellist.add(parcel);
//	            	}
//	            	debts.putParcelableArrayList("articles", parcellist);
//	              	intent.putExtras(debts);
//		        	startActivity(intent);
//	        	}
//	        });
	        
		return rootView;
	}
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {		
		if(arg2 == 2){

		}
	}
	
public class DebtThread extends Thread{
    	
    	public void run(){
    		try {
    			debts_list = PHPConnector.getBalance("get_debts.php");
    		} catch (ClientProtocolException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		} catch (IOException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		} catch (NotLoggedInException e) {
			// TODO Auto-generated catch block
    			notLoggedIn = true;
    		}
    	}
    }
	public class CreditThread extends Thread{
    	
    	public void run(){
    		try {
    			credits_list = PHPConnector.getBalance("get_credits.php");
    		} catch (ClientProtocolException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		} catch (IOException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		} catch (NotLoggedInException e) {
    			// TODO Auto-generated catch block
    			notLoggedIn = true;	
        	}
    	}
	}
}