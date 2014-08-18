package com.timkonieczny.yuome;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class NotificationAdapter extends ArrayAdapter<NotificationRowItem> {

    Context context;

    public NotificationAdapter(Context context, int resourceId, List<NotificationRowItem> items) {
        super(context, resourceId, items);
        this.context = context;
    }

    public class ViewHolder {
        ImageView image;
        TextView title;
        TextView description;
    }


    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        NotificationRowItem rowItem = getItem(position);

        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.notification_list_row, null);
            holder = new ViewHolder();
            holder.title = (TextView) convertView.findViewById(R.id.title);
            holder.description = (TextView) convertView.findViewById(R.id.description);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.title.setText(rowItem.getTitle());
        holder.description.setText(rowItem.getDesc());

        return convertView;
    }
}