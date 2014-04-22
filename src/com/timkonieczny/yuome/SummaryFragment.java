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
		
		final LinearLayout linearLayout = (LinearLayout) rootView.findViewById(R.id.rect);
		
		
		Paint paint = new Paint();
        paint.setColor(Color.parseColor("#CD5C5C"));
        Bitmap bitmap = Bitmap.createBitmap(MainActivity.width, MainActivity.height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawRect(0.1f*MainActivity.width, 0.1f*MainActivity.height, 0.45f*MainActivity.width, 0.9f*MainActivity.height, paint);
        canvas.drawRect(0.55f*MainActivity.width, 0.1f*MainActivity.height, 0.9f*MainActivity.width, 0.9f*MainActivity.height, paint);
        
        
        if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
        	linearLayout.setBackgroundDrawable(new BitmapDrawable(this.getResources(),bitmap));
        } else {
        	linearLayout.setBackground(new BitmapDrawable(this.getResources(),bitmap));
        };
        
		return rootView;
	}
}