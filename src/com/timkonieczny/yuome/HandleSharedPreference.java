package com.timkonieczny.yuome;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.util.Log;

public class HandleSharedPreference {

    public static void setUserCredentials(Context context, String username, String password){
        Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString("username", username);
        editor.putString("password", password);
        editor.commit();
    }

    public static String getUserCredentials(Context context, String key){
    	Log.d("SaveSharedPreference",PreferenceManager.getDefaultSharedPreferences(context).getString(key, ""));
        return PreferenceManager.getDefaultSharedPreferences(context).getString(key, "");
    }
    
    public static SharedPreferences getSharedPreferences(Context context){
    	return PreferenceManager.getDefaultSharedPreferences(context);
    }
}
