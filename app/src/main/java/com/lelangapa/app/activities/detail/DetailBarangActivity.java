package com.lelangapa.app.activities.detail;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.lelangapa.app.R;
import com.lelangapa.app.fragments.detail.detailitemowner.DetailItemAuctioneerFragment;
import com.lelangapa.app.fragments.detail.detailitemuser.DetailFragment;
import com.lelangapa.app.preferences.SessionManager;

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
                        .addToBackStack(null)
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

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        if (sessionManager.isLoggedIn() && !auctioneerLoginID.equals(onItemAuctioneerID))
        {
            MenuInflater menuInflater = getMenuInflater();
            menuInflater.inflate(R.menu.activity_detail_favorite, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed(){
        int count = getSupportFragmentManager().getBackStackEntryCount();
        Log.d("BACKSTACK", Integer.toString(count));
        if (count <= 1) {
            finish();
            //additional code
        } else {
            getSupportFragmentManager().popBackStack();
        }
    }

    public void changeActionBarTitle(String title)
    {
        getSupportActionBar().setTitle(title);
    }
    public void addFragmentStack(Fragment fragment, String title)
    {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_detail_barang_layout, fragment)
                .addToBackStack(null)
                .commit();
        changeActionBarTitle(title);
    }
}
