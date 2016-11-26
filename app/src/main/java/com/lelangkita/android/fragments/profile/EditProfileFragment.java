package com.lelangkita.android.fragments.profile;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.lelangkita.android.R;
import com.lelangkita.android.activities.ProfileActivity;
import com.lelangkita.android.apicalls.EditUserProfileAPI;
import com.lelangkita.android.apicalls.GetUserProfileAPI;
import com.lelangkita.android.interfaces.DataReceiver;
import com.lelangkita.android.preferences.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Andre on 11/19/2016.
 */

public class EditProfileFragment extends Fragment {
    private EditText editText_editProfile_Nama, editText_editProfile_Email;
    private String editText_nama;
    private String editText_telepon;
    private String editText_email;
    private EditText editText_editProfile_Telepon;
    private Button editProfile_simpan_btn;
    private SessionManager sessionManager;
    private DataReceiver uploadUserProfileData;
    private RequestQueue queue;
    private HashMap<String, String> session;
    private HashMap<String, String> userProfileData;
    private ProgressBar progressBar_infoakun, progressBar_infokontak;
    public EditProfileFragment(){}
    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_userprofile_editprofile_layout, container, false);
        editText_editProfile_Nama = (EditText) view.findViewById(R.id.fragment_usereditprofile_name);
        editText_editProfile_Telepon = (EditText) view.findViewById(R.id.fragment_usereditprofile_phone);
        editText_editProfile_Email = (EditText) view.findViewById(R.id.fragment_usereditprofile_email);
        editProfile_simpan_btn = (Button) view.findViewById(R.id.fragment_usereditprofile_simpan_button);
        progressBar_infoakun = (ProgressBar) view.findViewById(R.id.fragment_userprofile_editprofile_progress_bar_informasiakun);
        progressBar_infoakun.setVisibility(View.VISIBLE);
        progressBar_infokontak = (ProgressBar) view.findViewById(R.id.fragment_userprofile_editprofile_progress_bar_informasikontak);
        progressBar_infokontak.setVisibility(View.VISIBLE);
        editText_editProfile_Nama.setVisibility(View.INVISIBLE);
        editText_editProfile_Telepon.setVisibility(View.INVISIBLE);
        editText_editProfile_Email.setVisibility(View.INVISIBLE);
        sessionManager = new SessionManager(getActivity());
        session = sessionManager.getSession();
        return view;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        DataReceiver dataReceiver = new DataReceiver() {
            @Override
            public void dataReceived(Object output) {
                String response = output.toString();
                progressBar_infoakun.setVisibility(View.GONE);
                progressBar_infokontak.setVisibility(View.GONE);
                editText_editProfile_Nama.setVisibility(View.VISIBLE);
                editText_editProfile_Nama.setEnabled(false);
                editText_editProfile_Nama.setFocusable(false);
                editText_editProfile_Telepon.setVisibility(View.VISIBLE);
                editText_editProfile_Email.setVisibility(View.VISIBLE);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray responseData = jsonObject.getJSONArray("data");
                    JSONObject userDataObject = responseData.getJSONObject(0);
                    if (userDataObject!=null){
                        editText_nama = userDataObject.getString("name");
                        editText_telepon = userDataObject.getString("phone");
                        editText_email = userDataObject.getString("email");
                        editText_editProfile_Nama.setText(editText_nama);
                        editText_editProfile_Telepon.setText(editText_telepon);
                        editText_editProfile_Email.setText(editText_email);
                    }
                    else {
                        Toast.makeText(getActivity(), "Tidak ada data", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        GetUserProfileAPI getUserProfileAPI = new GetUserProfileAPI(session.get(sessionManager.getKEY_ID()), dataReceiver);
        queue = Volley.newRequestQueue(getActivity());
        queue.add(getUserProfileAPI);
        editProfile_simpan_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserProfileAPI();
            }
        });
    }
    private void updateUserProfileAPI (){
        String editUserProfileNama = editText_editProfile_Nama.getText().toString();
        String editUserProfileTelepon = editText_editProfile_Telepon.getText().toString();
        final String editUserProfileEmail = editText_editProfile_Email.getText().toString();
        final String oldUserProfileEmail = session.get(sessionManager.getKEY_EMAIL());
        putUserProfileData(session.get(sessionManager.getKEY_ID()),editUserProfileNama, editUserProfileTelepon, editUserProfileEmail);
        uploadUserProfileData = new DataReceiver() {
            @Override
            public void dataReceived(Object output) {
                String response = output.toString();
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    String result = jsonResponse.getString("status");
                    if (result.equals("success")){
                        if (!(oldUserProfileEmail.equals(editUserProfileEmail))){
                            sessionManager.editEmailSessionPreference(editUserProfileEmail);
                        }
                        Toast.makeText(getActivity(), "Pengubahan profil telah berhasil", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent (getActivity(), ProfileActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        getActivity().startActivity(intent);
                        getActivity().finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };
        EditUserProfileAPI editUserProfileAPI = new EditUserProfileAPI(userProfileData, uploadUserProfileData);
        queue.add(editUserProfileAPI);
    }
    private void putUserProfileData(String _userid, String _name, String _phone, String _email){
        userProfileData = new HashMap<>();
        userProfileData.put("userid", _userid);
        userProfileData.put("name", _name);
        userProfileData.put("phone", _phone);
        userProfileData.put("email", _email);
    }
}
