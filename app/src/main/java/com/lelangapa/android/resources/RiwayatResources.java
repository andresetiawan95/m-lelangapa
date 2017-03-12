package com.lelangapa.android.resources;

/**
 * Created by andre on 11/03/17.
 */

public class RiwayatResources {
    private String idBid, idItem, idAuctioneer, namaAuctioneer, namaItem, hargaBid, bidTimestamp;
    private boolean winStatus;
    private Integer bidStatus, bidTime;

    public void setIdBid(String idBid)
    {
        this.idBid = idBid;
    }
    public void setIdItem(String idItem)
    {
        this.idItem = idItem;
    }
    public void setIdAuctioneer(String idAuctioneer)
    {
        this.idAuctioneer = idAuctioneer;
    }
    public void setNamaAuctioneer(String namaAuctioneer)
    {
        this.namaAuctioneer = namaAuctioneer;
    }
    public void setNamaItem(String namaItem)
    {
        this.namaItem = namaItem;
    }
    public void setHargaBid(String hargaBid)
    {
        this.hargaBid = hargaBid;
    }
    public void setBidTimestamp(String bidTimestamp)
    {
        this.bidTimestamp = bidTimestamp;
    }
    public void setWinStatus(boolean winStatus)
    {
        this.winStatus = winStatus;
    }
    public void setBidStatus(Integer bidStatus)
    {
        this.bidStatus = bidStatus;
    }
    public void setBidTime(Integer bidTime)
    {
        this.bidTime = bidTime;
    }

    public String getIdBid()
    {
        return idBid;
    }
    public String getIdItem()
    {
        return idItem;
    }
    public String getIdAuctioneer()
    {
        return idAuctioneer;
    }
    public String getNamaAuctioneer()
    {
        return namaAuctioneer;
    }
    public String getNamaItem()
    {
        return namaItem;
    }
    public String getHargaBid()
    {
        return hargaBid;
    }
    public String getBidTimestamp()
    {
        return bidTimestamp;
    }
    public boolean getWinStatus()
    {
        return winStatus;
    }
    public Integer getBidStatus()
    {
        return bidStatus;
    }
    public Integer getBidTime()
    {
        return bidTime;
    }
}
