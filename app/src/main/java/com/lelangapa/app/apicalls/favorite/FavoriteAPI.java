package com.lelangapa.app.apicalls.favorite;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.lelangapa.app.interfaces.DataReceiver;
import com.lelangapa.app.preferences.SessionManager;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by andre on 24/02/17.
 */

public class  FavoriteAPI {
    public class GetFavoriteItemIDAndUserID extends StringRequest {
        private static final String GETFAVORITEUSERITEMURL = "https://no-api.lelangapa.com/apis/v1/favorites/";
        public GetFavoriteItemIDAndUserID(String userID, String itemID, final DataReceiver dataReceiver){
            super(Method.GET, GETFAVORITEUSERITEMURL + userID + "-" + itemID, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    dataReceiver.dataReceived(response);
                }
            }, null);
        }
        @Override
        protected Response<String> parseNetworkResponse(NetworkResponse networkResponse)
        {
            //Log.v("Header GETFAVORITE", networkResponse.headers.toString());
            return super.parseNetworkResponse(networkResponse);
        }
        @Override
        public Map<String, String> getHeaders() throws AuthFailureError {
            Map<String, String>  params = new HashMap<String, String>();
            params.put("Cache-Control", "max-age=0");
            if (SessionManager.isLoggedInStatic()) {
                params.put("token", SessionManager.getUserTokenStatic());
            }
            //Log.v("REQUEST HEADER", params.toString());
            return params;
        }
    }

    public static class GetFavorite extends StringRequest {
        private static final String GETFAVORITEURL = "https://no-api.lelangapa.com/apis/v1/favorites/";
        public GetFavorite(String userID, final DataReceiver dataReceiver){
            super(Method.GET, GETFAVORITEURL + userID, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    dataReceiver.dataReceived(response);
                }
            }, null);
        }
        @Override
        protected Response<String> parseNetworkResponse(NetworkResponse networkResponse)
        {
            //Log.v("Header LALALALLAA", networkResponse.headers.toString());
            return super.parseNetworkResponse(networkResponse);
        }
        @Override
        public Map<String, String> getHeaders() throws AuthFailureError {
            Map<String, String>  params = new HashMap<String, String>();
            params.put("Cache-Control", "max-age=0");
            if (SessionManager.isLoggedInStatic()) {
                params.put("token", SessionManager.getUserTokenStatic());
             }
            //Log.v("REQUEST HEADER", params.toString());
            return params;
        }
    }

    public class InsertFavorite extends StringRequest {
        private HashMap<String, String> dataFavorite;
        private static final String INSERTFAVORITEURL = "https://no-api.lelangapa.com/apis/v1/favorites";
        public InsertFavorite(HashMap<String, String> dataFavorite, final DataReceiver dataReceiver){
            super(Method.POST, INSERTFAVORITEURL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    dataReceiver.dataReceived(response);
                }
            }, null);
            this.dataFavorite = dataFavorite;
        }
        @Override
        public Map<String, String> getHeaders() throws AuthFailureError
        {
            Map<String, String> params = new HashMap<>();
            if (SessionManager.isLoggedInStatic()) {
                params.put("token", SessionManager.getUserTokenStatic());
                //Log.v("HEADER", params.toString());
            }
            return params;
        }
        @Override
        public HashMap<String, String> getParams()
        {
            return dataFavorite;
        }
    }

    public class RemoveFavorite extends StringRequest {
        private HashMap<String, String> dataFavorite;
        private static final String REMOVEFAVORITEURL = "https://no-api.lelangapa.com/apis/v1/favorites";
        public RemoveFavorite(HashMap<String, String> dataFavorite, final DataReceiver dataReceiver){
            super(Method.PUT, REMOVEFAVORITEURL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    dataReceiver.dataReceived(response);
                }
            }, null);
            this.dataFavorite = dataFavorite;
        }
        @Override
        public Map<String, String> getHeaders() throws AuthFailureError
        {
            Map<String, String> params = new HashMap<>();
            if (SessionManager.isLoggedInStatic()) {
                params.put("token", SessionManager.getUserTokenStatic());
                //Log.v("HEADER", params.toString());
            }
            return params;
        }
        @Override
        public HashMap<String, String> getParams()
        {
            return dataFavorite;
        }
    }

    public static GetFavorite initializeGetFavorite(String userID, DataReceiver dataReceiver)
    {
        GetFavorite getFavorite = new GetFavorite(userID, dataReceiver);
        return getFavorite;
    }

    public GetFavoriteItemIDAndUserID initializeGetFavoriteItemIDAndUserID
            (String userID, String itemID, DataReceiver dataReceiver)
    {
        GetFavoriteItemIDAndUserID getFavoriteItemIDAndUserID
                = new GetFavoriteItemIDAndUserID(userID, itemID, dataReceiver);
        return getFavoriteItemIDAndUserID;
    }

    public InsertFavorite initializeInsertFavorite(HashMap<String, String> dataFavorite, DataReceiver dataReceiver)
    {
        InsertFavorite insertFavorite = new InsertFavorite(dataFavorite, dataReceiver);
        return insertFavorite;
    }

    public RemoveFavorite initializeRemoveFavorite(HashMap<String, String> dataFavorite, DataReceiver dataReceiver)
    {
        RemoveFavorite removeFavorite = new RemoveFavorite(dataFavorite, dataReceiver);
        return removeFavorite;
    }
}
