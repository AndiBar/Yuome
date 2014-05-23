package com.timkonieczny.yuome;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.client.ClientProtocolException;

import android.app.ListFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ReceiptsFragment extends ListFragment { // jeweils eine Klasse für jedes Fragment erstellen

	protected String userID, receiptString;
	public static ArrayList<Receipt> receiptsList;
	private Thread receiptsThread;
	
	public ReceiptsFragment() {
		// Empty constructor required for fragment subclasses
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_receipts, container, false);
		fillReceiptsList();
		try {
			receiptsThread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ReceiptAdapter adapter = new ReceiptAdapter(rootView.getContext(), receiptsList);
		setListAdapter(adapter);
		return rootView;
	}
	
	private ArrayList<Receipt> fillReceiptsList(){	// TODO: ggf. Vorhandene Einkäufe beim ersten Start der App herunterladen
		
		receiptsThread= new Thread(
			new Runnable(){
				public void run(){
					try {				//TODO: Wirft Exception
							receiptString=PHPConnector.getReceiptsFromUser();
							Log.d("receiptString",receiptString);
							
					        String[] receiptData = receiptString.split(":");
					        for(String i: receiptData){
					        	Log.d("receiptData",i);
					        }
							
							ReceiptsFragment.receiptsList = new ArrayList<Receipt>();
//							ReceiptsFragment.receiptsList.add(new Receipt("01.01.2014","Aldi","Erik, Andi","15,63"));
							
							for(int i=0;i<receiptData.length;i+=5){
								ReceiptsFragment.receiptsList.add(new Receipt(receiptData[i+3],receiptData[i+2],receiptData[i+1],receiptData[i]));
							}
							
						} catch (ClientProtocolException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			);
		receiptsThread.start();
		
//		String[] receiptData= new String[3];
		
		return ReceiptsFragment.receiptsList;
	    
	    
	}
}