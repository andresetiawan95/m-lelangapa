package com.lelangkita.android.resources;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Andre on 12/30/2016.
 */

public class DateTimeConverter {
    private String inputDateFormat = "yyyy-MM-dd HH:mm:ss";
    private String dateFormatForServer = "yyyy-MM-dd'T'HH:mm:ss";
    private String outputDateFormatFromServer = "yyyy-MM-dd'T'HH:mm:ss'.000Z'";
    private String outputDateFormatPreprocessUTC = "yyyy-MM-dd HH:mm:ss '+0000'";
    private String outputDateFormatPreprocess = "yyyy-MM-dd HH:mm:ss Z";
    public String convertInputLocalTime (String time)
    {
        SimpleDateFormat sdf = new SimpleDateFormat(inputDateFormat);
        try {
            Date date = sdf.parse(time);
            sdf.applyPattern(dateFormatForServer);
            String output = sdf.format(date);
            String[] timezone = TimeZone.getDefault().getDisplayName(false, TimeZone.SHORT).split("T");
            output = output + timezone[1];
            Log.v("Time yang dikirim", output);
            return output;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String convertUTCToLocalTime (String time)
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
    }
}
