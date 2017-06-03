package com.lelangapa.app.fragments.detail.detailtawaran;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.lelangapa.app.R;
import com.lelangapa.app.interfaces.SocketSender;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by andre on 03/04/17.
 */

public class ChooseWinnerToggler {
    private Activity activity;
    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog alertDialog;
    private ProgressDialog progressDialog;

    private String itemID, bidID;

    private SocketSender socketSender;

    public ChooseWinnerToggler(Activity activity, SocketSender socketSender, String itemID)
    {
        this.activity = activity;
        this.socketSender = socketSender;
        this.itemID = itemID;
        initializeAlertDialogBuilder();
    }
    private void initializeAlertDialogBuilder()
    {
        alertDialogBuilder = new AlertDialog.Builder(activity);
        alertDialogBuilder.setTitle(R.string.DETAILFRAGMENT_WINNERSELECTED_ALERTDIALOGTITLE)
                .setMessage(R.string.DETAILFRAGMENT_WINNERSELECTED_ALERTDIALOGMSG)
                .setPositiveButton(R.string.DETAILFRAGMENT_WINNERSELECTED_ALERTDIALOGBUTTON,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                showProgressDialog();
                                sendDataToSocket();
                            }
                        });
    }
    public void setBidID(String bidID)
    {
        this.bidID = bidID;
    }
    private String buildJSONData()
    {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("bid_id_query", bidID);
            jsonObject.put("item_id_query", itemID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }
    private void sendDataToSocket()
    {
        socketSender.sendSocketData("winnerselected", buildJSONData());
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
        progressDialog = ProgressDialog.show(activity, "Memilih pemenang...", "Memilih pemenang. Harap tunggu");
    }
    public void unshowProgressDialog()
    {
        progressDialog.dismiss();
    }
}
