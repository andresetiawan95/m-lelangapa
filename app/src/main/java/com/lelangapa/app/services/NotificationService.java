package com.lelangapa.app.services;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.lelangapa.app.activities.detail.DetailBarangActivity;
import com.lelangapa.app.resources.PriceFormatter;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by andre on 10/04/17.
 */

public class NotificationService extends FirebaseMessagingService {
    private static final String TAG = NotificationService.class.getSimpleName();
    private NotificationUtilities notificationUtilities;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage)
    {
        //Log.e(TAG, "From : " + remoteMessage.getFrom());

        //if you send using "notification" param
        if (remoteMessage.getNotification() != null) {
            //Log.e(TAG, "title : " + remoteMessage.getNotification().getTitle());
            //Log.e(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());
        }

        //if you send using "data" param
        if (remoteMessage.getData().size() > 0) {
            //Log.e(TAG, "Data Payload : " + remoteMessage.getData().toString());

            try {
                JSONObject notifJSON = new JSONObject(remoteMessage.getData().toString());
                handleDataNotificationMessage(notifJSON);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleDataNotificationMessage(JSONObject jsonObject)
    {
        try {
            JSONObject data = jsonObject.getJSONObject("data");
            //Log.e("Notif masuk", data.toString());
            if (data.has("notif_indicator")) {
                if (data.getString("notif_indicator").equals("bidsuccess")) {
                    String title = data.getString("item_name");
                    String bidder = data.getString("bidder_name");
                    String price = PriceFormatter.formatPrice(data.getString("price_now"));

                    String message = "Penawaran anda telah dikalahkan oleh\n" + bidder + "\ndengan harga penawaran\nRp. " + price;
                    Intent resultIntent = new Intent(getApplicationContext(), DetailBarangActivity.class);

                    Bundle bundleExtras = new Bundle();
                    bundleExtras.putString("auctioneer_id", data.getString("auctioneer_id"));
                    bundleExtras.putString("items_id", data.getString("item_id"));

                    resultIntent.putExtras(bundleExtras);

                    showNotificationMessage(getApplicationContext(), title, message, resultIntent);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void showNotificationMessage(Context context, String title, String message, Intent intent)
    {
        notificationUtilities = new NotificationUtilities(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        notificationUtilities.showNotificationMessage(title, message, intent);
    }
}
