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
import java.util.ArrayList;
import java.util.HashMap;

public class ChooseContactsActivity extends ListActivity {
    ArrayAdapter<String> mAdapter;
     
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_contacts);
        
        setTitle("Kontakte");
        
        Bundle data = getIntent().getExtras();
        ArrayList<Article> articles = (ArrayList) data.getParcelableArrayList("articles");

        ArrayList<HashMap<String, String>> users_list = new ArrayList<HashMap<String,String>>();
        SimpleAdapter mAdapter;
        
        ArrayList<String> contacts = new ArrayList<String>();
        
        for(int index = 0; index < contacts.size(); index++){
        	HashMap<String, String> depts = new HashMap<String, String>();
        	depts.put("contact", "   " + contacts.get(index));
        	users_list.add(depts);
        }
        
        mAdapter = new SimpleAdapter(this,
        		users_list,
        		 R.layout.activity_choose_contacts_item,
                 new String[] {"contact"},
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
}
