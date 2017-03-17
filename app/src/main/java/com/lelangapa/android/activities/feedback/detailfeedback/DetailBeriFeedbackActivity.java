package com.lelangapa.android.activities.feedback.detailfeedback;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.lelangapa.android.R;
import com.lelangapa.android.fragments.feedback.berifeedback.detail.DetailBeriFeedbackFragment;

/**
 * Created by andre on 15/03/17.
 */

public class DetailBeriFeedbackActivity extends AppCompatActivity {
    private DetailBeriFeedbackFragment beriFeedbackFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        initializeViews();
        initializeFragment();
        setFragment();
    }
    private void initializeViews()
    {
        setContentView(R.layout.activity_feedback_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_action_bar_24dp);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Ulasan");
    }
    private void initializeFragment()
    {
        beriFeedbackFragment = new DetailBeriFeedbackFragment();
    }
    private void setFragment()
    {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_activity_feedback_detail_layout, beriFeedbackFragment)
                .commit();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
