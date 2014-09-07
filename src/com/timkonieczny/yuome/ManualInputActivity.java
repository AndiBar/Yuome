package com.timkonieczny.yuome;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import org.apache.http.client.ClientProtocolException;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.format.Time;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class ManualInputActivity extends ListActivity {
    public static SimpleAdapter mAdapter;
    public static SimpleAdapter storeAdapter;
    public static ArrayList<HashMap<String,String>> article_list = new ArrayList<HashMap<String,String>>();
    public static ArrayList<HashMap<String,String>> stores = new ArrayList<HashMap<String,String>>();
    public static double balance_value = 0.0;
    public static String date;
    public static Time time;
    public static Spinner mStoreSpinner;

    public static ProgressDialog dialog = null;
    public static AlertDialogs dialogs;
    public static TextView date_view;
    
    private TextView mTextView;
    private ListView mListView;
    
    private static Context context;
    private static Activity activity;
    
    private AutoCompleteTextView mItemsTextView;
     
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_input);
        setTitle("Artikel");
        context = getApplicationContext();
        activity = this;
        
        time = new Time(Time.getCurrentTimezone());
        time.setToNow();
        date = time.format("%Y.%m.%d");
        
        date_view = (TextView) findViewById(R.id.date);
        date_view.setText(time.format("%d.%m.%Y"));
        
        date_view.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				dialogs.changeDateDialog();	
			}
        	
        });
        
        Thread stores_thread = new StoresThread();
        stores_thread.start();
        
        try {
        	long waitMillis = 10000;
        	while (stores_thread.isAlive()) {
        	   stores_thread.join(waitMillis);
        	}
        } catch (InterruptedException e) {
        }
        
        Collections.sort(stores, new MapComparator("title"));
        
        HashMap<String,String> other_store = new HashMap<String,String>();
        other_store.put("ID","0");
        other_store.put("title","Sonstige");
        stores.add(other_store);
        
        mStoreSpinner = (Spinner) findViewById(R.id.store_spinner);
        storeAdapter = new SimpleAdapter(this,
        		stores,
        		R.layout.store_spinner_item,
                 new String[] {"title","ID"},
                 new int[] {R.id.title, R.id.storeID});
        mStoreSpinner.setAdapter(storeAdapter);
        
        mStoreSpinner.setOnItemSelectedListener(new OnItemSelectedListener(){

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				if(arg2 == mStoreSpinner.getCount()-1){
					mStoreSpinner.setSelection(0);
					dialogs.newStoreDialog();
				}	
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}	
        });
                        
        TextView text = (TextView) findViewById(R.id.total);
        text.setText(String.valueOf(balance_value) + "€   ");
        
        mAdapter = new SimpleAdapter(this,
        		article_list,
        		 R.layout.row_articles,
                 new String[] {"article","amount", "price"},
                 new int[] {R.id.row_title,R.id.row_amount,R.id.row_price});

    	setListAdapter(mAdapter);

        ListView listView = getListView();
        
        dialogs = new AlertDialogs(this);
        
        Button new_article = (Button) findViewById(R.id.new_article);
        new_article.setOnClickListener(new OnClickListener(){
        	public void onClick(View view){	
                dialogs.newArticleDialog();   
        	}
        });
        Button delete_articles = (Button) findViewById(R.id.delete_articles);
        delete_articles.setOnClickListener(new OnClickListener(){
        	public void onClick(View view){
        		dialogs.deleteAllArticlesDialog();
        	}
        });
        // Create a ListView-specific touch listener. ListViews are given special treatment because
        // by default they handle touches for their list items... i.e. they're in charge of drawing
        // the pressed state (the list selector), handling list item clicks, etc.
        SwipeDismissListViewTouchListener touchListener =
                new SwipeDismissListViewTouchListener(
                        listView,
                        new SwipeDismissListViewTouchListener.DismissCallbacks() {
                            @Override
                            public boolean canDismiss(int position) {
                                return true;
                            }

                            @Override
                            public void onDismiss(ListView listView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {
                                	HashMap<String, String> article = new HashMap<String, String>();
                                    article = (HashMap<String, String>) mAdapter.getItem(position);
                                    article_list.remove(article);
                                    balance_value = balance_value - Double.parseDouble(article.get("price")) * Integer.parseInt(article.get("amount"));
                                    balance_value = Math.round(balance_value * 100) / 100.;
                                    TextView text = (TextView) findViewById(R.id.total);
                                    text.setText(String.valueOf(balance_value) + "€   ");
                                }
                               mAdapter.notifyDataSetChanged();
                            }
                        });
        listView.setOnTouchListener(touchListener);
        // Setting this scroll listener is required to ensure that during ListView scrolling,
        // we don't look for swipes.
        listView.setOnScrollListener(touchListener.makeScrollListener());
    }
    
    public static void addArticle(String title, String price, String amount, Activity activity) throws NumberFormatException, ArgumentNullException{
    	balance_value = balance_value + Double.parseDouble(price) * Integer.parseInt(amount);
        balance_value = Math.round(balance_value * 100) / 100.;
        
        if(title.equals("")){
        	throw new ArgumentNullException("Kein Artikelname angegeben.");
        }
        else{
	        HashMap<String, String> depts = new HashMap<String, String>();
	    	depts.put("article", title);
	    	depts.put("price", price);
	    	depts.put("amount", amount);
	    	article_list.add(depts);
	        
	        TextView text = (TextView) activity.findViewById(R.id.total);
	        text.setText(String.valueOf(balance_value) + "€   ");
	    	mAdapter.notifyDataSetChanged();
        }
    }
    
    public static void addStore(String title){
    	  Thread add_store_thread = new AddStoreThread(title);
          add_store_thread.start();
          try {
          	long waitMillis = 10000;
          	while (add_store_thread.isAlive()) {
          	   add_store_thread.join(waitMillis);
          	}
          } catch (InterruptedException e) {
          }
         
          Thread stores_thread = new StoresThread();
          stores_thread.start();
          
          try {
          	long waitMillis = 10000;
          	while (stores_thread.isAlive()) {
          	   stores_thread.join(waitMillis);
          	}
          } catch (InterruptedException e) {
          }
          
          Collections.sort(stores, new MapComparator("title"));
          
          HashMap<String,String> other_store = new HashMap<String,String>();
          other_store.put("ID","0");
          other_store.put("title","Sonstige");
          stores.add(other_store);
          
          storeAdapter = new SimpleAdapter(context,
          		stores,
          		R.layout.store_spinner_item,
                   new String[] {"title","ID"},
                   new int[] {R.id.title, R.id.storeID});
          
          mStoreSpinner.setAdapter(storeAdapter);
          
          Integer position = 0;
          
          for(int i = 0; i < stores.size(); i++){
        	  if(stores.get(i).get("title").equals(title)){
        		  position = i;
        	  }
          }
          
          mStoreSpinner.setSelection(position);
    }
    
    @SuppressLint("SimpleDateFormat")
	public static void changeDate(Calendar new_date){
    	SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
    	if(time.toMillis(true) >= new_date.getTimeInMillis()){
    		date_view.setText(format.format(new Date(new_date.getTimeInMillis())).toString());
	    	SimpleDateFormat int_format = new SimpleDateFormat("yyyy-MM-dd");
	    	date = int_format.format(new Date(new_date.getTimeInMillis()));
	    	System.out.println("erfolg");
    	}
    	else{
    		AlertDialogs.showAlert(activity, "Fehler", "Das angegebene Datum ist später als das aktuelle Datum.");
    	}
    }
    
    public static void deleteAllArticles(Activity activity){
    	article_list.removeAll(article_list);
    	balance_value = 0.0;
        TextView text = (TextView) activity.findViewById(R.id.total);
        text.setText(String.valueOf(balance_value) + "€   ");
    	mAdapter.notifyDataSetChanged();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.receipt_postprocessing, menu);
        return true;
    }
    
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
        case R.id.action_addbuy:
        	dialog = ProgressDialog.show(ManualInputActivity.this, "","Abrufen der Kontakte", true);
        	Intent intent = new Intent(this, ChooseContactsActivity.class);
        	ArrayList parcellist = new ArrayList<Article>();
        	Bundle articles = new Bundle();
        	for(HashMap<String,String> article : article_list){
        		Parcelable parcel = new Article(article.get("article"),article.get("price"),article.get("amount"));
        		parcellist.add(parcel);
        	}
        	articles.putParcelableArrayList("articles", parcellist);
        	HashMap<String,String> store_map = (HashMap<String, String>) mStoreSpinner.getSelectedItem();
        	articles.putString("storeID", store_map.get("ID"));
        	articles.putString("date", date);
        	articles.putDouble("total", balance_value);
          	intent.putExtras(articles);
            startActivity(intent);
          break;
        
        default:
          break;
        }

        return true;
      } 
    
    protected double calculateValue(ArrayList<HashMap<String, Double>> depts_list){
		return 0;
    	
    }

	//@Override
	//public void onItemSelected(AdapterView<?> arg0, View arg1, int pos, long arg3) {	//Auswertung des Spinners
	//	mTextView.setText(mStores[pos]);
	//	getItemList(mStores[pos]);
	//}

	//@Override
	//public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	//}
	
	private void getItemList(String store){
		System.out.println("getItemList");
		final String Store=store;
		new Thread(
        		new Runnable(){
        			public void run(){		//TODO: wirft immer noch Exception
        				try{
        					final String response = PHPConnector.getItemListResponse(Store);
        					System.out.println("Response : " + response);
        				}catch(Exception e){
        					runOnUiThread(new Runnable() {
        		                 public void run() {
        		                     Toast.makeText(ManualInputActivity.this,"Connection failed", Toast.LENGTH_SHORT).show();
        		                 }
        		             });
        				}
        			}
        		}
        ).start();
		
	   	 try{
			 
	     }catch(Exception e){
	         
		 }
    }
		
	public void onBackPressed(){
		Log.d("ManualInputActivity", "BackButton pressed");
		Intent intent = new Intent(this, MainActivity.class);
		intent.putExtra("drawerPosition", 0);
		startActivity(intent);
		
//		Fragment fragment = new SummaryFragment();
//		FragmentManager fragmentManager = getFragmentManager();
//		fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

		// update selected item and title, then close the drawer
//		mMainMenu.setItemChecked(position, true);
//		mFragmentName=mMainMenuItems[position];
//		getActionBar().setTitle(mFragmentName);
//		mDrawerLayout.closeDrawer(mMainMenu);
	}
}

class AddStoreThread extends Thread{
	
	  private String title;
    
	  public AddStoreThread(String title){
  	  this.title = title;
    }
	
	  public void run(){
	       	try {
				PHPConnector.addStore(title);
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	  }
}

class StoresThread extends Thread{
	  public void run(){
	       	try {
				ManualInputActivity.stores = PHPConnector.getData("get_stores.php");
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	  }
}