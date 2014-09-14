package com.timkonieczny.yuome;

import java.util.List;

import android.content.Context;
import android.widget.ArrayAdapter;

public class InputAdapter extends ArrayAdapter<String>{

	public InputAdapter(Context context, int resource, List<String> objects) {
		super(context, resource, objects);
	}
	
	@Override
	public boolean isEnabled(int position){
		if(position==0){
			return false;
		}else{
			return true;
		}
	}

}
