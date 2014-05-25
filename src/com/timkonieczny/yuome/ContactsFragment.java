package com.timkonieczny.yuome;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.client.ClientProtocolException;

import com.timkonieczny.yuome.ChooseContactsActivity.FriendsThread;

import android.app.Fragment;
import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class ContactsFragment extends ListFragment {
    public static ArrayList<HashMap<String, String>> friends_list = new ArrayList<HashMap<String,String>>();
	private ListView settings;
    public static ContactsFragmentAdapter mAdapter;


	public ContactsFragment() {
		// Empty constructor required for fragment subclasses
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Thread friends_thread = new FriendsThread();
        friends_thread.start();
        
        try {
        	long waitMillis = 10000;
        	while (friends_thread.isAlive()) {
        	   friends_thread.join(waitMillis);
        	}
        } catch (InterruptedException e) {
        	}
        
        mAdapter = new ContactsFragmentAdapter(getActivity(),
        		friends_list,
        		 R.layout.fragment_contacts_item,
                 new String[] {"title"},
                 new int[] {R.id.title});
        
        ListView listView = getListView();
        setListAdapter(mAdapter);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		setHasOptionsMenu(true);	
		View rootView = inflater.inflate(R.layout.fragment_contacts, container, false);
	
		return rootView;
	}
	
	// Action Bar Button
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.contacts_new, menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	        case R.id.action_new_contacts:
	        	Intent intent = new Intent(getActivity(), ContactsNewActivity.class);
	            startActivity(intent);
	            return true;
	            
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	  
	public class FriendsThread extends Thread{
  	  public void run(){
  		  try {
  			  friends_list = PHPConnector.getData("get_friends.php");
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