package com.lelangapa.android.activities.riwayat.detailriwayat;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.lelangapa.android.R;
import com.lelangapa.android.fragments.riwayat.detailriwayat.DetailRiwayatFragment;

/**
 * Created by andre on 15/03/17.
 */

public class DetailRiwayatActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private DetailRiwayatFragment detailRiwayatFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        initializeFragment();
        initializeViews();
    }
    private void initializeFragment()
    {
        detailRiwayatFragment = new DetailRiwayatFragment();
    }
    private void initializeViews()
    {
        setContentView(R.layout.activity_riwayat_detail);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Detail Riwayat");
        setupFragment();
    }
    private void setupFragment()
    {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_activity_riwayat_detail_layout, detailRiwayatFragment)
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
