package com.timkonieczny.yuome;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.client.ClientProtocolException;

import android.app.ListFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ReceiptsFragment extends ListFragment { // jeweils eine Klasse für jedes Fragment erstellen

	protected String userID;
	
	public ReceiptsFragment() {
		// Empty constructor required for fragment subclasses
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_receipts, container, false);
		ReceiptAdapter adapter = new ReceiptAdapter(rootView.getContext(), fillReceiptsList());
		setListAdapter(adapter);
		return rootView;
	}
	
	private ArrayList<Receipt> fillReceiptsList(){	// TODO: ggf. Vorhandene Einkäufe beim ersten Start der App herunterladen
		
		new Thread(
			new Runnable(){
				public void run(){
					try {				//TODO: Wirft Exception
							Log.d("Receipts",PHPConnector.getReceiptsFromUser());
						} catch (ClientProtocolException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}	//TODO: php-Datei hochladen
				}
			).start();
		
		ArrayList<Receipt> receiptsList = new ArrayList<Receipt>();
		receiptsList.add(new Receipt("01.01.2014","Aldi","Erik, Andi","15,63"));
	    
	    return receiptsList;
	}
}