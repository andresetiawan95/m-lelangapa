package com.lelangapa.app.fragments.profile.chat.chatlist;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lelangapa.app.R;
import com.lelangapa.app.apicalls.chat.ChatRoomListAPI;
import com.lelangapa.app.apicalls.singleton.RequestController;
import com.lelangapa.app.interfaces.DataReceiver;
import com.lelangapa.app.preferences.SessionManager;
import com.lelangapa.app.resources.ChatResources;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by andre on 28/11/16.
 */

public class UserChatFragment extends Fragment {
    private ArrayList<ChatResources> listChatRoom;
    private String userID;
    private DataReceiver dataReceiver;

    private SessionManager sessionManager;
    private HashMap<String, String> session;

    private SwipeRefreshLayout swipeRefreshLayout;

    private UserChatEmptyFragment emptyFragment;
    private UserChatNoEmptyFragment noEmptyFragment;
    public UserChatFragment()
    {
        initializeConstant();
    }
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        initializeChildFragments();
        initializeDataReceiver();
        initializeSession();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_chat_room_list_layout, container, false);
        initializeViews(view);
        setSwipeRefreshLayoutProperties();
        return view;
    }
    private void initializeConstant()
    {
        listChatRoom = new ArrayList<>();
    }
    private void initializeSession()
    {
        sessionManager = new SessionManager(getActivity());
        if (sessionManager.isLoggedIn())
        {
            session = SessionManager.getSessionStatic();
            userID = session.get(sessionManager.getKEY_ID());
        }
    }
    private void initializeViews(View view)
    {
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.fragment_chat_room_list_layout_swipe_refreshLayout);
    }
    private void initializeChildFragments()
    {
        emptyFragment = new UserChatEmptyFragment();
        noEmptyFragment = new UserChatNoEmptyFragment();
    }
    private void initializeDataReceiver()
    {
        dataReceiver = new DataReceiver() {
            @Override
            public void dataReceived(Object output) {
                String response = output.toString();
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    listChatRoom.clear();
                    JSONArray responseArray = jsonResponse.getJSONArray("data");
                    for (int i=0;i<responseArray.length();i++){
                        JSONObject arrayObject = responseArray.getJSONObject(i);
                        ChatResources chatResources = new ChatResources();
                        if (arrayObject.getString("id_user_1").equals(userID)) {
                            chatResources.setIdUser(arrayObject.getString("id_user_2"));
                        }
                        else {
                            chatResources.setIdUser(arrayObject.getString("id_user_1"));
                        }
                        chatResources.setChatRoom(arrayObject.getString("room"));
                        chatResources.setNamaUser(arrayObject.getString("name"));
                        listChatRoom.add(chatResources);
                    }
                    whenDataChatRoomsAlreadyReceived();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
    }
    private void whenDataChatRoomsAlreadyReceived()
    {
        swipeRefreshLayout.setRefreshing(false);
        if (listChatRoom.isEmpty()) {
            getFragmentManager().beginTransaction()
                    .replace(R.id.fragment_chat_room_list_layout, emptyFragment)
                    .commit();
        }
        else {
            swipeRefreshLayout.setEnabled(false);
            noEmptyFragment.setUserID(userID);
            noEmptyFragment.setListChatRoom(listChatRoom);
            getFragmentManager().beginTransaction()
                    .replace(R.id.fragment_chat_room_list_layout, noEmptyFragment)
                    .commit();
        }
    }
    private void setSwipeRefreshLayoutProperties()
    {
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_orange_dark);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //Log.v("RefreshToggle", "Refresh Toggle on FavoriteFragment");
                getChatRoomList();
            }
        });
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                //Log.v("RefreshPost", "Refresh POST on FavoriteFragment");
                swipeRefreshLayout.setRefreshing(true);
                getChatRoomList();
            }
        });
    }
    private void getChatRoomList()
    {
        ChatRoomListAPI.GetRoomList roomList = ChatRoomListAPI.getInstanceRoomList(userID, dataReceiver);
        RequestController.getInstance(getActivity()).addToRequestQueue(roomList);
    }
}
