package com.lelangapa.android.resources;

/**
 * Created by andre on 25/04/17.
 */

public class ItemImageResources extends ImageResources {
    String uniqueIDImage, imageURL;
    boolean isChanged, isMainImage;
    public ItemImageResources()
    {
        isChanged = false;
    }

    public boolean isMainImage() {
        return isMainImage;
    }

    public void setMainImage(boolean mainImage) {
        isMainImage = mainImage;
    }

    public String getUniqueIDImage() {
        return uniqueIDImage;
    }
    public void setUniqueIDImage(String uniqueIDImage) {
        this.uniqueIDImage = uniqueIDImage;
    }
    public String getImageURL() {
        return imageURL;
    }
    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
    public void setImageChanged(boolean bool)
    {
        this.isChanged = bool;
    }
    public boolean isImageChanged()
    {
        return isChanged;
    }
}
