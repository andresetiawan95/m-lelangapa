package com.lelangapa.android.fragments.userpublic.feedback;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lelangapa.android.R;
import com.lelangapa.android.adapters.UserPublicFeedbackAdapter;
import com.lelangapa.android.resources.FeedbackResources;

import java.util.ArrayList;

/**
 * Created by andre on 31/03/17.
 */

public class NoEmptyFragment extends Fragment {
    private ArrayList<FeedbackResources> listFeedback;

    private RecyclerView recyclerView;
    private UserPublicFeedbackAdapter userPublicFeedbackAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_detail_user_public_feedback_noempty_layout, container, false);
        initializeViews(view);
        initializeAdapter();
        setRecyclerViewProperties();
        return view;
    }

    private void initializeViews(View view)
    {
        recyclerView = (RecyclerView) view.findViewById(R.id.fragment_detail_user_public_feedback_layout_recyclerview);
    }
    private void initializeAdapter()
    {
        userPublicFeedbackAdapter = new UserPublicFeedbackAdapter(getActivity(), listFeedback);
    }
    private void setRecyclerViewProperties()
    {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(userPublicFeedbackAdapter);
    }
    public void setListFeedback(ArrayList<FeedbackResources> list)
    {
        this.listFeedback = list;
    }
}
