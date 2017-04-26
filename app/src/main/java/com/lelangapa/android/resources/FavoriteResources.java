package com.lelangapa.android.resources;

/**
 * Created by andre on 25/02/17.
 */

public class FavoriteResources {
    private String idFavorite;
    private String idItemFavorite;
    private String namaItemFavorite;
    private String idUserAuctioneer;
    private String namaUserAuctioneerItemFavorite;
    private String timeListedItemFavorite;
    private String imageURLItem;
    public String getIdUserAuctioneer() {
        return idUserAuctioneer;
    }

    public void setIdUserAuctioneer(String idUserAuctioneer) {
        this.idUserAuctioneer = idUserAuctioneer;
    }
    public void setIdFavorite(String idFavorite)
    {
        this.idFavorite = idFavorite;
    }
    public void setIdItemFavorite(String idItemFavorite)
    {
        this.idItemFavorite = idItemFavorite;
    }
    public void setNamaItemFavorite(String namaItemFavorite)
    {
        this.namaItemFavorite = namaItemFavorite;
    }
    public void setNamaUserAuctioneerItemFavorite(String namaUserAuctioneerItemFavorite)
    {
        this.namaUserAuctioneerItemFavorite = namaUserAuctioneerItemFavorite;
    }
    public void setTimeListedItemFavorite(String timeListedItemFavorite)
    {
        this.timeListedItemFavorite = timeListedItemFavorite;
    }
    public void setImageURLItem(String imageURLItem)
    {
        this.imageURLItem = imageURLItem;
    }

    public String getIdFavorite()
    {
        return this.idFavorite;
    }
    public String getIdItemFavorite()
    {
        return this.idItemFavorite;
    }
    public String getNamaItemFavorite()
    {
        return this.namaItemFavorite;
    }
    public String getNamaUserAuctioneerItemFavorite()
    {
        return this.namaUserAuctioneerItemFavorite;
    }
    public String getTimeListedItemFavorite()
    {
        return this.timeListedItemFavorite;
    }
    public String getImageURLItem()
    {
        return this.imageURLItem;
    }
}
