/*
 * Copyright 2013 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.timkonieczny.yuome;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.SimpleAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.client.ClientProtocolException;

public class ChooseContactsActivity extends ListActivity {
    public static SimpleAdapter mAdapter;
    public static ArrayList<HashMap<String, String>> friends_list = new ArrayList<HashMap<String,String>>();
     
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_contacts);
        
        setTitle("Kontakte");
        
        Bundle data = getIntent().getExtras();
        ArrayList<Article> articles = (ArrayList) data.getParcelableArrayList("articles");
        
        Thread friends_thread = new FriendsThread();
        friends_thread.start();
        
        try {
        	long waitMillis = 10000;
        	   friends_thread.join(waitMillis);
        	   if (friends_thread.isAlive()) {
        	      // Die 10 Sekunden sind um; der Thread läuft noch
        	   } else {
        	      // Thread ist beendet
        	   }
        	} catch (InterruptedException e) {
        	   // Thread wurde abgebrochen
        	}
        
        mAdapter = new SimpleAdapter(this,
        		friends_list,
        		 R.layout.activity_choose_contacts_item,
                 new String[] {"username"},
                 new int[] {R.id.contactCheckBox});
        
        setListAdapter(mAdapter);	
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.choose_contacts, menu);
        return true;
      }
      public boolean onOptionsItemSelected(MenuItem item) {
          switch (item.getItemId()) {
          case R.id.action_addbuy:
          	Intent intent = new Intent(this, MainActivity.class);
              startActivity(intent);
            break;
          
          default:
            break;
          }

          return true;
        }
      public class FriendsThread extends Thread{
    	  public void run(){
  	       	try {
  				friends_list = PHPConnector.getFriends();
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
