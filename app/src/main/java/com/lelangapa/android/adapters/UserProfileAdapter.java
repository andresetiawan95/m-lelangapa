package com.lelangapa.android.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lelangapa.android.R;
import com.lelangapa.android.resources.UserProfile;

import java.util.List;

/**
 * Created by Andre on 11/19/2016.
 */

public class UserProfileAdapter extends RecyclerView.Adapter<UserProfileAdapter.MyViewHolder> {
    private List<UserProfile> userProfile;

    public UserProfileAdapter(List<UserProfile> userProfile){
        this.userProfile = userProfile;
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView upMenu, upMenuDesc;
        public MyViewHolder(View view){
            super(view);
            upMenu = (TextView) view.findViewById(R.id.userprofile_menu_list_maintitle);
            upMenuDesc = (TextView) view.findViewById(R.id.userprofile_menu_list_menudesc);
        }
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.userprofile_menu_list, parent, false);
        return new MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(MyViewHolder viewHolder, int position){
        UserProfile up = userProfile.get(position);
        viewHolder.upMenu.setText(up.getUserProfileMenu());
        viewHolder.upMenuDesc.setText(up.getUserProfileMenuDesc());
    }
    @Override
    public int getItemCount(){
        return userProfile.size();
    }
}
