package com.lelangapa.android.services;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by andre on 10/04/17.
 */

public class NotificationService extends FirebaseMessagingService {
    private static final String TAG = NotificationService.class.getSimpleName();

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage)
    {
        Log.e(TAG, "From : " + remoteMessage.getFrom());
        Log.e(TAG, "title : " + remoteMessage.getNotification().getTitle());
        Log.e(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());
    }
}
