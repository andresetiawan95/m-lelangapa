package com.lelangkita.android.fragments.profile;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.lelangkita.android.R;
import com.lelangkita.android.apicalls.EditProfileAPI;
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
    private EditText editText_editProfile_Telepon;
    private SessionManager sessionManager;
    private HashMap<String, String> session;
    private ProgressBar progressBar_infoakun, progressBar_infokontak;
    public EditProfileFragment(){}
    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_userprofile_editprofile_layout, container, false);
        editText_editProfile_Nama = (EditText) view.findViewById(R.id.fragment_usereditprofile_name);
        editText_editProfile_Telepon = (EditText) view.findViewById(R.id.fragment_usereditprofile_phone);
        editText_editProfile_Email = (EditText) view.findViewById(R.id.fragment_usereditprofile_email);
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
                        editText_editProfile_Nama.setText(editText_nama);
                        editText_editProfile_Telepon.setText(editText_telepon);
                        editText_editProfile_Email.setText(userDataObject.getString("email"));
                    }
                    else {
                        Toast.makeText(getActivity(), "Tidak ada data", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        EditProfileAPI editProfileAPI = new EditProfileAPI(session.get(sessionManager.getKEY_ID()), dataReceiver);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(editProfileAPI);
    }
}
