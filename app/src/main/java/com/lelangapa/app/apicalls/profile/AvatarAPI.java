package com.lelangapa.app.apicalls.profile;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.lelangapa.app.interfaces.DataReceiver;

import java.util.HashMap;

/**
 * Created by andre on 24/04/17.
 */

public class AvatarAPI {
    public static class UploadAvatar extends StringRequest
    {
        private HashMap<String, String> data;
        private static final String UPLOADAVATARURL = "http://es3.lelangapa.com/avatar.php";
        private UploadAvatar(HashMap<String, String> data, final DataReceiver dataReceiver)
        {
            super(Method.POST, UPLOADAVATARURL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    dataReceiver.dataReceived(response);
                }
            }, null);
            this.data = data;
        }
        @Override
        public HashMap<String, String> getParams() {
           return data;
        }
    }

    public static UploadAvatar instanceUploadAvatar(HashMap<String, String> data, DataReceiver dataReceiver)
    {
        return new UploadAvatar(data, dataReceiver);
    }
}
