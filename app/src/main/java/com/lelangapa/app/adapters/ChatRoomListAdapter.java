package com.lelangapa.app.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lelangapa.app.R;
import com.lelangapa.app.resources.ChatResources;

import java.util.ArrayList;

/**
 * Created by andre on 25/03/17.
 */

public class ChatRoomListAdapter extends RecyclerView.Adapter<ChatRoomListAdapter.ViewHolder>{
    private Context context;
    private ArrayList<ChatResources> listChatRoom;

    public ChatRoomListAdapter(Context context, ArrayList<ChatResources> list)
    {
        this.context = context;
        this.listChatRoom = list;
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public ImageView imageView_avatar;
        public TextView textView_nama;
        public ViewHolder (View view)
        {
            super(view);
            imageView_avatar = (ImageView) view.findViewById(R.id.fragment_chat_room_list_layout_items_avatar);
            textView_nama = (TextView) view.findViewById(R.id.fragment_chat_room_list_layout_items_name);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_chat_room_list_layout_items, parent, false);
        return new ChatRoomListAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ChatResources chatResources = listChatRoom.get(position);
        holder.textView_nama.setText(chatResources.getNamaUser());
    }

    @Override
    public int getItemCount() {
        return listChatRoom.size();
    }

    public void updateDataset(ArrayList<ChatResources> list)
    {
        this.listChatRoom = list;
        notifyDataSetChanged();
    }
}
