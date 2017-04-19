package com.lelangapa.android.fragments.detail.ownerblocklist;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.lelangapa.android.R;
import com.lelangapa.android.interfaces.DataReceiver;

import java.util.HashMap;

/**
 * Created by andre on 19/04/17.
 */

public class UnblockToggler {
    private Activity activity;
    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog alertDialog;
    private ProgressDialog progressDialog;

    private HashMap<String, String> unblockData;
    private String itemID, auctioneerID, bidderID;
    private Integer bidTime;

    private DataReceiver dataReceiver;

    public UnblockToggler(Activity activity, DataReceiver dataReceiver, String itemID, String auctioneerID, Integer bidTime) {
        this.activity = activity;
        this.dataReceiver = dataReceiver;
        this.itemID = itemID;
        this.auctioneerID = auctioneerID;
        this.bidTime = bidTime;
        initializeConstant();
        initializeAlertDialog();
    }
    private void initializeConstant() {
        unblockData = new HashMap<>();
    }
    private void initializeAlertDialog() {
        alertDialogBuilder = new AlertDialog.Builder(activity);
        alertDialogBuilder.setTitle(R.string.UNBLOCK_USER_ALERTDIALOGTITLE)
                .setMessage(R.string.UNBLOCK_USER_ALERTDIALOGMSG)
                .setPositiveButton(R.string.UNBLOCK_USER_ALERTDIALOGOKBUTTON, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        showProgressDialog();
                        buildRequestBodyData();
                        sendUnblockDataToServer();
                    }
                })
                .setNegativeButton(R.string.UNBLOCK_USER_ALERTDIALOGCANCELBUTTON, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
    }
    public void setBidderIDToUnblock(String bidderID)
    {
        this.bidderID = bidderID;
    }
    public void showAlertDialog()
    {
        alertDialog = alertDialogBuilder.create();
        if (!activity.isFinishing()) {
            alertDialog.show();
        }
    }
    private void buildRequestBodyData() {
        unblockData.put("id_item", itemID);
        unblockData.put("id_user_auctioneer", auctioneerID);
        unblockData.put("id_user_blocked", bidderID);
        unblockData.put("bid_time", Integer.toString(bidTime));
    }
    private void showProgressDialog() {
        progressDialog = ProgressDialog.show(activity, "unblock pengguna", "Harap tunggu");
    }
    public void unshowProgressDialog() {
        progressDialog.dismiss();
    }
    private void sendUnblockDataToServer()
    {

    }
}
