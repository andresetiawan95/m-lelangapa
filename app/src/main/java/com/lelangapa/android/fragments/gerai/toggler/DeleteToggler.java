package com.lelangapa.android.fragments.gerai.toggler;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.lelangapa.android.R;
import com.lelangapa.android.apicalls.gerai.DeleteBarangAPI;
import com.lelangapa.android.apicalls.singleton.RequestController;
import com.lelangapa.android.interfaces.DataReceiver;

import java.util.HashMap;

/**
 * Created by andre on 09/05/17.
 */

public class DeleteToggler {
    private Activity activity;
    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog alertDialog;
    private ProgressDialog progressDialog;

    private HashMap<String, String> deleteData;
    private String itemID;
    private DataReceiver dataReceiver;

    public DeleteToggler(Activity activity, DataReceiver dataReceiver) {
        this.activity = activity;
        this.dataReceiver = dataReceiver;
        initializeConstant();
        initializeAlertDialog();
    }
    private void initializeConstant() {
        deleteData = new HashMap<>();
    }
    private void initializeAlertDialog() {
        alertDialogBuilder = new AlertDialog.Builder(activity);
        alertDialogBuilder.setTitle(R.string.DELETE_ITEM_GERAI_ALERTDIALOGTITLE)
                .setMessage(R.string.DELETE_ITEM_GERAI_ALERTDIALOGMSG)
                .setPositiveButton(R.string.DELETE_ITEM_GERAI_ALERTDIALOGOKBUTTON, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        showProgressDialog();
                        buildRequestData();
                        sendRequestToServer();
                    }
                })
                .setNegativeButton(R.string.DELETE_ITEM_GERAI_ALERTDIALOGCANCELBUTTON, null);
    }

    public void showAlertDialog() {
        alertDialog = alertDialogBuilder.create();
        if (!activity.isFinishing()) {
            alertDialog.show();
        }
    }
    private void showProgressDialog() {
        progressDialog = ProgressDialog.show(activity, "Menghapus item...", "Harap tunggu...");
    }
    public void unshowProgressDialog() {
        progressDialog.dismiss();
    }
    private void buildRequestData() {
        deleteData.put("item_id", itemID);
    }
    public void setItemIDToBeDeleted(String itemID) {
        this.itemID = itemID;
    }
    private void sendRequestToServer() {
        DeleteBarangAPI deleteAPI = new DeleteBarangAPI(deleteData, dataReceiver);
        RequestController.getInstance(activity).addToRequestQueue(deleteAPI);
    }
}
