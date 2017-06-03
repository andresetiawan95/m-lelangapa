package com.lelangapa.app.fragments.detail.detailitemfavorite;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.lelangapa.app.R;
import com.lelangapa.app.apicalls.favorite.FavoriteAPI;
import com.lelangapa.app.apicalls.singleton.RequestController;
import com.lelangapa.app.interfaces.DataReceiver;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by andre on 05/03/17.
 */

public class FavoriteToggler {
    private AlertDialog.Builder favoriteAlertDialogBuilder;
    private AlertDialog favoriteAlertDialog;
    private FavoriteProgressDialog progressDialog;
    private Activity activity;

    private DataReceiver doneInsertReceived, doneFavoriteStatusLoadReceived,favoriteStatusReceived;
    private String userID, itemID;
    private HashMap<String, String> dataFavorite;
    public FavoriteToggler(Activity activity, String userID, String itemID, DataReceiver favoriteReceiver)
    {
        this.activity = activity;
        dataFavorite = new HashMap<>();
        dataFavorite.put("id_user", userID);
        dataFavorite.put("id_item", itemID);
        this.userID = userID;
        this.itemID = itemID;
        this.favoriteStatusReceived = favoriteReceiver;
        initializeProgressDialog();
        initializeAlertDialogBuilder();
        initializeDataReceivers();
    }
    private void initializeProgressDialog()
    {
        progressDialog = new FavoriteProgressDialog(activity);
    }
    private void initializeAlertDialogBuilder()
    {
        this.favoriteAlertDialogBuilder = new AlertDialog.Builder(activity);
        favoriteAlertDialogBuilder
                .setTitle(R.string.DETAILFRAGMENT_FAVORITE_ALERTDIALOGTITLE)
                .setMessage(R.string.DETAILFRAGMENT_FAVORITE_ALERTDIALOGMSG)
                .setPositiveButton(R.string.DETAILFRAGMENT_FAVORITE_ALERTDIALOGOKBUTTON,
                        new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        progressDialog.showDialog();
                        postToFavorite();
                    }
                })
                .setNegativeButton(R.string.DETAILFRAGMENT_FAVORITE_ALERTDIALOGCANCELBUTTON,
                        new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        favoriteAlertDialog.dismiss();
                    }
                });
    }
    private void initializeDataReceivers()
    {
        doneInsertReceived = new DataReceiver() {
            @Override
            public void dataReceived(Object output) {
                //jika insertion favorit sudah berhasil
                String response = output.toString();
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    String status = jsonResponse.getString("status");
                    if (status.equals("success")){
                        loadFavoriteStatus();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        doneFavoriteStatusLoadReceived = new DataReceiver() {
            @Override
            public void dataReceived(Object output) {
                //jika loadFavoriteStatus sudah selesai
                String response = output.toString();
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    JSONArray responseArray = jsonResponse.getJSONArray("data");
                    JSONObject favoriteStatusObject = responseArray.getJSONObject(0);

                    String idFavorite = favoriteStatusObject.getString("id_favorite_return");
                    progressDialog.unshowDialog();
                    favoriteStatusReceived.dataReceived(idFavorite);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
    }
    public void showAlertDialog()
    {
        favoriteAlertDialog = favoriteAlertDialogBuilder.create();
        if (!activity.isFinishing())
        {
            favoriteAlertDialog.show();
        }
    }
    private void postToFavorite()
    {
        FavoriteAPI favoriteAPI = new FavoriteAPI();
        FavoriteAPI.InsertFavorite insertFavoriteAPI =
                favoriteAPI.initializeInsertFavorite(dataFavorite, doneInsertReceived);
        RequestController.getInstance(activity).addToRequestQueue(insertFavoriteAPI);
    }
    private void loadFavoriteStatus()
    {
        FavoriteAPI favoriteAPI = new FavoriteAPI();
        FavoriteAPI.GetFavoriteItemIDAndUserID favoriteItemIDAndUserIDAPI =
                favoriteAPI.initializeGetFavoriteItemIDAndUserID
                        (userID, itemID, doneFavoriteStatusLoadReceived);
        RequestController.getInstance(activity).addToRequestQueue(favoriteItemIDAndUserIDAPI);
    }
}