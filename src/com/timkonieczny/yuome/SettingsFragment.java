package com.timkonieczny.yuome;

import java.util.ArrayList;
import java.util.Arrays;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class SettingsFragment extends Fragment {

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
		return rootView;
	}
}