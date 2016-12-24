package com.lelangkita.android.activities.detail;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.lelangkita.android.R;
import com.lelangkita.android.fragments.detail.DetailFragment;

/**
 * Created by Andre on 12/24/2016.
 */

public class DetailBarangActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_barang);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Detail");
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_detail_barang_layout, new DetailFragment())
                .commit();
    }
}
