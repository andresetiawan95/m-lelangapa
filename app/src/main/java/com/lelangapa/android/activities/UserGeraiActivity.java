package com.lelangapa.android.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.lelangapa.android.R;
import com.lelangapa.android.activities.gerai.UserLelangBarangActivity;
import com.lelangapa.android.fragments.gerai.UserGeraiFragment;

/**
 * Created by andre on 27/11/16.
 */

public class UserGeraiActivity extends AppCompatActivity {
    private FloatingActionButton geraiFloatingButton;
    private UserGeraiFragment userGeraiFragment;
    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        initializeViews();
        initializeFragments();
        initializeOnClickListener();
        setupFragment();
    }
    private void initializeViews() {
        setContentView(R.layout.activity_gerai);
        geraiFloatingButton = (FloatingActionButton) findViewById(R.id.add_gerai_floating_button);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Gerai");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    private void initializeFragments() {
        userGeraiFragment = new UserGeraiFragment();
    }
    private void initializeOnClickListener() {
        geraiFloatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserGeraiActivity.this, UserLelangBarangActivity.class);
                startActivity(intent);
            }
        });
    }
    private void setupFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_user_gerai_layout, userGeraiFragment)
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
