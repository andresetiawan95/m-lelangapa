package com.lelangapa.android.apicalls.chat;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.lelangapa.android.interfaces.DataReceiver;
import com.lelangapa.android.preferences.SessionManager;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by andre on 25/03/17.
 */

public class ChatRoomListAPI {
    public static class GetRoomList extends StringRequest {
        private static final String GETCHATROOMURL = "https://no-api.lelangapa.com/apis/v1/chat/rooms";
        private static String userID;
        private GetRoomList(String userID, final DataReceiver dataReceiver)
        {
            super(Method.GET, GETCHATROOMURL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    dataReceiver.dataReceived(response);
                }
            }, null);
            GetRoomList.userID = userID;
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
    }

    public static GetRoomList getInstanceRoomList(String userID, DataReceiver dataReceiver)
    {
        return new GetRoomList(userID, dataReceiver);
    }
}
