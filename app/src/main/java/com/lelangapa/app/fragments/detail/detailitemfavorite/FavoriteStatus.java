package com.lelangapa.app.fragments.detail.detailitemfavorite;

import android.app.Activity;

import com.lelangapa.app.apicalls.favorite.FavoriteAPI;
import com.lelangapa.app.apicalls.singleton.RequestController;
import com.lelangapa.app.interfaces.DataReceiver;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by andre on 04/03/17.
 */

public class FavoriteStatus {
    private DataReceiver favoriteStatusReceiver;
    private String userID, itemID;
    private Activity activity;

    public FavoriteStatus(String userID, String itemID, Activity activity)
    {
        this.userID = userID;
        this.itemID = itemID;
        this.activity = activity;
    }
    public void setFavoriteStatusReceiver(DataReceiver dataReceiver)
    {
        this.favoriteStatusReceiver = dataReceiver;
        getFavoriteStatusOnUserIDAndItemID();
    }
    private DataReceiver setDataReceiverWhenRespondReceived()
    {
        DataReceiver dataReceiver = new DataReceiver() {
            @Override
            public void dataReceived(Object output) {
                String response = output.toString();
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    JSONArray responseArray = jsonResponse.getJSONArray("data");
                    JSONObject favoriteStatusObject = responseArray.getJSONObject(0);

                    String idFavorite = favoriteStatusObject.getString("id_favorite_return");
                    favoriteStatusReceiver.dataReceived(idFavorite);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        return dataReceiver;
    }
    private void getFavoriteStatusOnUserIDAndItemID()
    {
        DataReceiver dataReceiver = setDataReceiverWhenRespondReceived();
        FavoriteAPI favoriteAPI = new FavoriteAPI();
        FavoriteAPI.GetFavoriteItemIDAndUserID favoriteItemIDAndUserIDAPI =
                favoriteAPI.initializeGetFavoriteItemIDAndUserID
                        (userID, itemID, dataReceiver);
        RequestController.getInstance(activity).addToRequestQueue(favoriteItemIDAndUserIDAPI);
    }
}
