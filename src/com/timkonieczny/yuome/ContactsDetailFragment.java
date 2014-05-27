package com.timkonieczny.yuome;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

import com.timkonieczny.yuome.ContactsFragment.FriendsThread;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ContactsDetailFragment extends Fragment{
	private String friend = SaveValue.getSelectedFriendName();
	private double debts;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setHasOptionsMenu(true);	
		View rootView = inflater.inflate(R.layout.fragment_contacts_detail, container, false);
		
		getActivity().getActionBar().setTitle(friend);
		System.out.println("Contact Details Fragment name: " + friend);
		
		Thread debtsThread = new getDebtsThread();
        debtsThread.start();
        
        try {
        	debtsThread.join();
        	System.out.println(debts);
        	TextView toGive = (TextView)rootView.findViewById(R.id.debts);
        	String debtsString = "\u00a0" + String.valueOf(Math.abs(debts)) + "\u00a0\u20ac";
        	if (debts < 0) {
            	toGive.setText(getResources().getString(R.string.inDept) + "\u00a0" + friend + debtsString);
        	} else if (debts > 0) {
        		toGive.setText(getResources().getString(R.string.owing) + "\u00a0" + friend  + debtsString);
        	} else {
        		toGive.setText(R.string.beEeven);
        	}
        } catch (InterruptedException e) {
        	e.printStackTrace();
        }
        
        
		return rootView;
	}
	
	public class getDebtsThread extends Thread{
	  	  public void run(){
	  		  try {
	  			  debts = PHPConnector.getDebt(friend);
	  		  } catch (ClientProtocolException e) {
	  			  // TODO Auto-generated catch block
	  			  e.printStackTrace();
	  		  } catch (IOException e) {
	  			  // TODO Auto-generated catch block
	  			  e.printStackTrace();
	  		  }
	  	  }
		}

}
