package com.timkonieczny.yuome;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.ClientProtocolException;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class NotificationsFragment extends Fragment implements OnItemClickListener {

	// TODO: PHP-Anbindung...
	protected String notificationString;
	private Thread notificationThread;
	
	private List<NotificationRowItem> rowItems;

	public NotificationsFragment() {
		// Empty constructor required for fragment subclasses
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_notifications, container, false);
		
		notificationThread= new Thread(	//TODO: Serverprobleme??
				new Runnable(){
					public void run(){
						try {				//TODO: Wirft Exception
								notificationString=PHPConnector.getNotifications();
								System.out.println("notificationString: "+notificationString);
								
//						        String[] receiptData = notificationString.split(":");
//						        for(String i: receiptData){
//						        	System.out.println("receiptData: "+i);
//						        }
//								
//								ReceiptsFragment.receiptsList = new ArrayList<Receipt>();
////								ReceiptsFragment.receiptsList.add(new Receipt("01.01.2014","Aldi","Erik, Andi","15,63"));
//								
//								for(int i=0;i<receiptData.length;i+=6){
//									ReceiptsFragment.receiptsList.add(new Receipt(receiptData[i], receiptData[i+4],receiptData[i+3],receiptData[i+2],receiptData[i+1]));
//								}
								
							} catch (ClientProtocolException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								notificationString="failed";
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								notificationString="failed";
							}
						}
					}
				);
			notificationThread.start();
			try {
				notificationThread.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			int debugtype = 0;	//Testdaten
			String debugdate = "2014-08-18";
			float debugtotal = 12.95f;
			String debugstore = "REWE";
			String debugowner = "Andreas Helms";
			
			String[][] notifications = new String[][]{
					new String[]{String.valueOf(debugtype),debugowner,debugdate,debugstore,String.valueOf(debugtotal)},	//Testdaten
					new String[]{"1","Erik Harbeck"},
					new String[]{"2","Andreas Helms","5,63","0,00"}
			};
//			String[] notification = new String[]{String.valueOf(debugtype),debugowner,debugdate,debugstore,String.valueOf(debugtotal)};
			
			ListView lv = (ListView) rootView.findViewById(R.id.myList);
	        rowItems = new ArrayList<NotificationRowItem>();

	        //Populate the List
	        NotificationRowItem item=new NotificationRowItem("placeholder", "placeholder");
	        for (int i = 0; i < notifications.length; i++) {
	        	switch(notifications[i][0]){
	        	case "0":
	        		item = new NotificationRowItem("Neue Rechnung von "+notifications[i][1]+", "+notifications[i][4]+"€", notifications[i][3]+" vom "+notifications[i][2]+".");
	        		break;
	        	case "1":
	        		item = new NotificationRowItem("Neuer Kontakt", notifications[i][1]);
	        		break;
	        	case "2":
	        		item = new NotificationRowItem(notifications[i][1]+" hat "+notifications[i][2]+"€ erhalten.", "Offener Betrag: "+notifications[i][3]+"€.");
	        }
//	        	if(notifications[i][0].equals("0")){
//	        		NotificationRowItem item = new NotificationRowItem("Neue Rechnung von "+notifications[i][1]+", "+notifications[i][4]+"€", notifications[i][3]+" vom "+notifications[i][2]+".");
//	        	}else 
	            rowItems.add(item);
	        }

	        // Set the adapter on the ListView
	        NotificationAdapter adapter = new NotificationAdapter(getActivity(), R.layout.notification_list_row, rowItems);
	        lv.setAdapter(adapter);

		return rootView;
	}
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {		
		if(arg2 == 2){

		}
	}
}