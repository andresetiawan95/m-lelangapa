package com.lelangapa.android.fragments.profile;

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
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.lelangapa.android.R;
import com.lelangapa.android.apicalls.EditPasswordAPI;
import com.lelangapa.android.interfaces.DataReceiver;
import com.lelangapa.android.preferences.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by andre on 22/11/16.
 */

public class EditPasswordFragment extends Fragment {
    private EditText editText_editPassword_oldPassword;
    private EditText editText_editPassword_newPassword;
    private EditText editText_editPassword_confirmNewPassword;
    private TextInputLayout textInputLayout_oldpassword, textInputLayout_newpassword, textInputLayout_confirmpassword;
    private Button button_editPassword_simpan;
    private RequestQueue queue;
    private SessionManager sessionManager;
    private DataReceiver uploadUserPasswordData;
    private HashMap<String, String> userPasswordData = new HashMap<>();
    private HashMap<String, String> session;
    private boolean checker=true;
    public EditPasswordFragment(){};
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_userprofile_editpassword_layout, container, false);
        editText_editPassword_oldPassword = (EditText) view.findViewById(R.id.fragment_userprofile_editpassword_oldpassword);
        editText_editPassword_newPassword = (EditText) view.findViewById(R.id.fragment_userprofile_editpassword_newpassword);
        editText_editPassword_confirmNewPassword = (EditText) view.findViewById(R.id.fragment_userprofile_editpassword_confirmnewpassword);
        textInputLayout_oldpassword = (TextInputLayout) view.findViewById(R.id.fragment_userprofile_editpassword_textinputlayoutoldpassword);
        textInputLayout_newpassword = (TextInputLayout) view.findViewById(R.id.fragment_userprofile_editpassword_textinputlayoutnewpassword);
        textInputLayout_confirmpassword = (TextInputLayout) view.findViewById(R.id.fragment_userprofile_editpassword_textinputlayoutconfirmpassword);
        button_editPassword_simpan = (Button) view.findViewById(R.id.fragment_userprofile_editpassword_simpan);
        sessionManager = new SessionManager(getActivity());
        session = sessionManager.getSession();
        return view;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        queue = Volley.newRequestQueue(getActivity());
        button_editPassword_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkErrorEnabled()){
                    updateUserPasswordAPI();
                }
            }
        });
        checkInputValidation();
    }
    private boolean checkErrorEnabled(){
        if (!textInputLayout_oldpassword.isErrorEnabled() && !textInputLayout_newpassword.isErrorEnabled() && !textInputLayout_confirmpassword.isErrorEnabled()){
            return true;
        }
        return false;
    }
    private void updateUserPasswordAPI(){
        putUserPasswordData();
        uploadUserPasswordData = new DataReceiver() {
            @Override
            public void dataReceived(Object output) {
                String response = output.toString();
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    String result = jsonResponse.getString("status");
                    if (result.equals("success")){
                        Toast.makeText(getActivity(), "Pergantian password telah dilakukan.", Toast.LENGTH_SHORT).show();
                        /*Intent intent = new Intent(getActivity(), LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        getActivity().startActivity(intent);*/
                        getActivity().finish();
                    }
                    else if (result.equals("error-2")){
                        textInputLayout_oldpassword.setErrorEnabled(true);
                        textInputLayout_oldpassword.setError("Password salah");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        EditPasswordAPI editPasswordAPI = new EditPasswordAPI(userPasswordData, uploadUserPasswordData);
        queue.add(editPasswordAPI);
    }
    private void putUserPasswordData(){
        userPasswordData.put("userid", session.get(sessionManager.getKEY_ID()));
        userPasswordData.put("oldpassword", editText_editPassword_oldPassword.getText().toString());
        userPasswordData.put("newpassword", editText_editPassword_newPassword.getText().toString());
    }

    private void checkInputValidation(){
        editText_editPassword_oldPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(editText_editPassword_oldPassword.getText().toString().trim().equalsIgnoreCase("")){
                    textInputLayout_oldpassword.setErrorEnabled(true);
                    textInputLayout_oldpassword.setError("Field ini tidak boleh kosong");
                }
                else {
                    textInputLayout_oldpassword.setErrorEnabled(false);
                    textInputLayout_oldpassword.setError(null);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
        editText_editPassword_newPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(editText_editPassword_newPassword.getText().toString().trim().equalsIgnoreCase("")){
                    textInputLayout_newpassword.setErrorEnabled(true);
                    textInputLayout_newpassword.setError("Field ini tidak boleh kosong");
                }
                else {
                    textInputLayout_newpassword.setErrorEnabled(false);
                    textInputLayout_newpassword.setError(null);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
        editText_editPassword_confirmNewPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (editText_editPassword_confirmNewPassword.getText().toString().trim().equalsIgnoreCase("")){
                    textInputLayout_confirmpassword.setErrorEnabled(true);
                    textInputLayout_confirmpassword.setError("Field ini tidak boleh kosong");
                }
                else {
                    textInputLayout_confirmpassword.setErrorEnabled(false);
                    textInputLayout_confirmpassword.setError(null);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
                if (!(editText_editPassword_confirmNewPassword.getText().toString().trim().equals(editText_editPassword_newPassword.getText().toString().trim()))
                        && !editText_editPassword_confirmNewPassword.getText().toString().trim().equalsIgnoreCase("")){
                    textInputLayout_confirmpassword.setErrorEnabled(true);
                    textInputLayout_confirmpassword.setError("Password tidak sama.");
                }
            }
        });
    }
}
