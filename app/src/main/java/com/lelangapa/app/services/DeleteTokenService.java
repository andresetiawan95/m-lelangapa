package com.lelangapa.app.services;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;

import java.io.IOException;

/**
 * Created by andre on 10/04/17.
 */

public class DeleteTokenService extends IntentService {
    public static final String TAG = DeleteTokenService.class.getSimpleName();

    public DeleteTokenService() {
        super(TAG);
    }
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String originalToken = getTokenFromSharedPreferences();
        Log.d(TAG, "Token before deletion: " + originalToken);

        try {
            FirebaseInstanceId.getInstance().deleteInstanceId();
            saveTokenToSharedPreferences("", false);
            String tokenCheck = getTokenFromSharedPreferences();
            Log.d(TAG, "Token deleted. Proof: " + tokenCheck);

            Log.d(TAG, "Getting new token");
            FirebaseInstanceId.getInstance().getToken();

            Intent broadcastIntent = new Intent(NotifConfig.REMOVING_TOKEN);
            LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    private void saveTokenToSharedPreferences(String token, Boolean submitted) {
        SharedPreferences prefs = getApplicationContext().getSharedPreferences(NotifConfig.SHARED_PREF, 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("fcmToken", token);
        editor.putBoolean("isSubmittedToServer", submitted);
        editor.commit();
    }
    private String getTokenFromSharedPreferences() {
        SharedPreferences prefs = getApplicationContext().getSharedPreferences(NotifConfig.SHARED_PREF, 0);
        return prefs.getString("fcmToken", null);
    }
}
