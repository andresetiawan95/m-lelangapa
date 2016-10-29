package com.lelangkita.android.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.lelangkita.android.R;
import com.lelangkita.android.fragments.BerandaHomeFragment;
import com.lelangkita.android.fragments.HomeFragment;
import com.lelangkita.android.fragments.TrendingHomeFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static DrawerLayout drawer;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    public static ActionBarDrawerToggle toggle;

    private class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager){
            super(manager);
        }
        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }
        @Override
        public int getCount() {
            return mFragmentList.size();
        }
        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }
    }
    private void setUpViewPager(ViewPager viewPager){
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new BerandaHomeFragment(), "BERANDA");
        adapter.addFragment(new TrendingHomeFragment(), "TRENDING");
        viewPager.setAdapter(adapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Log.v ("OnCreate", "oncreate started");
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        drawer = (DrawerLayout) findViewById(R.id.activity_main);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        //toggle.syncState();
        viewPager = (ViewPager) findViewById(R.id.viewpager_main);
        tabLayout = (TabLayout) findViewById(R.id.tab_main);
        setUpViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int id = item.getItemId();
                if (id==R.id.nav_home){
                    HomeFragment homeFragment = new HomeFragment();
                    getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_main_layout, homeFragment)
                            .commit();
                }
                else if (id==R.id.nav_login) {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
//                    Log.d("pindah fragment", "fragmen login");

                    /*LoginFragment loginFragment = new LoginFragment();
                    drawer.closeDrawers();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_main_layout, loginFragment)
                            .addToBackStack(null).commit();
*/
                }
                else if (id==R.id.nav_register){
                    Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                    startActivity(intent);
  /*                  RegisterFragment registerFragment = new RegisterFragment();
                    drawer.closeDrawers();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_main_layout, registerFragment)
                            .addToBackStack(null)
                            .commit();*/
                }
                //Toast.makeText(getApplicationContext(), item.getItemId(), Toast.LENGTH_SHORT).show();
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.activity_main);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });
        if (savedInstanceState == null){
            //toggle.setDrawerIndicatorEnabled(true);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_main_layout, new HomeFragment())
                    .commit();
        }
        /*
        * dibawah ini digunakan untuk mengubah hamburger icon dan back arrow icon
        * setDisplayHomeAsUpEnabled bernilai true jika ada fragment pada stack fragment
        * dalam hal ini, akan mengeluarkan
        *
        *
        * */
        /*getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    //drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
               //     toggle.setDrawerIndicatorEnabled(false);
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onBackPressed();
                        }
                    });

                } else {
                //    toggle.setDrawerIndicatorEnabled(true);
                    //drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                    getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                    toggle.syncState();
                    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            drawer.openDrawer(GravityCompat.START);
                        }
                    });
                }
            }
        });*/
    }
    @Override
    public void onBackPressed(){
        int count = getFragmentManager().getBackStackEntryCount();
        if (count == 0) {
            super.onBackPressed();
            //additional code
        } else {
            getSupportFragmentManager().popBackStack();
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
//    @Override
//    public void onBackPressed() {
//        /*int count = getFragmentManager().getBackStackEntryCount();
//        if (count == 0) {
//            super.onBackPressed();
//            //additional code
//        } else {
//            getFragmentManager().popBackStack();
//        }*/
//        Fragment f = getSupportFragmentManager().findFragmentById(R.id.fragment_main_layout);
//        String fragmentName = f.getClass().getSimpleName();
//        if (fragmentName == "Lelangkita"){
//            Log.d("backpres","backpressed");
//          //  Toast.makeText(MainActivity.this, "Di home", Toast.LENGTH_SHORT);
//        }
//        else {
//            getFragmentManager().popBackStack();
//        }
//        super.onBackPressed();
//    }
//    setelah memanggil drawertoggle pada onCreate
    @Override
    protected void onPostCreate(Bundle savedInstanceState){
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        toggle.syncState();
    }
    @Override
    public void onConfigurationChanged(Configuration config){
        super.onConfigurationChanged(config);
        toggle.onConfigurationChanged(config);
    }
    @Override
    public void onResume(){
        super.onResume();
//        Log.v("OnResunme", "on resume started");
    }
}
