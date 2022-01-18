package com.eightpeak.salakafarm.date;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.eightpeak.salakafarm.R;

@SuppressLint("UseSparseArrays")
public class BS extends Fragment{

	TextView t;
	int startingNepYear = 2000;

	int startingNepMonth = 1;

	int startingNepDay = 1;

	int dayOfWeek = Calendar.WEDNESDAY;

	int startingEngYear = 1943;

	int startingEngMonth = 4;

	int startingEngDay = 14;

	Map<Integer, int[]> nepaliMap;

	public BS() {
		// Required empty public constructor
	}

	public String getNepaliConvertion(String date) {
		nepaliMap = new HashMap<Integer, int[]>();

		nepaliMap.put(2000, new int[]{0, 30, 32, 31, 32, 31, 30, 30, 30, 29,
				30, 29, 31});
		nepaliMap.put(2001, new int[]{0, 31, 31, 32, 31, 31, 31, 30, 29, 30,
				29, 30, 30});
		nepaliMap.put(2002, new int[]{0, 31, 31, 32, 32, 31, 30, 30, 29, 30,
				29, 30, 30});
		nepaliMap.put(2003, new int[]{0, 31, 32, 31, 32, 31, 30, 30, 30, 29,
				29, 30, 31});
		nepaliMap.put(2004, new int[]{0, 30, 32, 31, 32, 31, 30, 30, 30, 29,
				30, 29, 31});
		nepaliMap.put(2005, new int[]{0, 31, 31, 32, 31, 31, 31, 30, 29, 30,
				29, 30, 30});
		nepaliMap.put(2006, new int[]{0, 31, 31, 32, 32, 31, 30, 30, 29, 30,
				29, 30, 30});
		nepaliMap.put(2007, new int[]{0, 31, 32, 31, 32, 31, 30, 30, 30, 29,
				29, 30, 31});
		nepaliMap.put(2008, new int[]{0, 31, 31, 31, 32, 31, 31, 29, 30, 30,
				29, 29, 31});
		nepaliMap.put(2009, new int[]{0, 31, 31, 32, 31, 31, 31, 30, 29, 30,
				29, 30, 30});
		nepaliMap.put(2010, new int[]{0, 31, 31, 32, 32, 31, 30, 30, 29, 30,
				29, 30, 30});
		nepaliMap.put(2011, new int[]{0, 31, 32, 31, 32, 31, 30, 30, 30, 29,
				29, 30, 31});
		nepaliMap.put(2012, new int[]{0, 31, 31, 31, 32, 31, 31, 29, 30, 30,
				29, 30, 30});
		nepaliMap.put(2013, new int[]{0, 31, 31, 32, 31, 31, 31, 30, 29, 30,
				29, 30, 30});
		nepaliMap.put(2014, new int[]{0, 31, 31, 32, 32, 31, 30, 30, 29, 30,
				29, 30, 30});
		nepaliMap.put(2015, new int[]{0, 31, 32, 31, 32, 31, 30, 30, 30, 29,
				29, 30, 31});
		nepaliMap.put(2016, new int[]{0, 31, 31, 31, 32, 31, 31, 29, 30, 30,
				29, 30, 30});
		nepaliMap.put(2017, new int[]{0, 31, 31, 32, 31, 31, 31, 30, 29, 30,
				29, 30, 30});
		nepaliMap.put(2018, new int[]{0, 31, 32, 31, 32, 31, 30, 30, 29, 30,
				29, 30, 30});
		nepaliMap.put(2019, new int[]{0, 31, 32, 31, 32, 31, 30, 30, 30, 29,
				30, 29, 31});
		nepaliMap.put(2020, new int[]{0, 31, 31, 31, 32, 31, 31, 30, 29, 30,
				29, 30, 30});
		nepaliMap.put(2021, new int[]{0, 31, 31, 32, 31, 31, 31, 30, 29, 30,
				29, 30, 30});
		nepaliMap.put(2022, new int[]{0, 31, 32, 31, 32, 31, 30, 30, 30, 29,
				29, 30, 30});
		nepaliMap.put(2023, new int[]{0, 31, 32, 31, 32, 31, 30, 30, 30, 29,
				30, 29, 31});
		nepaliMap.put(2024, new int[]{0, 31, 31, 31, 32, 31, 31, 30, 29, 30,
				29, 30, 30});
		nepaliMap.put(2025, new int[]{0, 31, 31, 32, 31, 31, 31, 30, 29, 30,
				29, 30, 30});
		nepaliMap.put(2026, new int[]{0, 31, 32, 31, 32, 31, 30, 30, 30, 29,
				29, 30, 31});
		nepaliMap.put(2027, new int[]{0, 30, 32, 31, 32, 31, 30, 30, 30, 29,
				30, 29, 31});
		nepaliMap.put(2028, new int[]{0, 31, 31, 32, 31, 31, 31, 30, 29, 30,
				29, 30, 30});
		nepaliMap.put(2029, new int[]{0, 31, 31, 32, 31, 32, 30, 30, 29, 30,
				29, 30, 30});
		nepaliMap.put(2030, new int[]{0, 31, 32, 31, 32, 31, 30, 30, 30, 29,
				29, 30, 31});
		nepaliMap.put(2031, new int[]{0, 30, 32, 31, 32, 31, 30, 30, 30, 29,
				30, 29, 31});
		nepaliMap.put(2032, new int[]{0, 31, 31, 32, 31, 31, 31, 30, 29, 30,
				29, 30, 30});
		nepaliMap.put(2033, new int[]{0, 31, 31, 32, 32, 31, 30, 30, 29, 30,
				29, 30, 30});
		nepaliMap.put(2034, new int[]{0, 31, 32, 31, 32, 31, 30, 30, 30, 29,
				29, 30, 31});
		nepaliMap.put(2035, new int[]{0, 30, 32, 31, 32, 31, 31, 29, 30, 30,
				29, 29, 31});
		nepaliMap.put(2036, new int[]{0, 31, 31, 32, 31, 31, 31, 30, 29, 30,
				29, 30, 30});
		nepaliMap.put(2037, new int[]{0, 31, 31, 32, 32, 31, 30, 30, 29, 30,
				29, 30, 30});
		nepaliMap.put(2038, new int[]{0, 31, 32, 31, 32, 31, 30, 30, 30, 29,
				29, 30, 31});
		nepaliMap.put(2039, new int[]{0, 31, 31, 31, 32, 31, 31, 29, 30, 30,
				29, 30, 30});
		nepaliMap.put(2040, new int[]{0, 31, 31, 32, 31, 31, 31, 30, 29, 30,
				29, 30, 30});
		nepaliMap.put(2041, new int[]{0, 31, 31, 32, 32, 31, 30, 30, 29, 30,
				29, 30, 30});
		nepaliMap.put(2042, new int[]{0, 31, 32, 31, 32, 31, 30, 30, 30, 29,
				29, 30, 31});
		nepaliMap.put(2043, new int[]{0, 31, 31, 31, 32, 31, 31, 29, 30, 30,
				29, 30, 30});
		nepaliMap.put(2044, new int[]{0, 31, 31, 32, 31, 31, 31, 30, 29, 30,
				29, 30, 30});
		nepaliMap.put(2045, new int[]{0, 31, 32, 31, 32, 31, 30, 30, 29, 30,
				29, 30, 30});
		nepaliMap.put(2046, new int[]{0, 31, 32, 31, 32, 31, 30, 30, 30, 29,
				29, 30, 31});
		nepaliMap.put(2047, new int[]{0, 31, 31, 31, 32, 31, 31, 30, 29, 30,
				29, 30, 30});
		nepaliMap.put(2048, new int[]{0, 31, 31, 32, 31, 31, 31, 30, 29, 30,
				29, 30, 30});
		nepaliMap.put(2049, new int[]{0, 31, 32, 31, 32, 31, 30, 30, 30, 29,
				29, 30, 30});
		nepaliMap.put(2050, new int[]{0, 31, 32, 31, 32, 31, 30, 30, 30, 29,
				30, 29, 31});
		nepaliMap.put(2051, new int[]{0, 31, 31, 31, 32, 31, 31, 30, 29, 30,
				29, 30, 30});
		nepaliMap.put(2052, new int[]{0, 31, 31, 32, 31, 31, 31, 30, 29, 30,
				29, 30, 30});
		nepaliMap.put(2053, new int[]{0, 31, 32, 31, 32, 31, 30, 30, 30, 29,
				29, 30, 30});
		nepaliMap.put(2054, new int[]{0, 31, 32, 31, 32, 31, 30, 30, 30, 29,
				30, 29, 31});
		nepaliMap.put(2055, new int[]{0, 31, 31, 32, 31, 31, 31, 30, 29, 30,
				29, 30, 30});
		nepaliMap.put(2056, new int[]{0, 31, 31, 32, 31, 32, 30, 30, 29, 30,
				29, 30, 30});
		nepaliMap.put(2057, new int[]{0, 31, 32, 31, 32, 31, 30, 30, 30, 29,
				29, 30, 31});
		nepaliMap.put(2058, new int[]{0, 30, 32, 31, 32, 31, 30, 30, 30, 29,
				30, 29, 31});
		nepaliMap.put(2059, new int[]{0, 31, 31, 32, 31, 31, 31, 30, 29, 30,
				29, 30, 30});
		nepaliMap.put(2060, new int[]{0, 31, 31, 32, 32, 31, 30, 30, 29, 30,
				29, 30, 30});
		nepaliMap.put(2061, new int[]{0, 31, 32, 31, 32, 31, 30, 30, 30, 29,
				29, 30, 31});
		nepaliMap.put(2062, new int[]{0, 30, 32, 31, 32, 31, 31, 29, 30, 29,
				30, 29, 31});
		nepaliMap.put(2063, new int[]{0, 31, 31, 32, 31, 31, 31, 30, 29, 30,
				29, 30, 30});
		nepaliMap.put(2064, new int[]{0, 31, 31, 32, 32, 31, 30, 30, 29, 30,
				29, 30, 30});
		nepaliMap.put(2065, new int[]{0, 31, 32, 31, 32, 31, 30, 30, 30, 29,
				29, 30, 31});
		nepaliMap.put(2066, new int[]{0, 31, 31, 31, 32, 31, 31, 29, 30, 30,
				29, 29, 31});
		nepaliMap.put(2067, new int[]{0, 31, 31, 32, 31, 31, 31, 30, 29, 30,
				29, 30, 30});
		nepaliMap.put(2068, new int[]{0, 31, 31, 32, 32, 31, 30, 30, 29, 30,
				29, 30, 30});
		nepaliMap.put(2069, new int[]{0, 31, 32, 31, 32, 31, 30, 30, 30, 29,
				29, 30, 31});
		nepaliMap.put(2070, new int[]{0, 31, 31, 31, 32, 31, 31, 29, 30, 30,
				29, 30, 30});
		nepaliMap.put(2071, new int[]{0, 31, 31, 32, 31, 31, 31, 30, 29, 30,
				29, 30, 30});
		nepaliMap.put(2072, new int[]{0, 31, 32, 31, 32, 31, 30, 30, 29, 30,
				29, 30, 30});
		nepaliMap.put(2073, new int[]{0, 31, 32, 31, 32, 31, 30, 30, 30, 29,
				29, 30, 31});
		nepaliMap.put(2074, new int[]{0, 31, 31, 31, 32, 31, 31, 30, 29, 30,
				29, 30, 30});
		nepaliMap.put(2075, new int[]{0, 31, 31, 32, 31, 31, 31, 30, 29, 30,
				29, 30, 30});
		nepaliMap.put(2076, new int[]{0, 31, 32, 31, 32, 31, 30, 30, 30, 29,
				29, 30, 30});
		nepaliMap.put(2077, new int[]{0, 31, 32, 31, 32, 31, 30, 30, 30, 29,
				30, 29, 31});
		nepaliMap.put(2078, new int[]{0, 31, 31, 31, 32, 31, 31, 30, 29, 30,
				29, 30, 30});
		nepaliMap.put(2079, new int[]{0, 31, 31, 32, 31, 31, 31, 30, 29, 30,
				29, 30, 30});
		nepaliMap.put(2080, new int[]{0, 31, 32, 31, 32, 31, 30, 30, 30, 29,
				29, 30, 30});
		nepaliMap.put(2081, new int[]{0, 31, 31, 32, 32, 31, 30, 30, 30, 29,
				30, 30, 30});
		nepaliMap.put(2082, new int[]{0, 30, 32, 31, 32, 31, 30, 30, 30, 29,
				30, 30, 30});
		nepaliMap.put(2083, new int[]{0, 31, 31, 32, 31, 31, 30, 30, 30, 29,
				30, 30, 30});
		nepaliMap.put(2084, new int[]{0, 31, 31, 32, 31, 31, 30, 30, 30, 29,
				30, 30, 30});
		nepaliMap.put(2085, new int[]{0, 31, 32, 31, 32, 30, 31, 30, 30, 29,
				30, 30, 30});
		nepaliMap.put(2086, new int[]{0, 30, 32, 31, 32, 31, 30, 30, 30, 29,
				30, 30, 30});
		nepaliMap.put(2087, new int[]{0, 31, 31, 32, 31, 31, 31, 30, 30, 29,
				30, 30, 30});
		nepaliMap.put(2088, new int[]{0, 30, 31, 32, 32, 30, 31, 30, 30, 29,
				30, 30, 30});
		nepaliMap.put(2089, new int[]{0, 30, 32, 31, 32, 31, 30, 30, 30, 29,
				30, 30, 30});
		nepaliMap.put(2090, new int[]{0, 30, 32, 31, 32, 31, 30, 30, 30, 29,
				30, 30, 30});


//		int nepYear = num1;
//		int nepMonth = num2;
//		int nepDay = num3;

		String[] dateList = date.split("-");
		int nepYear =Integer.parseInt( dateList[0]);

		int nepMonth =Integer.parseInt( dateList[1])-1;

		int nepDay = Integer.parseInt( dateList[2]);










		long totalNepDaysCount = 0;

		int engYear = startingEngYear;
		int engMonth = startingEngMonth;
		int engDay = startingEngDay;

		int endDayOfMonth = 0;

		int[] daysInMonth = new int[]{0, 31, 28, 31, 30, 31, 30, 31,
				31, 30, 31, 30, 31};
		int[] daysInMonthOfLeapYear = new int[]{0, 31, 29, 31, 30,
				31, 30, 31, 31, 30, 31, 30, 31};

		// count total days in-terms of year
		for (int i = startingNepYear; i < nepYear; i++) {
			for (int j = 1; j <= 12; j++) {
				totalNepDaysCount += nepaliMap.get(i)[j];
			}
		}

		// count total days in-terms of month
		for (int j = startingNepMonth; j < nepMonth; j++) {
			totalNepDaysCount += nepaliMap.get(nepYear)[j];
		}

		// count total days in-terms of date
		totalNepDaysCount += nepDay - startingNepDay;

		while (totalNepDaysCount != 0) {
			if (isLeapYear(engYear)) {
				endDayOfMonth = daysInMonthOfLeapYear[engMonth];
			} else {
				endDayOfMonth = daysInMonth[engMonth];
			}
			engDay++;
			dayOfWeek++;
			if (engDay > endDayOfMonth) {
				engMonth++;
				engDay = 1;
				if (engMonth > 12) {
					engYear++;
					engMonth = 1;
				}
			}
			if (dayOfWeek > 7) {
				dayOfWeek = 1;
			}
			totalNepDaysCount--;

		}

		switch (dayOfWeek) {
			case 1:

				t.setText("The Date in AD is \n\n" + engYear + " /"
						+ engMonth + " /" + engDay + " Sunday");

				dayOfWeek = Calendar.WEDNESDAY;
				break;
			case 2:

				t.setText("The Date in AD is \n\n" + engYear + " /"
						+ engMonth + " /" + engDay + " Monday");

				dayOfWeek = Calendar.WEDNESDAY;
				break;
			case 3:

				t.setText("The Date in AD is \n\n" + engYear + " /"
						+ engMonth + " /" + engDay + " Tuesday");

				dayOfWeek = Calendar.WEDNESDAY;
				break;
			case 4:

				t.setText("The Date in AD is \n\n" + engYear + " /"
						+ engMonth + " /" + engDay + " Wednesday");

				dayOfWeek = Calendar.WEDNESDAY;
				break;
			case 5:

				t.setText("The Date in AD is \n\n" + engYear + " /"
						+ engMonth + " /" + engDay + " Thursday");

				dayOfWeek = Calendar.WEDNESDAY;
				break;
			case 6:

				t.setText("The Date in AD is \n\n" + engYear + " /"
						+ engMonth + " /" + engDay + " Friday");

				dayOfWeek = Calendar.WEDNESDAY;
				break;
			case 7:

				t.setText("The Date in AD is \n\n" + engYear + " /"
						+ engMonth + " /" + engDay + " Saturday");

				dayOfWeek = Calendar.WEDNESDAY;
				break;

		}

		return "";
	}

	private boolean isLeapYear(int year) {
		// TODO Auto-generated method stub
		if (year % 100 == 0) {
			return year % 400 == 0;
		} else {
			return year % 4 == 0;
		}
	}





}
