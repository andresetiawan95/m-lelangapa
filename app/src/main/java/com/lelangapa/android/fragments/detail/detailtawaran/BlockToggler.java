package com.lelangapa.android.fragments.detail.detailtawaran;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.lelangapa.android.R;
import com.lelangapa.android.apicalls.detail.daftartawaran.DaftarTawaranAPI;
import com.lelangapa.android.apicalls.singleton.RequestController;
import com.lelangapa.android.interfaces.DataReceiver;

import java.util.HashMap;

/**
 * Created by andre on 03/04/17.
 */

public class BlockToggler {
    private Activity activity;
    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog alertDialog;
    private ProgressDialog progressDialog;

    private HashMap<String, String> blockData;
    private String itemID, auctioneerID, bidderID;
    private Integer bidTime;

    private DataReceiver dataReceiver;

    public BlockToggler(Activity activity, DataReceiver dataReceiver, String itemID, String auctioneerID, Integer bidTime)
    {
        this.activity = activity;
        this.dataReceiver = dataReceiver;
        this.itemID = itemID;
        this.auctioneerID = auctioneerID;
        this.bidTime = bidTime;
        initializeConstant();
        initializeAlertDialog();
    }
    private void initializeConstant()
    {
        blockData = new HashMap<>();
    }
    private void initializeAlertDialog()
    {
        alertDialogBuilder = new AlertDialog.Builder(activity);
        alertDialogBuilder.setTitle(R.string.BLOCK_USER_ALERTDIALOGTITLE)
                .setMessage(R.string.BLOCK_USER_ALERTDIALOGMSG)
                .setPositiveButton(R.string.BLOCK_USER_ALERTDIALOGOKBUTTON, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        showProgressDialog();
                        buildRequestBodyData();
                        sendBlockDataToServer();
                    }
                });
    }
    private void buildRequestBodyData()
    {
        blockData.put("id_item", itemID);
        blockData.put("id_user_auctioneer", auctioneerID);
        blockData.put("id_user_blocked", bidderID);
        blockData.put("bid_time", Integer.toString(bidTime));
    }
    public void setBidderIDToBlock(String bidderID)
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
    private void showProgressDialog()
    {
        progressDialog = ProgressDialog.show(activity, "Block pengguna", "Harap tunggu");
    }
    public void unshowProgressDialog()
    {
        progressDialog.dismiss();
    }
    private void sendBlockDataToServer()
    {
        DaftarTawaranAPI.BlockUser blockUserAPI
                = DaftarTawaranAPI.instanceBlockUser(blockData, dataReceiver);
        RequestController.getInstance(activity).addToRequestQueue(blockUserAPI);
    }
}
