package com.timkonieczny.yuome;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.util.Log;

public class PHPConnector {
	
	public static String stringResponse;
	public static HttpClient httpclient = new DefaultHttpClient();
	public static HttpGet httpget;
	public static HttpPost httppost;
	public static ArrayList<NameValuePair> nameValuePairs;
	public static ResponseHandler<String> responseHandler;
	public static final String server = "http://andibar.dyndns.org/Yuome/";
	public static HttpEntity entity;
	public static HttpResponse httpResponse;
	public static String response;
	private final static String tag="Response von ";
	
	public static String doRequest(ArrayList<NameValuePair> args, String url){
		httppost = new HttpPost(server + url);
        try {
			httppost.setEntity(new UrlEncodedFormEntity(args));
			httpResponse = httpclient.execute(httppost);
			entity = httpResponse.getEntity();
			stringResponse = EntityUtils.toString(entity,"UTF-8");
			entity.consumeContent();
			Log.d(tag+url, stringResponse);
			return stringResponse;
		} catch (IOException|ParseException e) {
			e.printStackTrace();
		}
		return "error";
	}
	
	public static String doRequest(String url){
		httppost = new HttpPost(server + url);
        try {
			httpResponse = httpclient.execute(httppost);
			entity = httpResponse.getEntity();
			stringResponse = EntityUtils.toString(entity,"UTF-8");
			entity.consumeContent();
			Log.d(tag+url, stringResponse);
			return stringResponse;
		} catch (IOException|ParseException e) {
			e.printStackTrace();
		}
		return "error";
	}

}
