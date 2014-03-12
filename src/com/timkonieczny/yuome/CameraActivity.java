package com.timkonieczny.yuome;

import java.io.File;
import java.io.IOException;

import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.Menu;
import android.widget.ImageView;

public class CameraActivity extends Activity {

	static final int REQUEST_IMAGE_CAPTURE = 1;
	private ImageView mImageView;
	private File file;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_camera);
		mImageView = (ImageView) findViewById(R.id.ImageView);
		File photoFile=null;
		try {
			photoFile=createImageFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		if (photoFile != null) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
        }
		
//		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//	    if (intent.resolveActivity(getPackageManager()) != null) {
//	        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
//	    }
	}
	
	private File createImageFile() throws IOException{
		file = getExternalFilesDir(null);
		File image = File.createTempFile(
		        "receipt",  /* prefix */
		        ".jpg",         /* suffix */
		        file      /* directory */
		);
		return image;
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.camera, menu);
		return true;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
			System.out.println("blablabla");
	        Bundle extras = data.getExtras();
	        Bitmap imageBitmap = (Bitmap) extras.get("data");
	        mImageView.setImageBitmap(imageBitmap);
	    }
	    if (resultCode==-1){
	    }
	}

}
