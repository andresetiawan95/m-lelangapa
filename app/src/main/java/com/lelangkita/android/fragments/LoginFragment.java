package com.lelangkita.android.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lelangkita.android.R;
import com.lelangkita.android.activities.RegisterActivity;


public class LoginFragment extends Fragment {
    private TextView txtRegister;
    public LoginFragment(){

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_login_layout, container, false);
//        ((LoginActivity)getActivity()).getSupportActionBar().setTitle("Login");
        txtRegister = (TextView) view.findViewById(R.id.fragment_login_register_textview);
//        ((LoginActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setHasOptionsMenu(true);
        return view;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
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
