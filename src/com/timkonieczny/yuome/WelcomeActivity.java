package com.timkonieczny.yuome;

import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import android.os.Build;
import android.os.Bundle;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
    ProgressDialog dialog = null;

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setTheme(android.R.style.Theme_Holo_NoActionBar);
		setContentView(R.layout.activity_welcome);
		
		loginButton = (Button)findViewById(R.id.LoginButton);
        editUsername = (EditText)findViewById(R.id.EditUsername);
        editPassword= (EditText)findViewById(R.id.EditPassword);
        debugText = (TextView)findViewById(R.id.debug);

        loginButton.setOnClickListener(
    		new OnClickListener() {
	            @Override
	            public void onClick(View v) {
	                dialog = ProgressDialog.show(WelcomeActivity.this, "","Login läuft", true);
	                new Thread(
	                		new Runnable(){
	                			public void run(){
	                				userLogin();
	                			}
	                		}
	                ).start();
	            }
    		}
    	);
	}

	void userLogin(){
	   	 try{
			 final String response = PHPConnector.getLoginResponse("http://andibar.dyndns.org/Yuome/check_for_user.php", editUsername.getText().toString().trim(), editPassword.getText().toString().trim());
	         System.out.println("Response : " + response);
	         dialog.dismiss();
	         if(response.equalsIgnoreCase(editUsername.getText() + " has logged in successfully.")){
	             runOnUiThread(new Runnable() {
	                 public void run() {
	                     Toast.makeText(WelcomeActivity.this,response, Toast.LENGTH_SHORT).show();
	                 }
	             });
	
	             Intent intent = new Intent(this, MainActivity.class);
	             startActivity(intent);
	         }
	         else if(response.equalsIgnoreCase(editUsername.getText() + " already logged in.")){
	             runOnUiThread(new Runnable() {
	                 public void run() {
	                     Toast.makeText(WelcomeActivity.this,response, Toast.LENGTH_SHORT).show();
	                 }
	             });
	             Intent intent = new Intent(this, MainActivity.class);
	             startActivity(intent);
	         }else{
	             AlertDialogs.showAlert(this,"Login Error","User not found or password incorrect.");
	         }
	
	     }catch(Exception e){
	         dialog.dismiss();
	         AlertDialogs.showAlert(this,"Connection Error",e.getMessage());
		 }
    }
    public void showAlert(){
        WelcomeActivity.this.runOnUiThread(new Runnable() {
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(WelcomeActivity.this);
                builder.setMessage("Username oder Passwort stimmt nicht ï¿½berein.")
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
    
    public void debug(View view){	//TODO
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}