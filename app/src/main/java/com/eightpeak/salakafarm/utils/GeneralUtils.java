package com.eightpeak.salakafarm.utils;

import android.location.Location;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.eightpeak.salakafarm.R;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

public class GeneralUtils {
   private static Map<Integer, int[]> nepaliMap;

    private static int startingEngYear = 1944;

    private static int startingEngMonth = 0;

    private static int startingEngDay = 1;

    private static int dayOfWeek = Calendar.SATURDAY;

    private static int startingNepYear = 2000;

    private static int startingNepMonth = 9;

    private static int startingNepDay = 17;

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
    public static float calculateDistance(Double startLat, Double startLng, Double endLat, Double endLng) {
        float distance;
        Location startingLocation = new Location("starting point");
        startingLocation.setLatitude(startLat);
        startingLocation.setLongitude(startLng);

        //Get the target location
        Location endingLocation = new Location("ending point");
        endingLocation.setLatitude(endLat);
        endingLocation.setLongitude(endLng);

        distance = startingLocation.distanceTo(endingLocation);


        return distance;
    }


    public static String calculateNepaliDate(int day, int month, int year) {



        nepaliMap = new HashMap<Integer, int[]>();

        nepaliMap.put(2000, new int[] { 0, 30, 32, 31, 32, 31, 30, 30, 30, 29,
                30, 29, 31 });
        nepaliMap.put(2001, new int[] { 0, 31, 31, 32, 31, 31, 31, 30, 29, 30,
                29, 30, 30 });
        nepaliMap.put(2002, new int[] { 0, 31, 31, 32, 32, 31, 30, 30, 29, 30,
                29, 30, 30 });
        nepaliMap.put(2003, new int[] { 0, 31, 32, 31, 32, 31, 30, 30, 30, 29,
                29, 30, 31 });
        nepaliMap.put(2004, new int[] { 0, 30, 32, 31, 32, 31, 30, 30, 30, 29,
                30, 29, 31 });
        nepaliMap.put(2005, new int[] { 0, 31, 31, 32, 31, 31, 31, 30, 29, 30,
                29, 30, 30 });
        nepaliMap.put(2006, new int[] { 0, 31, 31, 32, 32, 31, 30, 30, 29, 30,
                29, 30, 30 });
        nepaliMap.put(2007, new int[] { 0, 31, 32, 31, 32, 31, 30, 30, 30, 29,
                29, 30, 31 });
        nepaliMap.put(2008, new int[] { 0, 31, 31, 31, 32, 31, 31, 29, 30, 30,
                29, 29, 31 });
        nepaliMap.put(2009, new int[] { 0, 31, 31, 32, 31, 31, 31, 30, 29, 30,
                29, 30, 30 });
        nepaliMap.put(2010, new int[] { 0, 31, 31, 32, 32, 31, 30, 30, 29, 30,
                29, 30, 30 });
        nepaliMap.put(2011, new int[] { 0, 31, 32, 31, 32, 31, 30, 30, 30, 29,
                29, 30, 31 });
        nepaliMap.put(2012, new int[] { 0, 31, 31, 31, 32, 31, 31, 29, 30, 30,
                29, 30, 30 });
        nepaliMap.put(2013, new int[] { 0, 31, 31, 32, 31, 31, 31, 30, 29, 30,
                29, 30, 30 });
        nepaliMap.put(2014, new int[] { 0, 31, 31, 32, 32, 31, 30, 30, 29, 30,
                29, 30, 30 });
        nepaliMap.put(2015, new int[] { 0, 31, 32, 31, 32, 31, 30, 30, 30, 29,
                29, 30, 31 });
        nepaliMap.put(2016, new int[] { 0, 31, 31, 31, 32, 31, 31, 29, 30, 30,
                29, 30, 30 });
        nepaliMap.put(2017, new int[] { 0, 31, 31, 32, 31, 31, 31, 30, 29, 30,
                29, 30, 30 });
        nepaliMap.put(2018, new int[] { 0, 31, 32, 31, 32, 31, 30, 30, 29, 30,
                29, 30, 30 });
        nepaliMap.put(2019, new int[] { 0, 31, 32, 31, 32, 31, 30, 30, 30, 29,
                30, 29, 31 });
        nepaliMap.put(2020, new int[] { 0, 31, 31, 31, 32, 31, 31, 30, 29, 30,
                29, 30, 30 });
        nepaliMap.put(2021, new int[] { 0, 31, 31, 32, 31, 31, 31, 30, 29, 30,
                29, 30, 30 });
        nepaliMap.put(2022, new int[] { 0, 31, 32, 31, 32, 31, 30, 30, 30, 29,
                29, 30, 30 });
        nepaliMap.put(2023, new int[] { 0, 31, 32, 31, 32, 31, 30, 30, 30, 29,
                30, 29, 31 });
        nepaliMap.put(2024, new int[] { 0, 31, 31, 31, 32, 31, 31, 30, 29, 30,
                29, 30, 30 });
        nepaliMap.put(2025, new int[] { 0, 31, 31, 32, 31, 31, 31, 30, 29, 30,
                29, 30, 30 });
        nepaliMap.put(2026, new int[] { 0, 31, 32, 31, 32, 31, 30, 30, 30, 29,
                29, 30, 31 });
        nepaliMap.put(2027, new int[] { 0, 30, 32, 31, 32, 31, 30, 30, 30, 29,
                30, 29, 31 });
        nepaliMap.put(2028, new int[] { 0, 31, 31, 32, 31, 31, 31, 30, 29, 30,
                29, 30, 30 });
        nepaliMap.put(2029, new int[] { 0, 31, 31, 32, 31, 32, 30, 30, 29, 30,
                29, 30, 30 });
        nepaliMap.put(2030, new int[] { 0, 31, 32, 31, 32, 31, 30, 30, 30, 29,
                29, 30, 31 });
        nepaliMap.put(2031, new int[] { 0, 30, 32, 31, 32, 31, 30, 30, 30, 29,
                30, 29, 31 });
        nepaliMap.put(2032, new int[] { 0, 31, 31, 32, 31, 31, 31, 30, 29, 30,
                29, 30, 30 });
        nepaliMap.put(2033, new int[] { 0, 31, 31, 32, 32, 31, 30, 30, 29, 30,
                29, 30, 30 });
        nepaliMap.put(2034, new int[] { 0, 31, 32, 31, 32, 31, 30, 30, 30, 29,
                29, 30, 31 });
        nepaliMap.put(2035, new int[] { 0, 30, 32, 31, 32, 31, 31, 29, 30, 30,
                29, 29, 31 });
        nepaliMap.put(2036, new int[] { 0, 31, 31, 32, 31, 31, 31, 30, 29, 30,
                29, 30, 30 });
        nepaliMap.put(2037, new int[] { 0, 31, 31, 32, 32, 31, 30, 30, 29, 30,
                29, 30, 30 });
        nepaliMap.put(2038, new int[] { 0, 31, 32, 31, 32, 31, 30, 30, 30, 29,
                29, 30, 31 });
        nepaliMap.put(2039, new int[] { 0, 31, 31, 31, 32, 31, 31, 29, 30, 30,
                29, 30, 30 });
        nepaliMap.put(2040, new int[] { 0, 31, 31, 32, 31, 31, 31, 30, 29, 30,
                29, 30, 30 });
        nepaliMap.put(2041, new int[] { 0, 31, 31, 32, 32, 31, 30, 30, 29, 30,
                29, 30, 30 });
        nepaliMap.put(2042, new int[] { 0, 31, 32, 31, 32, 31, 30, 30, 30, 29,
                29, 30, 31 });
        nepaliMap.put(2043, new int[] { 0, 31, 31, 31, 32, 31, 31, 29, 30, 30,
                29, 30, 30 });
        nepaliMap.put(2044, new int[] { 0, 31, 31, 32, 31, 31, 31, 30, 29, 30,
                29, 30, 30 });
        nepaliMap.put(2045, new int[] { 0, 31, 32, 31, 32, 31, 30, 30, 29, 30,
                29, 30, 30 });
        nepaliMap.put(2046, new int[] { 0, 31, 32, 31, 32, 31, 30, 30, 30, 29,
                29, 30, 31 });
        nepaliMap.put(2047, new int[] { 0, 31, 31, 31, 32, 31, 31, 30, 29, 30,
                29, 30, 30 });
        nepaliMap.put(2048, new int[] { 0, 31, 31, 32, 31, 31, 31, 30, 29, 30,
                29, 30, 30 });
        nepaliMap.put(2049, new int[] { 0, 31, 32, 31, 32, 31, 30, 30, 30, 29,
                29, 30, 30 });
        nepaliMap.put(2050, new int[] { 0, 31, 32, 31, 32, 31, 30, 30, 30, 29,
                30, 29, 31 });
        nepaliMap.put(2051, new int[] { 0, 31, 31, 31, 32, 31, 31, 30, 29, 30,
                29, 30, 30 });
        nepaliMap.put(2052, new int[] { 0, 31, 31, 32, 31, 31, 31, 30, 29, 30,
                29, 30, 30 });
        nepaliMap.put(2053, new int[] { 0, 31, 32, 31, 32, 31, 30, 30, 30, 29,
                29, 30, 30 });
        nepaliMap.put(2054, new int[] { 0, 31, 32, 31, 32, 31, 30, 30, 30, 29,
                30, 29, 31 });
        nepaliMap.put(2055, new int[] { 0, 31, 31, 32, 31, 31, 31, 30, 29, 30,
                29, 30, 30 });
        nepaliMap.put(2056, new int[] { 0, 31, 31, 32, 31, 32, 30, 30, 29, 30,
                29, 30, 30 });
        nepaliMap.put(2057, new int[] { 0, 31, 32, 31, 32, 31, 30, 30, 30, 29,
                29, 30, 31 });
        nepaliMap.put(2058, new int[] { 0, 30, 32, 31, 32, 31, 30, 30, 30, 29,
                30, 29, 31 });
        nepaliMap.put(2059, new int[] { 0, 31, 31, 32, 31, 31, 31, 30, 29, 30,
                29, 30, 30 });
        nepaliMap.put(2060, new int[] { 0, 31, 31, 32, 32, 31, 30, 30, 29, 30,
                29, 30, 30 });
        nepaliMap.put(2061, new int[] { 0, 31, 32, 31, 32, 31, 30, 30, 30, 29,
                29, 30, 31 });
        nepaliMap.put(2062, new int[] { 0, 30, 32, 31, 32, 31, 31, 29, 30, 29,
                30, 29, 31 });
        nepaliMap.put(2063, new int[] { 0, 31, 31, 32, 31, 31, 31, 30, 29, 30,
                29, 30, 30 });
        nepaliMap.put(2064, new int[] { 0, 31, 31, 32, 32, 31, 30, 30, 29, 30,
                29, 30, 30 });
        nepaliMap.put(2065, new int[] { 0, 31, 32, 31, 32, 31, 30, 30, 30, 29,
                29, 30, 31 });
        nepaliMap.put(2066, new int[] { 0, 31, 31, 31, 32, 31, 31, 29, 30, 30,
                29, 29, 31 });
        nepaliMap.put(2067, new int[] { 0, 31, 31, 32, 31, 31, 31, 30, 29, 30,
                29, 30, 30 });
        nepaliMap.put(2068, new int[] { 0, 31, 31, 32, 32, 31, 30, 30, 29, 30,
                29, 30, 30 });
        nepaliMap.put(2069, new int[] { 0, 31, 32, 31, 32, 31, 30, 30, 30, 29,
                29, 30, 31 });
        nepaliMap.put(2070, new int[] { 0, 31, 31, 31, 32, 31, 31, 29, 30, 30,
                29, 30, 30 });
        nepaliMap.put(2071, new int[] { 0, 31, 31, 32, 31, 31, 31, 30, 29, 30,
                29, 30, 30 });
        nepaliMap.put(2072, new int[] { 0, 31, 32, 31, 32, 31, 30, 30, 29, 30,
                29, 30, 30 });
        nepaliMap.put(2073, new int[] { 0, 31, 32, 31, 32, 31, 30, 30, 30, 29,
                29, 30, 31 });
        nepaliMap.put(2074, new int[] { 0, 31, 31, 31, 32, 31, 31, 30, 29, 30,
                29, 30, 30 });
        nepaliMap.put(2075, new int[] { 0, 31, 31, 32, 31, 31, 31, 30, 29, 30,
                29, 30, 30 });
        nepaliMap.put(2076, new int[] { 0, 31, 32, 31, 32, 31, 30, 30, 30, 29,
                29, 30, 30 });
        nepaliMap.put(2077, new int[] { 0, 31, 32, 31, 32, 31, 30, 30, 30, 29,
                30, 29, 31 });
        nepaliMap.put(2078, new int[] { 0, 31, 31, 31, 32, 31, 31, 30, 29, 30,
                29, 30, 30 });
        nepaliMap.put(2079, new int[] { 0, 31, 31, 32, 31, 31, 31, 30, 29, 30,
                29, 30, 30 });
        nepaliMap.put(2080, new int[] { 0, 31, 32, 31, 32, 31, 30, 30, 30, 29,
                29, 30, 30 });
        nepaliMap.put(2081, new int[] { 0, 31, 31, 32, 32, 31, 30, 30, 30, 29,
                30, 30, 30 });
        nepaliMap.put(2082, new int[] { 0, 30, 32, 31, 32, 31, 30, 30, 30, 29,
                30, 30, 30 });
        nepaliMap.put(2083, new int[] { 0, 31, 31, 32, 31, 31, 30, 30, 30, 29,
                30, 30, 30 });
        nepaliMap.put(2084, new int[] { 0, 31, 31, 32, 31, 31, 30, 30, 30, 29,
                30, 30, 30 });
        nepaliMap.put(2085, new int[] { 0, 31, 32, 31, 32, 30, 31, 30, 30, 29,
                30, 30, 30 });
        nepaliMap.put(2086, new int[] { 0, 30, 32, 31, 32, 31, 30, 30, 30, 29,
                30, 30, 30 });
        nepaliMap.put(2087, new int[] { 0, 31, 31, 32, 31, 31, 31, 30, 30, 29,
                30, 30, 30 });
        nepaliMap.put(2088, new int[] { 0, 30, 31, 32, 32, 30, 31, 30, 30, 29,
                30, 30, 30 });
        nepaliMap.put(2089, new int[] { 0, 30, 32, 31, 32, 31, 30, 30, 30, 29,
                30, 30, 30 });
        nepaliMap.put(2090, new int[] { 0, 30, 32, 31, 32, 31, 30, 30, 30, 29,
                30, 30, 30 });


                int engYear = year;

                int engMonth = month;

                int engDay = day;

                int nepYear = startingNepYear;
                int nepMonth = startingNepMonth;
                int nepDay = startingNepDay;

                Calendar currentEngDate = new GregorianCalendar();

                currentEngDate.set(engYear, engMonth, engDay);

                Calendar baseEngDate = new GregorianCalendar();

                baseEngDate.set(startingEngYear, startingEngMonth,
                        startingEngDay);

                long totalEngDaysCount = daysBetween(baseEngDate,
                        currentEngDate);

                while (totalEngDaysCount != 0) {

                    int daysInIthMonth = nepaliMap.get(nepYear)[nepMonth];

                    nepDay++;
                    if (nepDay > daysInIthMonth) {
                        nepMonth++;
                        nepDay = 1;
                    }

                    if (nepMonth > 12) {
                        nepYear++;
                        nepMonth = 1;
                    }

                    dayOfWeek++; // count the days in terms of 7 days
                    if (dayOfWeek > 7) {
                        dayOfWeek = 1;
                    }

                    totalEngDaysCount--;
                }
                switch (dayOfWeek) {
                    case 1:
                        Log.i("TAG", "calculateNepaliDate: "+"The Date in BS is \n\n" + nepYear + " /"
                                + nepMonth + " /" + nepDay + " Sunday");
//                        t.setText("The Date in BS is \n\n" + nepYear + " /"
//                                + nepMonth + " /" + nepDay + " Sunday");

                        dayOfWeek = Calendar.SATURDAY;
                        break;
                    case 2:
                        Log.i("TAG", "calculateNepaliDate: "+"The Date in BS is \n\n" + nepYear + " /"
                                + nepMonth + " /" + nepDay + " Sunday");
//                        t.setText("The Date in BS is \n\n" + nepYear + " /"
//                                + nepMonth + " /" + nepDay + " Monday");

                        dayOfWeek = Calendar.SATURDAY;
                        break;
                    case 3:
                        Log.i("TAG", "calculateNepaliDate: "+"The Date in BS is \n\n" + nepYear + " /"
                                + nepMonth + " /" + nepDay + " Sunday");
//                        t.setText("The Date in BS is \n\n" + nepYear + " /"
//                                + nepMonth + " /" + nepDay + " Tuesday");

                        dayOfWeek = Calendar.SATURDAY;
                        break;
                    case 4:
                        Log.i("TAG", "calculateNepaliDate: "+"The Date in BS is \n\n" + nepYear + " /"
                                + nepMonth + " /" + nepDay + " Sunday");
//                        t.setText("The Date in BS is \n\n" + nepYear + " /"
//                                + nepMonth + " /" + nepDay + " Wednesday");

                        dayOfWeek = Calendar.SATURDAY;
                        break;
                    case 5:
                        Log.i("TAG", "calculateNepaliDate: "+"The Date in BS is \n\n" + nepYear + " /"
                                + nepMonth + " /" + nepDay + " Sunday");
//                        t.setText("The Date in BS is \n\n" + nepYear + " /"
//                                + nepMonth + " /" + nepDay + " Thursday");

                        dayOfWeek = Calendar.SATURDAY;
                        break;
                    case 6:
                        Log.i("TAG", "calculateNepaliDate: "+"The Date in BS is \n\n" + nepYear + " /"
                                + nepMonth + " /" + nepDay + " Sunday");
//                        t.setText("The Date in BS is \n\n" + nepYear + " /"
//                                + nepMonth + " /" + nepDay + " Friday");

                        dayOfWeek = Calendar.SATURDAY;
                        break;
                    case 7:
                        Log.i("TAG", "calculateNepaliDate: "+"The Date in BS is \n\n" + nepYear + " /"
                                + nepMonth + " /" + nepDay + " Sunday");
//                        t.setText("The Date in BS is \n\n" + nepYear + " /"
//                                + nepMonth + " /" + nepDay + " Saturday");

                        dayOfWeek = Calendar.SATURDAY;
                        break;

                }


        return "";
    }
    private static long daysBetween(Calendar startDate, Calendar endDate) {
        Calendar date = (Calendar) startDate.clone();
        long daysBetween = 0;
        while (date.before(endDate)) {
            date.add(Calendar.DAY_OF_MONTH, 1);
            daysBetween++;
        }

        return daysBetween;
    }



}