package com.lelangapa.app.resources;

import android.graphics.Bitmap;

/**
 * Created by andre on 04/04/17.
 */

public class ImageResources {
    private String idImage;
    private String base64ImageString;
    private Bitmap bitmap;
    public String getIdImage() {
        return idImage;
    }

    public void setIdImage(String idImage) {
        this.idImage = idImage;
    }

    public String getBase64ImageString() {
        return base64ImageString;
    }

    public void setBase64ImageString(String base64ImageString) {
        this.base64ImageString = base64ImageString;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
