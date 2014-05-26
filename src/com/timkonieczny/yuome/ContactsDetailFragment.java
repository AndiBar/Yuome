package com.timkonieczny.yuome;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ContactsDetailFragment extends Fragment{
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		setHasOptionsMenu(true);	
		View rootView = inflater.inflate(R.layout.fragment_contacts_detail, container, false);
		
		getActivity().getActionBar().setTitle(SaveValue.getSelectedFriendName());

		return rootView;
	}
	

}
