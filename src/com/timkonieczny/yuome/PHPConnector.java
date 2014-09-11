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

	
	public static String getResponse(String url) throws ClientProtocolException, IOException{
		httpget = new HttpGet(server + url);
		responseHandler = new BasicResponseHandler();
		response = httpclient.execute(httpget, responseHandler);
		return response;
	}
	
	public static String getLoginResponse(String url, String user, String pass) throws ClientProtocolException, IOException{
        httppost = new HttpPost(server + url);
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
	
	public static String getItemListResponse(String url, String store) throws ClientProtocolException, IOException{
		httppost.getEntity().consumeContent();		//TODO: Welche Verbindung ist offen?
        httppost = new HttpPost(server + url);
        nameValuePairs = new ArrayList<NameValuePair>(1);
        nameValuePairs.add(new BasicNameValuePair("store",store));  
        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        response = httpclient.execute(httppost, responseHandler);

		return response;
	}
	
	public static void addBuy(ArrayList<HashMap<String,String>> articles, ArrayList<String> contacts, String storeID, String date, Double total) throws ClientProtocolException, IOException{
		double debit_value = 0 - (total / (contacts.size() + 1));
		debit_value = Math.round(debit_value * 100) / 100.;
		double credit_value = total + debit_value;
		httppost = new HttpPost("http://andibar.dyndns.org/Yuome/add_buy.php");
        nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("article_number",String.valueOf(articles.size())));
        for(int i = 0; i < articles.size(); i++){
        	nameValuePairs.add(new BasicNameValuePair("article" + i,articles.get(i).get("article")));
        	nameValuePairs.add(new BasicNameValuePair("price" + i,articles.get(i).get("price")));
        	nameValuePairs.add(new BasicNameValuePair("amount" + i,articles.get(i).get("amount")));
        }
        nameValuePairs.add(new BasicNameValuePair("user_number",String.valueOf(contacts.size())));
        for(int i = 0; i < contacts.size(); i++){
        	nameValuePairs.add(new BasicNameValuePair("ID" + i,contacts.get(i)));
        }  
        nameValuePairs.add(new BasicNameValuePair("storeID",storeID));
        nameValuePairs.add(new BasicNameValuePair("date",date));
        nameValuePairs.add(new BasicNameValuePair("debit",String.valueOf(debit_value)));
        nameValuePairs.add(new BasicNameValuePair("credit",String.valueOf(credit_value)));
		httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		ResponseHandler<String> responseHandler = new BasicResponseHandler();
        response = httpclient.execute(httppost, responseHandler);
	}	
	
	public static String addFriend(String friend) throws ClientProtocolException, IOException {
		httppost = new HttpPost(server + "add_friend.php");
		nameValuePairs.add(new BasicNameValuePair("friend",friend));
		httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

        ResponseHandler<String> responseHandler = new BasicResponseHandler();
		response = httpclient.execute(httppost, responseHandler);
		System.out.println(response);
		return(response);
	}
	
	public static ArrayList<HashMap<String,String>> getData(String url) throws ClientProtocolException, IOException{
		httpget = new HttpGet(server + url);
		httpclient.execute(httpget);
        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        response = httpclient.execute(httpget, responseHandler);
        System.out.println(response);
        
        String[] data_unformatted = response.split(",");
        ArrayList<HashMap<String,String>> data = new ArrayList<HashMap<String,String>>();
        try {
	        for(String item : data_unformatted){
	        	HashMap data_map = new HashMap<String, String>();
	        	String[] data_array = item.split(":");
	        	data_map.put("ID", data_array[0]);
	        	data_map.put("title", data_array[1]);
	        	data.add(data_map);
	        }
        } catch (ArrayIndexOutOfBoundsException e) {
        	System.out.println();
        }
		return data;
	}
	
	public static ArrayList<HashMap<String,String>> getBalance(String url) throws ClientProtocolException, IOException{
		httpget = new HttpGet(server + url);
		httpclient.execute(httpget);
        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        response = httpclient.execute(httpget, responseHandler);
        System.out.println(response);
        
        ArrayList<HashMap<String,String>> balance = new ArrayList<HashMap<String,String>>();
        
        if(!response.equals("no debts found.")){
	        String[] data_unformatted = response.split(",");
	        for(String item : data_unformatted){
	        	HashMap data_map = new HashMap<String, String>();
	        	String[] balance_array = item.split(":");
	        	String[] data_array = balance_array[1].split("#");
	        	data_map.put("ID", balance_array[0]);
	        	data_map.put("balance", data_array[0]);
	        	data_map.put("username", data_array[1]);
	        	balance.add(data_map);
	        }
        }
		return balance;
	}
	
	public static String getReceiptsFromUser() throws ClientProtocolException, IOException{
		httpget = new HttpGet(new String("http://andibar.dyndns.org/Yuome/get_user_receipts.php"));
		responseHandler = new BasicResponseHandler();
		response = httpclient.execute(httpget, responseHandler);
		return response;
	}
	
	public static String getArticles(String id) throws ClientProtocolException, IOException{
		nameValuePairs = new ArrayList<NameValuePair>(2);
        nameValuePairs.add(new BasicNameValuePair("id",id));  
        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		
		httpget = new HttpGet(new String("http://andibar.dyndns.org/Yuome/get_articles.php"));
		responseHandler = new BasicResponseHandler();
		response = httpclient.execute(httpget, responseHandler);
		return response;
	}
	
	public static double getDebt (String friend) throws ClientProtocolException, IOException {
		httppost = new HttpPost(server + "get_friend_detail.php");
		nameValuePairs.add(new BasicNameValuePair("friend",friend));
		httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

        ResponseHandler<String> responseHandler = new BasicResponseHandler();
		response = httpclient.execute(httppost, responseHandler);
		System.out.println("Response: " + response);
        
        double debts = Double.parseDouble(response);
        System.out.println("Double response: " + debts);
		return(debts);
	}
	
	public static void deleteFriend (String friend) throws ClientProtocolException, IOException {
		httppost = new HttpPost(server + "delete_friend.php");
		nameValuePairs.add(new BasicNameValuePair("friend",friend));
		httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

        ResponseHandler<String> responseHandler = new BasicResponseHandler();
		response = httpclient.execute(httppost, responseHandler);
		System.out.println("Response: " + response);
	}
	
	public static void acceptFriends (ArrayList<String> friends) throws ClientProtocolException, IOException {
		httppost = new HttpPost(server + "accept_friend.php");
		
		for(String user: friends) {
			nameValuePairs.add(new BasicNameValuePair("friend", user));
		}
		httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
        ResponseHandler<String> responseHandler = new BasicResponseHandler();
		response = httpclient.execute(httppost, responseHandler);
		System.out.println("Response: " + response);
	}
	
	public static void declineFriends (ArrayList<String> friends) throws ClientProtocolException, IOException {
		httppost = new HttpPost(server + "decline_friend.php");
		
		for(String user: friends) {
			nameValuePairs.add(new BasicNameValuePair("friend", user));
		}
		httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
        ResponseHandler<String> responseHandler = new BasicResponseHandler();
		response = httpclient.execute(httppost, responseHandler);
		System.out.println("Response: " + response);
	}
	
}
