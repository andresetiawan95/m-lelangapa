package com.lelangkita.android.activities;

import android.content.Intent;
import android.content.SharedPreferences;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.lelangkita.android.R;
import com.lelangkita.android.fragments.BerandaHomeFragment;
import com.lelangkita.android.fragments.CategoryHomeFragment;
import com.lelangkita.android.fragments.HomeFragment;
import com.lelangkita.android.fragments.TrendingHomeFragment;
import com.lelangkita.android.preferences.SessionManager;
import com.lelangkita.android.viewpagers.HomeViewPagerAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static DrawerLayout drawer;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private SessionManager sessionManager;
    public static ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    private final String headerPrefix = "Halo, ";
    private void setUpViewPager(ViewPager viewPager){
        HomeViewPagerAdapter adapter = new HomeViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new BerandaHomeFragment(), getResources().getString(R.string.HOME));
        adapter.addFragment(new TrendingHomeFragment(), getResources().getString(R.string.TRENDING));
        adapter.addFragment(new CategoryHomeFragment(), getResources().getString(R.string.CATEGORY));
        viewPager.setAdapter(adapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Log.v ("OnCreate", "oncreate started");

        sessionManager = new SessionManager(MainActivity.this);
        if (sessionManager.isLoggedIn()){
            setContentView(R.layout.activity_main_loggedin);
            drawer = (DrawerLayout) findViewById(R.id.activity_main_loggedin);
            navigationView = (NavigationView) findViewById(R.id.nav_view_loggedin);
            //Get header of navigation bar
            View header = navigationView.getHeaderView(0);
            TextView userInfoNameNavbar = (TextView) header.findViewById(R.id.user_info_name_navbar);
            TextView userInfoEmailNavbar = (TextView) header.findViewById(R.id.user_info_email_navbar);
            HashMap<String, String> userInfo = sessionManager.getSession();
            userInfoNameNavbar.setText(headerPrefix + userInfo.get(sessionManager.getKEY_NAME()));
            userInfoEmailNavbar.setText(userInfo.get(sessionManager.getKEY_EMAIL()));
        }
        else {
            setContentView(R.layout.activity_main);
            drawer = (DrawerLayout) findViewById(R.id.activity_main);
            navigationView = (NavigationView) findViewById(R.id.nav_view);
        }
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        //toggle.syncState();
        viewPager = (ViewPager) findViewById(R.id.viewpager_main);
        tabLayout = (TabLayout) findViewById(R.id.tab_main);
        setUpViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);


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
                else if (id==R.id.nav_profil){
                    Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                    startActivity(intent);
                }
                //Toast.makeText(getApplicationContext(), item.getItemId(), Toast.LENGTH_SHORT).show();
                else if (id==R.id.nav_logout){
                    sessionManager.destroySession();
                    Intent intent = new Intent(MainActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }
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
        * dalam hal ini, akan mengeluarkan backarrow
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
