package com.lelangapa.android.activities.profile;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.lelangapa.android.R;
import com.lelangapa.android.fragments.profile.EditAlamatFragment;

/**
 * Created by Andre on 11/22/2016.
 */

public class EditAlamatActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userprofile_editalamat);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Update Alamat");
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_userprofile_editalamat_layout, new EditAlamatFragment())
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
