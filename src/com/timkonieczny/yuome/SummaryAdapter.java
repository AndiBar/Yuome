package com.timkonieczny.yuome;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

public class SummaryAdapter extends ArrayAdapter<SummaryRowItem> {

    Context context;

    public SummaryAdapter(Context context, int resourceId, List<SummaryRowItem> items) {
        super(context, resourceId, items);
        this.context = context;
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
        ViewHolder holder;
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
	        holder.rightText.setText(rowItem.getCredit()+"€");
	        holder.leftText.setText(rowItem.getDebit()+"€");
//	        holder.totalText.setText(rowItem.getTotal()+"€");
	        
	        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) holder.right.getLayoutParams();
	        params.height = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, rowItem.getCreditDrawableHeight(), convertView.getResources().getDisplayMetrics()));
	        holder.right.setLayoutParams(params);
	        Log.d("CreditDrawableHeight", Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, rowItem.getCreditDrawableHeight(), convertView.getResources().getDisplayMetrics()))+"");
	        params = (RelativeLayout.LayoutParams) holder.left.getLayoutParams();
	        params.height = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, rowItem.getDebitDrawableHeight(), convertView.getResources().getDisplayMetrics()));
	        holder.left.setLayoutParams(params);
	        Log.d("DebitDrawableHeight", Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, rowItem.getDebitDrawableHeight(), convertView.getResources().getDisplayMetrics()))+"");
	        
        }else{
	        holder.title.setText(rowItem.getTitle());
	        holder.description.setText(rowItem.getDesc());
        }
        
        

        return convertView;
    }
}