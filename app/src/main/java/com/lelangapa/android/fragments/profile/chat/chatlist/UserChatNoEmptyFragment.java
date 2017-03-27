package com.lelangapa.android.fragments.profile.chat.chatlist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.lelangapa.android.R;
import com.lelangapa.android.activities.profile.chat.UserChatRoomActivity;
import com.lelangapa.android.adapters.ChatRoomListAdapter;
import com.lelangapa.android.apicalls.chat.ChatRoomListAPI;
import com.lelangapa.android.apicalls.singleton.RequestController;
import com.lelangapa.android.decorations.DividerItemDecoration;
import com.lelangapa.android.interfaces.DataReceiver;
import com.lelangapa.android.interfaces.OnItemClickListener;
import com.lelangapa.android.listeners.RecyclerItemClickListener;
import com.lelangapa.android.resources.ChatResources;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by andre on 22/03/17.
 */

public class UserChatNoEmptyFragment extends Fragment {
    private ArrayList<ChatResources> listChatRoom;
    private String userID;
    private DataReceiver dataReceiver;

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView_roomList;
    private ChatRoomListAdapter chatRoomListAdapter;

    private Intent intent;
    private Bundle bundleExtras;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        initializeConstants();
        initializeDataReceiver();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_chat_room_list_noempty_layout, container, false);
        initializeViews(view);
        setSwipeRefreshLayoutProperties();
        initializeRecyclerViewAdapter();
        initializeRecyclerViewProperties();
        return view;
    }
    private void initializeConstants()
    {
        intent = new Intent(getActivity(), UserChatRoomActivity.class);
    }
    private void initializeViews(View view)
    {
        swipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.fragment_chat_room_list_layout_noempty_swipeRefreshLayout);
        recyclerView_roomList = (RecyclerView) view.findViewById(R.id.fragment_chat_room_list_layout_recyclerview);
    }
    private void initializeRecyclerViewAdapter()
    {
        chatRoomListAdapter = new ChatRoomListAdapter(getActivity(), listChatRoom);
    }
    private void initializeRecyclerViewProperties()
    {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView_roomList.setLayoutManager(layoutManager);
        recyclerView_roomList.setItemAnimator(new DefaultItemAnimator());
        recyclerView_roomList.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayout.VERTICAL));
        recyclerView_roomList.setAdapter(chatRoomListAdapter);
        recyclerView_roomList.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), recyclerView_roomList, new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                bundleExtras = new Bundle();
                bundleExtras.putString("your_user_id", userID);
                bundleExtras.putString("your_friend_user_id", listChatRoom.get(position).getIdUser());
                bundleExtras.putString("your_friend_name", listChatRoom.get(position).getNamaUser());
                intent.putExtras(bundleExtras);
                startActivity(intent);
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));
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
                    chatRoomListAdapter.updateDataset(listChatRoom);
                    swipeRefreshLayout.setRefreshing(false);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
    }
    private void setSwipeRefreshLayoutProperties()
    {
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_orange_dark);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getChatRoomList();
            }
        });
    }
    public void setUserID(String userID)
    {
        this.userID = userID;
    }
    public void setListChatRoom(ArrayList<ChatResources> list)
    {
        this.listChatRoom = list;
    }
    private void getChatRoomList()
    {
        ChatRoomListAPI.GetRoomList roomList = ChatRoomListAPI.getInstanceRoomList(userID, dataReceiver);
        RequestController.getInstance(getActivity()).addToRequestQueue(roomList);
    }
}
