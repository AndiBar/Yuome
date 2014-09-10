package com.timkonieczny.yuome;

import java.util.ArrayList;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ListFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class ReceiptsFragment extends ListFragment implements OnItemClickListener { // jeweils eine Klasse für jedes Fragment erstellen

	protected String userID, receiptString;
	public static ArrayList<Receipt> receiptsList;
	private Thread receiptsThread;
	private String[][] people, sharePaid;
	
	public ReceiptsFragment() {
		// Empty constructor required for fragment subclasses
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_receipts, container, false);
		fillReceiptsList();
		try {
			receiptsThread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(!receiptString.equals("no receipts")){
			ReceiptAdapter adapter = new ReceiptAdapter(rootView.getContext(), receiptsList);
			setListAdapter(adapter);
		}
		return rootView;
	}
	
	@Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Log.i("ReceiptsFragment", "[onListItemClick] Selected Position "+ position);
        
        
        Bundle bundle = new Bundle();
        String receiptId = receiptsList.get(position).getId();
        bundle.putString("message", receiptId );
        
        boolean isOwner = receiptsList.get(position).isOwner();
        bundle.putBoolean("isOwner", isOwner );
        
        
        bundle.putStringArray("people", people[position]);
        //bundle.putStringArray("share_paid", sharePaid[position]);
        
        boolean[] sharePaidBool = new boolean[sharePaid[position].length];
        for(int i=0; i<sharePaidBool.length; i++){
        	if(sharePaid[position][i].equals("1")){
        		sharePaidBool[i]=true;
        	}else{
        		sharePaidBool[i]=false;
        	}
        }
        
        bundle.putBooleanArray("share_paid", sharePaidBool);
        
        Fragment fragment = new ReceiptDetailsFragment();
        fragment.setArguments(bundle);
		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

		getActivity().getActionBar().setTitle("Details");
    }
	
	private ArrayList<Receipt> fillReceiptsList(){	// TODO: ggf. Vorhandene Einkäufe beim ersten Start der App herunterladen
		
		receiptsThread= new Thread(
			new Runnable(){
				public void run(){
					receiptString=PHPConnector.doRequest("get_user_receipts.php");
					//receiptString=PHPConnector.getReceiptsFromUser();
					System.out.println("receiptString: "+receiptString);
					
					if(!receiptString.equals("no receipts")){
					    String[] receiptData = receiptString.split(":");
					    for(String i: receiptData){
					    	System.out.println("receiptData: "+i);
					    }
						
						ReceiptsFragment.receiptsList = new ArrayList<Receipt>();
//							ReceiptsFragment.receiptsList.add(new Receipt("01.01.2014","Aldi","Erik, Andi","15,63"));
						
						boolean isOwner;
						
						int j=0;
						
						people=new String[receiptData.length][];
						sharePaid=new String[receiptData.length][];
						
						for(int i=0;i<receiptData.length;i+=7){
							
							people[j] = receiptData[i+2].split(", ");
							sharePaid[j] = receiptData[i+6].split(", ");
							
							if(receiptData[i+5].equals("isOwner")){	//Owner ist der erste der Liste an Personen
								isOwner = true;
							}else{
								isOwner = false;
							}
							ReceiptsFragment.receiptsList.add(new Receipt(receiptData[i], receiptData[i+4],receiptData[i+3],receiptData[i+2],receiptData[i+1],isOwner));
							j++;
						}
					}
					}
				}
			);
		receiptsThread.start();
		
//		String[] receiptData= new String[3];
		
		return ReceiptsFragment.receiptsList;
	    
	    
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		
	}
	
	
}