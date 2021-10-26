package com.eightpeak.salakafarm.database;

import static com.eightpeak.salakafarm.utils.Constants.DEFAULT;

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
    private static final String APP_LANGUAGE= "AppLanguage";
    private static final String MY_POSITION= "my_position";
    private static final String USER_FIRST_NAME= "user_first_name";
    private static final String USER_SECOND_NAME= "user_second_name";
    private static final String USER_CONTACT_NO= "user_contact_no";
    private static final String USER_EMAIL= "user_email";
    private static final String USER_COUNTRY= "user_country";
    private static final String USER_ADDRESS_1= "user_address_1";
    private static final String USER_ADDRESS_2= "user_address_2";
    private static final String USER_TOKEN= "user_token";



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



    public void setCurrentPosition(String threshold) {
        userPrefEditor.putString(MY_POSITION, threshold);
        userPrefEditor.commit();
    }


    public String getCurrentPosition() {
        return userPref.getString(MY_POSITION, "Kathmandu, Nepal");
    }

    public  void setFirstName(String language) {
        userPrefEditor.putString(USER_FIRST_NAME, language);
        userPrefEditor.commit();
    }

    public String getFirstName() {
        return userPref.getString(USER_FIRST_NAME, DEFAULT);
    }

  public  void setLastName(String language) {
        userPrefEditor.putString(USER_SECOND_NAME, language);
        userPrefEditor.commit();
    }

    public String getLastName() {
        return userPref.getString(USER_SECOND_NAME, DEFAULT);
    }

  public  void setEmail(String language) {
        userPrefEditor.putString(USER_EMAIL, language);
        userPrefEditor.commit();
    }

    public String getEmail() {
        return userPref.getString(USER_EMAIL, DEFAULT);
    }

  public  void setContactNo(String language) {
        userPrefEditor.putString(USER_CONTACT_NO, language);
        userPrefEditor.commit();
    }

    public String getContactNo() {
        return userPref.getString(USER_CONTACT_NO, DEFAULT);
    }

  public  void setUserCountry(String language) {
        userPrefEditor.putString(USER_COUNTRY, language);
        userPrefEditor.commit();
    }

    public String getUserCountry() {
        return userPref.getString(USER_COUNTRY, DEFAULT);
    }



  public  void setUserAddress1(String language) {
        userPrefEditor.putString(USER_ADDRESS_1, language);
        userPrefEditor.commit();
    }

    public String getUserAddress1() {
        return userPref.getString(USER_ADDRESS_1, DEFAULT);
    }

  public  void setUserAddress2(String language) {
        userPrefEditor.putString(USER_ADDRESS_2, language);
        userPrefEditor.commit();
    }

    public String getUserAddress2() {
        return userPref.getString(USER_ADDRESS_2, DEFAULT);
    }

    public  void setUserToken(String language) {
        userPrefEditor.putString(USER_TOKEN, language);
        userPrefEditor.commit();
    }

    public String getUserToken() {
        return userPref.getString(USER_TOKEN, DEFAULT);
    }



    public void clearData() {
        userPrefEditor = userPref.edit();
        // Clearing all data from Shared Preferences
        userPrefEditor.clear();
        userPrefEditor.apply();

    }

}