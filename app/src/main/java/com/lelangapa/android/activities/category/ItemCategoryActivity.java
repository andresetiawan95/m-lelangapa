package com.lelangapa.android.activities.category;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.lelangapa.android.R;
import com.lelangapa.android.fragments.home.category.ItemCategoryFragment;

/**
 * Created by andre on 03/05/17.
 */

public class ItemCategoryActivity extends AppCompatActivity {
    private ItemCategoryFragment itemCategoryFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        initializeViews();
        initializeFragments();
        setupFragment();
    }
    private void initializeViews()
    {
        setContentView(R.layout.activity_item_category);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getIntent().getExtras().getString("nama_category"));
    }
    private void initializeFragments()
    {
        itemCategoryFragment = new ItemCategoryFragment();
    }
    private void setupFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_item_category_layout, itemCategoryFragment)
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
