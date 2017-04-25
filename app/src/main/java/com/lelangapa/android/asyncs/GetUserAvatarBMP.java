package com.lelangapa.android.asyncs;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.lelangapa.android.interfaces.DataReceiver;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by andre on 22/04/17.
 */

public class GetUserAvatarBMP extends AsyncTask<String, Void, Bitmap> {
    private DataReceiver whenAvatarLoaded;
    public GetUserAvatarBMP(DataReceiver dataReceiver)
    {
        this.whenAvatarLoaded = dataReceiver;
    }
    @Override
    protected void onPreExecute()
    {
        super.onPreExecute();
    }
    @Override
    protected Bitmap doInBackground(String... params) {
        String url = params[0];
        URL url_image;
        Bitmap image = null;
        try {
            url_image = new URL(url);
            image = BitmapFactory.decodeStream(url_image.openConnection().getInputStream());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }
    @Override
    protected void onPostExecute(Bitmap b)
    {
        super.onPostExecute(b);
        whenAvatarLoaded.dataReceived(b);
    }
}
