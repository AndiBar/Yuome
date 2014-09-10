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

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

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


public class DebtsActivity extends ListActivity implements OnItemClickListener, OnClickListener{
    
	public static SimpleAdapter mAdapter;
	public ArrayList<HashMap<String,String>> debts_list = new ArrayList<HashMap<String,String>>();
	public ArrayList<HashMap<String,String>> debts_changed = new ArrayList<HashMap<String,String>>();
	private PopupWindow popupMessage;
	private int selected_debt;
	private Double partial_amount;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance);
        
        setTitle("Meine Schulden");
        
        final Bundle data = getIntent().getExtras();
        ArrayList<Article> debts = (ArrayList) data.getParcelableArrayList("articles");
        for(Article debt : debts){
        	HashMap<String,String> debt_hash = new HashMap<String,String>();
        	debt_hash.put("ID",debt.getArticle());
        	debt_hash.put("balance",debt.getPrice());
        	debt_hash.put("username",debt.getAmount());
    		debts_list.add(debt_hash);
    	}
        
        mAdapter = new SimpleAdapter(this,
        		debts_list,
        		 R.layout.row_debts,
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
        View popupLayout = inflater.inflate(R.layout.popup_debts, layoutOfPopup);
        
        popupMessage.setContentView(popupLayout);
        
        Button button = (Button) popupLayout.findViewById(R.id.popup_button);
        button.setOnClickListener(this);
       
    }
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
		selected_debt = arg2;
		final int selected = arg2;
		final RadioButton radio_button1 = (RadioButton) popupMessage.getContentView().findViewById(R.id.radioButton1);
		radio_button1.setChecked(true);
		RadioButton radio_button2 = (RadioButton) popupMessage.getContentView().findViewById(R.id.radioButton2);
		TextView amount_view = (TextView) popupMessage.getContentView().findViewById(R.id.amount_text);
		amount_view.setText("+" + debts_list.get(selected).get("balance") + "€");
		radio_button2.setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				AlertDialog.Builder alert = new AlertDialog.Builder(DebtsActivity.this);

				alert.setTitle("Teilbetrag");
				alert.setMessage("Bitte Teilbetrag von " + debts_list.get(selected).get("balance") + "€ angeben:");

				final EditText input = new EditText(DebtsActivity.this);
				alert.setView(input);

				alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
				  
				  RadioButton radio_button2 = (RadioButton) popupMessage.getContentView().findViewById(R.id.radioButton2);
				  TextView amount_view = (TextView) popupMessage.getContentView().findViewById(R.id.amount_text);
				  try{
					  Double new_balance = Math.round((Double.parseDouble(debts_list.get(selected).get("balance")) - Double.parseDouble(input.getText().toString())) * 100) / 100.;
					  if(new_balance >= 0 && Double.parseDouble(input.getText().toString()) >= 0){
						  amount_view.setText("+" + input.getText() + "€");
						  partial_amount = Double.parseDouble(input.getText().toString());
					  }
					  else{
						  AlertDialogs.showAlert(DebtsActivity.this, "Error", "Eingabe ist fehlerhaft.");
						  radio_button2.setChecked(false);
						  radio_button1.setChecked(true);
					  }
				  }catch(NumberFormatException e){
					  AlertDialogs.showAlert(DebtsActivity.this, "Error", e.getMessage());
					  radio_button2.setChecked(false);
					  radio_button1.setChecked(true);
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
			HashMap<String,String> debt_hash = debts_list.get(selected_debt);
			if(debt_hash.get("ID") == debt_adapter.get("ID")){
				debt_hash.put("balance", "0");
				debts_list.set(selected_debt, debt_hash);
				debts_changed.add(debt_hash);
			}
		}
		mAdapter.notifyDataSetChanged();
        RadioButton radio_button2 = (RadioButton) popupMessage.getContentView().findViewById(R.id.radioButton2);
        if(radio_button2.isChecked()){
        	HashMap<String,String> debt_adapter = (HashMap<String, String>) mAdapter.getItem(selected_debt);
			HashMap<String,String> debt_hash = debts_list.get(selected_debt);
			String balance = debt_hash.get("balance");
			Double new_balance = Math.round((Double.parseDouble(balance) - partial_amount) * 100) / 100.;
			if(debt_hash.get("ID") == debt_adapter.get("ID")){
				debt_hash.put("balance", new_balance.toString());
				debts_list.set(selected_debt, debt_hash);
				debts_changed.add(debt_hash);
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
        					new AlertDialog.Builder(DebtsActivity.this)
        					.setTitle("Speichern")
        					.setMessage("Änderungen speichern?")
        					.setIcon(android.R.drawable.ic_dialog_alert)
        					.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
        		
        				    public void onClick(DialogInterface dialog, int whichButton) {
        				    	Thread update_debt_thread = new updateDebtThread();
        				        update_debt_thread.start();
        				        
        				        try {
        				        	long waitMillis = 10000;
        				        	while (update_debt_thread.isAlive()) {
        				        	   update_debt_thread.join(waitMillis);
        				        	}
        				        } catch (InterruptedException e) {
        				        	}
        					        Toast.makeText(DebtsActivity.this, "Änderungen gespeichert.", Toast.LENGTH_SHORT).show();
        					        Intent intent = new Intent(DebtsActivity.this, MainActivity.class);
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
    public class updateDebtThread extends Thread{
    	public void run(){
    		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("debts_number",String.valueOf(debts_changed.size())));
			for(int i = 0; i < debts_changed.size(); i++){
				nameValuePairs.add(new BasicNameValuePair("ID" + i,debts_changed.get(i).get("ID")));
				nameValuePairs.add(new BasicNameValuePair("balance" + i,debts_changed.get(i).get("balance")));
			}
			PHPConnector.doRequest(nameValuePairs, "update_debts.php");
  	  	}
    }
    
}

