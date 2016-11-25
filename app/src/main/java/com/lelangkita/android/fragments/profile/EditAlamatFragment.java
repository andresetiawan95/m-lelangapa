package com.lelangkita.android.fragments.profile;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
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
 * Created by andre on 22/11/16.
 */

public class EditAlamatFragment extends Fragment {
    private EditText editText_Alamat;
    private String alamat_Response;
    private SessionManager sessionManager;
    private HashMap<String, String> session;
    public EditAlamatFragment(){};
    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_userprofile_editalamat_layout, container, false);
        editText_Alamat = (EditText) view.findViewById(R.id.fragment_userprofile_editalamat_alamat);
        editText_Alamat.setVisibility(View.INVISIBLE);
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
                editText_Alamat.setVisibility(View.VISIBLE);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray responseData = jsonObject.getJSONArray("data");
                    JSONObject userDataObject = responseData.getJSONObject(0);
                    if (userDataObject!=null){
                        editText_Alamat.setText(userDataObject.getString("address"));
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
