package com.timkonieczny.yuome;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SummaryAdapter extends ArrayAdapter<SummaryRowItem>{

    Context context;
    ArrayList<HashMap<String, String>> debts_list, credits_list;
    ViewHolder holder;

    public SummaryAdapter(Context context, int resourceId, List<SummaryRowItem> items, ArrayList<HashMap<String, String>> debts_list, ArrayList<HashMap<String, String>> credits_list) {
        super(context, resourceId, items);
        this.context = context;
        this.debts_list = debts_list;
        this.credits_list = credits_list;
    }

    public class ViewHolder {
        TextView title;
        TextView description;
        View left;
        View right;
        TextView rightText;
        TextView leftText;
        TextView totalText;
    }


    @SuppressLint("NewApi")
	public View getView(int position, View convertView, ViewGroup parent) {
        SummaryRowItem rowItem = getItem(position);

        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
        	if(position==0){
        		convertView = mInflater.inflate(R.layout.summary_list_row, null);
	            holder = new ViewHolder();
	            holder.left = (View) convertView.findViewById(R.id.left);
	            holder.right = (View) convertView.findViewById(R.id.right);
	            holder.rightText = (TextView) convertView.findViewById(R.id.right_text);
	            holder.leftText = (TextView) convertView.findViewById(R.id.left_text);
//	            holder.totalText = (TextView) convertView.findViewById(R.id.total);
	            convertView.setTag(holder);
        	}else{
	            convertView = mInflater.inflate(R.layout.notification_list_row, null);
	            holder = new ViewHolder();
	            holder.title = (TextView) convertView.findViewById(R.id.title);
	            holder.description = (TextView) convertView.findViewById(R.id.description);
	            convertView.setTag(holder);
        	}
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        
        if(position==0){
	        holder.rightText.setText(rowItem.getDebit()+"€");
	        holder.leftText.setText(rowItem.getCredit()+"€");
	        holder.rightText.setOnClickListener(debitTextClickHandler);
	        holder.leftText.setOnClickListener(creditTextClickHandler);
//	        holder.totalText.setText(rowItem.getTotal()+"€");
	        
	        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) holder.left.getLayoutParams();
	        params.height = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, rowItem.getCreditDrawableHeight(), convertView.getResources().getDisplayMetrics()));
	        holder.left.setLayoutParams(params);
	        Log.d("CreditDrawableHeight", Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, rowItem.getCreditDrawableHeight(), convertView.getResources().getDisplayMetrics()))+"");
	        
	        params = (RelativeLayout.LayoutParams) holder.right.getLayoutParams();
	        params.height = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, rowItem.getDebitDrawableHeight(), convertView.getResources().getDisplayMetrics()));
	        holder.right.setLayoutParams(params);
	        Log.d("DebitDrawableHeight", Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, rowItem.getDebitDrawableHeight(), convertView.getResources().getDisplayMetrics()))+"");
	        
        }else{
	        holder.title.setText(rowItem.getTitle());
	        holder.description.setText(rowItem.getDesc());
        }
        
        WelcomeActivity.dialog.dismiss();
	    Log.d("SummaryFragment","ProgressDialog dismissed");

        return convertView;
    }
	
	View.OnClickListener debitTextClickHandler = new View.OnClickListener() {
	    public void onClick(View v) {
	    	Log.d("SummaryAdapter","rightText clicked");
        	Intent intent = new Intent((MainActivity)context, DebtsActivity.class);
    		ArrayList parcellist = new ArrayList<Article>();
        	Bundle debts = new Bundle();
        	for(HashMap<String,String> debt : debts_list){
        		Parcelable parcel = new Article(debt.get("ID"),debt.get("balance"),debt.get("username"));
        		parcellist.add(parcel);
        	}
        	debts.putParcelableArrayList("articles", parcellist);
          	intent.putExtras(debts);
        	context.startActivity(intent);
	    }
	  };
	
	  View.OnClickListener creditTextClickHandler = new View.OnClickListener() {
		    public void onClick(View v) {
		    	Log.d("SummaryAdapter","leftText clicked");
		    	Intent intent = new Intent((MainActivity)context, CreditsActivity.class);
	    		ArrayList parcellist = new ArrayList<Article>();
	        	Bundle credits = new Bundle();
	        	for(HashMap<String,String> credit : credits_list){
	        		Parcelable parcel = new Article(credit.get("ID"),credit.get("balance"),credit.get("username"));
	        		parcellist.add(parcel);
	        	}
	        	credits.putParcelableArrayList("articles", parcellist);
	          	intent.putExtras(credits);
	        	context.startActivity(intent);
		    }
		  };
}