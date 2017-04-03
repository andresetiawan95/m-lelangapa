package com.lelangapa.android.fragments.detail.detailtawaran;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.lelangapa.android.R;
import com.lelangapa.android.interfaces.SocketSender;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by andre on 03/04/17.
 */

public class CancelToggler {
    private Activity activity;
    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog alertDialog;
    private ProgressDialog progressDialog;

    private String itemID, bidID, bidderID;
    private Integer bidTime;

    private SocketSender socketSender;

    public CancelToggler(Activity activity, SocketSender socketSender, String itemID, Integer bidTime)
    {
        this.activity = activity;
        this.socketSender = socketSender;
        this.itemID = itemID;
        this.bidTime = bidTime;
        initializeAlertDialogBuilder();
    }
    private void initializeAlertDialogBuilder()
    {
        alertDialogBuilder = new AlertDialog.Builder(activity);
        alertDialogBuilder.setTitle(R.string.CANCEL_BID_ALERTDIALOGTITLE)
                .setMessage(R.string.CANCEL_BID_ALERTDIALOGMSG)
                .setPositiveButton(R.string.CANCEL_BID_ALERTDIALOGOKBUTTON,
                        new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        showProgressDialog();
                        sendDataToSocket();
                    }
                });
    }
    public void setBidIDAndBidderID(String bidID, String bidderID)
    {
        this.bidID = bidID;
        this.bidderID = bidderID;
    }
    private String buildJSONData()
    {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id_bid", bidID);
            jsonObject.put("id_user_bidder", bidderID);
            jsonObject.put("id_item", itemID);
            jsonObject.put("bid_time", bidTime);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }
    private void sendDataToSocket()
    {
        socketSender.sendSocketData("cancelbid", buildJSONData());
    }
    public void showAlertDialog()
    {
        alertDialog = alertDialogBuilder.create();
        if (!activity.isFinishing()) {
            alertDialog.show();
        }
    }
    private void showProgressDialog()
    {
        progressDialog = ProgressDialog.show(activity, "Membatalkan penawaran...", "Membatalkan penawaran. Harap tunggu");
    }
    public void unshowProgressDialog()
    {
        progressDialog.dismiss();
    }
}
