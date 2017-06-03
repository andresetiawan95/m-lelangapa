package com.lelangapa.app.activities.riwayat;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.lelangapa.app.R;
import com.lelangapa.app.fragments.riwayat.RiwayatFragment;

/**
 * Created by andre on 21/02/17.
 */

public class RiwayatActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private RiwayatFragment riwayatFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        initializeFragment();
        initializeViews();
    }

    /*
    * Initialization method start here
    * */
    private void initializeFragment()
    {
        riwayatFragment = new RiwayatFragment();
    }
    private void initializeViews()
    {
        setContentView(R.layout.activity_riwayat);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Riwayat");
        setupFragment();
    }
    /*
    * Initialization method end here
    * */

    /*
    * Setup fragment method start here
    * */
    private void setupFragment()
    {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_activity_riwayat_layout, riwayatFragment)
                .commit();
    }
    /*
    * Setup fragment method end here
    * */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            super.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
