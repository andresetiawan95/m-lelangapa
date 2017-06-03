package com.lelangapa.app.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.lelangapa.app.R;
import com.lelangapa.app.activities.MainActivity;
import com.lelangapa.app.activities.RegisterActivity;
import com.lelangapa.app.apicalls.LoginAPI;
import com.lelangapa.app.preferences.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Pattern;


public class LoginFragment extends Fragment {
    private TextView txtRegister;
    private Button btnLogin;
    private EditText editText_username;
    private EditText editText_password;
    private TextInputLayout textInputLayout_username;
    private TextInputLayout textInputLayout_password;
    private String username, password;
    private Pattern regexPattern;
    public LoginFragment(){
        regexPattern = Pattern.compile("[^a-zA-Z0-9@._]");
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_login_layout, container, false);
//        ((LoginActivity)getActivity()).getSupportActionBar().setTitle("Login");
        btnLogin = (Button) view.findViewById(R.id.fragment_login_button);
        txtRegister = (TextView) view.findViewById(R.id.fragment_login_register_textview);
        editText_username = (EditText) view.findViewById(R.id.fragment_login_username);
        editText_password = (EditText) view.findViewById(R.id.fragment_login_password);
        textInputLayout_username = (TextInputLayout) view.findViewById(R.id.input_login_layout_username);
        textInputLayout_password = (TextInputLayout) view.findViewById(R.id.input_login_layout_password);
        setInputValidation();
//        ((LoginActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setHasOptionsMenu(true);
        return view;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view){
                String username = editText_username.getText().toString();
                String password = editText_password.getText().toString();
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            String res = jsonResponse.getString("result");
                            if (res.equals("1")){
                                JSONArray userData = jsonResponse.getJSONArray("data");
                                JSONObject userDataObject = userData.getJSONObject(0);
                                SessionManager sessionManager = new SessionManager(getActivity());
                                sessionManager.createSession(userDataObject.getString("id_user_return"),
                                        userDataObject.getString("username_return"),
                                        userDataObject.getString("name_user_return"),
                                        userDataObject.getString("email_user_return"),
                                        userDataObject.getString("domain_user_return"),
                                        jsonResponse.getString("token"));

                                Intent loginIntent = new Intent(getActivity(), MainActivity.class);
                                loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                getActivity().startActivity(loginIntent);
                                getActivity().finish();
                            }
                            else {
                                Toast.makeText(getActivity(), "login salah", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                if (validateInput()) {
                    LoginAPI loginAPI = new LoginAPI(username, password, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(getActivity());
                    queue.add(loginAPI);
                }
            }
        });
        txtRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view){
                Intent intent = new Intent (getActivity(), RegisterActivity.class);
                startActivity(intent);
//                getFragmentManager().beginTransaction()
//                        .replace(R.id.fragment_main_layout, new RegisterFragment())
//                        .addToBackStack(null)
//                        .commit();
            }
        });
    }
    private void setInputValidation()
    {
        editText_username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                username = editText_username.getText().toString();
                if (username.contains(" ")) {
                    textInputLayout_username.setErrorEnabled(true);
                    textInputLayout_username.setError("Tidak boleh mengandung spasi");
                }
                else if (regexPattern.matcher(username).find()) {
                    textInputLayout_username.setErrorEnabled(true);
                    textInputLayout_username.setError("Mengandung karakter tidak valid");
                }
                else {
                    textInputLayout_username.setErrorEnabled(false);
                    textInputLayout_username.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        editText_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                password = editText_password.getText().toString();
                if (password.contains(" ")) {
                    textInputLayout_password.setErrorEnabled(true);
                    textInputLayout_password.setError("Tidak boleh mengandung spasi");
                }
                else if (regexPattern.matcher(password).find()) {
                    textInputLayout_password.setErrorEnabled(true);
                    textInputLayout_password.setError("Mengandung karakter tidak valid");
                }
                else {
                    textInputLayout_password.setErrorEnabled(false);
                    textInputLayout_password.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
    private boolean validateInput()
    {
        if (!textInputLayout_username.isErrorEnabled() && !textInputLayout_password.isErrorEnabled()) {
            if (editText_username.getText().toString().equalsIgnoreCase(""))
            {
                textInputLayout_username.setErrorEnabled(true);
                textInputLayout_username.setError("Tidak boleh kosong");
                return false;
            }
            if (editText_password.getText().toString().equalsIgnoreCase(""))
            {
                textInputLayout_password.setErrorEnabled(true);
                textInputLayout_password.setError("Tidak boleh kosong");
                return false;
            }
            return true;
        }
        return false;
    }
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        Log.d("tekan", "tekan");
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                // this takes the user 'back', as if they pressed the left-facing triangle icon on the main android toolbar.
//                // if this doesn't work as desired, another possibility is to call `finish()` here.
//                getActivity().onBackPressed();
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }
}
