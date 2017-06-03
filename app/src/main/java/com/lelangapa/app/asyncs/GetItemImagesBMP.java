package com.lelangapa.app.asyncs;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import com.lelangapa.app.interfaces.ImageLoaderReceiver;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by andre on 22/04/17.
 */

public class GetItemImagesBMP extends AsyncTask<String[], Void, Bitmap[]> {
    private ImageLoaderReceiver imageLoaderReceiver;
    public GetItemImagesBMP(ImageLoaderReceiver receiver)
    {
        imageLoaderReceiver = receiver;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }
    @Override
    protected Bitmap[] doInBackground(String[]... params) {
        String[] listURL = params[0];
        Bitmap[] listBitmapImages = new Bitmap[listURL.length];

        for (int x=0;x<listURL.length;x++) {
            try {
                Log.v("URL", listURL[x]);
                URL url_image = new URL(listURL[x]);
                listBitmapImages[x] = BitmapFactory.decodeStream(url_image.openConnection().getInputStream());
                if (listBitmapImages[x] != null) {
                    Log.v("Bitmap loaded", "bitmap on index " + x + " loaded");
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return listBitmapImages;
    }
    @Override
    protected void onPostExecute(Bitmap[] arrayBitmap) {
        super.onPostExecute(arrayBitmap);
        imageLoaderReceiver.loaded(arrayBitmap);
    }
}
