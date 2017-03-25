package com.lelangapa.android.fragments.profile.chat.chatroom;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lelangapa.android.R;

/**
 * Created by andre on 28/11/16.
 */

public class UserChatRoomFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_chat_room_message_layout, container, false);
        return view;
    }
}
