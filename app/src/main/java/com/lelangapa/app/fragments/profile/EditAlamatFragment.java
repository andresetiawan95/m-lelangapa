package com.lelangapa.app.fragments.profile;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.lelangapa.app.R;
import com.lelangapa.app.apicalls.EditAlamatAPI;
import com.lelangapa.app.apicalls.GetUserProfileAPI;
import com.lelangapa.app.apicalls.singleton.RequestController;
import com.lelangapa.app.interfaces.DataReceiver;
import com.lelangapa.app.preferences.SessionManager;
import com.lelangapa.app.preferences.sqlites.SQLiteHandler;
import com.lelangapa.app.resources.sqls.GeoStatics;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by andre on 22/11/16.
 */

public class EditAlamatFragment extends Fragment {
    private EditText editText_Alamat;
    private String alamat_Response;
    private Button editAlamat_simpan;
    private Spinner spinnerProvince, spinnerCity;
    private LinearLayout linearLayout_spinners;
    private RequestQueue queue;
    private DataReceiver uploadUserAlamatData;
    private SessionManager sessionManager;
    private HashMap<String, String> userAlamatData = new HashMap<>();
    private HashMap<String, String> session;
    private static int ProvID, CityID;
    private static ArrayList<String> listProvinceName, listCityName;

    private SQLiteHandler dbhandler;
    public EditAlamatFragment(){
        initializeConstants();
    };
    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_userprofile_editalamat_layout, container, false);
        initializeViews(view);
        initializeContentProviders();
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
                            ProvID = userDataObject.getInt("province_id_return");
                            CityID = userDataObject.getInt("city_id_return");
                            setupSpinnerProperties();
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
    private void initializeConstants() {
        listProvinceName = new ArrayList<>();
        listCityName = new ArrayList<>();
    }
    private void initializeViews(View view) {
        editText_Alamat = (EditText) view.findViewById(R.id.fragment_userprofile_editalamat_alamat);
        editText_Alamat.setVisibility(View.INVISIBLE);
        editAlamat_simpan = (Button) view.findViewById(R.id.fragment_userprofile_editalamat_simpan);
        spinnerProvince = (Spinner) view.findViewById(R.id.fragment_userprofile_editalamat_province);
        spinnerCity = (Spinner) view.findViewById(R.id.fragment_userprofile_editalamat_city);
        linearLayout_spinners = (LinearLayout) view.findViewById(R.id.fragment_userprofile_editalamat_city_province_spinners);
        linearLayout_spinners.setVisibility(View.GONE);
    }
    private void initializeContentProviders() {
        dbhandler = new SQLiteHandler(getActivity());
    }
    private void setupSpinnerProperties() {
        int currentProvincePosition = 0;
        linearLayout_spinners.setVisibility(View.VISIBLE);
        dbhandler.getAllProvinceList();
        listProvinceName.clear();
        for (int x = 0; x< GeoStatics.getInstance().getProvincesList().size(); x++) {
            listProvinceName.add(GeoStatics.getInstance().getProvincesList().get(x).getProvinceName());
        }
        for (int x=0;x<GeoStatics.getInstance().getProvincesList().size(); x++) {
            if (GeoStatics.getInstance().getProvincesList().get(x).getProvinceID() == ProvID) {
                currentProvincePosition = x;
                break;
            }
        }
        ArrayAdapter provinceAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, listProvinceName);
        provinceAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerProvince.setAdapter(provinceAdapter);
        spinnerProvince.setSelection(currentProvincePosition);
        spinnerProvince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ProvID = GeoStatics.getInstance().getProvincesList().get(position).getProvinceID();
                int currentCityPosition=0;
                dbhandler.getAllCitiesList(ProvID);
                listCityName.clear();
                for (int x=0;x<GeoStatics.getInstance().getCitiesList().size();x++) {
                    listCityName.add(GeoStatics.getInstance().getCitiesList().get(x).getCityName());
                }
                for (int x=0;x<GeoStatics.getInstance().getCitiesList().size();x++) {
                    if (GeoStatics.getInstance().getCitiesList().get(x).getCityID() == CityID) {
                        currentCityPosition = x;
                        break;
                    }
                }
                ArrayAdapter cityAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, listCityName);
                cityAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                spinnerCity.setAdapter(cityAdapter);
                spinnerCity.setSelection(currentCityPosition);
                spinnerCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        CityID = GeoStatics.getInstance().getCitiesList().get(position).getCityID();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {}
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
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
        userAlamatData.put("id_province", Integer.toString(ProvID));
        userAlamatData.put("id_city", Integer.toString(CityID));
    }
}
