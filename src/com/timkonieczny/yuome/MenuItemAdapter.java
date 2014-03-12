package com.timkonieczny.yuome;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MenuItemAdapter extends ArrayAdapter<DrawerMenuItem> {
	private Context mContext;
	private int mLayoutResourceId;
	private DrawerMenuItem mMenuItems[] = null;
	private View mRow;
	private DrawerMenuItemHolder mHolder;
	private LayoutInflater mInflater;
	DrawerMenuItem mDrawerMenuItem;

	public MenuItemAdapter(Context context, int layoutResourceId, DrawerMenuItem[] menuItems) {
		super(context, layoutResourceId, menuItems);
		this.mLayoutResourceId = layoutResourceId;
		this.mContext = context;
		this.mMenuItems = menuItems;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		mRow = convertView;
		mHolder = null;

		if (mRow == null) {
			mInflater = ((Activity) mContext).getLayoutInflater();
			mRow = mInflater.inflate(mLayoutResourceId, parent, false);

			mHolder = new DrawerMenuItemHolder();
			mHolder.icon = (ImageView) mRow.findViewById(R.id.Icon);
			mHolder.menuItemName = (TextView) mRow.findViewById(R.id.MenuItem);

			mRow.setTag(mHolder);
		} else {
			mHolder = (DrawerMenuItemHolder) mRow.getTag();
		}

		mDrawerMenuItem = mMenuItems[position];
		mHolder.menuItemName.setText(mDrawerMenuItem.menuItemName);
		mHolder.icon.setImageResource(mDrawerMenuItem.icon);

		return mRow;
	}

	static class DrawerMenuItemHolder {
		ImageView icon;
		TextView menuItemName;
	}
}
