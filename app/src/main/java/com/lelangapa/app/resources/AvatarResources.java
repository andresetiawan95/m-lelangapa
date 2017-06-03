package com.lelangapa.app.resources;

/**
 * Created by andre on 24/04/17.
 */

public class AvatarResources extends ImageResources {
    boolean isChanged;
    public AvatarResources()
    {
        this.isChanged = false;
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
