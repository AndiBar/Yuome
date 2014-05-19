package com.timkonieczny.yuome;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

public class ContactsNewActivity extends Activity {
	
	private String username;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_new);
        setTitle("Neuen Kontakt erstellen");
        
        EditText usernameTextfield = (EditText)findViewById(R.id.contacts_new_enterusername);
        usernameTextfield.addTextChangedListener(new TextWatcher(){
			@Override
			public void afterTextChanged(Editable arg0) {
				username = arg0.toString();
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
				
			}
        
	});}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.receipt_postprocessing, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	        case R.id.action_addbuy:
	        		new Thread(new Runnable(){
						@Override
						public void run() {
				        	try {
				        		PHPConnector.addFriend(username);
				        	} catch (ClientProtocolException e) {
				        		e.printStackTrace();
				        	} catch (IOException e) {
				        		e.printStackTrace();
				        	}
						}
	        		}).start();
	        	
	        	
	        	Intent intent = new Intent(this, MainActivity.class);
	        	intent.putExtra("selectedMenu", 3);
	            startActivity(intent);
	            
	            return true;

	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}

}
