package com.timkonieczny.yuome;

import java.util.ArrayList;
import java.util.Arrays;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnShowListener;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

public class MoneyReceivedDialogFragment extends DialogFragment {
    
	String[] people;
	String receiptId, owner;
	boolean[] sharePaid;
	float total;
	public boolean[] checkedPerson;
	private AlertDialog dialog;
	private Button okButton;
	
	public MoneyReceivedDialogFragment(String[] people, String receiptId, float total, boolean[] sharePaid) {
		owner = people[0];
		this.people = Arrays.copyOfRange(people, 1, people.length);	//erster Name ist der Owner, der wird weggelassen
		this.receiptId = receiptId;
		this.total = total;
		this.sharePaid=sharePaid;
		checkedPerson = new boolean[this.people.length];
		for(boolean b:checkedPerson){
			b=false;
		}
	}
	
	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Betrag bezahlt?")
        	   .setMultiChoiceItems(people, null, new DialogInterface.OnMultiChoiceClickListener(){
        		   @Override
        		   public void onClick(DialogInterface dialog, int which, boolean isChecked){
        			   checkedPerson[which]=isChecked;
        			   //okButton.setEnabled(false);
        		   }
        	   })
               .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                	   Thread moneyReceivedThread= new Thread(
               				new Runnable(){
               					public void run(){
               						ArrayList<String> checkedPeople = new ArrayList<String>();
									for(int i=0; i<checkedPerson.length; i++){
										if(checkedPerson[i]){
											checkedPeople.add(people[i]);
										}
									}
									
									
									ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
									nameValuePairs.add(new BasicNameValuePair("how_many_people",Integer.toString(checkedPeople.size())));
									nameValuePairs.add(new BasicNameValuePair("individual_share",Float.toString(total)));
									nameValuePairs.add(new BasicNameValuePair("receipt_id",receiptId));
									nameValuePairs.add(new BasicNameValuePair("owner",owner));
									for(int i = 0; i < checkedPeople.size(); i++){
										nameValuePairs.add(new BasicNameValuePair("person" + i,checkedPeople.get(i)));
									}
									
									PHPConnector.doRequest(nameValuePairs, "set_paid.php");
               						}
               					}
               				);
               			moneyReceivedThread.start();
               			try {
               				moneyReceivedThread.join();
               			} catch (InterruptedException e) {
               				// TODO Auto-generated catch block
               				e.printStackTrace();
               			}
                   }
               })
               .setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       // User cancelled the dialog
                   }
               });
        // Create the AlertDialog object and return it
        dialog = builder.create();
        //okButton=dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        dialog.setOnShowListener(new OnShowListener(){

			@Override
			public void onShow(DialogInterface dialog) {
				ListView l=((AlertDialog)dialog).getListView();
				View item;
				for(int i=0;i<sharePaid.length;i++){
					if(sharePaid[i]){	//wenn Person bereits gezahlt hat, Listeneintrag ausgrauen
						item=l.getChildAt(i);
						item.setOnClickListener(null);
						item.setEnabled(false);
					}
				}
				
			}
        	
        });

        dialog.show();
        //okButton.setEnabled(false);
        return dialog;
    }
}