package com.lelangapa.app.activities.feedback.berifeedback;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.lelangapa.app.R;
import com.lelangapa.app.fragments.feedback.berifeedback.auctioneer.AuctioneerFragment;
import com.lelangapa.app.fragments.feedback.berifeedback.winner.WinnerFragment;
import com.lelangapa.app.viewpagers.FeedbackViewPagerAdapter;

/**
 * Created by andre on 10/03/17.
 */

public class BeriFeedbackActivity extends AppCompatActivity {
    private TabLayout tabLayout_beriFeedback;
    private ViewPager viewPager_beriFeedback;

    private WinnerFragment winnerFragment;
    private AuctioneerFragment auctioneerFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        initializeFragments();
        initializeViews();
    }

    private void initializeViews()
    {
        setContentView(R.layout.activity_berifeedback);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_user_feedback);
        tabLayout_beriFeedback = (TabLayout) findViewById(R.id.tab_user_feedback);
        viewPager_beriFeedback = (ViewPager) findViewById(R.id.viewpager_berifeedback);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Beri Feedback");
        setupViewPager();
        tabLayout_beriFeedback.setupWithViewPager(viewPager_beriFeedback);
    }
    private void initializeFragments()
    {
        winnerFragment = new WinnerFragment();
        auctioneerFragment = new AuctioneerFragment();
    }
    private void setupViewPager()
    {
        FeedbackViewPagerAdapter pagerAdapter = new FeedbackViewPagerAdapter(getSupportFragmentManager());
        pagerAdapter.addFragment(winnerFragment, "Pemenang");
        pagerAdapter.addFragment(auctioneerFragment, "Pelelang");
        viewPager_beriFeedback.setAdapter(pagerAdapter);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            super.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
