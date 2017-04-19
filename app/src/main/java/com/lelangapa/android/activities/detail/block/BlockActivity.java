package com.lelangapa.android.activities.detail.block;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.lelangapa.android.R;
import com.lelangapa.android.fragments.detail.ownerblocklist.DaftarBlockFragment;

/**
 * Created by andre on 18/04/17.
 */

public class BlockActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        initializeViews();
        setupFragment();
    }
    private void initializeViews()
    {
        setContentView(R.layout.activity_daftar_block);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Daftar Block");
    }
    private void setupFragment()
    {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_daftar_block_layout, new DaftarBlockFragment())
                .commit();
    }
}
