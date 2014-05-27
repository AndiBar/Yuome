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
import com.timkonieczny.yuome.DebtsActivity.updateDebtThread;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;


public class CreditsActivity extends ListActivity implements OnItemClickListener, OnClickListener{
    
	public static SimpleAdapter mAdapter;
	public ArrayList<HashMap<String,String>> credits_list = new ArrayList<HashMap<String,String>>();
	public ArrayList<HashMap<String,String>> credits_changed = new ArrayList<HashMap<String,String>>();
	private PopupWindow popupMessage;
	private int selected_debt;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance);
        
        setTitle("Meine Kredite");
        
        final Bundle data = getIntent().getExtras();
        ArrayList<Article> credits = (ArrayList) data.getParcelableArrayList("articles");
        for(Article credit : credits){
        	HashMap<String,String> credit_hash = new HashMap<String,String>();
        	credit_hash.put("ID",credit.getArticle());
        	credit_hash.put("balance",credit.getPrice());
        	credit_hash.put("username",credit.getAmount());
    		credits_list.add(credit_hash);
    	}
        
        mAdapter = new SimpleAdapter(this,
        		credits_list,
        		 R.layout.row_credits,
                 new String[] {"username", "balance"},
                 new int[] {R.id.row_title,R.id.row_balance});

    	setListAdapter(mAdapter);
    	
    	mAdapter.notifyDataSetChanged();

        ListView listView = getListView();
       
        listView.setOnItemClickListener(this);
        
        LinearLayout layoutOfPopup = new LinearLayout(this);
        
        popupMessage = new PopupWindow(layoutOfPopup, LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupLayout = inflater.inflate(R.layout.popup_credits, layoutOfPopup);
        
        popupMessage.setContentView(popupLayout);
        
        Button button = (Button) popupLayout.findViewById(R.id.popup_button);
        button.setOnClickListener(this);
       
    }
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
		selected_debt = arg2;
		final int selected = arg2;
		RadioButton radio_button2 = (RadioButton) popupMessage.getContentView().findViewById(R.id.radioButton2);
		radio_button2.setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				AlertDialog.Builder alert = new AlertDialog.Builder(CreditsActivity.this);

				alert.setTitle("Teilbetrag");
				alert.setMessage("Bitte Teilbetrag von " + credits_list.get(selected).get("balance") + "€ angeben:");

				final EditText input = new EditText(CreditsActivity.this);
				alert.setView(input);

				alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
				  
				  TextView amount_view = (TextView) popupMessage.getContentView().findViewById(R.id.amount_text);
				  try{
					  Double new_balance = Math.round((Double.parseDouble(credits_list.get(selected).get("balance")) - Double.parseDouble(input.getText().toString())) * 100) / 100.;
					  if(new_balance >= 0 && Double.parseDouble(input.getText().toString()) >= 0){
						  amount_view.setText(input.getText());
					  }
					  else{
						  AlertDialogs.showAlert(CreditsActivity.this, "Error", "Eingabe ist fehlerhaft.");
					  }
				  }catch(NumberFormatException e){
					  AlertDialogs.showAlert(CreditsActivity.this, "Error", e.getMessage());
				  }
				}
				});

				alert.show();
			}
		});
    	if(popupMessage.isShowing()){
			popupMessage.dismiss();
		}
		popupMessage.showAsDropDown(arg1);
		
	}
    public void onClick(View v) {
		RadioButton radio_button1 = (RadioButton) popupMessage.getContentView().findViewById(R.id.radioButton1);
		if(radio_button1.isChecked()){
			HashMap<String,String> debt_adapter = (HashMap<String, String>) mAdapter.getItem(selected_debt);
			HashMap<String,String> debt_hash = credits_list.get(selected_debt);
			if(debt_hash.get("ID") == debt_adapter.get("ID")){
				debt_hash.put("balance", "0");
				credits_list.remove(selected_debt);
				credits_list.add(debt_hash);
				credits_changed.add(debt_hash);
			}
		}
		mAdapter.notifyDataSetChanged();
        radio_button1.setChecked(false);
        RadioButton radio_button2 = (RadioButton) popupMessage.getContentView().findViewById(R.id.radioButton2);
        if(radio_button2.isChecked()){
        	TextView amount_view = (TextView) popupMessage.getContentView().findViewById(R.id.amount_text);
        	HashMap<String,String> debt_adapter = (HashMap<String, String>) mAdapter.getItem(selected_debt);
			HashMap<String,String> debt_hash = credits_list.get(selected_debt);
			String balance = debt_hash.get("balance");
			Double new_balance = Math.round((Double.parseDouble(balance) - Double.parseDouble(amount_view.getText().toString())) * 100) / 100.;
			if(debt_hash.get("ID") == debt_adapter.get("ID")){
				debt_hash.put("balance", new_balance.toString());
				credits_list.remove(selected_debt);
				credits_list.add(debt_hash);
				credits_changed.add(debt_hash);
			}
        }
        radio_button2.setChecked(false);
		popupMessage.dismiss();
		// TODO Auto-generated method stub
		
	}    
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.choose_contacts, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
	          case R.id.action_addbuy:
        			runOnUiThread(new Runnable() {
        	            public void run() {
        					new AlertDialog.Builder(CreditsActivity.this)
        					.setTitle("Speichern")
        					.setMessage("Änderungen speichern?")
        					.setIcon(android.R.drawable.ic_dialog_alert)
        					.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
        		
        				    public void onClick(DialogInterface dialog, int whichButton) {
        				    	Thread update_credit_thread = new updateCreditThread();
        				        update_credit_thread.start();
        				        
        				        try {
        				        	long waitMillis = 10000;
        				        	while (update_credit_thread.isAlive()) {
        				        	   update_credit_thread.join(waitMillis);
        				        	}
        				        } catch (InterruptedException e) {
        				        	}
        					        Toast.makeText(CreditsActivity.this, "Änderungen gespeichert.", Toast.LENGTH_SHORT).show();
        					        Intent intent = new Intent(CreditsActivity.this, MainActivity.class);
        				            startActivity(intent);
        				    }})
        				 .setNegativeButton(android.R.string.no, null).show();
        	            }
        			});
	            break;
	          
	          default:
	            break;
        }
	          return true;
    }
    public class updateCreditThread extends Thread{
    	public void run(){
    		try {
				PHPConnector.updateBalance(credits_changed, "update_credits.php");
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
