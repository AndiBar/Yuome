package com.timkonieczny.yuome;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.apache.http.client.ClientProtocolException;


public class ManualInputActivity extends ListActivity{
    public static SimpleAdapter mAdapter;
    public static ArrayList<HashMap<String,String>> article_list = new ArrayList<HashMap<String,String>>();
    public static double balance_value;

    public AlertDialogs dialogs;
     
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_input);
        
    	
        setTitle("Artikel");
        
        		balance_value = 0.0;
    	        
    	        TextView text = (TextView) findViewById(R.id.text4);
    	        text.setText(String.valueOf(balance_value) + "€   ");
    	        
    	        mAdapter = new SimpleAdapter(this,
    	        		article_list,
    	        		 R.layout.row_articles,
                         new String[] {"article", "price"},
                         new int[] {R.id.text1,
                                 R.id.text2});
    	        

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
                                    balance_value = balance_value - Double.parseDouble(article.get("price"));
                                    balance_value = Math.round(balance_value * 100) / 100.;
                                    TextView text = (TextView) findViewById(R.id.text4);
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
    public static void addArticle(String title, String price, Activity activity){
    	HashMap<String, String> depts = new HashMap<String, String>();
    	depts.put("article", title);
    	depts.put("price", price);
    	article_list.add(depts);

    	balance_value = balance_value + Double.parseDouble(price);
        balance_value = Math.round(balance_value * 100) / 100.;
        
        TextView text = (TextView) activity.findViewById(R.id.text4);
        text.setText(String.valueOf(balance_value) + "€   ");
    	mAdapter.notifyDataSetChanged();
    }
    public static void deleteAllArticles(Activity activity){
    	article_list.removeAll(article_list);
    	balance_value = 0.0;
        TextView text = (TextView) activity.findViewById(R.id.text4);
        text.setText(String.valueOf(balance_value) + "€   ");
    	mAdapter.notifyDataSetChanged();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.article_input, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
        case R.id.action_addbuy:
        	Intent intent = new Intent(this, ChoseContactsActivity.class);
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
}