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
 * Created by andre on 06/03/17.
 */

public class UnfavoriteToggler {
    private AlertDialog.Builder favoriteAlertDialogBuilder;
    private AlertDialog favoriteAlertDialog;
    private UnfavoriteProgressDialog progressDialog;
    private Activity activity;

    private DataReceiver doneRemoveReceived, doneFavoriteStatusLoadReceived, favoriteStatusReceived;
    private String userID, itemID, favoriteID;
    private HashMap<String, String> dataFavorite;

    public UnfavoriteToggler(Activity activity, String userID, String itemID, DataReceiver favoriteReceiver)
    {
        this.activity = activity;
        this.userID = userID;
        this.itemID = itemID;
        this.favoriteStatusReceived = favoriteReceiver;
        this.dataFavorite = new HashMap<>();

        initializeProgressDialog();
        initializeAlertDialogBuilder();
        initializeDataReceivers();
    }
    private void initializeProgressDialog()
    {
        progressDialog = new UnfavoriteProgressDialog(activity);
    }
    private void initializeAlertDialogBuilder()
    {
        this.favoriteAlertDialogBuilder = new AlertDialog.Builder(activity);
        favoriteAlertDialogBuilder
                .setTitle(R.string.DETAILFRAGMENT_UNFAVORITE_ALERTDIALOGTITLE)
                .setMessage(R.string.DETAILFRAGMENT_UNFAVORITE_ALERTDIALOGMSG)
                .setPositiveButton(R.string.DETAILFRAGMENT_UNFAVORITE_ALERTDIALOGOKBUTTON,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                progressDialog.showDialog();
                                removeFromFavorite();
                            }
                        })
                .setNegativeButton(R.string.DETAILFRAGMENT_UNFAVORITE_ALERTDIALOGCANCELBUTTON,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
    }
    private void initializeDataReceivers()
    {
        doneRemoveReceived = new DataReceiver() {
            @Override
            public void dataReceived(Object output) {
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
    private void initializeDataFavorite()
    {
        dataFavorite.put("id_user", userID);
        dataFavorite.put("id_item", itemID);
        dataFavorite.put("id_favorite", favoriteID);
    }
    public void setFavoriteID(String favoriteID)
    {
        this.favoriteID = favoriteID;
        initializeDataFavorite();
    }
    public void showAlertDialog()
    {
        favoriteAlertDialog = favoriteAlertDialogBuilder.create();
        if (!activity.isFinishing())
        {
            favoriteAlertDialog.show();
        }
    }
    private void removeFromFavorite()
    {
        FavoriteAPI favoriteAPI = new FavoriteAPI();
        FavoriteAPI.RemoveFavorite removeFavoriteAPI =
                favoriteAPI.initializeRemoveFavorite(dataFavorite, doneRemoveReceived);
        RequestController.getInstance(activity).addToRequestQueue(removeFavoriteAPI);
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
