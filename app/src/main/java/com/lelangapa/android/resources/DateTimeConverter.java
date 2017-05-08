package com.lelangapa.android.resources;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Andre on 12/30/2016.
 */

public class DateTimeConverter {
    private String formatWhenUserInputDate = "dd-MM-yyyy";
    private String formatWhenUserInputTime = "HH:mm";
    private String inputDateFormat = "dd-MM-yyyy HH:mm:ss";
    private String dateFormatForServer = "yyyy-MM-dd'T'HH:mm:ss";
    private String outputFromServerForUserFormat = "dd-MM-yyyy'T'HH:mm.SSS'Z'";
    private String outputIndonesiaFormat = "dd-MM-yyyy HH:mm";
//    private String outputDateFormatFromServer = "yyyy-MM-dd'T'HH:mm:ss'.000Z'";
//    private String outputDateFormatPreprocessUTC = "yyyy-MM-dd HH:mm:ss '+0000'";
//    private String outputDateFormatPreprocess = "yyyy-MM-dd HH:mm:ss Z";
    public String convertInputLocalTime (String time)
    {
        SimpleDateFormat sdf = new SimpleDateFormat(inputDateFormat);
        try {
            Date date = sdf.parse(time);
            sdf.applyPattern(dateFormatForServer);
            String output = sdf.format(date);
            //memisahkan GMT+0700 menjadi 'GMT' dan '+0700'
            String[] timezone = TimeZone.getDefault().getDisplayName(false, TimeZone.SHORT).split("T");
            output = output + timezone[1];
            //Log.v("Time yang dikirim", output);
            return output;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String convertUTCToLocalTime (String time)
    {
        //menggunakan joda time
        DateTime now = new DateTime(time, DateTimeZone.UTC);
        DateTime toLocalTime = now.withZone(DateTimeZone.getDefault());
        DateTimeFormatter dmf = DateTimeFormat.forPattern(outputFromServerForUserFormat);
        return dmf.print(toLocalTime);
    }
    public String convertUTCToLocalTimeIndonesiaFormat (String time)
    {
        //menggunakan joda time
        DateTime now = new DateTime(time, DateTimeZone.UTC);
        DateTime toIndonesiaTime = now.withZone(DateTimeZone.getDefault());
        DateTimeFormatter dmf = DateTimeFormat.forPattern(outputIndonesiaFormat);
        return dmf.print(toIndonesiaTime);
    }
    public boolean compareUTCTimeWithStartAndEndTime(String startTime, String endTime, String nowTime)
    {
        DateTime nowDateTime = new DateTime(nowTime, DateTimeZone.UTC);
        DateTime startDateTime = new DateTime(startTime, DateTimeZone.UTC);
        DateTime endDateTime = new DateTime(endTime, DateTimeZone.UTC);
        if (startDateTime.isBefore(nowDateTime) && endDateTime.isAfter(nowDateTime))
        {
            return true;
        }
        return false;
    }
    public long getUTCTimeInMillisecond(String time)
    {
        DateTime dateTime = new DateTime(time, DateTimeZone.UTC);
        return dateTime.getMillis();
    }
    public String convertUserDateInput(String time) {

        DateTimeFormatter dmf = DateTimeFormat.forPattern(formatWhenUserInputDate);
        DateTime dateTime = dmf.parseDateTime(time);
        return dmf.print(dateTime);
    }
    /*
    public void convertUTCToLocalTimeOld (String time)
    {

        SimpleDateFormat sdf = new SimpleDateFormat(outputDateFormatFromServer);
        SimpleDateFormat sdfToLocal = new SimpleDateFormat(outputDateFormatPreprocess);
        try {
            //merubah data yang diterima menjadi format UTC sehingga bisa dirubah ke timezone local time
            Date date = sdf.parse(time);
            sdf.applyPattern(outputDateFormatPreprocessUTC);
            String preprocess = sdf.format(date);
            //Log.v("Hasil ubah jadi UTC", preprocess);

            //merubah data dari format UTC ke timezone masing2
            Date newDate = sdfToLocal.parse(preprocess);
            sdfToLocal.setTimeZone(TimeZone.getDefault());
            sdfToLocal.applyPattern(outputDateFormatFromServer);
            String output = sdfToLocal.format(newDate);
            return output;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }*/
}
