package com.lelangapa.app.activities.detail;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.lelangapa.app.R;
import com.lelangapa.app.fragments.detail.daftartawaranfinal.DaftarTawaranFinalFragment;

/**
 * Created by andre on 08/02/17.
 */

public class DaftarTawaranFinalActivity extends AppCompatActivity {
    private DaftarTawaranFinalFragment fragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeViews();
        initializeFragment();
        setupFragment();
    }
    private void initializeViews() {
        setContentView(R.layout.activity_daftar_tawaran_final);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Tawaran");
    }
    private void initializeFragment() {
        fragment = new DaftarTawaranFinalFragment();
    }
    private void setupFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_activity_daftar_tawaran_final_layout, fragment)
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
