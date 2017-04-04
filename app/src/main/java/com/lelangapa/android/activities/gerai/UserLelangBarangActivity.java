package com.lelangapa.android.activities.gerai;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.lelangapa.android.R;
import com.lelangapa.android.fragments.gerai.UserLelangBarangFragment;

/**
 * Created by andre on 01/12/16.
 */

public class UserLelangBarangActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lelang_barang);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Lelang Barang");
        UserLelangBarangFragment lelangBarangFragment = new UserLelangBarangFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_user_lelang_barang_layout, lelangBarangFragment)
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