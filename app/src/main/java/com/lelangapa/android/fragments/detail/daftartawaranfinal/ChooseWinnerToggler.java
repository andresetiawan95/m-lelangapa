package com.lelangapa.android.fragments.detail.daftartawaranfinal;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.lelangapa.android.R;
import com.lelangapa.android.apicalls.detail.daftartawaran.DaftarTawaranAPI;
import com.lelangapa.android.apicalls.singleton.RequestController;
import com.lelangapa.android.interfaces.DataReceiver;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by andre on 11/05/17.
 */

public class ChooseWinnerToggler {
    private Activity activity;
    private AlertDialog.Builder alertDialogBuilder, alertDialogBuilderSelectionSuccess, alertDialogBuilderSelectionFailed;
    private AlertDialog alertDialog;
    private ProgressDialog progressDialog;

    private String idBid, namaBidder, hargaBid;
    private HashMap<String, String> winnerData;
    private DataReceiver whenWinnerSelected, afterSelectionDoneReceiver;

    public ChooseWinnerToggler(Activity activity, DataReceiver dataReceiver) {
        this.activity = activity;
        this.afterSelectionDoneReceiver = dataReceiver;
        initializeConstants();
        initializeDataReceiver();
        initializeAlertDialogBuilder();
    }
    private void initializeConstants() {
        winnerData = new HashMap<>();
    }
    private void initializeDataReceiver() {
        whenWinnerSelected = new DataReceiver() {
            @Override
            public void dataReceived(Object output) {
                String response = output.toString();
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    if (jsonResponse.getString("status").equals("success")) {
                        //show alert dialog when selection success
                        unshowProgressDialog();
                        showAlertDialogSelectionSuccess();
                    }
                    else {
                        //show alert dialog when selection failed
                        unshowProgressDialog();
                        showAlertDialogSelectionFailed();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
    }
    private String buildMessageForAlertDialogChoosingWinner() {
        String messageForChoosingWinner1 = activity.getResources().getString(R.string.DETAILTAWARANFINAL_SELECTWINNER_ASK_ALERTDIALOGMSG1);
        String messageForChoosingWinner2 = activity.getResources().getString(R.string.DETAILTAWARANFINAL_SELECTWINNER_ASK_ALERTDIALOGMSG2);
        String messageForChoosingWinner3 = activity.getResources().getString(R.string.DETAILTAWARANFINAL_SELECTWINNER_ASK_ALERTDIALOGMSG3);
        return messageForChoosingWinner1 +" "+ namaBidder +" "+ messageForChoosingWinner2 +" "+ hargaBid +" "+ messageForChoosingWinner3;
    }
    private void initializeAlertDialogBuilder() {
        alertDialogBuilderSelectionSuccess = new AlertDialog.Builder(activity);
        alertDialogBuilderSelectionSuccess.setTitle(R.string.DETAILTAWARANFINAL_SELECTWINNER_SUCCESS_ALERTDIALOGTITLE)
                .setMessage(R.string.DETAILTAWARANFINAL_SELECTWINNER_SUCCESS_ALERTDIALOGMGS)
                .setPositiveButton(R.string.DETAILTAWARANFINAL_SELECTWINNER_SUCCESS_ALERTDIALOGOKBUTTON, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        afterSelectionDoneReceiver.dataReceived("done");
                    }
                });

        alertDialogBuilderSelectionFailed = new AlertDialog.Builder(activity);
        alertDialogBuilderSelectionFailed.setTitle(R.string.DETAILTAWARANFINAL_SELECTWINNER_FAILED_ALERTDIALOGTITLE)
                .setMessage(R.string.DETAILTAWARANFINAL_SELECTWINNER_FAILED_ALERTDIALOGMGS)
                .setPositiveButton(R.string.DETAILTAWARANFINAL_SELECTWINNER_FAILED_ALERTDIALOGOKBUTTON, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        afterSelectionDoneReceiver.dataReceived("failed");
                    }
                });
    }
    public void showAlertDialog() {
        alertDialogBuilder = new AlertDialog.Builder(activity);
        alertDialogBuilder.setTitle(R.string.DETAILTAWARANFINAL_SELECTWINNER_ASK_ALERTDIALOGTITLE)
                .setMessage(buildMessageForAlertDialogChoosingWinner())
                .setPositiveButton(R.string.DETAILTAWARANFINAL_SELECTWINNER_ASK_ALERTDIALOGOKBUTTON, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        showProgressDialog();
                        buildWinnerData();
                        sendWinnerDataToServer();
                    }
                })
                .setNegativeButton(R.string.DETAILTAWARANFINAL_SELECTWINNER_ASK_ALERTDIALOGCANCELBUTTON, null);

        alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
    private void showAlertDialogSelectionSuccess() {
        alertDialog = alertDialogBuilderSelectionSuccess.create();
        alertDialog.show();
    }
    private void showAlertDialogSelectionFailed() {
        alertDialog = alertDialogBuilderSelectionFailed.create();
        alertDialog.show();
    }
    private void showProgressDialog() {
        progressDialog = ProgressDialog.show(activity, "Memilih...", "Harap tunggu...");
    }
    private void unshowProgressDialog() {
        progressDialog.dismiss();
    }
    public void setInformation(String id, String nama, String harga) {
        this.idBid = id;
        this.namaBidder = nama;
        this.hargaBid = harga;
    }
    private void buildWinnerData() {
        winnerData.put("bid_id_query", idBid);
    }
    private void sendWinnerDataToServer() {
        DaftarTawaranAPI.SelectWinner selectWinnerAPI =
                DaftarTawaranAPI.instanceSelectWinner(winnerData, whenWinnerSelected);
        RequestController.getInstance(activity).addToRequestQueue(selectWinnerAPI);
    }
}
