package com.lelangapa.app.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lelangapa.app.R;
import com.lelangapa.app.activities.UserGeraiActivity;
import com.lelangapa.app.activities.favorite.FavoriteListActivity;
import com.lelangapa.app.activities.feedback.berifeedback.BeriFeedbackActivity;
import com.lelangapa.app.activities.feedback.feedbackanda.FeedbackAndaActivity;
import com.lelangapa.app.activities.profile.EditAlamatActivity;
import com.lelangapa.app.activities.profile.EditPasswordActivity;
import com.lelangapa.app.activities.profile.EditProfileActivity;
import com.lelangapa.app.activities.profile.chat.UserChatActivity;
import com.lelangapa.app.activities.riwayat.RiwayatActivity;
import com.lelangapa.app.adapters.UserProfileAdapter;
import com.lelangapa.app.decorations.DividerItemDecoration;
import com.lelangapa.app.interfaces.OnItemClickListener;
import com.lelangapa.app.listeners.RecyclerItemClickListener;
import com.lelangapa.app.preferences.SessionManager;
import com.lelangapa.app.resources.UserProfile;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andre on 11/18/2016.
 */

public class ProfileFragment extends Fragment {
    private RecyclerView userProfileRecyclerView;
    private List<UserProfile> userProfileList = new ArrayList<>();
    private UserProfileAdapter upAdapter;

    private TextView userProfileName, userProfileEmail;
    private ImageView imageView_avatar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_userprofile_layout, container ,false);
        initializeViews(view);
        SessionManager sessionManager = new SessionManager(getActivity());
        if (sessionManager.isLoggedIn()){
            userProfileName.setText(SessionManager.getSessionStatic().get(sessionManager.getKEY_NAME()));
            userProfileEmail.setText(SessionManager.getSessionStatic().get(sessionManager.getKEY_EMAIL()));
            loadUserAvatar();
        }
        initializeMenuAdapter();
        setupRecyclerViewProperties();
        generateMenuList();
        return view;
    }
    private void initializeViews(View view) {
        imageView_avatar = (ImageView) view.findViewById(R.id.fragment_userprofile_image_profile);
        userProfileName = (TextView) view.findViewById(R.id.fragment_userprofile_user_name);
        userProfileEmail = (TextView) view.findViewById(R.id.fragment_userprofile_user_email);
        userProfileRecyclerView = (RecyclerView) view.findViewById(R.id.userprofile_recyclerview);
    }
    private void initializeMenuAdapter() {
        upAdapter = new UserProfileAdapter(userProfileList);
    }
    private void setupRecyclerViewProperties() {
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
    }
    private void generateMenuList() {
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
        /*userProfileMenuData = new UserProfile();
        userProfileMenuData.setUserProfileMenu("Tiket Bantuan");
        userProfileMenuData.setUserProfileMenuDesc("Buka tiket bantuan anda.");
        userProfileList.add(userProfileMenuData);
        userProfileMenuData = new UserProfile();
        userProfileMenuData.setUserProfileMenu("Logout");
        userProfileMenuData.setUserProfileMenuDesc("Logout akun anda.");
        userProfileList.add(userProfileMenuData);*/
        upAdapter.notifyDataSetChanged();

    }
    private void loadUserAvatar() {
        if (!SessionManager.getSessionStatic().get(SessionManager.KEY_AVATAR).equals("null"))
            Picasso.with(getActivity()).load("http://img-s7.lelangapa.com/" + SessionManager.getSessionStatic().get(SessionManager.KEY_AVATAR))
                    .into(imageView_avatar);
    }
}
