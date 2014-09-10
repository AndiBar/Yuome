package com.timkonieczny.yuome;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
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
       //listView.setClickable(true);
       listView.setFocusableInTouchMode(false);
       listView.setFocusable(false);
        listView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            	
            	Log.i("ContactsFragment", "[onListItemClick] Selected Position "+ position + "ID: " + id);
            	
    			Fragment fragment = new ContactsDetailFragment();
    			FragmentManager fragmentManager = getFragmentManager();
    			fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
    			getActivity().getActionBar().setTitle("Kontakt Details");
            }
        });
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
  		  
  		  
  		  String stringResponse = PHPConnector.doRequest("get_friends.php");
		
		String[] data_unformatted = stringResponse.split(",");
		friends_list = new ArrayList<HashMap<String,String>>();
		if(!stringResponse.equals("no friends found")){
		    for(String item : data_unformatted){
		    	HashMap<String, String>data_map = new HashMap<String, String>();
		    	String[] data_array = item.split(":");
		    	data_map.put("ID", data_array[0]);
		    	data_map.put("title", data_array[1]);
		    	friends_list.add(data_map);
		    }
		}else{
			HashMap<String, String>data_map = new HashMap<String, String>();
			data_map.put("ID", "0");
			data_map.put("title", "Du hast noch keine Kontakte");
			friends_list.add(data_map);
		}
  	  }
	}


}