package com.lelangapa.app.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.lelangapa.app.R;
import com.lelangapa.app.fragments.LoginFragment;

/**
 * Created by andre on 24/10/16.
 */

public class LoginActivity extends AppCompatActivity {
    private TextView txtRegister;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        //Log.v("Oncreate login", "on create login");
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Login");
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_login_layout, new LoginFragment())
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
