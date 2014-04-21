package com.timkonieczny.yuome;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class ContactsNewActivity extends Activity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_new);
        setTitle("Neuen Kontakt erstellen");
        
	}
	
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
	        	Intent intent = new Intent(this, MainActivity.class);
	        	intent.putExtra("selectedMenu", 3);
	            startActivity(intent);
	            
	            return true;

	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}

}
