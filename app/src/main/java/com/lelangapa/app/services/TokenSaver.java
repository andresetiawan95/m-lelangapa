package com.lelangapa.app.services;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.provider.Settings;

import com.lelangapa.app.apicalls.notification.NotificationAPI;
import com.lelangapa.app.apicalls.singleton.RequestController;

import java.util.HashMap;

/**
 * Created by andre on 10/04/17.
 */

public class TokenSaver {

    public static String getDeviceID(Context context) {
        return Settings.Secure.getString(context.getContentResolver(),Settings
                .Secure.ANDROID_ID) + Build.SERIAL;
    }
    public static void sendTokenToSharedPreferences(Context context, String token) {
        SharedPreferences prefs = context.getSharedPreferences(NotifConfig.SHARED_PREF, 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("fcmToken", token);
        editor.putBoolean("isSubmittedToServer", false);
        editor.commit();
    }
    public static void tokenSent(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(NotifConfig.SHARED_PREF, 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("isSubmittedToServer", true);
        editor.commit();
    }
    public static void sendTokenToServer(Context context, String token, String userID) {
        //Log.e("TOKEN SENDING", token);
        HashMap<String, String> data = new HashMap<>();
        data.put("id_device", Settings.Secure.getString(context.getContentResolver(),Settings
                .Secure.ANDROID_ID) + Build.SERIAL);
        data.put("id_user", userID);
        data.put("fcm_token", token);

        NotificationAPI.SendToken sendTokenAPI = NotificationAPI.getSendTokenInstance(data);
        RequestController.getInstance(context).addToRequestQueue(sendTokenAPI);
    }
}
