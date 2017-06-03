package com.lelangapa.app.activities.userpublic;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.lelangapa.app.R;
import com.lelangapa.app.fragments.userpublic.feedback.FeedbackMainFragment;
import com.lelangapa.app.fragments.userpublic.gerai.GeraiMainFragment;
import com.lelangapa.app.fragments.userpublic.riwayat.RiwayatMainFragment;
import com.lelangapa.app.viewpagers.UserPublicViewPagerAdapter;

/**
 * Created by andre on 28/03/17.
 */

public class DetailUserPublicActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Toolbar toolbar;
    private TextView textView_toolbar, textView_namaUser;

    private AppBarLayout appBarLayout;
    private boolean isShow = false;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        initializeViews();
        setupViews();
        setupViewPager();
        setupTabLayoutAndViewpager();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
    private void initializeViews()
    {
        setContentView(R.layout.activity_detail_user_public);
        appBarLayout = (AppBarLayout) findViewById(R.id.activity_detail_user_public_appbar);
        toolbar = (Toolbar) findViewById(R.id.activity_detail_user_public_toolbar);
        tabLayout = (TabLayout) findViewById(R.id.activity_detail_user_public_tablayout);
        viewPager = (ViewPager) findViewById(R.id.activity_detail_user_public_viewpager);
        textView_toolbar = (TextView) findViewById(R.id.activity_detail_user_public_toolbar_textview);
        textView_namaUser = (TextView) findViewById(R.id.activity_detail_user_public_name);
        setSupportActionBar(toolbar);
        textView_toolbar.setVisibility(View.GONE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.activity_detail_user_public_collapsing_toolbar);
        collapsingToolbarLayout.setTitleEnabled(false);
        //collapsingToolbarLayout.setTitle("Detail User");

        collapsingToolbarLayout.setCollapsedTitleTextColor(ContextCompat.getColor(this, R.color.backgroundLayoutWhiteColor));
        collapsedToolbarForNotShowingTitleWhenExpanded(collapsingToolbarLayout);
        //collapsingToolbarLayout.setExpandedTitleColor(ContextCompat.getColor(this,R.color.backgroundLayoutWhiteColor));
    }
    private void setupViews()
    {
        String name = getIntent().getStringExtra("nama_user");
        textView_namaUser.setText(name);
        textView_toolbar.setText(name);
    }
    private void setupTabLayoutAndViewpager()
    {
        tabLayout.setupWithViewPager(viewPager);
    }
    private void setupViewPager()
    {
        UserPublicViewPagerAdapter userPublicViewPagerAdapter = new UserPublicViewPagerAdapter(getSupportFragmentManager());
        userPublicViewPagerAdapter.addFragment(new GeraiMainFragment(), "Gerai");
        userPublicViewPagerAdapter.addFragment(new RiwayatMainFragment(), "Riwayat");
        userPublicViewPagerAdapter.addFragment(new FeedbackMainFragment(), "Feedback");

        int limit = (userPublicViewPagerAdapter.getCount() > 1 ? userPublicViewPagerAdapter.getCount() - 1 : 1);
//        /userPublicViewPagerAdapter.addFragment(null, "Info");
        viewPager.setAdapter(userPublicViewPagerAdapter);
        viewPager.setOffscreenPageLimit(limit);
    }
    private void collapsedToolbarForNotShowingTitleWhenExpanded(final CollapsingToolbarLayout collapsingToolbarLayout)
    {
       // AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.activity_detail_user_public_appbar);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            //boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    textView_toolbar.setVisibility(View.VISIBLE);
                    //collapsingToolbarLayout.setTitle(" ");
                    isShow = true;
                } else if(isShow) {
                    textView_toolbar.setVisibility(View.GONE);
                    //collapsingToolbarLayout.setTitle(" ");//carefull there should a space between double quote otherwise it wont work
                    isShow = false;
                }
            }
        });
    }
    public void collapseToolbar()
    {
        if (!isShow) {
            //Log.v("collapsing", "is collapsing");
            appBarLayout.setExpanded(false);
            isShow = true;
        }
    }
}
