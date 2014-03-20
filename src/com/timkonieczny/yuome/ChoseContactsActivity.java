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
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
//import com.timkonieczny.yuomeclickdummy.R;











import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class ChoseContactsActivity extends ListActivity {
    ArrayAdapter<String> mAdapter;
     
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chose_contacts);
        
        setTitle("Kontakte");
        
        ArrayList<HashMap<String, String>> users_list = new ArrayList<HashMap<String,String>>();
        SimpleAdapter mAdapter;
        
        String[] contacts = new String[]{
        		"Erik Harbeck",
        		"Nicolas Schwartau",
        		"Tim Konieczny"};
        
        for(int index = 0; index < contacts.length; index++){
        	HashMap<String, String> depts = new HashMap<String, String>();
        	depts.put("contact", "   " + contacts[index]);
        	users_list.add(depts);
        }
        
        mAdapter = new SimpleAdapter(this,
        		users_list,
        		 R.layout.activity_chose_contacts_item,
                 new String[] {"contact"},
                 new int[] {R.id.contactCheckBox});
        
        setListAdapter(mAdapter);	
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.chose_contacts, menu);
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
