package com.timkonieczny.yuome;

import java.util.ArrayList;
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
import android.content.DialogInterface;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SignupActivity extends Activity {
	
	Button signupButton;
    EditText editUsername,editPassword,repeatPassword;
    HttpPost httppost;
    StringBuffer buffer;
    HttpResponse response;
    HttpClient httpclient;

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setTheme(android.R.style.Theme_Holo_NoActionBar);
		setContentView(R.layout.activity_signup);
		
		signupButton = (Button)findViewById(R.id.SignupButton);
        editUsername = (EditText)findViewById(R.id.EditUsername);
        editPassword= (EditText)findViewById(R.id.EditPassword);
        repeatPassword= (EditText)findViewById(R.id.RepeatPassword);

        signupButton.setOnClickListener(
    		new OnClickListener() {
	            @Override
	            public void onClick(View v) {
	            	WelcomeActivity.dialog = ProgressDialog.show(SignupActivity.this, "","Login läuft", true);
	            	if(editPassword.getText().toString().trim().equals(repeatPassword.getText().toString().trim())){
		                new Thread(
		                		new Runnable(){
		                			public void run(){
		                				userSignup();
		                			}
		                		}
		                ).start();
	            	}else{
	        			Toast.makeText(SignupActivity.this,"Passwörter stimmen nicht überein", Toast.LENGTH_SHORT).show();
	            	}
	            }
    		}
    	);
	}

	void userSignup(){        	
        	
        	ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("username",editUsername.getText().toString().trim()));  
            nameValuePairs.add(new BasicNameValuePair("password",PasswordHash.toHash(editPassword.getText().toString().trim())));
            
            String response = PHPConnector.doRequest(nameValuePairs, "add_user.php");

            if(response.equalsIgnoreCase("Success")){
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(SignupActivity.this,"Registrierung erfolgreich.", Toast.LENGTH_SHORT).show();
                    }
                });
                WelcomeActivity.userLogin(editUsername.getText().toString().trim(),editPassword.getText().toString().trim(), this);
            }else{
                showAlert();
            }
    }
    public void showAlert(){
        SignupActivity.this.runOnUiThread(new Runnable() {
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
                builder.setTitle("Leider ist ein Fehler aufgetreten.");
                builder.setMessage("Bitte versuchen sie es später erneut.")
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
}