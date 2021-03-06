package com.timkonieczny.yuome;

import java.util.ArrayList;
import java.util.Arrays;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
	private static ProgressDialog dialog = null;

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

		String[] settingItems = { "Username �ndern", "Passwort �ndern",
				"Abmelden", "Konto l�schen" };
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
			dialog = ProgressDialog.show(getActivity(), "","Daten werden geladen", true);
			new Thread(new Runnable(){public void run(){
				String username = "";
				username = PHPConnector.doRequest("check_for_user.php");
				String user = username.split(" ")[0];
				String already = username.split(" ")[1];
				if(!already.equals("already")){
					AlertDialogs.showAlert(getActivity(), "Error", "Fehler beim abmelden. Benutzer bereits abgemeldet?");
					dialog.dismiss();
				}else{
					showLogoutMessage(user);
				}
			}}).start();
		}
	}
	public void showLogoutMessage(String user){
		final String username = user;
		getActivity().runOnUiThread(new Runnable() {
            public void run() {
            	dialog.dismiss();
				new AlertDialog.Builder(getActivity())
				.setTitle("Abmelden")
				.setMessage(username + " wirklich abmelden?")
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
	
			    public void onClick(DialogInterface dialog, int whichButton) {
			    	new Thread(new Runnable(){public void run(){
				    	PHPConnector.doRequest("log_off.php");
			    	}}).start();
				        Toast.makeText(getActivity(), "Erfolgreich abgemeldet.", Toast.LENGTH_SHORT).show();
				        Intent intent = new Intent(getActivity(), WelcomeActivity.class);
				        
				        Editor editor = PreferenceManager.getDefaultSharedPreferences(getActivity()).edit();
				        editor.clear();
				        editor.commit();
				        //TODO: SharedPreferences einzeln l�schen? Per Getter holen und nicht-static-Variable �bergeben?
			            
				        startActivity(intent);
			    }})
			 .setNegativeButton(android.R.string.no, null).show();
            }
		});
	}
}