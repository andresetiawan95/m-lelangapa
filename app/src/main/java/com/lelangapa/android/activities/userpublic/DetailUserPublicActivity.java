package com.lelangapa.android.activities.userpublic;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.lelangapa.android.R;
import com.lelangapa.android.fragments.userpublic.UserPublicFeedbackFragment;
import com.lelangapa.android.fragments.userpublic.UserPublicGeraiFragment;
import com.lelangapa.android.fragments.userpublic.UserPublicInfoFragment;
import com.lelangapa.android.viewpagers.UserPublicViewPagerAdapter;

/**
 * Created by andre on 28/03/17.
 */

public class DetailUserPublicActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        initializeViews();
        setupViewPager();
        setupTabLayoutAndViewpager();
    }
    private void initializeViews()
    {
        setContentView(R.layout.activity_detail_user_public);
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_detail_user_public_toolbar);
        tabLayout = (TabLayout) findViewById(R.id.activity_detail_user_public_tablayout);
        viewPager = (ViewPager) findViewById(R.id.activity_detail_user_public_viewpager);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(" ");
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.activity_detail_user_public_collapsing_toolbar);
        //collapsingToolbarLayout.setTitle("Detail User");

        collapsingToolbarLayout.setCollapsedTitleTextColor(ContextCompat.getColor(this, R.color.backgroundLayoutWhiteColor));
        collapsedToolbarForNotShowingTitleWhenExpanded(collapsingToolbarLayout);
        //collapsingToolbarLayout.setExpandedTitleColor(ContextCompat.getColor(this,R.color.backgroundLayoutWhiteColor));
    }
    private void setupTabLayoutAndViewpager()
    {
        tabLayout.setupWithViewPager(viewPager);
    }
    private void setupViewPager()
    {
        UserPublicViewPagerAdapter userPublicViewPagerAdapter = new UserPublicViewPagerAdapter(getSupportFragmentManager());
        userPublicViewPagerAdapter.addFragment(new UserPublicGeraiFragment(), "Gerai");
        userPublicViewPagerAdapter.addFragment(new UserPublicFeedbackFragment(), "Feedback");
        userPublicViewPagerAdapter.addFragment(new UserPublicInfoFragment(), "Info");
//        /userPublicViewPagerAdapter.addFragment(null, "Info");
        viewPager.setAdapter(userPublicViewPagerAdapter);
    }
    private void collapsedToolbarForNotShowingTitleWhenExpanded(final CollapsingToolbarLayout collapsingToolbarLayout)
    {
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.activity_detail_user_public_appbar);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbarLayout.setTitle("Title");
                    isShow = true;
                } else if(isShow) {
                    collapsingToolbarLayout.setTitle(" ");//carefull there should a space between double quote otherwise it wont work
                    isShow = false;
                }
            }
        });
    }
}
