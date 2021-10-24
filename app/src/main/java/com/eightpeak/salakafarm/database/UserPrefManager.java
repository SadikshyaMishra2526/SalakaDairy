package com.eightpeak.salakafarm.database;

import android.content.Context;
import android.content.SharedPreferences;

public class UserPrefManager {

    private SharedPreferences userPref;
    private SharedPreferences.Editor userPrefEditor;
    private Context mContext;

    // shared pref mode
    private static final int PRIVATE_MODE = 0;

    // Shared preferences file name
    public static final String PREF_NAME = "com-eightpeak-salakafarm-mds-shared-pref";

    private static final String SET_THRESHOLD = "threshold_set";

    private static final String APP_LANGUAGE= "AppLanguage";


    private static final String APP_NETWORK= "AppNetwork";


    private static final String MY_POSITION= "my_position";



    public UserPrefManager(Context context) {
        this.mContext = context;
        userPref = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        userPrefEditor = userPref.edit();
    }



    public  void setLanguage(String language) {
        userPrefEditor.putString(APP_LANGUAGE, language);
        userPrefEditor.commit();
    }


    public String getLanguage() {
        return userPref.getString(APP_LANGUAGE, "en");
    }


    public void setNetworkStatus(Boolean networkStatus) {
        userPrefEditor.putBoolean(APP_NETWORK, networkStatus);
        userPrefEditor.commit();
    }


    public Boolean getNetworkStatus() {
        return userPref.getBoolean(APP_NETWORK, true);
    }


    public void setThreshold(String threshold) {
        userPrefEditor.putString(SET_THRESHOLD, threshold);
        userPrefEditor.commit();
    }


    public String getThreshold() {
        return userPref.getString(SET_THRESHOLD, "90");
    }



    public void setCurrentPosition(String threshold) {
        userPrefEditor.putString(MY_POSITION, threshold);
        userPrefEditor.commit();
    }


    public String getCurrentPosition() {
        return userPref.getString(MY_POSITION, "Kathmandu, Nepal");
    }


    public void clearData() {
        userPrefEditor = userPref.edit();
        // Clearing all data from Shared Preferences
        userPrefEditor.clear();
        userPrefEditor.apply();

    }

}