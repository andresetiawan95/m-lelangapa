package com.lelangapa.android.fragments.search;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lelangapa.android.R;

/**
 * Created by andre on 17/12/16.
 */

public class MainSearchTextChangeFragment extends Fragment {
    private TextView textChangeTextView;
    private String textInit;
    public MainSearchTextChangeFragment(){}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_main_search_textchange_layout, container, false);
        textChangeTextView = (TextView) view.findViewById(R.id.fragment_main_search_textchange_query);
        textChangeTextView.setText(textInit);
        return view;
    }
    public void setTextInit(String text){
        textInit = text;
    }
    public void changeText(String newText){
        textChangeTextView.setText(newText);
    }
}
