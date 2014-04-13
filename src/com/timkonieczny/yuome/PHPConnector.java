package com.timkonieczny.yuome;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

public class PHPConnector {
	
	public static String response;
	public static HttpClient httpclient = new DefaultHttpClient();
	public static HttpGet httpget;
	public static HttpPost httppost;
	public static ArrayList<NameValuePair> nameValuePairs;
	public static ResponseHandler<String> responseHandler;
	
	public static String getResponse(String url) throws ClientProtocolException, IOException{
		httpget = new HttpGet(url);
		responseHandler = new BasicResponseHandler();
		response = httpclient.execute(httpget, responseHandler);
		return response;
	}
	public static String getLoginResponse(String url, String user, String pass) throws ClientProtocolException, IOException{
        httppost = new HttpPost(url);
        nameValuePairs = new ArrayList<NameValuePair>(2);
        nameValuePairs.add(new BasicNameValuePair("username",user));  
        nameValuePairs.add(new BasicNameValuePair("password",pass));
        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        response = httpclient.execute(httppost, responseHandler);
		
		return response;
	}
	public static void logOff() throws ClientProtocolException, IOException{
		httpget = new HttpGet("http://andibar.dyndns.org/Yuome/log_off.php");
		httpclient.execute(httpget);

	}
	public static ArrayList<HashMap<String,String>> getFriends() throws ClientProtocolException, IOException{
		httpget = new HttpGet("http://andibar.dyndns.org/Yuome/get_friends.php");
		httpclient.execute(httpget);
        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        response = httpclient.execute(httpget, responseHandler);
        String[] friends_unformatted = response.split(",");
        ArrayList<HashMap<String,String>> friends = new ArrayList<HashMap<String,String>>();
        for(String friend : friends_unformatted){
        	HashMap friends_map = new HashMap<String, String>();
        	String[] friends_array = friend.split(":");
        	friends_map.put("ID", friends_array[0]);
        	friends_map.put("username", friends_array[1]);
        	friends.add(friends_map);
        }
		return friends;
	}
}
