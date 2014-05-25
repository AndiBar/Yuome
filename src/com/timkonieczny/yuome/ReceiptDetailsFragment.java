package com.timkonieczny.yuome;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.client.ClientProtocolException;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ReceiptDetailsFragment extends Fragment { // jeweils eine Klasse für jedes Fragment erstellen
	
	public String articlesString, id;
	
	public ReceiptDetailsFragment() {
		// Empty constructor required for fragment subclasses
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.receipt_details, container, false);
		id=this.getArguments().getString("message");
		Log.d("ReceiptDetailsFragment",this.getArguments().getString("message"));
		
		Thread articlesThread= new Thread(
				new Runnable(){
					public void run(){
						try {				//TODO: Wirft Exception
								articlesString=PHPConnector.getArticles(id);
								Log.d("articlesString",articlesString);
								
						        String[] articleData = articlesString.split(":");
						        for(String i: articleData){
						        	Log.d("receiptData",i);
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
			articlesThread.start();
		
		return rootView;
	}
}