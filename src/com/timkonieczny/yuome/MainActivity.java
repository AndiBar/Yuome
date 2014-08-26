package com.timkonieczny.yuome;

import com.timkonieczny.yuome.R;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class MainActivity extends Activity {
	private DrawerLayout mDrawerLayout;
	private ActionBarDrawerToggle mDrawerToggle;
	private DrawerMenuItem mMenuItems[];
	private int mMenuIcons[];
	private MenuItemAdapter adapter;
	private ListView mMainMenu;
	private String mFragmentName;
	private String[] mMainMenuItems;
	private int selectedMenu = 1;
	
	public static int height, width;



	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)	//TODO
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		width = size.x;
		height = size.y;
		
		mDrawerLayout = (DrawerLayout) findViewById(R.id.DrawerLayout);
		mMainMenu = (ListView)findViewById(R.id.MainMenu);
		mMainMenuItems = getResources().getStringArray(R.array.main_menu);
		mFragmentName = mMainMenuItems[1];
		
		mMenuItems = new DrawerMenuItem[5];
		
		mMenuIcons = new int[]{
			R.drawable.ic_action_camera,
			R.drawable.summary,
			R.drawable.receipts,
			R.drawable.ic_action_group,
			R.drawable.ic_action_settings
		};
		
		for(int i=0; i<mMenuItems.length;i++){
			mMenuItems[i]=new DrawerMenuItem(mMenuIcons[i], mMainMenuItems[i]);
		}
		
		adapter = new MenuItemAdapter(this, R.layout.main_menu_item, mMenuItems);
		

        mMainMenu.setAdapter(adapter);
		
		mMainMenu.setOnItemClickListener(
				new DrawerItemClickListener()
		);

		// enable ActionBar app icon to behave as action to toggle nav drawer
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		// ActionBarDrawerToggle ties together the the proper interactions
		// between the sliding drawer and the action bar app icon
		mDrawerToggle = new ActionBarDrawerToggle(
				this, /* host Activity */
				mDrawerLayout, /* DrawerLayout object */
				R.drawable.ic_drawer, /* nav drawer image to replace 'Up' caret */
				R.string.drawer_open, /* "open drawer" description for accessibility */
				R.string.drawer_close /* "close drawer" description for accessibility */
		) {
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(mFragmentName);
				invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
			}

			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle("Yuome");
				invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);
		
		Intent intent = getIntent();
		selectedMenu = intent.getIntExtra("selectedMenu", 1); // Menue aus voriger Activity beziehen, falls gesetzt
		
		if (savedInstanceState == null) { // ersten Screen auswählen
			selectMenuItem(selectedMenu);		
		}
		
		if(getIntent().getExtras()!=null){	// zu öffnendes Fragment wenn in einer anderen Activity der Back-Button gedrückt wird
			int drawerPosition = getIntent().getExtras().getInt("drawerPosition");
			Log.d("MainActivity", "DrawerPosition: "+drawerPosition);
			openInputFragment(drawerPosition);
		}else{
			mDrawerLayout.openDrawer(mMainMenu); // Beim Erstellen der Activity wird der Drawer geöffnet (wird nicht geöffnet, wenn woanders Back gedrückt wird)
		}
		
//		WelcomeActivity.dialog.dismiss();
	}
	


	

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {			//Handler für das Öffnen des Drawers bei Drücken des Up-Buttons
        if (mDrawerLayout.isDrawerVisible(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
            getActionBar().setTitle(mFragmentName);
        } else {
            mDrawerLayout.openDrawer(GravityCompat.START);
            getActionBar().setTitle("Yuome");
        }
        return false;
	}
	
	@Override
	public void onBackPressed() {
		if (mDrawerLayout.isDrawerVisible(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }
	}

	/* The click listener for ListView in the navigation drawer */
	private class DrawerItemClickListener implements ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			selectMenuItem(position);
		}
	}
	

	private void selectMenuItem(int position) { // Auswahl der Fragments passend zum ListItem
		if (position == 0) { // Eingabeauswahl wird geöffnet
			openInputFragment(position);
			mDrawerLayout.closeDrawer(mMainMenu);
		}
		if (position == 1) { // Schuldenübersicht
			openSummaryFragment(position);
			mDrawerLayout.closeDrawer(mMainMenu);
		}
		if (position == 2) { // Meine Kassenzettel
			openReceiptsFragment(position);
			mDrawerLayout.closeDrawer(mMainMenu);
		}
		if (position == 3) { // Kontakte
			openContactsFragment(position);
			mDrawerLayout.closeDrawer(mMainMenu);
		}
		if (position == 4) { // Kontoeinstellungen
			openSettingsFragment(position);
			mDrawerLayout.closeDrawer(mMainMenu);
		}
	}
	
	private void openInputFragment(int position){
		Fragment fragment = new InputFragment();
		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
		
		// update selected item and title, then close the drawer
		mMainMenu.setItemChecked(position, true);
		mFragmentName=mMainMenuItems[position];
		getActionBar().setTitle(mFragmentName);
	}
	
	private void openSummaryFragment(int position){
		Fragment fragment = new SummaryFragment();
		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

		// update selected item and title, then close the drawer
		mMainMenu.setItemChecked(position, true);
		mFragmentName=mMainMenuItems[position];
		getActionBar().setTitle(mFragmentName);
	}
	
	private void openReceiptsFragment(int position){
		Fragment fragment = new ReceiptsFragment();
		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

		mMainMenu.setItemChecked(position, true);
		mFragmentName=mMainMenuItems[position];
		getActionBar().setTitle(mFragmentName);
	}
	
	private void openContactsFragment(int position){
		selectedMenu = 3;
		Fragment fragment = new ContactsFragment();
		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

		mMainMenu.setItemChecked(position, true);
		mFragmentName=mMainMenuItems[position];
		getActionBar().setTitle(mFragmentName);
	}
	
	private void openSettingsFragment(int position){
		Fragment fragment = new SettingsFragment();
		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

		mMainMenu.setItemChecked(position, true);
		mFragmentName=mMainMenuItems[position];
		getActionBar().setTitle(mFragmentName);
	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggle
		mDrawerToggle.onConfigurationChanged(newConfig);
	}
}