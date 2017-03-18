package com.lelangapa.android.fragments.feedback.feedbackanda.auctioneer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lelangapa.android.R;

/**
 * Created by andre on 18/03/17.
 */

public class AuctioneerEmptyFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_user_feedbackanda_auctioneer_empty_layout, container, false);
        return view;
    }
}
