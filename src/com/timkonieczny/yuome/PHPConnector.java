package com.timkonieczny.yuome;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class PHPConnector {
	
	public static String stringResponse;
	public static HttpClient httpclient = new DefaultHttpClient();
	public static HttpGet httpget;
	public static HttpPost httppost;
	public static ArrayList<NameValuePair> nameValuePairs;
	public static ResponseHandler<String> responseHandler;
	public static String server = "http://andibar.dyndns.org/Yuome/";
	public static HttpEntity entity;
	public static HttpResponse httpResponse;
	
	public static String getResponse(String url) throws ClientProtocolException, IOException{
		httpResponse = httpclient.execute(new HttpGet(server + url));
		entity = httpResponse.getEntity();
		stringResponse = EntityUtils.toString(entity,"UTF-8");
		entity.consumeContent();
		return stringResponse;
	}
	
	public static String getLoginResponse(String url, String user, String pass) throws ClientProtocolException, IOException{
        nameValuePairs = new ArrayList<NameValuePair>(2);
        nameValuePairs.add(new BasicNameValuePair("username",user));  
        nameValuePairs.add(new BasicNameValuePair("password",pass));
        httppost = new HttpPost(server + url);
        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
        httpResponse = httpclient.execute(httppost);
		entity = httpResponse.getEntity();
		stringResponse = EntityUtils.toString(entity,"UTF-8");
		entity.consumeContent();
		return stringResponse;
	}
	
	public static void logOff() throws ClientProtocolException, IOException{		
		httpResponse = httpclient.execute(new HttpGet(new String(server+"log_off.php")));
		httpResponse.getEntity().consumeContent();
	}
	
	public static String getItemListResponse(String store) throws ClientProtocolException, IOException{
		nameValuePairs = new ArrayList<NameValuePair>(1);
        nameValuePairs.add(new BasicNameValuePair("store",store));  
        httppost = new HttpPost(server+"search_item.php");
        
        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
        httpResponse = httpclient.execute(httppost);
		entity = httpResponse.getEntity();
		stringResponse = EntityUtils.toString(entity,"UTF-8");
		entity.consumeContent();

		return stringResponse;
	}
	
	public static void addBuy(ArrayList<HashMap<String,String>> articles, ArrayList<String> contacts, String storeID, String date, Double total) throws ClientProtocolException, IOException{
		double debit_value = 0 - (total / (contacts.size()));
		debit_value = Math.round(debit_value * 100) / 100.;
		double credit_value = total + debit_value;
		
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
        httppost = new HttpPost(server + "add_buy.php");
		httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		httpResponse = httpclient.execute(httppost);
		entity = httpResponse.getEntity();
		stringResponse = EntityUtils.toString(entity,"UTF-8");
		entity.consumeContent();
	}
	
	public static String addFriend(String friend) throws ClientProtocolException, IOException {
		nameValuePairs = new ArrayList<NameValuePair>(1);
		nameValuePairs.add(new BasicNameValuePair("friend",friend));
		httppost = new HttpPost(server + "add_friend.php");
		//httppost.getEntity().consumeContent();
        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		httpResponse = httpclient.execute(httppost);
		entity = httpResponse.getEntity();
		stringResponse = EntityUtils.toString(entity,"UTF-8");
		entity.consumeContent();
		System.out.println(stringResponse);
		return(stringResponse);
	}
	
	public static ArrayList<HashMap<String,String>> getBalance(String url) throws ClientProtocolException, IOException, NotLoggedInException{
		
		httpResponse = httpclient.execute(new HttpGet(server + url));
		entity = httpResponse.getEntity();
		stringResponse = EntityUtils.toString(entity,"UTF-8");
		entity.consumeContent();
		
        System.out.println(stringResponse); 
        ArrayList<HashMap<String,String>> balance = new ArrayList<HashMap<String,String>>();
        
        if(stringResponse.equals("no debts found.")){
        	
        }else if(stringResponse.equals("not logged in.")){
        	throw new NotLoggedInException("Bitte erneut anmelden.");
        }else{
        	String[] data_unformatted = stringResponse.split(",");
	        for(String item : data_unformatted){
	        	HashMap<String, String> data_map = new HashMap<String, String>();
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
	
	public static String updateBalance(ArrayList<HashMap<String,String>> debts_changed, String url) throws ClientProtocolException, IOException{
		nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("debts_number",String.valueOf(debts_changed.size())));
        for(int i = 0; i < debts_changed.size(); i++){
        	nameValuePairs.add(new BasicNameValuePair("ID" + i,debts_changed.get(i).get("ID")));
        	nameValuePairs.add(new BasicNameValuePair("balance" + i,debts_changed.get(i).get("balance")));
        }
		httppost = new HttpPost(server+url);
		httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		httpResponse = httpclient.execute(httppost);
		entity = httpResponse.getEntity();
		stringResponse = EntityUtils.toString(entity,"UTF-8");
		entity.consumeContent();
		return stringResponse;
	}	
	
	public static String getReceiptsFromUser() throws ClientProtocolException, IOException{
		httpResponse = httpclient.execute(new HttpGet(new String(server + "get_user_receipts.php")));
		entity = httpResponse.getEntity();
		stringResponse = EntityUtils.toString(entity,"UTF-8");
		entity.consumeContent();
		return stringResponse;
	}
	
	public static String getArticles(String id) throws ClientProtocolException, IOException{
		nameValuePairs = new ArrayList<NameValuePair>(1);
        nameValuePairs.add(new BasicNameValuePair("id",id)); 
        httppost = new HttpPost(server + "get_articles.php");
        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		httpResponse = httpclient.execute(httppost);
		entity = httpResponse.getEntity();
		stringResponse = EntityUtils.toString(entity,"UTF-8");
		entity.consumeContent();
		return stringResponse;
	}
	
	public static String getNotifications() throws ClientProtocolException, IOException{
		httpResponse = httpclient.execute(new HttpGet(new String(server + "get_notifications.php")));
		entity = httpResponse.getEntity();
		stringResponse = EntityUtils.toString(entity,"UTF-8");
		entity.consumeContent();
		return stringResponse;
	}
	
	public static ArrayList<HashMap<String,String>> getData(String url) throws ClientProtocolException, IOException{
        
        
        httpResponse = httpclient.execute(new HttpGet(server + url));
		entity = httpResponse.getEntity();
		stringResponse = EntityUtils.toString(entity,"UTF-8");
		entity.consumeContent();
		System.out.println(stringResponse);
        
        String[] data_unformatted = stringResponse.split(",");
        ArrayList<HashMap<String,String>> data = new ArrayList<HashMap<String,String>>();
        for(String item : data_unformatted){
        	HashMap<String, String>data_map = new HashMap<String, String>();
        	String[] data_array = item.split(":");
        	data_map.put("ID", data_array[0]);
        	data_map.put("title", data_array[1]);
        	data.add(data_map);
        }
		return data;
	}
}