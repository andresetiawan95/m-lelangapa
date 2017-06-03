package com.lelangapa.app.fragments.gerai;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.lelangapa.app.R;
import com.lelangapa.app.apicalls.gerai.GetBarangGeraiUserAPI;
import com.lelangapa.app.interfaces.DataReceiver;
import com.lelangapa.app.preferences.SessionManager;
import com.lelangapa.app.resources.UserGeraiResources;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by andre on 01/12/16.
 */

public class UserGeraiFragment extends Fragment {
    private ArrayList<UserGeraiResources> dataBarang = new ArrayList<>();
    private SessionManager sessionManager;
    private View view;
    private DataReceiver received;
    private HashMap<String, String> session;
    private LayoutInflater inflater;
    private ViewGroup container;
    public UserGeraiFragment(){}
    @Override
    public View onCreateView(LayoutInflater in, ViewGroup con, Bundle savedInstanceState){
        sessionManager = new SessionManager(getActivity());
        session = sessionManager.getSession();
        this.inflater = in;
        this.container = con;
        view = inflater.inflate(R.layout.fragment_user_gerai_layout, container, false);
        received = new DataReceiver() {
            @Override
            public void dataReceived(Object output) {
                String res = output.toString();
                if (res.equals("success")){
                    if (!dataBarang.isEmpty()){
                        UserGeraiNoEmptyFragment noEmptyFragment = new UserGeraiNoEmptyFragment();
                        noEmptyFragment.setDataBarang(dataBarang);
                        getFragmentManager().beginTransaction()
                                .replace(R.id.fragment_user_gerai_layout, noEmptyFragment)
                                .commit();
                    }
                    else {
                        UserGeraiEmptyFragment emptyFragment = new UserGeraiEmptyFragment();
                        getFragmentManager().beginTransaction()
                                .replace(R.id.fragment_user_gerai_layout, emptyFragment)
                                .commit();
                    }
                }
            }
        };
        getBarangOnUserID(session.get(sessionManager.getKEY_ID()));
        return view;
    }
    private void getBarangOnUserID(String userID){
        final DataReceiver dataReceiver = new DataReceiver() {
            @Override
            public void dataReceived(Object output) {
                String response = output.toString();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if (status.equals("success")){
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i<jsonArray.length(); i++){
                            JSONObject resObject = jsonArray.getJSONObject(i);
                            UserGeraiResources geraiResources = new UserGeraiResources();
                            geraiResources.setIdbarang(resObject.getString("items_id"));
                            geraiResources.setNamabarang(resObject.getString("items_name"));
                            geraiResources.setNamapengguna(resObject.getString("user_name"));
                            geraiResources.setHargaawal(resObject.getString("starting_price"));
                            geraiResources.setStatuslelang("active");
                            JSONArray resArrayGambar = resObject.getJSONArray("url");
                            for (int j=0;j<resArrayGambar.length();j++){
                                JSONObject resGambarObject = resArrayGambar.getJSONObject(j);
                                geraiResources.setUrlgambarbarang("http://img-s7.lelangapa.com/" + resGambarObject.getString("url"));
                            }
                            dataBarang.add(geraiResources);
                        }
                    }
                    received.dataReceived("success");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        GetBarangGeraiUserAPI getBarangGeraiUserAPI = new GetBarangGeraiUserAPI(userID, dataReceiver);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(getBarangGeraiUserAPI);
    }
}
