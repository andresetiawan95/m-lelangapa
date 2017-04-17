package com.lelangapa.android.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.lelangapa.android.R;
import com.lelangapa.android.activities.MainActivity;
import com.lelangapa.android.activities.RegisterActivity;
import com.lelangapa.android.apicalls.LoginAPI;
import com.lelangapa.android.preferences.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class LoginFragment extends Fragment {
    private TextView txtRegister;
    private Button btnLogin;
    private EditText eUsername;
    private EditText ePassword;
    public LoginFragment(){

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_login_layout, container, false);
//        ((LoginActivity)getActivity()).getSupportActionBar().setTitle("Login");
        btnLogin = (Button) view.findViewById(R.id.fragment_login_button);
        txtRegister = (TextView) view.findViewById(R.id.fragment_login_register_textview);
        eUsername = (EditText) view.findViewById(R.id.fragment_login_username);
        ePassword = (EditText) view.findViewById(R.id.fragment_login_password);
//        ((LoginActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setHasOptionsMenu(true);
        return view;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view){
                String username = eUsername.getText().toString();
                String password = ePassword.getText().toString();
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
                LoginAPI loginAPI = new LoginAPI(username, password, responseListener);
                RequestQueue queue = Volley.newRequestQueue(getActivity());
                queue.add(loginAPI);
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
