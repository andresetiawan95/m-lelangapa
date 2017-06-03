package com.lelangapa.app.activities.favorite;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.lelangapa.app.R;
import com.lelangapa.app.fragments.favorite.FavoriteFragment;

/**
 * Created by andre on 13/02/17.
 */

public class FavoriteListActivity extends AppCompatActivity {
    private FavoriteFragment favoriteFragment;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        initializeFavoriteFragment();
        initializeViews();
        setFavoriteFragment();
    }

    /*
    * Initialization method start here
    * */
    private void initializeFavoriteFragment()
    {
        favoriteFragment = new FavoriteFragment();
    }
    private void initializeViews()
    {
        setContentView(R.layout.activity_favorite);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Favorit");
    }
    private void setFavoriteFragment()
    {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_activity_favorite_layout, favoriteFragment)
                .commit();
    }
    /*
    * Initialization method end here
    * */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            super.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
