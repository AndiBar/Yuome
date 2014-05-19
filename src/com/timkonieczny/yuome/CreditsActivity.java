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

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.client.ClientProtocolException;

import com.timkonieczny.yuome.ChooseContactsActivity.FriendsThread;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;


public class CreditsActivity extends ListActivity {
    
	public static SimpleAdapter mAdapter;
	public ArrayList<HashMap<String,String>> credits_list = new ArrayList<HashMap<String,String>>();
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance);
        
        setTitle("Meine Kredite");
        
        Thread credit_thread = new CreditThread();
        credit_thread.start();
        
        try {
        	long waitMillis = 10000;
        	while (credit_thread.isAlive()) {
        	   credit_thread.join(waitMillis);
        	}
        } catch (InterruptedException e) {
        	}
        
        mAdapter = new SimpleAdapter(this,
        		credits_list,
        		 R.layout.row_credits,
                 new String[] {"username", "balance"},
                 new int[] {R.id.row_title,R.id.row_balance});

    	setListAdapter(mAdapter);
    	
    	mAdapter.notifyDataSetChanged();

        ListView listView = getListView();
       
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.choose_contacts, menu);
        return true;
    }
    
    public class CreditThread extends Thread{
    	
    	public void run(){
    		try {
    			credits_list = PHPConnector.getBalance("get_credits.php");
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
