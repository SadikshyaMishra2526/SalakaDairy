package com.eightpeak.salakafarm.utils;

import android.app.Activity;
import android.location.Location;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import com.eightpeak.salakafarm.R;

import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

public class GeneralUtils {
    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        assert imm != null;
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static String getUnicodeNumber(String number) {
        String unicodeChar = "";
        for (int i = 0; i < number.length(); i++) {
            char character = number.charAt(i);
            String valueOfchar = String.valueOf(character);
            if (valueOfchar.equals("1")) {
                valueOfchar = "१";
            } else if (valueOfchar.equals("2")) {
                valueOfchar = "२";
            } else if (valueOfchar.equals("3")) {
                valueOfchar = "३";
            } else if (valueOfchar.equals("4")) {
                valueOfchar = "४";
            } else if (valueOfchar.equals("5")) {
                valueOfchar = "५";
            } else if (valueOfchar.equals("6")) {
                valueOfchar = "६";
            } else if (valueOfchar.equals("7")) {
                valueOfchar = "७";
            } else if (valueOfchar.equals("8")) {
                valueOfchar = "८";
            } else if (valueOfchar.equals("9")) {
                valueOfchar = "९";
            } else if (valueOfchar.equals("0")) {
                valueOfchar = "०";
            }

            unicodeChar = unicodeChar + valueOfchar;

        }

        return unicodeChar;
    }
    public static double calculateDistance(Double startLat, Double startLng, Double endLat, Double endLng) {
//        float distance;
//        Location startingLocation = new Location("starting point");
//        startingLocation.setLatitude(startLat);
//        startingLocation.setLongitude(startLng);
//
//        //Get the target location
//        Location endingLocation = new Location("ending point");
//        endingLocation.setLatitude(endLat);
//        endingLocation.setLongitude(endLng);
//
//        distance = startingLocation.distanceTo(endingLocation);
//
//
//        return distance;
//    }
        // haversine great circle distance approximation, returns meters
        double theta = startLng - endLng;
        double dist = Math.sin(deg2rad(startLat)) * Math.sin(deg2rad(endLat))
                + Math.cos(deg2rad(startLat)) * Math.cos(deg2rad(endLat))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60; // 60 nautical miles per degree of seperation
        dist = dist * 1852; // 1852 meters per nautical mile
        return (dist);
    }

    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private static double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }


    public static String getTodayDate(){
     return String.valueOf(android.text.format.DateFormat.format("yyyy-MM-dd", new java.util.Date()));
    }


    public static byte[] decoderfun(String enval) {
        Log.i("TAG", "decoderfun: "+enval);
        return Base64.decode(enval.getBytes(), Base64.DEFAULT);
    }
}