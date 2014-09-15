package com.timkonieczny.yuome;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.message.BasicNameValuePair;

import com.timkonieczny.yuome.ContactsFragment.FriendsThread;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
        
        final Button button = (Button) rootView.findViewById(R.id.delete_friend);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                System.out.println("delete Friend klicked");
                if (debts == 0) {
                	Thread deleteThread = new deleteFriendThread();
                    deleteThread.start();
                    try {
						deleteThread.join();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                    Toast toast = Toast.makeText(getActivity(), getResources().getString(R.string.contact_delete_success), 5);
                    toast.show();
                        
                    Fragment fragment = new ContactsFragment();
        			FragmentManager fragmentManager = getFragmentManager();
        			fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
                    
                } else {
                	AlertDialogs.showAlert(getActivity(), getResources().getString(R.string.contact_delete_error), getResources().getString(R.string.contact_delete_error_description));
                }
            }
        });
    
		return rootView;
	}
	
	public class getDebtsThread extends Thread{
		public void run(){			
			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("friend", friend));
			
			String debtsString = PHPConnector.doRequest(nameValuePairs, "get_friend_detail.php");
			debts = Double.parseDouble(debtsString);			
		}
	}
	
	public class deleteFriendThread extends Thread{
		public void run(){
			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("friend", friend));
			
			PHPConnector.doRequest(nameValuePairs, "delete_friend.php");
			
		}
	}
}
