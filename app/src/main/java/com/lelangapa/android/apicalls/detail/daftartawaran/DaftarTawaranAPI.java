package com.lelangapa.android.apicalls.detail.daftartawaran;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.lelangapa.android.interfaces.DataReceiver;
import com.lelangapa.android.preferences.SessionManager;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by andre on 02/04/17.
 */

public class DaftarTawaranAPI {
    public static class GetOfferListAPI extends StringRequest
    {
        private static final String GETOFFERLISTURL = "https://no-api.lelangapa.com/apis/v1/bids/item/";
        private GetOfferListAPI(String urlparams, final DataReceiver dataReceiver)
        {
            super(Method.GET, GETOFFERLISTURL + urlparams, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    dataReceiver.dataReceived(response);
                }
            }, null);
        }
        @Override
        public Map<String, String> getHeaders() throws AuthFailureError
        {
            Map<String, String> params = new HashMap<>();
            if (SessionManager.isLoggedInStatic()) {
                params.put("token", SessionManager.getUserTokenStatic());
                Log.v("HEADER", params.toString());
            }
            return params;
        }
    }
    public static class BlockUser extends StringRequest
    {
        private HashMap<String, String> data = new HashMap<>();
        private static final String BLOCKUSERURL = "https://no-api.lelangapa.com/apis/v1/bids/block";
        private BlockUser(HashMap<String, String> data, final DataReceiver dataReceiver)
        {
            super(Method.POST, BLOCKUSERURL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    dataReceiver.dataReceived(response);
                }
            }, null);
            this.data = data;
        }
        @Override
        public Map<String, String> getHeaders() throws AuthFailureError
        {
            Map<String, String> params = new HashMap<>();
            if (SessionManager.isLoggedInStatic()) {
                params.put("token", SessionManager.getUserTokenStatic());
                Log.v("HEADER", params.toString());
            }
            return params;
        }
        @Override
        public HashMap<String, String> getParams(){
            return data;
        }
    }
    public static class UnblockUser extends StringRequest
    {
        private HashMap<String, String> data = new HashMap<>();
        private static final String UNBLOCKUSERURL = "https://no-api.lelangapa.com/apis/v1/bids/unblock";
        private UnblockUser(HashMap<String, String> data, final DataReceiver dataReceiver)
        {
            super(Method.POST, UNBLOCKUSERURL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    dataReceiver.dataReceived(response);
                }
            }, null);
            this.data = data;
        }
        @Override
        public Map<String, String> getHeaders() throws AuthFailureError
        {
            Map<String, String> params = new HashMap<>();
            if (SessionManager.isLoggedInStatic()) {
                params.put("token", SessionManager.getUserTokenStatic());
                Log.v("HEADER", params.toString());
            }
            return params;
        }
        @Override
        public HashMap<String, String> getParams(){
            return data;
        }
    }
    public static GetOfferListAPI instanceGetOfferList(String urlparams, DataReceiver dataReceiver) {
        return new GetOfferListAPI(urlparams, dataReceiver);
    }
    public static BlockUser instanceBlockUser(HashMap<String, String> data, DataReceiver dataReceiver) {
        return new BlockUser(data, dataReceiver);
    }
    public static UnblockUser instanceUnblockUser(HashMap<String, String> data, DataReceiver dataReceiver) {
        return new UnblockUser(data, dataReceiver);
    }
}
