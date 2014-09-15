package com.timkonieczny.yuome;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.message.BasicNameValuePair;

import com.timkonieczny.yuome.ContactsFragment.MyMenuItemStuffListener;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ListActivity;
import android.app.ProgressDialog;
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
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;



public class ContactsAttendingActivity extends ListActivity {
	
    public static ArrayList<HashMap<String, String>> friends_attending_list = new ArrayList<HashMap<String,String>>();
    public static ChooseContactsAdapter mAdapter;
    public static ProgressDialog dialog = null;

	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_attending);
        setTitle(R.string.contacts_attending_title);
        
        Thread friends_thread = new FriendsThread();
        friends_thread.start();
        try {
        	long waitMillis = 10000;
        	while (friends_thread.isAlive()) {
        	   friends_thread.join(waitMillis);
        	}
        } catch (InterruptedException e) {
        	e.printStackTrace();
        }
        
       if (friends_attending_list != null && friends_attending_list.size() != 0) { 
       
	        mAdapter = new ChooseContactsAdapter(this,
	        		friends_attending_list,
	        		 R.layout.activity_choose_contacts_item,
	                 new String[] {"title"},
	                 new int[] {R.id.title});
	        
	       ListView listView = this.getListView();
	       setListAdapter(mAdapter);
	       //listView.setClickable(true);
	       listView.setFocusableInTouchMode(false);
	       listView.setFocusable(false);
	       listView.setOnItemClickListener(new OnItemClickListener() {
	            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	            	SaveValue.setSelectedFriendName(friends_attending_list.get(position).get("title"));
	            	Log.i("ContactsFragment", "Friend: " + friends_attending_list.get(position).get("title"));
	    			Fragment fragment = new ContactsDetailFragment();
	    			FragmentManager fragmentManager = getFragmentManager();
	    			fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
	            }
	        });
        
       }
	}
	
	// Action Bar Button
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actionbar_contacts_attending, menu);
        return true;
    }
    
    public boolean onOptionsItemSelected(MenuItem item) {
    	Intent intent;
        switch (item.getItemId()) {
	          case R.id.action_accept:
	        	dialog = ProgressDialog.show(ContactsAttendingActivity.this, "", getString(R.string.dialog_contact_handling), true);
	            new Thread(
	            	new Runnable(){
	            		public void run(){	 
	            			
	            			for(String user: mAdapter.getCheckedUserIDs()) {
		            			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	            				nameValuePairs.add(new BasicNameValuePair("friend", user));
	            				PHPConnector.doRequest(nameValuePairs, "accept_friend.php");
							}
	            			
	            		}
	        		}
	            ).start();
	          	intent = new Intent(this, MainActivity.class);
	        	startActivity(intent);
	            break;
	            
	            
	          case R.id.action_decline:
		        	dialog = ProgressDialog.show(ContactsAttendingActivity.this, "", getString(R.string.dialog_contact_handling), true);
		            new Thread(
		            	new Runnable(){
		            		public void run(){
		            			for(String user: mAdapter.getCheckedUserIDs()) {
			            			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		            				nameValuePairs.add(new BasicNameValuePair("friend", user));
		            				PHPConnector.doRequest(nameValuePairs, "decline_friend.php");
								}
		            		}
		        		}
		            ).start();
		          	intent = new Intent(this, MainActivity.class);
		        	startActivity(intent);
		            break;
	          
	          default:
	            break;
        }
	          return true;
  }


	public class FriendsThread extends Thread{
	  	  public void run(){
	  		String stringResponse = PHPConnector.doRequest("get_friends_attending.php");
			String[] data_unformatted = stringResponse.split(",");
			friends_attending_list = new ArrayList<HashMap<String,String>>();
			if(!stringResponse.equals("no friends found")){
			    for(String item : data_unformatted){
			    	HashMap<String, String>data_map = new HashMap<String, String>();
			    	String[] data_array = item.split(":");
			    	data_map.put("ID", data_array[0]);
			    	data_map.put("title", data_array[1]);
			    	friends_attending_list.add(data_map);
			    }
			}
	  	  }
		}
	
}
