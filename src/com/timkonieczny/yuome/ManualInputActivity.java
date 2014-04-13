package com.timkonieczny.yuome;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class ManualInputActivity extends ListActivity{
    public static SimpleAdapter mAdapter;
    public static ArrayList<HashMap<String,String>> article_list = new ArrayList<HashMap<String,String>>();
    public static double balance_value;

    public AlertDialogs dialogs;
    
    private TextView mTextView;
    private ListView mListView;
     
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_input);
        setTitle("Artikel");
        
		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		
		SearchView searchView = (SearchView) findViewById(R.id.search_view);
		searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
		searchView.setIconifiedByDefault(false);
		
		mTextView = (TextView) findViewById(R.id.text);
        mListView = (ListView) findViewById(R.id.dynamic_list);
        
        
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
        	ArrayList parcellist = new ArrayList<Article>();
        	Bundle articles = new Bundle();
        	for(HashMap<String,String> article : article_list){
        		Parcelable parcel = new Article(article.get("article"),article.get("price"));
        		parcellist.add(parcel);
        	}
        	articles.putParcelableArrayList("articles", parcellist);
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
    
    @Override
    protected void onNewIntent(Intent intent) {
        // Because this activity has set launchMode="singleTop", the system calls this method
        // to deliver the intent if this activity is currently the foreground activity when
        // invoked again (when the user executes a search from this activity, we don't create
        // a new instance of this activity, so the system delivers the search intent here)
    	
    	System.out.println("onNewIntent called");
    	
        if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            // handles a click on a search suggestion; launches activity to show word
            Intent wordIntent = new Intent(this, WordActivity.class);   //TODO: Was passiert, wenn in der Liste ein Eintrag ausgewählt wird?
            wordIntent.setData(intent.getData());
            startActivity(wordIntent);
        } else if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            // handles a search query
            String query = intent.getStringExtra(SearchManager.QUERY);
            showResults(query);
        }
    }
    
    private void showResults(String query) {

        Cursor cursor = managedQuery(DictionaryProvider.CONTENT_URI, null, null, new String[] {query}, null);
        
        System.out.println("showResults called.");
        
        if (cursor == null) {
            // There are no results
            mTextView.setText("Kein Ergebnis gefunden");
        } else {
            // Specify the columns we want to display in the result
            String[] from = new String[] { DictionaryDatabase.KEY_WORD,
                                           DictionaryDatabase.KEY_DEFINITION };

            // Specify the corresponding layout elements where we want the columns to go
            int[] to = new int[] { R.id.word,
                                   R.id.definition };

            // Create a simple cursor adapter for the definitions and apply them to the ListView
            SimpleCursorAdapter words = new SimpleCursorAdapter(this, R.layout.result, cursor, from, to);
            mListView.setAdapter(words);

            // Define the on-click listener for the list items
            mListView.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    // Build the Intent used to open WordActivity with a specific word Uri
                    Intent wordIntent = new Intent(getApplicationContext(), WordActivity.class);
                    Uri data = Uri.withAppendedPath(DictionaryProvider.CONTENT_URI,
                                                    String.valueOf(id));
                    wordIntent.setData(data);
                    startActivity(wordIntent);
                }
            });
        }
    }
}