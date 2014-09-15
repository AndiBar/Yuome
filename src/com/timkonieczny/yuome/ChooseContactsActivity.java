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
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public class ChooseContactsActivity extends ListActivity {
    public static ChooseContactsAdapter mAdapter;
    public static ArrayList<HashMap<String, String>> friends_list = new ArrayList<HashMap<String,String>>();
    public static ProgressDialog dialog = null;
    public Bundle data;
    public ArrayList<HashMap<String, String>> article_list;
     
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_contacts);
        
        setTitle("Kontakte");
        
        ManualInputActivity.dialog.dismiss();
        
        Thread friends_thread = new FriendsThread();
        friends_thread.start();
        
        try {
        	long waitMillis = 10000;
        	while (friends_thread.isAlive()) {
        	   friends_thread.join(waitMillis);
        	}
        } catch (InterruptedException e) {
        	}
        
        HashMap<String,String> owner = new HashMap<String,String>();
        owner.put("title", "Ich");
        owner.put("ID", "0");
        friends_list.add(friends_list.get(0));
        friends_list.set(0,owner);
        
        mAdapter = new ChooseContactsAdapter(this,
        		friends_list,
        		 R.layout.activity_choose_contacts_item,
                 new String[] {"title", "contactCheckBox"},
                 new int[] {R.id.title, R.id.contactCheckBox});
        
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
	        	dialog = ProgressDialog.show(ChooseContactsActivity.this, "","Einkauf wird verarbeitet", true);
	        	data = getIntent().getExtras();
	        	article_list = new ArrayList<HashMap<String,String>>();
	            ArrayList<Article> articles = (ArrayList) data.getParcelableArrayList("articles");
	            for(Article article : articles){
	            	HashMap<String,String> article_hash = new HashMap<String,String>();
	            	article_hash.put("article",article.getArticle());
	            	article_hash.put("price",article.getPrice());
	            	article_hash.put("amount",article.getAmount());
	        		article_list.add(article_hash);
	        	}
	            Thread add_buy_thread = new AddBuyThread();
	            add_buy_thread.start();
	            
	            try {
	            	long waitMillis = 10000;
	            	while (add_buy_thread.isAlive()) {
	            	   add_buy_thread.join(waitMillis);
	            	}
	            } catch (InterruptedException e) {
	            	}
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
    	  }
    }
    public class AddBuyThread extends Thread{
    	public void run(){
    		double debit_value = 0 - (data.getDouble("total") / (mAdapter.getCheckedUserIDs().size()));
			debit_value = Math.round(debit_value * 100) / 100.;
			double credit_value = data.getDouble("total") + debit_value;
			
			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("article_number",String.valueOf(article_list.size())));
			for(int i = 0; i < article_list.size(); i++){
				nameValuePairs.add(new BasicNameValuePair("article" + i,article_list.get(i).get("article")));
				nameValuePairs.add(new BasicNameValuePair("price" + i,article_list.get(i).get("price")));
				nameValuePairs.add(new BasicNameValuePair("amount" + i,article_list.get(i).get("amount")));
			}
			nameValuePairs.add(new BasicNameValuePair("user_number",String.valueOf(mAdapter.getCheckedUserIDs().size())));
			for(int i = 0; i < mAdapter.getCheckedUserIDs().size(); i++){
				nameValuePairs.add(new BasicNameValuePair("ID" + i,mAdapter.getCheckedUserIDs().get(i)));
			}  
			nameValuePairs.add(new BasicNameValuePair("storeID",data.getString("storeID")));
			nameValuePairs.add(new BasicNameValuePair("date",data.getString("date")));
			nameValuePairs.add(new BasicNameValuePair("debit",String.valueOf(debit_value)));
			nameValuePairs.add(new BasicNameValuePair("credit",String.valueOf(credit_value)));
			
			PHPConnector.doRequest(nameValuePairs, "add_buy.php");
    	}
    }
}
