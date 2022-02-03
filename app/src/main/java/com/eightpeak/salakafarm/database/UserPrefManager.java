package com.eightpeak.salakafarm.database;

import static com.eightpeak.salakafarm.utils.Constants.DEFAULT;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.List;
import java.util.Set;

public class UserPrefManager {

    private SharedPreferences userPref;
    private SharedPreferences.Editor userPrefEditor;
    private Context mContext;

    // shared pref mode
    private static final int PRIVATE_MODE = 0;
    // Shared preferences file name
    public static final String PREF_NAME = "com-eightpeak-salakafarm-mds-user-shared-pref";
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


    private static final String SELECTED_SUB_ITEM= "sub_item_selected";
    private static final String SELECTED_PACKAGE_ITEM= "package_item_selected";

    private static final String CURRENT_LAT= "current_lat";
    private static final String CURRENT_LNG= "current_lng";


    private static final String ADDRESS_LIST= "address_list";

    private static final String FCM= "fcm";

    private static final String BANK_DETAILS= "bank_details";
    private static final String QR_PHOTO= "qr_photo";
    private static final String ACCOUNT_NAME= "account_name";

    private static final String POPUP= "popup";
    private static final String SUBSCRIPTION= "subscription";


    private static final String ACC_HOLDER_NAME= "acc_holder_name";

    private static final String BANK_NAME= "bank_name";
    private static final String DELIVERY_PERIOD= "delivery_period";
    private static final String AVATAR= "avatar";
    private static final String SUBSCRIPTION_SELECTED= "subscription_selected";


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

  public  void setAvatar(String avatar) {
        userPrefEditor.putString(AVATAR, avatar);
        userPrefEditor.commit();
    }

    public String getAvatar() {
        return userPref.getString(AVATAR, DEFAULT);
    }
//
//
//
//  public  void setUserAddress1(String language) {
//        userPrefEditor.putString(USER_ADDRESS_1, language);
//        userPrefEditor.commit();
//    }
//
//    public String getUserAddress1() {
//        return userPref.getString(USER_ADDRESS_1, DEFAULT);
//    }
//


  public  void setDeliveryPeriod(Integer language) {
        userPrefEditor.putInt(DELIVERY_PERIOD, language);
        userPrefEditor.commit();
    }

    public Integer getDeliveryPeriod() {
        return userPref.getInt(DELIVERY_PERIOD, 0);
    }
//

    public  void setSubSelected(Integer subSelected) {
        userPrefEditor.putInt(SELECTED_SUB_ITEM, subSelected);
        userPrefEditor.commit();
    }

    public Integer getSubSelected() {
        return userPref.getInt(SELECTED_SUB_ITEM, 0);
    }


    public  void setPackageSelected(Integer subSelected) {
        userPrefEditor.putInt(SELECTED_PACKAGE_ITEM, subSelected);
        userPrefEditor.commit();
    }

    public Integer getPackageSelected() {
        return userPref.getInt(SELECTED_PACKAGE_ITEM, 0);
    }


    public  void setCurrentLat(Float currentLat) {
        userPrefEditor.putFloat(CURRENT_LAT, currentLat);
        userPrefEditor.commit();
    }

    public Float getCurrentLat() {
        return userPref.getFloat(CURRENT_LAT, 0);
    }

    public  void setCurrentLng(Float currentLng) {
        userPrefEditor.putFloat(CURRENT_LNG, currentLng);
        userPrefEditor.commit();
    }

    public Float getCurrentLng() {
        return userPref.getFloat(CURRENT_LNG, 0);
    }


 public  void setAddressList(String addressList) {
        userPrefEditor.putString(ADDRESS_LIST, addressList);
        userPrefEditor.commit();
    }

    public String getAddressList() {
        return userPref.getString(ADDRESS_LIST,null);
    }


    public  void setFCMToken(String fcmToken) {
        userPrefEditor.putString(FCM, fcmToken);
        userPrefEditor.commit();
    }

    public String getFCMToken() {
        return userPref.getString(FCM, DEFAULT);
    }


    public  void setBankAccountNo(String bankAccountNo) {
        userPrefEditor.putString(BANK_DETAILS, bankAccountNo);
        userPrefEditor.commit();
    }

    public String getBankAccountNo() {
        return userPref.getString(BANK_DETAILS, DEFAULT);
    }

    public  void setQRPath(String qrPath) {
        userPrefEditor.putString(QR_PHOTO, qrPath);
        userPrefEditor.commit();
    }

    public String getQRPath() {
        return userPref.getString(QR_PHOTO, DEFAULT);
    }

  public  void setAccountName(String qrPath) {
        userPrefEditor.putString(ACCOUNT_NAME, qrPath);
        userPrefEditor.commit();
    }

    public String getAccountName() {
        return userPref.getString(ACCOUNT_NAME, DEFAULT);
    }





    public  void setBankName(String qrPath) {
        userPrefEditor.putString(BANK_NAME, qrPath);
        userPrefEditor.commit();
    }

    public String getBankName() {
        return userPref.getString(BANK_NAME, DEFAULT);
    }


    public  void setAccountHolderName(String qrPath) {
        userPrefEditor.putString(ACC_HOLDER_NAME, qrPath);
        userPrefEditor.commit();
    }

    public String getAccountHolderName() {
        return userPref.getString(ACC_HOLDER_NAME, DEFAULT);
    }


 public  void setSelectedPackage(Integer id) {
        userPrefEditor.putInt(SUBSCRIPTION_SELECTED, id);
        userPrefEditor.commit();
    }

    public Integer getSelectedPackage() {
        return userPref.getInt(SUBSCRIPTION_SELECTED, 0);
    }





    public  void setPopupBoolean(Boolean fcmToken) {
        userPrefEditor.putBoolean(POPUP, fcmToken);
        userPrefEditor.commit();
    }

    public Boolean getPopupBoolean() {
        return userPref.getBoolean(POPUP, false);
    }


    public void setSubscriptionStatus(Boolean fcmToken) {
        userPrefEditor.putBoolean(SUBSCRIPTION, fcmToken);
        userPrefEditor.commit();
    }

    public Boolean getSubscriptionStatus() {
        return userPref.getBoolean(SUBSCRIPTION, false);
    }

    public void clearData() {
        userPrefEditor = userPref.edit();
        // Clearing all data from Shared Preferences
        userPrefEditor.clear();
        userPrefEditor.apply();

    }

}