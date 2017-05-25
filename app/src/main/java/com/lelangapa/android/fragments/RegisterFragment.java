package com.lelangapa.android.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.lelangapa.android.R;
import com.lelangapa.android.activities.LoginActivity;
import com.lelangapa.android.apicalls.RegisterAPI;
import com.lelangapa.android.apicalls.singleton.RequestController;
import com.lelangapa.android.interfaces.DataReceiver;
import com.lelangapa.android.resources.ErrorMessage;
import com.lelangapa.android.resources.GeoResources;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by andre on 24/10/16.
 */

public class RegisterFragment extends Fragment {
    private Spinner spinnerProvince;
    private Spinner spinnerCity;
    private GeoResources geoResources;
    private Integer ProvID;
    private Integer CityID;
    private EditText namaLengkap, username, password, domain, email, address, telepon;
    private TextInputLayout til_nama, til_username, til_password, til_domain, til_email, til_address, til_telepon;
    private TextView domain_textview;
    private Button btnRegister;
    private String CONST_LELANGAPA_URL = "https://www.lelangapa.com/";
    private String _namaLengkap, _username, _password, _domain, _email, _address, _city, _province, _telepon;
    private Pattern regexPattern, regexPatternPassword;
    private HashMap<String, String> dataRegister;

    private Response.Listener<String> responseListener;
    private DataReceiver usernameReceiver, domainReceiver;
    private static Handler loadHandler;
    private static Runnable loadRunnable;

    private static int TYPE_USERNAME = 0, TYPE_DOMAIN = 1;
    private static final long LOAD_DELAY_MS = 500;

    public RegisterFragment(){
        initializeConstant();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_register_layout, container, false);
        initializeViews(view);
        initializeResponseListener();
        initializeDataReceivers();
        geoResources = new GeoResources();
        setSpinnerProvince();
        setupAllInputValidation();
        setupViewClickListener();
//        ((MainActivity)getActivity()).getSupportActionBar().setTitle("Register");
        setHasOptionsMenu(true);
        return view;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        domain.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String domain_view = CONST_LELANGAPA_URL + domain.getText().toString().trim();
                domain_textview.setText(domain_view);
            }
        });

    }
    private void initializeConstant() {
        dataRegister = new HashMap<>();
        regexPattern = Pattern.compile("[^a-zA-Z0-9-._]");
        loadHandler = new Handler(Looper.getMainLooper());
    }
    private void initializeResponseListener() {
        responseListener = new Response.Listener<String>() {
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
    }
    private void initializeDataReceivers() {
        usernameReceiver = new DataReceiver() {
            @Override
            public void dataReceived(Object output) {
                String result = output.toString();
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getString("status").equals("exist")) {
                        til_username.setErrorEnabled(true);
                        til_username.setError(ErrorMessage.USERNAME_EXIST);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };
        domainReceiver = new DataReceiver() {
            @Override
            public void dataReceived(Object output) {
                String result = output.toString();
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getString("status").equals("exist")) {
                        til_domain.setErrorEnabled(true);
                        til_domain.setError(ErrorMessage.DOMAIN_EXIST);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };
    }
    private void initializeViews(View view) {
        spinnerProvince = (Spinner) view.findViewById(R.id.spinner_province);
        spinnerCity = (Spinner) view.findViewById(R.id.spinner_city);
        namaLengkap = (EditText) view.findViewById(R.id.fragment_register_name);
        username = (EditText) view.findViewById(R.id.fragment_register_username);
        password = (EditText) view.findViewById(R.id.fragment_register_password);
        domain = (EditText) view.findViewById(R.id.fragment_register_domain);
        email = (EditText) view.findViewById(R.id.fragment_register_email);
        address = (EditText) view.findViewById(R.id.fragment_register_address);
        telepon = (EditText) view.findViewById(R.id.fragment_register_telepon);
        domain_textview = (TextView) view.findViewById(R.id.fragment_register_domain_textview);
        btnRegister = (Button) view.findViewById(R.id.fragment_register_button);

        til_nama = (TextInputLayout) view.findViewById(R.id.input_register_layout_name);
        til_username = (TextInputLayout) view.findViewById(R.id.input_register_layout_username);
        til_password = (TextInputLayout) view.findViewById(R.id.input_register_layout_password);
        til_domain = (TextInputLayout) view.findViewById(R.id.input_register_layout_domain);
        til_email = (TextInputLayout) view.findViewById(R.id.input_register_layout_email);
        til_address = (TextInputLayout) view.findViewById(R.id.input_register_layout_address);
        til_telepon = (TextInputLayout) view.findViewById(R.id.input_register_layout_telepon);
    }
    private void setupViewClickListener() {
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                setupInputDataRegister();
                if (validateInput() && validateEmail()) {
                    buildDataRegister();
                    RegisterAPI.Register registerAPI = RegisterAPI.instanceRegister(dataRegister, responseListener);
                    RequestController.getInstance(getActivity()).addToRequestQueue(registerAPI);
                }
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
    private void setupRegularInputValidation(final EditText editText, final TextInputLayout textInputLayout) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s.toString())) {
                    textInputLayout.setErrorEnabled(true);
                    textInputLayout.setError(ErrorMessage.EMPTY_ERROR_MESSAGE);
                }
                else {
                    textInputLayout.setErrorEnabled(false);
                    textInputLayout.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
    private void setupEmailValidation(final EditText editText, final TextInputLayout textInputLayout) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String tempEmail = s.toString();
                if (TextUtils.isEmpty(tempEmail) || !Patterns.EMAIL_ADDRESS.matcher(tempEmail).matches()) {
                    textInputLayout.setErrorEnabled(true);
                    textInputLayout.setError("Email tidak valid.");
                }
                else {
                    textInputLayout.setErrorEnabled(false);
                    textInputLayout.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
    private void setupPasswordInputValidation(final EditText editText, final TextInputLayout textInputLayout) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String input = s.toString();
                if (input.contains(" ")) {
                    textInputLayout.setErrorEnabled(true);
                    textInputLayout.setError("Tidak boleh mengandung spasi.");
                }
                else if (regexPattern.matcher(input).find()) {
                    textInputLayout.setErrorEnabled(true);
                    textInputLayout.setError("Mengandung karakter tidak valid.");
                }
                else {
                    textInputLayout.setErrorEnabled(false);
                    textInputLayout.setError(null);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
    private void setupInputValidationForUniqueEntry(final EditText editText, final TextInputLayout textInputLayout, final int type) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                final String input = s.toString();
                if (input.contains(" ")) {
                    textInputLayout.setErrorEnabled(true);
                    textInputLayout.setError("Tidak boleh mengandung spasi.");
                }
                else if (regexPattern.matcher(input).find()) {
                    textInputLayout.setErrorEnabled(true);
                    textInputLayout.setError("Mengandung karakter tidak valid.");
                }
                else {
                    textInputLayout.setErrorEnabled(false);
                    textInputLayout.setError(null);
                }

                //handler 1 second to call API
                if (!textInputLayout.isErrorEnabled()) {
                    //jika ga ada error, load API
                    loadHandler.removeCallbacks(loadRunnable);
                    loadRunnable = new Runnable() {
                        @Override
                        public void run() {
                            checkInputAvailability(type, input);
                        }
                    };
                    loadHandler.postDelayed(loadRunnable, LOAD_DELAY_MS);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
    private void setupAllInputValidation() {
        setupRegularInputValidation(namaLengkap, til_nama);
        setupInputValidationForUniqueEntry(username, til_username, TYPE_USERNAME);
        setupPasswordInputValidation(password, til_password);
        setupInputValidationForUniqueEntry(domain, til_domain, TYPE_DOMAIN);
        setupEmailValidation(email, til_email);
        setupRegularInputValidation(address, til_address);
        setupRegularInputValidation(telepon, til_telepon);
    }
    private boolean validateInput() {
        if (!(til_nama.isErrorEnabled() && til_username.isErrorEnabled() && til_password.isErrorEnabled() && til_domain.isErrorEnabled()
        && til_email.isErrorEnabled() && til_address.isErrorEnabled() && til_telepon.isErrorEnabled())) {
            if (_namaLengkap.trim().equalsIgnoreCase("")) {
                til_nama.setErrorEnabled(true);
                til_nama.setError(ErrorMessage.EMPTY_ERROR_MESSAGE);
                return false;
            }
            if (_username.trim().equalsIgnoreCase("")){
                til_username.setErrorEnabled(true);
                til_username.setError(ErrorMessage.EMPTY_ERROR_MESSAGE);
                return false;
            }
            if (_password.trim().equalsIgnoreCase("")) {
                til_password.setErrorEnabled(true);
                til_password.setError(ErrorMessage.EMPTY_ERROR_MESSAGE);
                return false;
            }
            if (_domain.trim().equalsIgnoreCase("")) {
                til_domain.setErrorEnabled(true);
                til_domain.setError(ErrorMessage.EMPTY_ERROR_MESSAGE);
                return false;
            }
            if (_email.trim().equalsIgnoreCase("")) {
                til_email.setErrorEnabled(true);
                til_email.setError(ErrorMessage.EMPTY_ERROR_MESSAGE);
                return false;
            }
            if (_address.trim().equalsIgnoreCase("")) {
                til_address.setErrorEnabled(true);
                til_address.setError(ErrorMessage.EMPTY_ERROR_MESSAGE);
                return false;
            }
            if (_telepon.trim().equalsIgnoreCase("")) {
                til_telepon.setErrorEnabled(true);
                til_telepon.setError(ErrorMessage.EMPTY_ERROR_MESSAGE);
                return false;
            }
            return true;
        }
        else {
            return false;
        }
    }
    private boolean validateEmail() {
        if (!til_email.isErrorEnabled()) {
            if (TextUtils.isEmpty(_email) || !Patterns.EMAIL_ADDRESS.matcher(_email).matches()) {
                til_email.setErrorEnabled(true);
                til_email.setError("Alamat email tidak valid.");
                return false;
            }
            return true;
        }
        return false;
    }
    private void setupInputDataRegister() {
        _namaLengkap = namaLengkap.getText().toString();
        _username = username.getText().toString();
        _password = password.getText().toString();
        _domain = domain.getText().toString();
        _email = email.getText().toString();
        _address = address.getText().toString();
        _city = CityID.toString();
        _province = ProvID.toString();
        _telepon = telepon.getText().toString();
    }
    private void buildDataRegister() {
        dataRegister.put("name", _namaLengkap);
        dataRegister.put("email", _email);
        dataRegister.put("password", _password);
        dataRegister.put("address", _address);
        dataRegister.put("phone", _telepon);
        dataRegister.put("id_city", _city);
        dataRegister.put("id_province", _province);
        dataRegister.put("username", _username);
        dataRegister.put("domain", _domain);
    }
    private void checkInputAvailability(int type, String input) {
        if (type==TYPE_USERNAME) checkUsername(input);
        else if (type==TYPE_DOMAIN) checkDomain(input);
    }
    private void checkUsername(String input) {
        RegisterAPI.CheckUsername checkUsername = RegisterAPI.instanceCheckUsername(input, usernameReceiver);
        RequestController.getInstance(getActivity()).addToRequestQueue(checkUsername);
    }
    private void checkDomain(String input) {
        RegisterAPI.CheckDomain checkDomain = RegisterAPI.instanceCheckDomain(input, domainReceiver);
        RequestController.getInstance(getActivity()).addToRequestQueue(checkDomain);
    }
}
