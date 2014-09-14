package com.timkonieczny.yuome;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;

import android.os.Build;
import android.os.Bundle;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class WelcomeActivity extends Activity {
	
	Button loginButton;
    EditText editUsername,editPassword;
    TextView debugText;
    HttpPost httppost;
    StringBuffer buffer;
    HttpResponse response;
    HttpClient httpclient;
    List<NameValuePair> nameValuePairs;
    protected static ProgressDialog dialog = null;
    private static Context context;

//    public WelcomeActivity(){
//    	Log.d("WelcomeActivity","constructor");
//    }
    
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		context=getApplicationContext();
		
		if(HandleSharedPreferences.getUserCredentials(this, "username").length()==0){
			Log.d("WelcomeActivity","no SharedPreference");
		}else{
			Log.d("WelcomeActivity","SharedPreference saved");
			Log.d("WelcomeActivity","Username="+HandleSharedPreferences.getUserCredentials(this, "username")+" Password="+HandleSharedPreferences.getUserCredentials(this, "password"));
			dialog = ProgressDialog.show(WelcomeActivity.this, "","Login läuft", true);
			Thread loginThread = new Thread(
					new Runnable(){
						public void run(){
							userLogin(HandleSharedPreferences.getUserCredentials(context, "username"),HandleSharedPreferences.getUserCredentials(context, "password"),WelcomeActivity.this);
						}
					}
			);
			loginThread.start();
			try {
				loginThread.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		setTheme(android.R.style.Theme_Holo_NoActionBar);
		setContentView(R.layout.activity_welcome);
		
		

		loginButton = (Button)findViewById(R.id.LoginButton);
        editUsername = (EditText)findViewById(R.id.EditUsername);
        editPassword= (EditText)findViewById(R.id.EditPassword);

        loginButton.setOnClickListener(
    		new OnClickListener() {
	            @Override
	            public void onClick(View v) {
	                dialog = ProgressDialog.show(WelcomeActivity.this, "","Login läuft", true);
	                new Thread(
	                		new Runnable(){
	                			public void run(){
	                				userLogin(editUsername.getText().toString(),editPassword.getText().toString(),WelcomeActivity.this);
	                			}
	                		}
	                ).start();
	            }
    		}
    	);
	}

	static void userLogin(String username, String password, final Activity activity){
	   		 
   		 ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
         nameValuePairs.add(new BasicNameValuePair("username",username.toString().trim()));
         nameValuePairs.add(new BasicNameValuePair("password",PasswordHash.toHash(password.toString().trim())));
   		 
         final String response = PHPConnector.doRequest(nameValuePairs, "check_for_user.php");
         if(response.equalsIgnoreCase(username + " has logged in successfully.")){
        	 activity.runOnUiThread(new Runnable() {
                 public void run() {
                     Toast.makeText(activity,response, Toast.LENGTH_SHORT).show();
                 }
             });
        	 
        	 HandleSharedPreferences.setUserCredentials(context, username, password);	//	username und pw werden gespeichert, damit beim nächsten Mal kein Login notwendig ist
        	 
             Intent intent = new Intent(activity, MainActivity.class);
             activity.startActivity(intent);
         }
         else if(response.equalsIgnoreCase(username + " already logged in.")){
        	 activity.runOnUiThread(new Runnable() {
                 public void run() {
                     Toast.makeText(activity,response, Toast.LENGTH_SHORT).show();
                 }
             });
             Intent intent = new Intent(activity, MainActivity.class);
             activity.startActivity(intent);
         }else{
        	 dialog.dismiss();
             AlertDialogs.showAlert(activity,"Login Error","User not found or password incorrect.");
         }
    }
	
    public void showAlert(){
        WelcomeActivity.this.runOnUiThread(new Runnable() {
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(WelcomeActivity.this);
                builder.setMessage("Username oder Passwort stimmt nicht �berein.")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }

    public void userSignup(View view){
        Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);
    }
}