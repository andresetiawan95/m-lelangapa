package com.lelangapa.android.fragments.profile.chat.chatlist;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lelangapa.android.R;

/**
 * Created by andre on 22/03/17.
 */

public class UserChatEmptyFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_chat_room_list_empty_layout, container, false);
        return view;
    }
}
