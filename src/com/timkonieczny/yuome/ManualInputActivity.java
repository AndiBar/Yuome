package com.timkonieczny.yuome;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import org.apache.http.client.ClientProtocolException;

import com.timkonieczny.yuome.ChooseContactsActivity.FriendsThread;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.format.Time;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ManualInputActivity extends ListActivity {
    public static SimpleAdapter mAdapter;
    public static ArrayList<HashMap<String,String>> article_list = new ArrayList<HashMap<String,String>>();
    public ArrayList<HashMap<String,String>> stores = new ArrayList<HashMap<String,String>>();
    public static double balance_value;
    public String date;

    public static ProgressDialog dialog = null;
    public AlertDialogs dialogs;
    
    private TextView mTextView;
    private ListView mListView;
    
    private AutoCompleteTextView mItemsTextView;
    private Spinner mStoreSpinner;
     
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_input);
        setTitle("Artikel");
        
        Time t = new Time(Time.getCurrentTimezone());
        t.setToNow();
        date = t.format("%Y.%m.%d");
        
        TextView date_view = (TextView) findViewById(R.id.date);
        date_view.setText(t.format("%d.%m.%Y"));
        
        Thread stores_thread = new StoresThread();
        stores_thread.start();
        
        try {
        	long waitMillis = 10000;
        	while (stores_thread.isAlive()) {
        	   stores_thread.join(waitMillis);
        	}
        } catch (InterruptedException e) {
        }
        
        mStoreSpinner = (Spinner) findViewById(R.id.store_spinner);
        SimpleAdapter storeAdapter = new SimpleAdapter(this,
        		stores,
        		R.layout.store_spinner_item,
                 new String[] {"title","ID"},
                 new int[] {R.id.title, R.id.storeID});
        mStoreSpinner.setAdapter(storeAdapter);
        //mStoreSpinner.setOnItemSelectedListener(this);

        //mItemsTextView = (AutoCompleteTextView) findViewById(R.id.add_item_textview);
        //adapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item, mItems);
        //mItemsTextView.setThreshold(2);
        //mItemsTextView.setAdapter(adapter);
        
		//mTextView = (TextView) findViewById(R.id.text);
        //mListView = (ListView) findViewById(R.id.dynamic_list);
                
		balance_value = 0.0;
        
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
    
    public static void addArticle(String title, String price, String amount, Activity activity){
    	HashMap<String, String> depts = new HashMap<String, String>();
    	depts.put("article", title);
    	depts.put("price", price);
    	depts.put("amount", amount);
    	article_list.add(depts);

    	balance_value = balance_value + Double.parseDouble(price) * Integer.parseInt(amount);
        balance_value = Math.round(balance_value * 100) / 100.;
        
        TextView text = (TextView) activity.findViewById(R.id.total);
        text.setText(String.valueOf(balance_value) + "€   ");
    	mAdapter.notifyDataSetChanged();
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
        					final String response = PHPConnector.getItemListResponse("http://andibar.dyndns.org/Yuome/search_item.php", Store);
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
	public class StoresThread extends Thread{
  	  public void run(){
	       	try {
				stores = PHPConnector.getStores();
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
  	  }
  }
}