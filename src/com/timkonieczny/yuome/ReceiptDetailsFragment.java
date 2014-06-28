package com.timkonieczny.yuome;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.client.ClientProtocolException;

import android.app.Fragment;
import android.app.ListFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ReceiptDetailsFragment extends ListFragment { // jeweils eine Klasse für jedes Fragment erstellen
	
	public String articlesString, id;
	public static ArrayList<Article> articlesList = new ArrayList<Article>();
	
	public ReceiptDetailsFragment() {
		// Empty constructor required for fragment subclasses
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_receipt_details, container, false);
		id=this.getArguments().getString("message");
		Log.d("ReceiptDetailsFragment",this.getArguments().getString("message"));
		
		Thread articlesThread= new Thread(
				new Runnable(){
					public void run(){
						try {				//TODO: Wirft Exception
								articlesString=PHPConnector.getArticles(id);
//								Log.d("articlesString",articlesString);
								
						        String[] articleData = articlesString.split(":");
//						        for(String i: articleData){
//						        	Log.d("receiptData",i);
//						        }
						        articlesList.clear();
						        for(int i=0;i<articleData.length;i+=3){
									ReceiptDetailsFragment.articlesList.add(new Article(articleData[i+1], articleData[i+2],articleData[i]));
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
			try {
				articlesThread.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			ArticleAdapter adapter = new ArticleAdapter(rootView.getContext(), articlesList);
			setListAdapter(adapter);
		
		return rootView;
	}
}