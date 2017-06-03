package com.lelangapa.app.fragments.feedback.berifeedback.detail;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lelangapa.app.R;

/**
 * Created by andre on 17/03/17.
 */

public class LoadingFeedbackFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_user_berifeedback_detail_loadingfeedback_layout, container, false);
        return view;
    }
}
