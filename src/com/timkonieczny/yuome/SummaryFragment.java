package com.timkonieczny.yuome;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class SummaryFragment extends Fragment {
	
    public SummaryFragment(){
    	
    }
    
	@SuppressLint("NewApi")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.fragment_summary, container, false);
		
		Paint paint = new Paint();
        paint.setColor(Color.parseColor("#CD5C5C"));
        Bitmap bg = Bitmap.createBitmap(480, 800, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bg);
        canvas.drawRect(50, 50, 200, 200, paint);
        LinearLayout ll = (LinearLayout) rootView.findViewById(R.id.rect);
        ll.setBackgroundDrawable(new BitmapDrawable(bg));
        
        float debt=15.00f;		// TODO: Beispielwerte für Schulden und Guthaben
		float credit=30.00f;
		
		float bottomEnd=0.9f*MainActivity.height;
		float debtTopEnd = calculateDiagram(credit, debt);
		float creditTopEnd = calculateDiagram(debt, credit);

		
		final LinearLayout leftLayout = (LinearLayout) rootView.findViewById(R.id.left);
		final LinearLayout rightLayout = (LinearLayout) rootView.findViewById(R.id.right);
		
		
		Paint red = new Paint();
		Paint green = new Paint();
		green.setColor(Color.parseColor("#15ff2b"));
        red.setColor(Color.parseColor("#CD5C5C"));
        Bitmap leftBitmap = Bitmap.createBitmap(MainActivity.width/2, MainActivity.height, Bitmap.Config.ARGB_8888);
        Bitmap rightBitmap = Bitmap.createBitmap(MainActivity.width/2, MainActivity.height, Bitmap.Config.ARGB_8888);
        Canvas leftCanvas = new Canvas(leftBitmap);
        Canvas rightCanvas = new Canvas(rightBitmap);
        leftCanvas.drawRect(0.05f*MainActivity.width, creditTopEnd, 0.45f*MainActivity.width, bottomEnd, green);
        rightCanvas.drawRect(0.05f*MainActivity.width, debtTopEnd, 0.45f*MainActivity.width, bottomEnd, red);
        
        if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
        	leftLayout.setBackgroundDrawable(new BitmapDrawable(this.getResources(),leftBitmap));
        	rightLayout.setBackgroundDrawable(new BitmapDrawable(this.getResources(),rightBitmap));
        } else {
        	leftLayout.setBackground(new BitmapDrawable(this.getResources(),leftBitmap));
        	rightLayout.setBackground(new BitmapDrawable(this.getResources(),rightBitmap));
        };
        
		return rootView;
	}

	private float calculateDiagram(float money1, float money2){
		
		if(money1>=10.00f){
			if(money2>=money1){
				return 0.1f*MainActivity.height;				
			}else{
				return (money2/money1)*0.9f*MainActivity.height;
			}
		}else{
			return (1-(0.1f*money2))*0.9f*MainActivity.height;
		}
	}
}