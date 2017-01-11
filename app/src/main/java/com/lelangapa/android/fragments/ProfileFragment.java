package com.lelangapa.android.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lelangapa.android.R;
import com.lelangapa.android.activities.UserGeraiActivity;
import com.lelangapa.android.activities.profile.EditAlamatActivity;
import com.lelangapa.android.activities.profile.EditPasswordActivity;
import com.lelangapa.android.activities.profile.EditProfileActivity;
import com.lelangapa.android.adapters.UserProfileAdapter;
import com.lelangapa.android.decorations.DividerItemDecoration;
import com.lelangapa.android.interfaces.OnItemClickListener;
import com.lelangapa.android.listeners.RecyclerItemClickListener;
import com.lelangapa.android.preferences.SessionManager;
import com.lelangapa.android.resources.UserProfile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Andre on 11/18/2016.
 */

public class ProfileFragment extends Fragment {
    private RecyclerView userProfileRecyclerView;
    private List<UserProfile> userProfileList = new ArrayList<>();
    private UserProfileAdapter upAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_userprofile_layout, container ,false);

        SessionManager sessionManager = new SessionManager(getActivity());
        if (sessionManager.isLoggedIn()){
            HashMap<String, String> userProfile = sessionManager.getSession();
            TextView userProfileName = (TextView) view.findViewById(R.id.fragment_userprofile_user_name);
            TextView userProfileEmail = (TextView) view.findViewById(R.id.fragment_userprofile_user_email);
            userProfileName.setText(userProfile.get(sessionManager.getKEY_NAME()));
            userProfileEmail.setText(userProfile.get(sessionManager.getKEY_EMAIL()));
        }

        userProfileRecyclerView = (RecyclerView) view.findViewById(R.id.userprofile_recyclerview);
        upAdapter = new UserProfileAdapter(userProfileList);
        RecyclerView.LayoutManager upLayoutManager = new LinearLayoutManager(getActivity()) {
            @Override
            public boolean canScrollVertically(){
                return false;
            }
        };
        userProfileRecyclerView.setLayoutManager(upLayoutManager);
        userProfileRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayout.VERTICAL));
        userProfileRecyclerView.setFocusable(false);
        userProfileRecyclerView.setItemAnimator(new DefaultItemAnimator());
        userProfileRecyclerView.setAdapter(upAdapter);
        userProfileRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), userProfileRecyclerView, new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if(position==1){
                    Intent intent = new Intent(getActivity(), UserGeraiActivity.class);
                    startActivity(intent);
                }
                if(position==2){
                    Intent intent = new Intent(getActivity(), EditProfileActivity.class);
                    startActivity(intent);
                }
                if (position==3){
                    Intent intent = new Intent(getActivity(), EditAlamatActivity.class);
                    startActivity(intent);
                }
                if (position==4){
                    Intent intent = new Intent(getActivity(), EditPasswordActivity.class);
                    startActivity(intent);
                }
            }
            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));

        UserProfile userProfileMenuData;

        userProfileMenuData = new UserProfile();
        userProfileMenuData.setUserProfileMenu("Chat");
        userProfileMenuData.setUserProfileMenuDesc("Periksa pesan anda.");
        userProfileList.add(userProfileMenuData);
        userProfileMenuData = new UserProfile();
        userProfileMenuData.setUserProfileMenu("Gerai");
        userProfileMenuData.setUserProfileMenuDesc("Periksa gerai lelang anda.");
        userProfileList.add(userProfileMenuData);
        userProfileMenuData = new UserProfile();
        userProfileMenuData.setUserProfileMenu("Update Profil");
        userProfileMenuData.setUserProfileMenuDesc("Perbaharui informasi profil anda.");
        userProfileList.add(userProfileMenuData);
        userProfileMenuData = new UserProfile();
        userProfileMenuData.setUserProfileMenu("Update Alamat");
        userProfileMenuData.setUserProfileMenuDesc("Perbaharui informasi alamat anda.");
        userProfileList.add(userProfileMenuData);
        userProfileMenuData = new UserProfile();
        userProfileMenuData.setUserProfileMenu("Update Password");
        userProfileMenuData.setUserProfileMenuDesc("Ubah kata sandi anda.");
        userProfileList.add(userProfileMenuData);
        userProfileMenuData = new UserProfile();
        userProfileMenuData.setUserProfileMenu("Review");
        userProfileMenuData.setUserProfileMenuDesc("Periksa review mengenai anda.");
        userProfileList.add(userProfileMenuData);
        userProfileMenuData = new UserProfile();
        userProfileMenuData.setUserProfileMenu("Tiket Bantuan");
        userProfileMenuData.setUserProfileMenuDesc("Buka tiket bantuan anda.");
        userProfileList.add(userProfileMenuData);
        userProfileMenuData = new UserProfile();
        userProfileMenuData.setUserProfileMenu("Logout");
        userProfileMenuData.setUserProfileMenuDesc("Logout akun anda.");
        userProfileList.add(userProfileMenuData);
        upAdapter.notifyDataSetChanged();

        return view;
    }
}
