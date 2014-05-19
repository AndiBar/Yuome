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


public class DebtsActivity extends ListActivity {
    
	public static SimpleAdapter mAdapter;
	public ArrayList<HashMap<String,String>> debts_list = new ArrayList<HashMap<String,String>>();
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_input);
        
        setTitle("Meine Schulden");
        
        Thread debt_thread = new DebtThread();
        debt_thread.start();
        
        try {
        	long waitMillis = 10000;
        	while (debt_thread.isAlive()) {
        	   debt_thread.join(waitMillis);
        	}
        } catch (InterruptedException e) {
        	}
        
        mAdapter = new SimpleAdapter(this,
        		debts_list,
        		 R.layout.row_articles,
                 new String[] {"ID","amount", "title"},
                 new int[] {R.id.row_title,R.id.row_amount,R.id.row_price});

    	setListAdapter(mAdapter);
    	
    	mAdapter.notifyDataSetChanged();

        ListView listView = getListView();
       
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.choose_contacts, menu);
        return true;
    }
    
    public class DebtThread extends Thread{
    	
    	public void run(){
    		try {
    			debts_list = PHPConnector.getData("get_debts.php");
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
