package com.lelangkita.android.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.lelangkita.android.R;
import com.lelangkita.android.activities.LoginActivity;
import com.lelangkita.android.apicalls.RegisterAPI;
import com.lelangkita.android.resources.GeoResources;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by andre on 24/10/16.
 */

public class RegisterFragment extends Fragment {
    private Spinner spinnerProvince;
    private Spinner spinnerCity;
    private GeoResources geoResources;
    private Integer ProvID;
    private Integer CityID;
    private EditText namaLengkap;
    private EditText username;
    private EditText password;
    private EditText email;
    private EditText address;
    private EditText telepon;
    private Button btnRegister;
    public RegisterFragment(){

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_register_layout, container, false);
        spinnerProvince = (Spinner) view.findViewById(R.id.spinner_province);
        spinnerCity = (Spinner) view.findViewById(R.id.spinner_city);
        namaLengkap = (EditText) view.findViewById(R.id.fragment_register_name);
        username = (EditText) view.findViewById(R.id.fragment_register_username);
        password = (EditText) view.findViewById(R.id.fragment_register_password);
        email = (EditText) view.findViewById(R.id.fragment_register_email);
        address = (EditText) view.findViewById(R.id.fragment_register_address);
        telepon = (EditText) view.findViewById(R.id.fragment_register_telepon);

        btnRegister = (Button) view.findViewById(R.id.fragment_register_button);

        geoResources = new GeoResources();
        setSpinnerProvince();
//        ((MainActivity)getActivity()).getSupportActionBar().setTitle("Register");
        setHasOptionsMenu(true);
        return view;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                String _namaLengkap = namaLengkap.getText().toString();
                String _username = username.getText().toString();
                String _password = password.getText().toString();
                String _email = email.getText().toString();
                String _address = address.getText().toString();
                String _city = CityID.toString();
                String _province = ProvID.toString();
                String _telepon = telepon.getText().toString();
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            String res = jsonResponse.getString("status");
                            if (res.equals("success")){
                                Intent intent = new Intent(getActivity(), LoginActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                getActivity().startActivity(intent);
                                getActivity().finish();
                            }
                            else {
                                Toast.makeText(getActivity(), "Registrasi gagal", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                RegisterAPI registerAPI = new RegisterAPI(_username, _namaLengkap, _password, _address,_email, _city, _province, _telepon, responseListener);
                RequestQueue queue = Volley.newRequestQueue(getActivity());
                queue.add(registerAPI);
            }
        });
    }
    // Province dan City masih di hardcode
    private void setSpinnerProvince(){
        List<String> provinces = geoResources.getProvinces();
        ArrayAdapter adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provinces);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerProvince.setAdapter(adapter);
        spinnerProvince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ProvID = position+1;
                List<String> city = geoResources.getCities(position+1);
                final List<Integer> cityID = geoResources.getCityID(position+1);
                ArrayAdapter cityAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, city);
                cityAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                spinnerCity.setAdapter(cityAdapter);
                spinnerCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        CityID = cityID.get(position);
                        //Toast.makeText(getActivity(), cityID.get(position).toString(), Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) { }
                });
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
    }
}
