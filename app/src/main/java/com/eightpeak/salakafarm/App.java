package com.eightpeak.salakafarm;

import android.app.Application;
import android.content.Context;

import java.util.ArrayList;


public class App extends Application {
     private static Context appContext;
    private static App instance;

    public static App getInstance() {
        return instance;
    }

    public static Context getContext() {
        return appContext;
    }

    public void setAppContext(Context mAppContext) {
        appContext = mAppContext;
    }


    private static ArrayList<String> data = new ArrayList<>();

    public static void addItem(String item) {
        data.add(item);
    }

    public static ArrayList<String> getData() {
        return data;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        this.setAppContext(getApplicationContext());
    }

}
