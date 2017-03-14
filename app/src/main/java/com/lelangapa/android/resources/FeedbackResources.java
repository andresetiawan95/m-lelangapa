package com.lelangapa.android.resources;

/**
 * Created by andre on 14/03/17.
 */

public class FeedbackResources {
    private String idRatinglogs, idItem, idUser, namaUser, namaItem;
    private Integer bidTime;
    private String statusUser;

    private boolean statusRating;

    public String getIdRatinglogs() {
        return idRatinglogs;
    }

    public void setIdRatinglogs(String idRatinglogs) {
        this.idRatinglogs = idRatinglogs;
    }

    public String getIdItem() {
        return idItem;
    }

    public void setIdItem(String idItem) {
        this.idItem = idItem;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getNamaUser() {
        return namaUser;
    }

    public void setNamaUser(String namaUser) {
        this.namaUser = namaUser;
    }

    public String getNamaItem() {
        return namaItem;
    }

    public void setNamaItem(String namaItem) {
        this.namaItem = namaItem;
    }

    public Integer getBidTime() {
        return bidTime;
    }

    public void setBidTime(Integer bidTime) {
        this.bidTime = bidTime;
    }

    public boolean isStatusRating() {
        return statusRating;
    }

    public void setStatusRating(boolean statusRating) {
        this.statusRating = statusRating;
    }

    public String getStatusUser() {
        return statusUser;
    }

    public void setStatusUser(String statusUser) {
        this.statusUser = statusUser;
    }
}
