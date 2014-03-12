package com.timkonieczny.yuome;

import android.app.ListActivity;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class ManualInputActivity extends ListActivity{
    ArrayAdapter<String> mAdapter;
    private PopupWindow popupMessage;
     
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_input);
        
        final SimpleAdapter mAdapter;
        
    	final ArrayList<HashMap<String,String>> depts_list = new ArrayList<HashMap<String,String>>();
        setTitle("Artikel");
        
    	        // Set up ListView example
    	        String[] articles = new String[]{};
    	        Double[] balance = new Double[]{};
    	        
    	        double balance_value = 0.0;
    	        for(Double value : balance){
    	        	balance_value = balance_value + value;
    	        }
    	        balance_value = Math.round(balance_value * 100) / 100.;
    	        
    	        TextView text = (TextView) findViewById(R.id.text4);
    	        text.setText(String.valueOf(balance_value) + "€   ");
    	        
    	        for(int index = 0; index < articles.length; index++){
    	        	HashMap<String, String> depts = new HashMap<String, String>();
    	        	depts.put("article", articles[index]);
    	        	depts.put("balance", balance[index].toString() + "€   ");
    	        	depts_list.add(depts);
    	        }
    	        
    	        mAdapter = new SimpleAdapter(this,
    	        		depts_list,
    	        		 R.layout.row_articles,
                         new String[] {"article", "balance"},
                         new int[] {R.id.text1,
                                 R.id.text2});
    	        

    	setListAdapter(mAdapter);

        ListView listView = getListView();
        
        LinearLayout layoutOfPopup = new LinearLayout(this);
        
        popupMessage = new PopupWindow(layoutOfPopup, LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View popupLayout = inflater.inflate(R.layout.popup_new_article, layoutOfPopup);
        
        popupMessage.setContentView(popupLayout);
        
        Button button = (Button) popupLayout.findViewById(R.id.new_article_button);
        button.setOnClickListener(new OnClickListener(){
    		public void onClick(View v) {
    			popupMessage.dismiss();	
    		}
        });
        
        Button new_article = (Button) findViewById(R.id.new_article);
        new_article.setOnClickListener(new OnClickListener(){
        	public void onClick(View view){
        		if(popupMessage.isShowing()){
        			popupMessage.dismiss();
        		}
        		popupMessage.showAsDropDown(view);
        		EditText title = (EditText) popupLayout.findViewById(R.id.title);
        		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(view.getRootView(), InputMethodManager.SHOW_IMPLICIT);
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
                                	HashMap<String, String> test = new HashMap<String, String>();
                                    test = (HashMap<String, String>) mAdapter.getItem(position);
                                    depts_list.remove(test);
                                }
                               mAdapter.notifyDataSetChanged();
                            }
                        });
        listView.setOnTouchListener(touchListener);
        // Setting this scroll listener is required to ensure that during ListView scrolling,
        // we don't look for swipes.
        listView.setOnScrollListener(touchListener.makeScrollListener());
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        return true;
      } 
    protected double calculateValue(ArrayList<HashMap<String, Double>> depts_list){
		return 0;
    	
    }
}