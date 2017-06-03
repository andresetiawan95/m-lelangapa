package com.lelangapa.app.activities.search.filter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.lelangapa.app.R;
import com.lelangapa.app.fragments.search.filter.FilterSearchFragment;
import com.lelangapa.app.preferences.FilterManager;

/**
 * Created by andre on 04/05/17.
 */

public class FilterSearchActivity extends AppCompatActivity {
    private FilterSearchFragment filterSearchFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeViews();
        initializeFragment();
        setupFragment();
    }

    private void initializeViews() {
        setContentView(R.layout.activity_filter);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_action_bar_24dp);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Filter");
    }

    private void initializeFragment() {
        filterSearchFragment = new FilterSearchFragment();
    }
    private void setupFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_filter_layout, filterSearchFragment)
                .commit();
    }
    public void whenFilterSubmitted(String params) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("params", params);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            if (!FilterManager.IS_FILTER_SPECIFIED) {
                FilterManager.destoryFilter();
            }
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
        if (!FilterManager.IS_FILTER_SPECIFIED) {
            FilterManager.destoryFilter();
        }
        super.onBackPressed();
    }
}
