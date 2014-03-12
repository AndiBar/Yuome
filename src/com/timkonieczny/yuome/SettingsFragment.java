package com.timkonieczny.yuome;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

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

public class SettingsFragment extends Fragment implements OnItemClickListener {

	private ListView settings;

	public SettingsFragment() {
		// Empty constructor required for fragment subclasses
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_settings,
				container, false);
		settings = (ListView) rootView.findViewById(R.id.settings);
		ArrayAdapter<String> listAdapter;

		String[] settingItems = { "Username ändern", "Passwort ändern",
				"Abmelden", "Konto löschen" };
		ArrayList<String> settingsList = new ArrayList<String>();
		settingsList.addAll(Arrays.asList(settingItems));

		// Create ArrayAdapter using the planet list.
		listAdapter = new ArrayAdapter<String>(getActivity(),
				R.layout.settings_item, settingsList);
		settings.setAdapter(listAdapter);
		settings.setOnItemClickListener(this);
		return rootView;
	}
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
		
		if(arg2 == 2){
			new Thread(new Runnable(){public void run(){
				String username = "";
				try{
					username = PHPConnector.getResponse("http://andibar.dyndns.org:5678/Yuome/check_for_user.php");
					String user = username.split(" ")[0];
					String already = username.split(" ")[1];
					if(!already.equals("already")){
						AlertDialogs.showAlert(getActivity(), "Error", "Fehler beim abmelden. Benutzer bereits abgemeldet?");
					}else{
						showLogoutMessage(user);
					}
				}catch(Exception e){
		            AlertDialogs.showAlert(getActivity(), "Error", e.getMessage());
		    	}
			}}).start();
		}
	}
	public void showLogoutMessage(String user){
		final String username = user;
		getActivity().runOnUiThread(new Runnable() {
            public void run() {
				new AlertDialog.Builder(getActivity())
				.setTitle("Abmelden")
				.setMessage(username + " wirklich abmelden?")
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
	
			    public void onClick(DialogInterface dialog, int whichButton) {
			    	new Thread(new Runnable(){public void run(){
				    	try {
							PHPConnector.logOff();
						} catch (ClientProtocolException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
			    	}}).start();
				        Toast.makeText(getActivity(), "Erfolgreich abgemeldet.", Toast.LENGTH_SHORT).show();
				        Intent intent = new Intent(getActivity(), WelcomeActivity.class);
			            startActivity(intent);
			    }})
			 .setNegativeButton(android.R.string.no, null).show();
            }
		});
	}
}