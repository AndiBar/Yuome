package com.timkonieczny.yuome;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

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
import android.widget.Toast;

public class SignupActivity extends Activity {
	
	Button signupButton;
    EditText editUsername,editPassword,repeatPassword;
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
		setContentView(R.layout.activity_signup);
		
		signupButton = (Button)findViewById(R.id.SignupButton);
        editUsername = (EditText)findViewById(R.id.EditUsername);
        editPassword= (EditText)findViewById(R.id.EditPassword);
        repeatPassword= (EditText)findViewById(R.id.RepeatPassword);

        signupButton.setOnClickListener(
    		new OnClickListener() {
	            @Override
	            public void onClick(View v) {
	            	if(editPassword.getText().toString().trim().equals(repeatPassword.getText().toString().trim())){
		                dialog = ProgressDialog.show(SignupActivity.this, "","Registrierung l�uft", true);
		                new Thread(
		                		new Runnable(){
		                			public void run(){
		                				userSignup();
		                			}
		                		}
		                ).start();
	            	}else{
	        			Toast.makeText(SignupActivity.this,"Passw�rter stimmen nicht �berein", Toast.LENGTH_SHORT).show();
	            	}
	            }
    		}
    	);
	}

	void userSignup(){
        try{
            httpclient=new DefaultHttpClient();
            httppost= new HttpPost("http://timbotombo.heliohost.org/add_user.php"); 
            
            nameValuePairs = new ArrayList<NameValuePair>(2);
            // Always use the same variable name for posting i.e the android side variable name and php side variable name should be similar,
            nameValuePairs.add(new BasicNameValuePair("username",editUsername.getText().toString().trim()));  // $Edittext_value = $_POST['Edittext_value'];
            nameValuePairs.add(new BasicNameValuePair("password",editPassword.getText().toString().trim()));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            //Execute HTTP Post Request
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            final String response = httpclient.execute(httppost, responseHandler);		//Bundles nutzen, um Login-Information an weitere Activities weiterzureichen
            System.out.println("Response : " + response);
            runOnUiThread(new Runnable() {
                public void run() {
                    //debugText.setText("Response from PHP : " + response);
                    dialog.dismiss();
                }
            });
            //System.out.println(response);
            if(response.equalsIgnoreCase("Success")){
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(SignupActivity.this,"Registrierung erfolgreich. Hallo, "+editUsername.getText().toString().trim()+"!", Toast.LENGTH_SHORT).show();
                    }
                });

                startActivity(new Intent(SignupActivity.this, MainActivity.class));
            }else{
                showAlert();
            }

        }catch(Exception e){
            dialog.dismiss();
            System.out.println("Exception : " + e.getMessage());
        }
    }
    public void showAlert(){
        SignupActivity.this.runOnUiThread(new Runnable() {
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
                builder.setTitle("Leider ist ein Fehler aufgetreten.");
                builder.setMessage("Bitte versuchen sie es sp�ter erneut.")
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