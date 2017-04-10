package com.lelangapa.android.services;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by andre on 10/04/17.
 */

public class NotificationInstanceIDService extends FirebaseInstanceIdService {
    private static final String TAG = NotificationInstanceIDService.class.getSimpleName();

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.e(TAG, "Token ID : " + refreshedToken);

        TokenSaver.sendTokenToSharedPreferences(getApplicationContext(), refreshedToken);
        retrieveToken();

        Intent registrationComplete = new Intent(NotifConfig.REGISTRATION_COMPLETE);
        registrationComplete.putExtra("token", refreshedToken);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

    private void retrieveToken()
    {
        SharedPreferences prefs = getApplicationContext().getSharedPreferences(NotifConfig.SHARED_PREF, 0);
        Log.v("TOKEN", prefs.getString("fcmToken", "null"));
        Log.v("IS-SUBMITTED", Boolean.toString(prefs.getBoolean("isSubmittedToServer", false)));
    }
}
