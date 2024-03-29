package com.eightpeak.salakafarm.serverconfig.network;


import android.content.Context;
import android.content.SharedPreferences;

public class TokenManager {

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    Context _context;

    private static TokenManager INSTANCE = null;

    private TokenManager(SharedPreferences prefs){
        this.prefs = prefs;
        this.editor = prefs.edit();
    }

   public static synchronized TokenManager getInstance(SharedPreferences prefs){
        if(INSTANCE == null){
            INSTANCE = new TokenManager(prefs);
        }
        return INSTANCE;
    }

    public void saveToken(String token){
        editor.putString("ACCESS_TOKEN", token).apply();
//        editor.putString("REFRESH_TOKEN", token.getRefreshToken()).commit();
    }

    public void deleteToken(){
        editor.remove("ACCESS_TOKEN").commit();
        editor.remove("REFRESH_TOKEN").commit();
        editor.clear();


    }

    public String getToken(){
//        AccessToken token = new AccessToken();
//        token.setAccessToken(prefs.getString("ACCESS_TOKEN", null));
//        token.setRefreshToken(prefs.getString("REFRESH_TOKEN", null));
        return prefs.getString("ACCESS_TOKEN", null);
    }



}