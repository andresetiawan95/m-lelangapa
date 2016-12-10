package com.lelangkita.android.activities.profile;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.lelangkita.android.R;
import com.lelangkita.android.fragments.profile.chat.UserHistoryChatFragment;
import com.lelangkita.android.fragments.profile.chat.UserNewChatFragment;
import com.lelangkita.android.viewpagers.ChatViewPagerAdapter;

/**
 * Created by andre on 27/11/16.
 */

public class UserChatActivity extends AppCompatActivity {
    private TabLayout chatTabLayout;
    private ViewPager chatViewPager;
    private void setUpChatViewPager(ViewPager viewPager){
        ChatViewPagerAdapter chatViewPagerAdapter= new ChatViewPagerAdapter(getSupportFragmentManager());
        chatViewPagerAdapter.addFragment(new UserNewChatFragment(), "Pesan Masuk");
        chatViewPagerAdapter.addFragment(new UserHistoryChatFragment(), "Riwayat Pesan");
        viewPager.setAdapter(chatViewPagerAdapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_user_chat);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Chat");
        chatTabLayout = (TabLayout) findViewById(R.id.tab_user_chat);
        chatViewPager = (ViewPager) findViewById(R.id.viewpager_user_chat);
        setUpChatViewPager(chatViewPager);
        chatTabLayout.setupWithViewPager(chatViewPager);
    }
}
