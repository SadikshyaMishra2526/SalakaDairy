package com.eightpeak.salakafarm.utils;

import android.util.Base64;

public class GeneralUtils {
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

//    public static byte[] decoderfun(String enval) {
//        byte[] conVal = Base64.decode(enval, Base64.DEFAULT);
//        return conVal;
//
//    }
}