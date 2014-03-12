package com.timkonieczny.yuome;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ReceiptsFragment extends Fragment { // jeweils eine Klasse für jedes Fragment erstellen

	public ReceiptsFragment() {
		// Empty constructor required for fragment subclasses
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_receipts, container, false);
		return rootView;
	}
}