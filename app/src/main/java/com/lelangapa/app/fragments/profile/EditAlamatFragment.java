package com.lelangapa.app.fragments.profile;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.lelangapa.app.R;
import com.lelangapa.app.apicalls.EditAlamatAPI;
import com.lelangapa.app.apicalls.GetUserProfileAPI;
import com.lelangapa.app.apicalls.singleton.RequestController;
import com.lelangapa.app.interfaces.DataReceiver;
import com.lelangapa.app.preferences.SessionManager;

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
    private Button editAlamat_simpan;
    private RequestQueue queue;
    private DataReceiver uploadUserAlamatData;
    private SessionManager sessionManager;
    private HashMap<String, String> userAlamatData = new HashMap<>();
    private HashMap<String, String> session;
    public EditAlamatFragment(){};
    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_userprofile_editalamat_layout, container, false);
        editText_Alamat = (EditText) view.findViewById(R.id.fragment_userprofile_editalamat_alamat);
        editText_Alamat.setVisibility(View.INVISIBLE);
        editAlamat_simpan = (Button) view.findViewById(R.id.fragment_userprofile_editalamat_simpan);
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
                if (isResumed()) {
                    String response = output.toString();
                    editText_Alamat.setVisibility(View.VISIBLE);
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray responseData = jsonObject.getJSONArray("data");
                        JSONObject userDataObject = responseData.getJSONObject(0);
                        if (userDataObject!=null){
                            editText_Alamat.setText(userDataObject.getString("address_user_return"));
                        }
                        else {
                            Toast.makeText(getActivity(), "Tidak ada data", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        GetUserProfileAPI getUserProfileAPI = new GetUserProfileAPI(session.get(sessionManager.getKEY_ID()), dataReceiver);
        RequestController.getInstance(getActivity()).addToRequestQueue(getUserProfileAPI);
        editAlamat_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserAlamatAPI();
            }
        });
    }
    private void updateUserAlamatAPI(){
        String updateAlamat = editText_Alamat.getText().toString();
        putUserAlamatData(updateAlamat);
        uploadUserAlamatData = new DataReceiver() {
            @Override
            public void dataReceived(Object output) {
                String response = output.toString();
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    String result = jsonResponse.getString("status");
                    if (result.equals("success")){
                        Toast.makeText(getActivity(), "Alamat berhasil diperbaharui.", Toast.LENGTH_SHORT).show();
                        /*Intent intent = new Intent (getActivity(), ProfileActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        getActivity().startActivity(intent);*/
                        getActivity().finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        EditAlamatAPI editAlamatAPI = new EditAlamatAPI(userAlamatData, uploadUserAlamatData);
        RequestController.getInstance(getActivity()).addToRequestQueue(editAlamatAPI);
    }
    private void putUserAlamatData(String _alamat){
        userAlamatData.put("address", _alamat);
        userAlamatData.put("userid", session.get(sessionManager.getKEY_ID()));
    }
}
