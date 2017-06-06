package com.lelangapa.app.fragments.profile.chat.chatroom;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageView;

import com.lelangapa.app.R;
import com.lelangapa.app.activities.profile.chat.UserChatRoomActivity;
import com.lelangapa.app.adapters.ChatMessageAdapter;
import com.lelangapa.app.apicalls.socket.ChattingSocket;
import com.lelangapa.app.interfaces.SocketReceiver;
import com.lelangapa.app.resources.ChatResources;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.socket.client.Socket;

/**
 * Created by andre on 28/11/16.
 */

public class UserChatRoomFragment extends Fragment {
    private ArrayList<ChatResources> listChatMessages;

    private ChattingSocket chattingSocket;
    private Socket socketBinder;
    private SocketReceiver onConnectedHandshake, onChatHistoryReceived, onSendStatus, onMessageReceived;
    private String yourUserID, yourFriendUserID, yourFriendName, roomChat;
    private ChatMessageAdapter chatMessageAdapter;
    private RecyclerView recyclerView_message;
    private EditText editText_message;
    private ImageView imageView_send;

    private View.OnClickListener onClickListener;
    private ViewTreeObserver.OnGlobalLayoutListener onGlobalLayoutListener;

    private Bundle bundleExtras;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        initializeConstants();
        initializeSocketReceivers();
        initializeSocket();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_chat_room_message_layout, container, false);
        initializeViews(view);
        checkViewScreenSize(view);
        setOnClickListener();
        setViewProperties();
        setRecyclerViewAdapter();
        setRecyclerViewProperties();
        connectSocket();
        return view;
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        disconnectSocket();
    }
    private void initializeConstants()
    {
        listChatMessages = new ArrayList<>();
        bundleExtras = getActivity().getIntent().getExtras();
        yourUserID = bundleExtras.getString("your_user_id");
        yourFriendUserID = bundleExtras.getString("your_friend_user_id");
        yourFriendName = bundleExtras.getString("your_friend_name");

        roomChat = yourUserID + "-" + yourFriendUserID;
    }
    private void initializeSocketReceivers()
    {
        setSocketOnConnectedHandshake();
        setSocketOnChatHistoryReceived();
        setSocketOnSentStatus();
        setSocketOnMessageReceived();
    }
    private void initializeSocket()
    {
        if (chattingSocket == null)
        {
            chattingSocket = new ChattingSocket(getActivity());
            chattingSocket.setSocketConnectedHandshake(onConnectedHandshake);
            chattingSocket.setSocketChatHistory(onChatHistoryReceived);
            chattingSocket.setSocketOnNewMessageReceived(onMessageReceived);
            chattingSocket.setSocketSendStatus(onSendStatus);
            socketBinder = chattingSocket.getSocket();
        }
    }
    private void initializeViews(View view)
    {
        recyclerView_message = (RecyclerView) view.findViewById(R.id.fragment_chat_message_room_layout_recyclerview);
        editText_message = (EditText) view.findViewById(R.id.fragment_chat_message_room_layout_input_msg);
        imageView_send = (ImageView) view.findViewById(R.id.fragment_chat_message_room_layout_send_msg);
    }
    private void setOnClickListener()
    {
        onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptSendMessage();
            }
        };
    }
    private void setViewProperties()
    {
        imageView_send.setOnClickListener(onClickListener);
    }
    private void checkViewScreenSize(View view)
    {
        setOnGlobalLayoutListener(view);
        view.getViewTreeObserver().addOnGlobalLayoutListener(onGlobalLayoutListener);
    }
    private void setOnGlobalLayoutListener(final View view)
    {
        onGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                view.getWindowVisibleDisplayFrame(r);
                int screenHeight = view.getRootView().getHeight();

                // r.bottom is the position above soft keypad or device button.
                // if keypad is shown, the r.bottom is smaller than that before.
                int keypadHeight = screenHeight - r.bottom;

                //Log.d("keypad height", Integer.toString(keypadHeight) + " " + Double.toString(screenHeight * 0.15));

                if (keypadHeight > screenHeight * 0.15) { // 0.15 ratio is perhaps enough to determine keypad height.
                    // keyboard is opened
                    scrollToBottom();
                }
                else {
                    // keyboard is closed
                }
            }
        };
    }
    private void setRecyclerViewAdapter()
    {
        chatMessageAdapter = new ChatMessageAdapter(getActivity(), listChatMessages);
    }
    private void setRecyclerViewProperties()
    {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView_message.setLayoutManager(layoutManager);
        recyclerView_message.setItemAnimator(new DefaultItemAnimator());
        recyclerView_message.setAdapter(chatMessageAdapter);
    }
    private void setSocketOnConnectedHandshake()
    {
        onConnectedHandshake = new SocketReceiver() {
            @Override
            public void socketReceived(Object status, Object response) {
                if (!chattingSocket.IS_JOINED_ROOM_STATUS)
                {
                    Log.v("Socket CONNECTED", "CONNECTED AND INITIALIZED");
                    socketBinder.emit("join-room", roomChat);
                    Log.v("Joining room init", "Joining room now");
                }
            }
        };
    }
    private void setSocketOnChatHistoryReceived()
    {
        onChatHistoryReceived = new SocketReceiver() {
            @Override
            public void socketReceived(Object status, Object response) {
                Log.v("Success Join", "Joining room success");
                Log.v("history recv", "history chat received");
                if (isResumed()) {
                    listChatMessages.clear();
                    JSONArray jsonArray = (JSONArray) response;
                    try {
                        for (int i = jsonArray.length() -1 ; i >=0; i--)
                        {
                            JSONObject arrayObject = jsonArray.getJSONObject(i);
                            ChatResources chatResources = new ChatResources();
                            chatResources.setChatMessage(arrayObject.getString("msg"));
                            if (arrayObject.getString("sender").equals(yourUserID)) {
                                chatResources.setIdUser(yourUserID);
                                chatResources.setNamaUser("You");
                                chatResources.setChatType(ChatResources.TYPE_MESSAGE_SENT);
                            }
                            else {
                                chatResources.setIdUser(yourFriendUserID);
                                chatResources.setNamaUser(yourFriendName);
                                chatResources.setChatType(ChatResources.TYPE_MESSAGE_RECEIVED);
                            }
                            chatResources.setChatRoom(arrayObject.getString("room"));
                            listChatMessages.add(chatResources);
                        }
                        ((UserChatRoomActivity) getActivity()).changeActionBarTitle(yourFriendName);
                        updateChatAdapter();
                        scrollToBottom();
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
    }
    private void setSocketOnSentStatus()
    {
        onSendStatus = new SocketReceiver() {
            @Override
            public void socketReceived(Object status, Object response) {
                //implemented later, maybe
            }
        };
    }
    private void setSocketOnMessageReceived()
    {
        //ketika ada pesan masuk
        onMessageReceived = new SocketReceiver() {
            @Override
            public void socketReceived(Object status, Object response) {
                JSONObject jsonObject = (JSONObject) response;
                try {
                    Log.v("MSGRECV", jsonObject.toString());
                    if (!jsonObject.getString("sender").equals(yourUserID))
                    {
                        String message = jsonObject.getString("msg");
                        addMessage(ChatResources.TYPE_MESSAGE_RECEIVED, message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
    }
    private void connectSocket()
    {
        Log.v("Connecting socket", "CONNECTING SOCKET...");
        socketBinder.connect();
        socketBinder.on("handshake", chattingSocket.onConnectedHandshake);
        socketBinder.on("chathistory", chattingSocket.onChatHistory);
        socketBinder.on("send-status", chattingSocket.onSendStatus);
        socketBinder.on("new-msg", chattingSocket.onNewMessage);
    }
    private void disconnectSocket()
    {
        socketBinder.disconnect();
        socketBinder.off("handshake", chattingSocket.onConnectedHandshake);
        socketBinder.off("chathistory", chattingSocket.onChatHistory);
        socketBinder.off("send-status", chattingSocket.onSendStatus);
        socketBinder.off("new-msg", chattingSocket.onNewMessage);
    }
    private void updateChatAdapter()
    {
        chatMessageAdapter.notifyDataSetChanged();
    }
    private void addMessage(int type, String message)
    {
        ChatResources cr = new ChatResources();
        if (type == ChatResources.TYPE_MESSAGE_SENT)
        {
            cr.setChatMessage(message);
            cr.setChatType(ChatResources.TYPE_MESSAGE_SENT);
            cr.setNamaUser("You");
            cr.setIdUser(yourUserID);
        }
        else
        {
            cr.setChatMessage(message);
            cr.setChatType(ChatResources.TYPE_MESSAGE_RECEIVED);
            cr.setNamaUser(yourFriendName);
            cr.setIdUser(yourFriendUserID);
        }
        listChatMessages.add(cr);
        chatMessageAdapter.notifyItemInserted(listChatMessages.size() -1);
        scrollToBottom();
    }
    private void attemptSendMessage()
    {
        if (!socketBinder.connected()) return;
        String message = editText_message.getText().toString().trim();
        if (TextUtils.isEmpty(message))
        {
            editText_message.requestFocus();
            return;
        }
        editText_message.setText("");
        addMessage(ChatResources.TYPE_MESSAGE_SENT, message);

        JSONObject sendMessage = new JSONObject();
        try {
            sendMessage.put("room", roomChat);
            sendMessage.put("body", message);

            socketBinder.emit("send", sendMessage.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void scrollToBottom()
    {
        if (chatMessageAdapter.getItemCount() > 0) {
            Log.v("scrolling", "scrolling to bottom");
            //recyclerView_message.getLayoutManager().smoothScrollToPosition(recyclerView_message, null, chatMessageAdapter.getItemCount() - 1);
            recyclerView_message.smoothScrollToPosition(chatMessageAdapter.getItemCount() - 1);
        }
    }
}
