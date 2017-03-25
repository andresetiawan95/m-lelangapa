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
import com.lelangapa.android.activities.favorite.FavoriteListActivity;
import com.lelangapa.android.activities.feedback.berifeedback.BeriFeedbackActivity;
import com.lelangapa.android.activities.feedback.feedbackanda.FeedbackAndaActivity;
import com.lelangapa.android.activities.profile.EditAlamatActivity;
import com.lelangapa.android.activities.profile.EditPasswordActivity;
import com.lelangapa.android.activities.profile.EditProfileActivity;
import com.lelangapa.android.activities.profile.chat.UserChatActivity;
import com.lelangapa.android.activities.riwayat.RiwayatActivity;
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
            HashMap<String, String> userProfile = SessionManager.getSessionStatic();
            TextView userProfileName = (TextView) view.findViewById(R.id.fragment_userprofile_user_name);
            TextView userProfileEmail = (TextView) view.findViewById(R.id.fragment_userprofile_user_email);
            userProfileName.setText(userProfile.get(sessionManager.getKEY_NAME()));
            userProfileEmail.setText(userProfile.get(sessionManager.getKEY_EMAIL()));
        }

        userProfileRecyclerView = (RecyclerView) view.findViewById(R.id.userprofile_recyclerview);
        upAdapter = new UserProfileAdapter(userProfileList);
        RecyclerView.LayoutManager upLayoutManager = new LinearLayoutManager(getActivity());
        userProfileRecyclerView.setLayoutManager(upLayoutManager);
        userProfileRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayout.VERTICAL));
        userProfileRecyclerView.setFocusable(false);
        //userProfileRecyclerView.setNestedScrollingEnabled(false);
        userProfileRecyclerView.setItemAnimator(new DefaultItemAnimator());
        userProfileRecyclerView.setAdapter(upAdapter);
        userProfileRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), userProfileRecyclerView, new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if(position==0){
                    Intent intent = new Intent(getActivity(), UserChatActivity.class);
                    startActivity(intent);
                }
                else if(position==1){
                    Intent intent = new Intent(getActivity(), UserGeraiActivity.class);
                    startActivity(intent);
                }
                else if(position==2){
                    Intent intent = new Intent(getActivity(), EditProfileActivity.class);
                    startActivity(intent);
                }
                else if (position==3){
                    Intent intent = new Intent(getActivity(), EditAlamatActivity.class);
                    startActivity(intent);
                }
                else if (position==4){
                    Intent intent = new Intent(getActivity(), EditPasswordActivity.class);
                    startActivity(intent);
                }
                else if (position==5){
                    Intent intent = new Intent(getActivity(), FavoriteListActivity.class);
                    startActivity(intent);
                }
                else if (position==6){
                    Intent intent = new Intent(getActivity(), RiwayatActivity.class);
                    startActivity(intent);
                }
                else if (position==7){
                    Intent intent = new Intent(getActivity(), BeriFeedbackActivity.class);
                    startActivity(intent);
                }
                else if (position==8){
                    Intent intent = new Intent(getActivity(), FeedbackAndaActivity.class);
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
        userProfileMenuData.setUserProfileMenu("Barang Favorit");
        userProfileMenuData.setUserProfileMenuDesc("Periksa barang favorit anda.");
        userProfileList.add(userProfileMenuData);
        userProfileMenuData = new UserProfile();
        userProfileMenuData.setUserProfileMenu("Riwayat Penawaran");
        userProfileMenuData.setUserProfileMenuDesc("Periksa riwayat penawaran anda.");
        userProfileList.add(userProfileMenuData);
        userProfileMenuData = new UserProfile();
        userProfileMenuData.setUserProfileMenu("Beri Feedback");
        userProfileMenuData.setUserProfileMenuDesc("Beri feedback sebagai auctioneer/winner.");
        userProfileList.add(userProfileMenuData);
        userProfileMenuData = new UserProfile();
        userProfileMenuData.setUserProfileMenu("Feedback Anda");
        userProfileMenuData.setUserProfileMenuDesc("Periksa feedback yang diberikan kepada anda.");
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
