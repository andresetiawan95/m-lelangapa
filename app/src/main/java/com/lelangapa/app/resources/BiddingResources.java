package com.lelangapa.app.resources;

/**
 * Created by andre on 09/01/17.
 */

public class BiddingResources {
    private String idBidder, namaBidder, hargaBid, avatarBidderUrl;
    private String idBid;
    private boolean winnerStatus;
    public void setIdBidder(String idBidder)
    {
        this.idBidder = idBidder;
    }
    public void setIdBid(String idBid)
    {
        this.idBid = idBid;
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
    public String getIdBid()
    {
        return idBid;
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

    public boolean isWinnerStatus() {
        return winnerStatus;
    }

    public void setWinnerStatus(boolean winnerStatus) {
        this.winnerStatus = winnerStatus;
    }
}
