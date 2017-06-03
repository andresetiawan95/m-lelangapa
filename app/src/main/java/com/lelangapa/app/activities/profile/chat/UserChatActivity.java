package com.lelangapa.app.activities.profile.chat;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.lelangapa.app.R;
import com.lelangapa.app.fragments.profile.chat.chatlist.UserChatFragment;

/**
 * Created by andre on 27/11/16.
 */

public class UserChatActivity extends AppCompatActivity {
    private UserChatFragment userChatFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        initializeFragment();
        setContentView(R.layout.activity_chat);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Chat");
        setupFragment();
    }
    private void initializeFragment()
    {
        userChatFragment = new UserChatFragment();
    }
    private void setupFragment()
    {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_user_chat_layout, userChatFragment)
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
