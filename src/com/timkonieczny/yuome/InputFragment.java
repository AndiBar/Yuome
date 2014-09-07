package com.timkonieczny.yuome;

import java.util.ArrayList;
import java.util.Arrays;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class InputFragment extends Fragment implements OnItemClickListener {

	private ListView settings;
	private static ProgressDialog dialog = null;

	public InputFragment() {
		// Empty constructor required for fragment subclasses
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_settings,
				container, false);
		settings = (ListView) rootView.findViewById(R.id.settings);
		ArrayAdapter<String> listAdapter;

		String[] settingItems = { "Kassenzettel scannen", "Manuelle Eingabe" };
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
		if(arg2 == 0){
			Intent intent = new Intent(getActivity(), CameraActivity.class);
        	startActivity(intent);
		}else if(arg2 == 1){
			dialog = ProgressDialog.show(getActivity(), "","Daten werden geladen", true);
			Intent intent = new Intent(getActivity(), ManualInputActivity.class);
        	startActivity(intent);
		}
	}
}