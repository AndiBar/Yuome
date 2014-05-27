package com.timkonieczny.yuome;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ContactsDetailFragment extends Fragment{
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		setHasOptionsMenu(true);	
		View rootView = inflater.inflate(R.layout.fragment_contacts_detail, container, false);
		
		final String friend = SaveValue.getSelectedFriendName();
		
		getActivity().getActionBar().setTitle(friend);
		
		new Thread(new Runnable(){
			@Override
			public void run() {
	        	try {
	        		PHPConnector.getDebt(friend);
	        	} catch (ClientProtocolException e) {
	        		e.printStackTrace();
	        	} catch (IOException e) {
	        		e.printStackTrace();
	        	}
			}
		}).start();
		
		return rootView;
	}
	

}
