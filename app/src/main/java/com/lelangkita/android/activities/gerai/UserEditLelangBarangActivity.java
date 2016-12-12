package com.lelangkita.android.activities.gerai;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.lelangkita.android.R;
import com.lelangkita.android.fragments.gerai.UserEditLelangBarangFragment;

/**
 * Created by andre on 10/12/16.
 */

public class UserEditLelangBarangActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_lelang_barang);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Edit Barang");
        UserEditLelangBarangFragment userEditLelangBarangFragment = new UserEditLelangBarangFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_user_edit_lelang_barang_layout, userEditLelangBarangFragment)
                .commit();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            super.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
