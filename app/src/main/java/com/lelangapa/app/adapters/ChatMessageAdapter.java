package com.lelangapa.app.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lelangapa.app.R;
import com.lelangapa.app.resources.ChatResources;

import java.util.ArrayList;

/**
 * Created by andre on 25/03/17.
 */

public class ChatMessageAdapter extends RecyclerView.Adapter<ChatMessageAdapter.ViewHolder> {
    private Context context;
    private ArrayList<ChatResources> listChatMessage;

    public ChatMessageAdapter(Context context, ArrayList<ChatResources> list)
    {
        this.context = context;
        this.listChatMessage = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case ChatResources.TYPE_MESSAGE_SENT :
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.fragment_chat_room_message_items_send, parent, false);
                return new SentMessageViewHolder(view);
            case ChatResources.TYPE_MESSAGE_RECEIVED:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.fragment_chat_room_message_items_receive, parent, false);
                return new ReceivedMessageViewHolder(view);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        int viewType = listChatMessage.get(position).getChatType();
        switch (viewType) {
            case ChatResources.TYPE_MESSAGE_SENT:
                SentMessageViewHolder sentMessageViewHolder = (SentMessageViewHolder) holder;
                sentMessageViewHolder.textView_namaUser.setText(listChatMessage.get(position).getNamaUser());
                sentMessageViewHolder.textView_message.setText(listChatMessage.get(position).getChatMessage());
                break;
            case ChatResources.TYPE_MESSAGE_RECEIVED:
                ReceivedMessageViewHolder receivedMessageViewHolder = (ReceivedMessageViewHolder) holder;
                receivedMessageViewHolder.textView_namaUser.setText(listChatMessage.get(position).getNamaUser());
                receivedMessageViewHolder.textView_message.setText(listChatMessage.get(position).getChatMessage());
                break;
        }
    }

    @Override
    public int getItemCount() {
        return listChatMessage.size();
    }

    @Override
    public int getItemViewType(int position)
    {
        return listChatMessage.get(position).getChatType();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public ViewHolder (View view)
        {
            super(view);
        }
    }

    public class SentMessageViewHolder extends ViewHolder
    {
        private TextView textView_namaUser, textView_message;
        public SentMessageViewHolder(View view)
        {
            super(view);
            textView_namaUser = (TextView) view.findViewById(R.id.fragment_chat_room_message_send_name);
            textView_message = (TextView) view.findViewById(R.id.fragment_chat_room_message_send_msg);
        }
    }

    public class ReceivedMessageViewHolder extends ViewHolder
    {
        private TextView textView_namaUser, textView_message;
        public ReceivedMessageViewHolder(View view)
        {
            super(view);
            textView_namaUser = (TextView) view.findViewById(R.id.fragment_chat_room_message_receive_name);
            textView_message = (TextView) view.findViewById(R.id.fragment_chat_room_message_receive_msg);
        }
    }
}
