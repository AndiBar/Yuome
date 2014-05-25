package com.timkonieczny.yuome;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.client.ClientProtocolException;

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
		ReceiptAdapter adapter = new ReceiptAdapter(rootView.getContext(), receiptsList);
		setListAdapter(adapter);
		return rootView;
	}
	
	@Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Log.i("ReceiptsFragment", "[onListItemClick] Selected Position "+ position);
        
        
        Bundle bundle = new Bundle();
        String myMessage = receiptsList.get(position).getId();
        bundle.putString("message", myMessage );
        
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
					try {				//TODO: Wirft Exception
							receiptString=PHPConnector.getReceiptsFromUser();
							Log.d("receiptString",receiptString);
							
					        String[] receiptData = receiptString.split(":");
					        for(String i: receiptData){
					        	Log.d("receiptData",i);
					        }
							
							ReceiptsFragment.receiptsList = new ArrayList<Receipt>();
//							ReceiptsFragment.receiptsList.add(new Receipt("01.01.2014","Aldi","Erik, Andi","15,63"));
							
							for(int i=0;i<receiptData.length;i+=6){
								ReceiptsFragment.receiptsList.add(new Receipt(receiptData[i], receiptData[i+4],receiptData[i+3],receiptData[i+2],receiptData[i+1]));
							}
							
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
		receiptsThread.start();
		
//		String[] receiptData= new String[3];
		
		return ReceiptsFragment.receiptsList;
	    
	    
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		
	}
}