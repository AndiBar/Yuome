package com.timkonieczny.yuome;

import java.io.IOException;
import java.text.DecimalFormat;

import org.apache.http.client.ClientProtocolException;

import com.timkonieczny.yuome.ChooseContactsActivity.FriendsThread;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;


import android.view.WindowManager;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

public class SummaryFragment extends Fragment {
	
	private int width, height;
	public String balance;
	
    public SummaryFragment(){
    	
    	Thread balance_thread = new BalanceThread();
        balance_thread.start();
        
        try {
        	long waitMillis = 10000;
        	while (balance_thread.isAlive()) {
        	   balance_thread.join(waitMillis);
        	}
        } catch (InterruptedException e) {
        	}
		
    }
    
	@SuppressLint("NewApi")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.fragment_summary, container, false);
		
		Context context = getActivity();
        Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        Point size = new Point();
        display.getSize(size);
        width = size.x;
        height = size.y;

        TypedValue tv = new TypedValue();
        int actionBarHeight=0;
        if (context.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true))
        {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data,getResources().getDisplayMetrics());
        }

        height -= actionBarHeight;
        System.out.println("h="+height);
		
		float debt = Float.parseFloat(balance.split(",")[1]);		// TODO: Beispielwerte für Schulden und Guthaben
		float credit = Float.parseFloat(balance.split(",")[0]);
		
		float bottomEnd=height;
		
		float debtTopEnd = calculateDiagram(debt, credit);
		float creditTopEnd = calculateDiagram(credit, debt);
		
		System.out.println("debtTop="+debtTopEnd);
		System.out.println("creditTop="+creditTopEnd);		
		System.out.println("screenHeight="+height);
		System.out.println("bottom="+bottomEnd);

		System.out.println("Balance:" + balance);
		
		final View leftLayout = rootView.findViewById(R.id.left);
		final View rightLayout =  rootView.findViewById(R.id.right);		
		
		Paint red = new Paint();
		Paint green = new Paint();
		green.setColor(Color.parseColor("#15ff2b"));
        red.setColor(Color.parseColor("#CD5C5C"));
        
        
        Bitmap leftBitmap = Bitmap.createBitmap((int) (width/2), (int) (height/2), Bitmap.Config.ARGB_8888);	//TODO: Bitmaps zu hoch
        Bitmap rightBitmap = Bitmap.createBitmap((int) (width/2), (int) (height/2), Bitmap.Config.ARGB_8888);
        
        Canvas leftCanvas = new Canvas(leftBitmap);
        Canvas rightCanvas = new Canvas(rightBitmap);
        
        leftCanvas.drawRect(0.1f*width/2, creditTopEnd*height/2, 0.95f*width/2, height/2, green);	// Es wird vom Layout ausgegangen, nicht vom screen
        rightCanvas.drawRect(0.05f*width/2, debtTopEnd*height/2, 0.9f*width/2, height/2, red);
        
        if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
        	leftLayout.setBackgroundDrawable(new BitmapDrawable(this.getResources(),leftBitmap));
        	rightLayout.setBackgroundDrawable(new BitmapDrawable(this.getResources(),rightBitmap));
        } else {
        	leftLayout.setBackground(new BitmapDrawable(this.getResources(),leftBitmap));
        	rightLayout.setBackground(new BitmapDrawable(this.getResources(),rightBitmap));
        };
        
        TextView creditText = (TextView)rootView.findViewById(R.id.left_text);
        TextView debtText = (TextView)rootView.findViewById(R.id.right_text);
        TextView totalText = (TextView)rootView.findViewById(R.id.total);
        
        debtText.setOnClickListener(new OnClickListener(){
        	public void onClick(View v){
        		Intent intent = new Intent(getActivity(), DebtsActivity.class);
	        	startActivity(intent);
        	}
        });
        
        creditText.setText(balance.split(",")[0]+"€");
        debtText.setText(balance.split(",")[1]+"€");
        Double balance_value = Math.round((credit-debt) * 100) / 100.;
        totalText.setText(balance_value.toString()+"€");
        
		return rootView;
	}
									// haupt...
	private float calculateDiagram(float money1, float money2){
		
		if(money1>=10.00f){
			if(money1>=money2){
				return 0.0f;		// eig 0		
			}else{
				return (1-(money1/money2));
			}
		}else{
			return (1-(0.1f*money1));
		}
	}
	public class BalanceThread extends Thread{
		
		public void run() {
			try {
				balance = PHPConnector.getResponse("get_balance.php");
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