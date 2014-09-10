package com.timkonieczny.yuome;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class SummaryFragment extends Fragment implements OnItemClickListener {

	// TODO: PHP-Anbindung...
	protected String notificationString;
	protected ArrayList<String[]> stringList = new ArrayList<String[]>();
	private Thread notificationThread;
	private int width, height;
	public String balance;
	private float debit;
	private float credit;
	ArrayList<HashMap<String, String>> debts_list;
	ArrayList<HashMap<String, String>> credits_list;
	private boolean notLoggedIn = false;
	public static ProgressDialog dialog = null;
	
	public static String regid;
	
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
        
		final SharedPreferences prefs = HandleSharedPreferences.getGcmPreferences(getActivity());
		regid = prefs.getString(HandleSharedPreferences.PROPERTY_REG_ID, "");
		
        for(HashMap debt : debts_list){
        	debit = (float) (Math.round((debit + Double.parseDouble(debt.get("balance").toString())) * 100) / 100.);
        }
        Log.d("debit", debit+"");
        
        for(HashMap credt : credits_list){
        	credit = (float) (Math.round((credit + Double.parseDouble(credt.get("balance").toString())) * 100) / 100.);
        }
        Log.d("credit", credit+"");
   
		notificationThread= new Thread(
				new Runnable(){
					public void run(){
						ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
						nameValuePairs.add(new BasicNameValuePair("regid",regid));
						
						notificationString=PHPConnector.doRequest(nameValuePairs, "get_notifications.php");
						
						String[] notificationData = notificationString.split(";");
						
						for(int i=0;i<notificationData.length;i++){
							System.out.println("notificationData: "+notificationData[i]);
							stringList.add(notificationData[i].split(":"));
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
			
			
			ListView lv = (ListView) rootView.findViewById(R.id.myList);
	        rowItems = new ArrayList<SummaryRowItem>();

	        //Populate the List
	        SummaryRowItem item=new SummaryRowItem("placeholder", "placeholder");

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
	        
	        
	        if(stringList!=null){

				for(int i=0;i<stringList.size();i++){
				
		        	switch(stringList.get(i)[0]){
			        	case "0":
			        		item = new SummaryRowItem("Neue Rechnung von "+stringList.get(i)[1]+", "+stringList.get(i)[4]+"€", stringList.get(i)[3]+" vom "+stringList.get(i)[2]+".");
			        		rowItems.add(item);
			        		break;
			        	case "1":
			        		item = new SummaryRowItem("Neuer Kontakt", stringList.get(i)[1]);
			        		rowItems.add(item);
			        		break;
			        	case "2":
			        		item = new SummaryRowItem(stringList.get(i)[2]+" hat "+stringList.get(i)[4]+"€ von dir erhalten.", "Die Rechnung vom "+stringList.get(i)[1]+" ("+stringList.get(i)[3]+") ist beglichen.");
			        		rowItems.add(item);
			        		break;
		        	}
				}
	        }
	        	        
	        // Set the adapter on the ListView
	        SummaryAdapter adapter = new SummaryAdapter(getActivity(), R.layout.notification_list_row, rowItems, debts_list, credits_list);
	        lv.setAdapter(adapter);
	        
		return rootView;
	}
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {		
		if(arg2 == 2){

		}
	}
	
public class DebtThread extends Thread{
    	
    	public void run(){
    		try {
    			String stringResponse = PHPConnector.doRequest("get_debts.php");
    			
    			if(stringResponse.equals("no debts found.")){
    	        	
    	        }else if(stringResponse.equals("not logged in.")){
    	        	throw new NotLoggedInException("Bitte erneut anmelden.");
    	        }else{
    	        	String[] data_unformatted = stringResponse.split(",");
    		        for(String item : data_unformatted){
    		        	HashMap<String, String> data_map = new HashMap<String, String>();
    		        	String[] balance_array = item.split(":");
    		        	String[] data_array = balance_array[1].split("#");
    		        	data_map.put("ID", balance_array[0]);
    		        	data_map.put("balance", data_array[0]);
    		        	data_map.put("username", data_array[1]);
    		        	debts_list.add(data_map);
    		        }
    		    }
    			
    		} catch (NotLoggedInException e) {
			// TODO Auto-generated catch block
    			notLoggedIn = true;
    		}
    	}
    }
	public class CreditThread extends Thread{
    	
    	public void run(){
    		try {
    			String stringResponse = PHPConnector.doRequest("get_credits.php");
    			
    			if(stringResponse.equals("no debts found.")){
    	        	
    	        }else if(stringResponse.equals("not logged in.")){
    	        	throw new NotLoggedInException("Bitte erneut anmelden.");
    	        }else{
    	        	String[] data_unformatted = stringResponse.split(",");
    		        for(String item : data_unformatted){
    		        	HashMap<String, String> data_map = new HashMap<String, String>();
    		        	String[] balance_array = item.split(":");
    		        	String[] data_array = balance_array[1].split("#");
    		        	data_map.put("ID", balance_array[0]);
    		        	data_map.put("balance", data_array[0]);
    		        	data_map.put("username", data_array[1]);
    		        	credits_list.add(data_map);
    		        }
    		    }
    			
    		} catch (NotLoggedInException e) {
    			// TODO Auto-generated catch block
    			notLoggedIn = true;
        	}
    	}
	}
}