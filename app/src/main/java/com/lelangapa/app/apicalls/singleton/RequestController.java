package com.lelangapa.app.apicalls.singleton;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * Created by andre on 02/02/17.
 */

public class RequestController {
    private static RequestController mRequestControllerInstance;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    private Context mContext;

    private RequestController (Context context)
    {
        this.mContext = context;
        this.mRequestQueue = getRequestQueue();
    }
    public static synchronized RequestController getInstance(Context context)
    {
        if (mRequestControllerInstance == null)
        {
            mRequestControllerInstance = new RequestController(context);
        }
        return mRequestControllerInstance;
    }
    public RequestQueue getRequestQueue()
    {
        if (mRequestQueue == null)
        {
            mRequestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
        }
        return mRequestQueue;
    }
    public <T> void addToRequestQueue(Request<T> req) {
        mRequestQueue.getCache().clear();
        req.setShouldCache(false);
        Log.v("CLEARD", "CACHE CLEARED");
        getRequestQueue().add(req);
    }
    public void cancelAllRequest(String tag) {
        mRequestQueue.cancelAll(tag);
    }
}
