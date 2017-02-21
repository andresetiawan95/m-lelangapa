package com.lelangapa.android.activities.detail;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.lelangapa.android.R;
import com.lelangapa.android.fragments.detail.detailitemowner.DetailItemAuctioneerFragment;
import com.lelangapa.android.fragments.detail.detailitemuser.DetailFragment;
import com.lelangapa.android.preferences.SessionManager;

import java.util.HashMap;

/**
 * Created by Andre on 12/24/2016.
 */

public class DetailBarangActivity extends AppCompatActivity {
    private String onItemAuctioneerID;
    private String auctioneerLoginID;
    private SessionManager sessionManager;
    private HashMap<String, String> userInfo;
    private DetailFragment detailFragment;
    private DetailItemAuctioneerFragment detailItemAuctioneerFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        detailFragment = new DetailFragment();
        detailItemAuctioneerFragment = new DetailItemAuctioneerFragment();
        setContentView(R.layout.activity_detail_barang);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Detail");
        sessionManager = new SessionManager(DetailBarangActivity.this);
        setDetailItemFragmentTransaction();
    }
    private void setDetailItemFragmentTransaction()
    {
        if (sessionManager.isLoggedIn())
        {
            userInfo = sessionManager.getSession();
            auctioneerLoginID = userInfo.get(sessionManager.getKEY_ID());
            onItemAuctioneerID = getIntent().getExtras().getString("auctioneer_id");
            if (auctioneerLoginID.equals(onItemAuctioneerID))
            {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_detail_barang_layout, detailItemAuctioneerFragment)
                        .commit();
            }
            else
            {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_detail_barang_layout, detailFragment)
                        .commit();
            }
        }
        else
        {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_detail_barang_layout, detailFragment)
                    .commit();
        }
        /*getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_detail_barang_layout, detailFragment)
                .commit();*/
    }
    @Override
    public void onResume()
    {
        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            super.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
