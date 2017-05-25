package com.lelangapa.android.activities;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.lelangapa.android.R;
import com.lelangapa.android.activities.favorite.FavoriteListActivity;
import com.lelangapa.android.activities.feedback.feedbackanda.FeedbackAndaActivity;
import com.lelangapa.android.activities.profile.chat.UserChatActivity;
import com.lelangapa.android.activities.search.MainSearchActivity;
import com.lelangapa.android.apicalls.notification.NotificationAPI;
import com.lelangapa.android.apicalls.singleton.RequestController;
import com.lelangapa.android.fragments.HomeFragment;
import com.lelangapa.android.fragments.home.BerandaHomeFragment;
import com.lelangapa.android.fragments.home.PromoHomeFragment;
import com.lelangapa.android.interfaces.DataReceiver;
import com.lelangapa.android.preferences.SessionManager;
import com.lelangapa.android.services.NotifConfig;
import com.lelangapa.android.services.TokenSaver;
import com.lelangapa.android.viewpagers.HomeViewPagerAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    public static DrawerLayout drawer;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    public static ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    private ProgressDialog progressDialog;

    private final String headerPrefix = "Halo, ";
    private SessionManager sessionManager;
    private HashMap<String, String> userInfo = new HashMap<>();

    private BroadcastReceiver tokenBroadcastReceiver;
    private DataReceiver logoutDataReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Log.v ("OnCreate", "oncreate started");

        setTokenBroadcastReceiver();
        Log.v("Android ID", Settings.Secure.getString(this.getContentResolver(),Settings
                .Secure.ANDROID_ID));
        sessionManager = new SessionManager(MainActivity.this);
        if (sessionManager.isLoggedIn()){
            setContentView(R.layout.activity_main_loggedin);
            drawer = (DrawerLayout) findViewById(R.id.activity_main_loggedin);
            navigationView = (NavigationView) findViewById(R.id.nav_view_loggedin);
            //Get header of navigation bar
            View header = navigationView.getHeaderView(0);
            TextView userInfoNameNavbar = (TextView) header.findViewById(R.id.user_info_name_navbar);
            TextView userInfoEmailNavbar = (TextView) header.findViewById(R.id.user_info_email_navbar);
            userInfo = sessionManager.getSession();
            userInfoNameNavbar.setText(headerPrefix + userInfo.get(sessionManager.getKEY_NAME()));
            userInfoEmailNavbar.setText(userInfo.get(sessionManager.getKEY_EMAIL()));
        }
        else {
            setContentView(R.layout.activity_main);
            drawer = (DrawerLayout) findViewById(R.id.activity_main);
            navigationView = (NavigationView) findViewById(R.id.nav_view);
        }
        setLogoutDataReceiver();
        sendTokenToServer();

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
                    /*HomeFragment homeFragment = new HomeFragment();
                    getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_main_layout, homeFragment)
                            .commit();*/
                    viewPager.setCurrentItem(0);
                }
                else if (id==R.id.nav_promo) {
                    viewPager.setCurrentItem(1);
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
                else if (id==R.id.nav_chat){
                    Intent intent = new Intent(MainActivity.this, UserChatActivity.class);
                    startActivity(intent);
                }
                else if (id==R.id.nav_gerai){
                    Intent intent = new Intent(MainActivity.this, UserGeraiActivity.class);
                    startActivity(intent);
                }
                else if (id==R.id.nav_favorite){
                    Intent intent = new Intent(MainActivity.this, FavoriteListActivity.class);
                    startActivity(intent);
                }
                else if (id==R.id.nav_feedback_anda){
                    Intent intent = new Intent(MainActivity.this, FeedbackAndaActivity.class);
                    startActivity(intent);
                }
                //Toast.makeText(getApplicationContext(), item.getItemId(), Toast.LENGTH_SHORT).show();
                else if (id==R.id.nav_logout){
                    changeLoginStatusWhenLogout();
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
    private void setUpViewPager(ViewPager viewPager){
        HomeViewPagerAdapter adapter = new HomeViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new BerandaHomeFragment(), getResources().getString(R.string.HOME));
        adapter.addFragment(new PromoHomeFragment(), getResources().getString(R.string.PROMO));
        int limit = adapter.getCount() > 1 ? adapter.getCount() - 1 : 1;
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(limit);
    }
    private void setTokenBroadcastReceiver() {
        tokenBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(NotifConfig.REGISTRATION_COMPLETE)) {
                    Log.e("ACTION RECEIVED", "INTENT ACTION RECEIVEDz");
                    sendTokenToServer();
                    //TokenSaver.tokenSent(getApplicationContext());
                }
            }
        };
    }
    private void setLogoutDataReceiver() {
        logoutDataReceiver = new DataReceiver() {
            @Override
            public void dataReceived(Object output) {
                String response = output.toString();
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    if (jsonResponse.getString("status").equals("success")) {
                        progressDialog.dismiss();
                        sessionManager.destroySession();
                        setTokenStatusWhenLogout();
                        Intent intent = new Intent(MainActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
    }
    private void setTokenStatusWhenLogout() {
        SharedPreferences prefs = getApplicationContext().getSharedPreferences(NotifConfig.SHARED_PREF, 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("isSubmittedToServer", false);
        editor.commit();
    }
    private void changeLoginStatusWhenLogout()
    {
        progressDialog = ProgressDialog.show(this, "Logout", "Harap tunggu");
        HashMap<String, String> data = new HashMap<>();
        data.put("id_device", TokenSaver.getDeviceID(this));
        NotificationAPI.Logout logoutAPI = NotificationAPI.getLogoutInstance(data, logoutDataReceiver);
        RequestController.getInstance(this).addToRequestQueue(logoutAPI);
    }
    private void sendTokenToServer() {
        if (!sessionManager.isLoggedIn()) return;
        SharedPreferences prefs = getApplicationContext().getSharedPreferences(NotifConfig.SHARED_PREF, 0);
        String fcmToken = prefs.getString("fcmToken", null);
        Boolean isSubmittedToServer = prefs.getBoolean("isSubmittedToServer", false);
        if (!TextUtils.isEmpty(fcmToken)) {
            if (!isSubmittedToServer) {
                Log.v("SENDING TOKEN", "Sending token : " + fcmToken);
                TokenSaver.sendTokenToServer(MainActivity.this, fcmToken, userInfo.get(sessionManager.getKEY_ID()));
                TokenSaver.tokenSent(getApplicationContext());
            }
        }
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
        if (item.getItemId()==R.id.action_search){
            Intent intent = new Intent(MainActivity.this, MainSearchActivity.class);
            startActivity(intent);
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
        LocalBroadcastManager.getInstance(this).registerReceiver(tokenBroadcastReceiver,
                new IntentFilter(NotifConfig.REGISTRATION_COMPLETE));
//        Log.v("OnResunme", "on resume started");
    }
    @Override
    public void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(tokenBroadcastReceiver);
        super.onPause();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.activity_main_menu, menu);
        /*SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));*/
        return super.onCreateOptionsMenu(menu);
    }
}
