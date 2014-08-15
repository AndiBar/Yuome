package com.timkonieczny.yuome;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.http.client.ClientProtocolException;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class NotificationsFragment extends Fragment implements OnItemClickListener {

	private ListView notifications;	// TODO: PHP-Anbindung...
	protected String notificationString;
	private Thread notificationThread;
	
	private List<NotificationRowItem> rowItems;

    private static Integer[] images = {
            R.drawable.red,
            R.drawable.red,
            R.drawable.red,
            R.drawable.red,
            R.drawable.red,
            R.drawable.red,
            R.drawable.red,
            R.drawable.red
    };

	public NotificationsFragment() {
		// Empty constructor required for fragment subclasses
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_notifications, container, false);
		
		ListView lv = (ListView) rootView.findViewById(R.id.myList);
        rowItems = new ArrayList<NotificationRowItem>();

        String[] titles = {"Movie1","Movie2","Movie3","Movie4","Movie5","Movie6","Movie7","Movie8"};
        String[] descriptions = {"First Movie","Second movie","Third Movie","Fourth Movie","Fifth Movie",
                "Sixth Movie","Seventh Movie","Eighth Movie"};

        //Populate the List
        for (int i = 0; i < titles.length; i++) {
            NotificationRowItem item = new NotificationRowItem(images[i], titles[i], descriptions[i]);
            rowItems.add(item);
        }

        // Set the adapter on the ListView
        NotificationAdapter adapter = new NotificationAdapter(getActivity(), R.layout.notification_list_row, rowItems);
        lv.setAdapter(adapter);
		
		
//		notificationThread= new Thread(
//				new Runnable(){
//					public void run(){
//						try {				//TODO: Wirft Exception
//								notificationString=PHPConnector.getNotifications();
//								System.out.println("notificationString: "+notificationString);
//								
////						        String[] receiptData = notificationString.split(":");
////						        for(String i: receiptData){
////						        	System.out.println("receiptData: "+i);
////						        }
////								
////								ReceiptsFragment.receiptsList = new ArrayList<Receipt>();
//////								ReceiptsFragment.receiptsList.add(new Receipt("01.01.2014","Aldi","Erik, Andi","15,63"));
////								
////								for(int i=0;i<receiptData.length;i+=6){
////									ReceiptsFragment.receiptsList.add(new Receipt(receiptData[i], receiptData[i+4],receiptData[i+3],receiptData[i+2],receiptData[i+1]));
////								}
//								
//							} catch (ClientProtocolException e) {
//								// TODO Auto-generated catch block
//								e.printStackTrace();
//								notificationString="failed";
//							} catch (IOException e) {
//								// TODO Auto-generated catch block
//								e.printStackTrace();
//								notificationString="failed";
//							}
//						}
//					}
//				);
//			notificationThread.start();
//			try {
//				notificationThread.join();
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		
//		
//		String[] settingItems = { notificationString, "Notification 2", "Notification 3", "Notification 4" };
//		ArrayList<String> settingsList = new ArrayList<String>();
//		settingsList.addAll(Arrays.asList(settingItems));
		return rootView;
	}
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {		
		if(arg2 == 2){

		}
	}
}