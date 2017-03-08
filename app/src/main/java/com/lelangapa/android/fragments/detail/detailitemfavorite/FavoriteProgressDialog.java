package com.lelangapa.android.fragments.detail.detailitemfavorite;

import android.app.Activity;
import android.app.ProgressDialog;

/**
 * Created by andre on 05/03/17.
 */

public class FavoriteProgressDialog {
    private ProgressDialog favoriteDialog;
    private Activity activity;
    public FavoriteProgressDialog(Activity activity)
    {
        this.activity = activity;
    }
    public void showDialog()
    {
        favoriteDialog = ProgressDialog.show(activity, "Sedang diproses", "Memasukkan ke daftar favorit." +
                " Harap tunggu.");
    }
    public void unshowDialog()
    {
        favoriteDialog.dismiss();
    }
}
