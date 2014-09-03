package com.timkonieczny.yuome;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import org.apache.http.client.ClientProtocolException;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

public class MoneyReceivedDialogFragment extends DialogFragment {
    
	String[] people;
	public boolean[] checkedPerson;
	
	public MoneyReceivedDialogFragment(String[] people) {
		this.people = Arrays.copyOfRange(people, 1, people.length);	//erster Name ist der Owner, der wird weggelassen
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
        		   }
        	   })
               .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                	   Thread moneyReceivedThread= new Thread(
               				new Runnable(){
               					public void run(){
               						try {
               								ArrayList<String> checkedPeople = new ArrayList<String>();
               								for(int i=0; i<checkedPerson.length; i++){
               									if(checkedPerson[i]){
               										checkedPeople.add(people[i]);
               									}
               								}
               								PHPConnector.setPaid(checkedPeople);
               								
               							} catch (ClientProtocolException e) {
               								// TODO Auto-generated catch block
               								e.printStackTrace();
               							} catch (IOException e) {
               								// TODO Auto-generated catch block
               								e.printStackTrace();
               							}
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
        return builder.create();
    }
}