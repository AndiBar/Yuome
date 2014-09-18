package com.timkonieczny.yuome;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ReceiptAdapter extends ArrayAdapter<Receipt> {

		private final Context context;
		private final ArrayList<Receipt> itemsArrayList;

		public ReceiptAdapter(Context context, ArrayList<Receipt> itemsArrayList) {
			
			super(context, R.layout.row_receipts, itemsArrayList);
			
			this.context = context;
			this.itemsArrayList = itemsArrayList;
//			this.itemsArrayList.clear();
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
		    
			// 1. Create inflater 
			LayoutInflater inflater = (LayoutInflater) context
		        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		    
			// 2. Get rowView from inflater
			View rowView = inflater.inflate(R.layout.row_receipts, parent, false);
		    
			// 3. Get the two text view from the rowView
			TextView date = (TextView) rowView.findViewById(R.id.receipt_date);
		    TextView shop = (TextView) rowView.findViewById(R.id.receipt_shop);
		    TextView owner = (TextView) rowView.findViewById(R.id.receipt_owner);
		    TextView names = (TextView) rowView.findViewById(R.id.receipt_names);
		    TextView sum = (TextView) rowView.findViewById(R.id.receipt_sum);
		    
		    // 4. Set the text for textView 
		    date.setText(itemsArrayList.get(position).getDate());
		    shop.setText(itemsArrayList.get(position).getShop() + " ");
		    owner.setText(itemsArrayList.get(position).getOwner());
		    names.setText(itemsArrayList.get(position).getNames());
		    sum.setText(itemsArrayList.get(position).getSum());
		   
		    // 5. return rowView
		    return rowView;
		}
}