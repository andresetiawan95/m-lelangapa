package com.lelangapa.android.fragments.feedback.berifeedback.detail;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

import com.lelangapa.android.R;
import com.lelangapa.android.resources.FeedbackResources;

/**
 * Created by andre on 16/03/17.
 */

public class GiveFeedbackFragment extends Fragment {
    private FeedbackResources feedbackResources;
    private RatingBar ratingBar_rateStar;
    private EditText editText_rateMessage;
    private Button button_simpan;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_user_berifeedback_detail_givefeedback_layout, container, false);
        initializeViews(view);
        setupViews();
        return view;
    }
    private void initializeViews(View view)
    {
        ratingBar_rateStar = (RatingBar) view.findViewById(R.id.fragment_user_berifeedback_givefeedback_rating);
        editText_rateMessage = (EditText) view.findViewById(R.id.fragment_user_berifeedback_givefeedback_ulasan);
        button_simpan = (Button) view.findViewById(R.id.fragment_user_berifeedback_givefeedback_simpan);
    }
    private void setupViews()
    {
        if (feedbackResources != null)
        {
            ratingBar_rateStar.setRating((float) feedbackResources.getRateGiven());
            if (feedbackResources.getRateMessage() != null)
            {
                editText_rateMessage.setText(feedbackResources.getRateMessage());
            }
        }
    }
    public void setGivenFeedback(FeedbackResources feedback)
    {
        this.feedbackResources = feedback;
    }
}
