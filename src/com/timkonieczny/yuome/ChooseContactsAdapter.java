package com.timkonieczny.yuome;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class ChooseContactsAdapter extends SimpleAdapter {
    private ArrayList<String> checked_user_ids;
    private LayoutInflater inflater;
    public ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String,String>>();

	public ChooseContactsAdapter(Context context,
			List<? extends Map<String, ?>> data, int resource, String[] from,
			int[] to) {
		super(context, data, resource, from, to);
		this.inflater = LayoutInflater.from(context);
		this.checked_user_ids = new ArrayList<String>();
		this.data = (ArrayList<HashMap<String, String>>) data;
		// TODO Auto-generated constructor stub
	}

	public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.activity_choose_contacts_item, null);
        }
        final CheckBox check_box = (CheckBox) convertView.findViewById(R.id.contactCheckBox);
        final TextView user_view = (TextView) convertView.findViewById(R.id.username);
        user_view.setText(data.get(position).get("username"));
      
		check_box.setOnClickListener(new OnClickListener() {
			    @Override
	        public void onClick(View v) {
	            if (check_box.isChecked()) {
	            System.out.println(data.get(position).get("ID"));
	            checked_user_ids.add(data.get(position).get("ID"));
	            }
	        }
	    });
	       
	    return convertView;
    }
	public ArrayList<String> getCheckedUserIDs(){
		return checked_user_ids;
	}
}
