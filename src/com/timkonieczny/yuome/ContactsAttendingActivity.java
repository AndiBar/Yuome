package com.timkonieczny.yuome;

import android.app.Activity;
import android.os.Bundle;



public class ContactsAttendingActivity extends Activity {
	
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_attending);
        setTitle(R.string.contacts_attending_title);
        
	}
	


}
