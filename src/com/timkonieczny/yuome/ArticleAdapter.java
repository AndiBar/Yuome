package com.timkonieczny.yuome;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ArticleAdapter extends ArrayAdapter<Article> {

		private final Context context;
		private final ArrayList<Article> itemsArrayList;

		public ArticleAdapter(Context context, ArrayList<Article> itemsArrayList) {
			
			super(context, R.layout.row_articles, itemsArrayList);
			
			this.context = context;
			this.itemsArrayList = itemsArrayList;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
		    
			// 1. Create inflater 
			LayoutInflater inflater = (LayoutInflater) context
		        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		    
			// 2. Get rowView from inflater
			View rowView = inflater.inflate(R.layout.row_articles, parent, false);
		    
			// 3. Get the two text view from the rowView
			TextView title = (TextView) rowView.findViewById(R.id.row_title);
		    TextView amount = (TextView) rowView.findViewById(R.id.row_amount);
		    TextView price = (TextView) rowView.findViewById(R.id.row_price);
		    
		    // 4. Set the text for textView 
		    title.setText(itemsArrayList.get(position).getArticle());
		    amount.setText(itemsArrayList.get(position).getAmount());
		    price.setText(itemsArrayList.get(position).getPrice());
		   
		    // 5. return rowView
		    return rowView;
		}
}