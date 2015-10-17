package com.vbstudio.twittersearch.utils;

import android.util.Log;

import com.vbstudio.twittersearch.fragment.BaseFragment;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by vaibhav on 17/10/15.
 */
public class DateUtils {

    public static final String SERVER_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String SERVER_TIME_FORMAT = "HH:mm:ss";

    public static final String APP_DATE_FORMAT = "dd-MMM-yyyy";
    public static final String APP_DATE_FORMAT_FULL_MONTH = "dd-MMMM-yyyy";
    public static final String APP_TIME_FORMAT = "hh:mm aa";
    public static final String MY_ACCESS_SESSION_DATE_FORMAT = "EEE dd MMM yyyy";

    public static Long convertMinutesToMillis(Long minutues) {
        return minutues * 3600 * 1000;
    }

    public static Date extractDateFromString(String inputTimeFormat, String strigifiedDateTime) {
        DateFormat df = new SimpleDateFormat(inputTimeFormat);
        Date date = new Date();
        try {
            date = df.parse(strigifiedDateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }

    public static String convertStringDateTimeToAnother(String inputTimeFormat, String outputTimeForamt, String strigifiedDateTime) {
        String dateTimeStringToReturn = "";
        Date extractedDate = extractDateFromString(inputTimeFormat, strigifiedDateTime);
        try {
            dateTimeStringToReturn = new SimpleDateFormat(outputTimeForamt).format(extractedDate);
        } catch (Exception e) {
            e.printStackTrace();
            return strigifiedDateTime;
        }
        return dateTimeStringToReturn;
    }

    public static long convertStringDateTimeToMillis(String inputTimeFormat, String strigifiedDateTime) {
        long dateInMillis = 0;
        Date extractedDate = extractDateFromString(inputTimeFormat, strigifiedDateTime);
        try {
            dateInMillis = extractedDate.getTime();
        } catch (Exception e) {
            e.printStackTrace();
            return dateInMillis;
        }
        return dateInMillis;
    }

    public static Boolean isToday(Long timeInMilliseconds) {
        SimpleDateFormat dateHeaderFormat = new SimpleDateFormat("EEE dd-MMM-yyyy");
        String todayDate = dateHeaderFormat.format(Calendar.getInstance().getTimeInMillis());
        String checkDate = dateHeaderFormat.format(timeInMilliseconds);

        if (todayDate.equals(checkDate)) {
            return true;
        }

        return false;
    }

    public static boolean isCurrentDatePast(long timeInCalendar) {
        Date currentDate = new Date();
        Date calendarDate = new Date();

        //startDateCalendar.setTime(startDateInMillis);
        currentDate.setHours(0);
        currentDate.setMinutes(0);
        currentDate.setSeconds(0);

        calendarDate.setTime(timeInCalendar);
        calendarDate.setHours(0);
        calendarDate.setMinutes(0);
        calendarDate.setSeconds(0);

        Log.i(BaseFragment.LOG_TAG, "START DATE: " + new SimpleDateFormat(APP_DATE_FORMAT).format(currentDate.getTime()));
        Log.i(BaseFragment.LOG_TAG, "END DATE: " + new SimpleDateFormat(APP_DATE_FORMAT).format(calendarDate.getTime()));
        Log.i(BaseFragment.LOG_TAG, "DATE CHECK (START after END): " + currentDate.after(calendarDate));

        return currentDate.after(calendarDate);
    }
}
