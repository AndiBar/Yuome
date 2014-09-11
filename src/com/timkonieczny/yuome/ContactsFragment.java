package com.timkonieczny.yuome;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.client.ClientProtocolException;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ListFragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ContactsFragment extends ListFragment {
    public static ArrayList<HashMap<String, String>> friends_list = new ArrayList<HashMap<String,String>>();
    public static ArrayList<HashMap<String, String>> friends_attending_list = new ArrayList<HashMap<String,String>>();
	private ListView settings;
    public static ContactsFragmentAdapter mAdapter;
    private int bell_number = 0;
    
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
        	e.printStackTrace();
        }
        
        mAdapter = new ContactsFragmentAdapter(getActivity(),
        		friends_list,
        		 R.layout.activity_choose_contacts,
                 new String[] {"title"},
                 new int[] {R.id.title});
        
       ListView listView = getListView();
       setListAdapter(mAdapter);
       //listView.setClickable(true);
       listView.setFocusableInTouchMode(false);
       listView.setFocusable(false);
       listView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            	SaveValue.setSelectedFriendName(friends_list.get(position).get("title"));
            	Log.i("ContactsFragment", "Friend: " + friends_list.get(position).get("title"));
    			Fragment fragment = new ContactsDetailFragment();
    			FragmentManager fragmentManager = getFragmentManager();
    			fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
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
		
		final View menu_hotlist = menu.findItem(R.id.action_attending_contacts).getActionView();
		
		MenuItem bell_button = menu.findItem(R.id.action_attending_contacts);
		TextView ui_bell = (TextView) menu_hotlist.findViewById(R.id.hotlist_hot);
		bell_number = friends_attending_list.size();
		if (bell_number == 0) {
			bell_button.setVisible(isHidden());
		} else {
			bell_button.setVisible(isVisible());
			ui_bell.setText(Integer.toString(bell_number));
		}
		
	    new MyMenuItemStuffListener(menu_hotlist, "Show hot message") {
	        @Override
	        public void onClick(View v) {
	        	Intent intent = new Intent(getActivity(), ContactsAttendingActivity.class);
	            startActivity(intent);
	        }
	    };
	}
	
	static abstract class MyMenuItemStuffListener implements View.OnClickListener, View.OnLongClickListener {
	    private String hint;
	    private View view;

	    MyMenuItemStuffListener(View view, String hint) {
	        this.view = view;
	        this.hint = hint;
	        view.setOnClickListener(this);
	        view.setOnLongClickListener(this);
	    }

	    @Override abstract public void onClick(View v);

	    @Override public boolean onLongClick(View v) {
	        final int[] screenPos = new int[2];
	        final Rect displayFrame = new Rect();
	        view.getLocationOnScreen(screenPos);
	        view.getWindowVisibleDisplayFrame(displayFrame);
	        final Context context = view.getContext();
	        final int width = view.getWidth();
	        final int height = view.getHeight();
	        final int midy = screenPos[1] + height / 2;
	        final int screenWidth = context.getResources().getDisplayMetrics().widthPixels;
	        Toast cheatSheet = Toast.makeText(context, hint, Toast.LENGTH_SHORT);
	        if (midy < displayFrame.height()) {
	            cheatSheet.setGravity(Gravity.TOP | Gravity.RIGHT,
	                    screenWidth - screenPos[0] - width / 2, height);
	        } else {
	            cheatSheet.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, height);
	        }
	        cheatSheet.show();
	        return true;
	    }
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
  		try {
  		    friends_attending_list = PHPConnector.getData("get_friends_attending.php");
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