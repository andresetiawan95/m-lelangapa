package com.lelangapa.app.fragments.detail.detailitemfavorite;

import android.app.Activity;
import android.app.ProgressDialog;

/**
 * Created by andre on 06/03/17.
 */

public class UnfavoriteProgressDialog {
    private ProgressDialog favoriteDialog;
    private Activity activity;

    public UnfavoriteProgressDialog(Activity activity)
    {
        this.activity = activity;
    }
    public void showDialog()
    {
        favoriteDialog = ProgressDialog.show(activity, "Sedang diproses", "Menghapus dari daftar favorit." +
                " Harap tunggu.");
    }
    public void unshowDialog()
    {
        favoriteDialog.dismiss();
    }
}
