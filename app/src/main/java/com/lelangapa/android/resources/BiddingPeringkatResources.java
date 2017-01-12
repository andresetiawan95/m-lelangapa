package com.lelangapa.android.resources;

/**
 * Created by andre on 09/01/17.
 */

public class BiddingPeringkatResources {
    private String idBidder, namaBidder, hargaBid, avatarBidderUrl;
    public void setIdBidder(String idBidder)
    {
        this.idBidder = idBidder;
    }
    public void setNamaBidder(String namaBidder)
    {
        this.namaBidder = namaBidder;
    }
    public void setHargaBid(String hargaBid)
    {
        this.hargaBid = hargaBid;
    }
    public void setAvatarBidderUrl(String url)
    {
        this.avatarBidderUrl = url;
    }
    public String getIdBidder()
    {
        return idBidder;
    }
    public String getNamaBidder()
    {
        return namaBidder;
    }
    public String getHargaBid()
    {
        return hargaBid;
    }
    public String getAvatarBidderUrl()
    {
        return avatarBidderUrl;
    }
}
