package com.timkonieczny.yuome;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.HashMap;


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
        inflater.inflate(R.menu.receipt_postprocessing, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
        case R.id.action_addbuy:
        	Intent intent = new Intent(this, ChooseContactsActivity.class);
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