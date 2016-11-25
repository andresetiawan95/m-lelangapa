package com.lelangkita.android.fragments.profile;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.lelangkita.android.R;

/**
 * Created by andre on 22/11/16.
 */

public class EditPasswordFragment extends Fragment {
    private EditText editText_editPassword_oldPassword, editText_editPassword_newPassword, editText_editPassword_confirmNewPassword;

    public EditPasswordFragment(){};
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_userprofile_editpassword_layout, container, false);
        return view;
    }
}
