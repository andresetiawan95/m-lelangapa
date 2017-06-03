package com.lelangapa.app.activities.profile.chat;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.lelangapa.app.R;
import com.lelangapa.app.fragments.profile.chat.chatroom.UserChatRoomFragment;

/**
 * Created by andre on 22/03/17.
 */

public class UserChatRoomActivity extends AppCompatActivity {
    private UserChatRoomFragment chatRoomFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        initializeFragment();
        initializeViews();
        setupFragment();
    }
    private void initializeFragment()
    {
        chatRoomFragment = new UserChatRoomFragment();
    }
    private void initializeViews()
    {
        setContentView(R.layout.activity_chat);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
    }
    private void setupFragment()
    {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_user_chat_layout, chatRoomFragment)
                .commit();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            super.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
    public void changeActionBarTitle(String title)
    {
        getSupportActionBar().setTitle(title);
    }
}
