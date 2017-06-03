package com.lelangapa.app.fragments.feedback.berifeedback.detail;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

import com.lelangapa.app.R;
import com.lelangapa.app.activities.feedback.berifeedback.BeriFeedbackActivity;
import com.lelangapa.app.apicalls.feedback.berifeedback.BeriFeedbackAPI;
import com.lelangapa.app.apicalls.singleton.RequestController;
import com.lelangapa.app.interfaces.DataReceiver;
import com.lelangapa.app.resources.FeedbackResources;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by andre on 16/03/17.
 */

public class GiveFeedbackFragment extends Fragment {
    private FeedbackResources feedbackResources;
    private RatingBar ratingBar_rateStar;
    private EditText editText_rateMessage;
    private TextInputLayout textInputLayout_rateMessage;
    private Button button_simpan;

    private AlertDialog feedbackAlertDialog;
    private ProgressDialog feedbackProgressDialog;
    private DataReceiver dataFeedbackReceiver;
    private View.OnClickListener onClickListener;

    private String statusUser;
    private HashMap<String, String> feedback;

    public GiveFeedbackFragment()
    {
        initializeConstants();
    }
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setStatusUser();
        initializeDataReceiverWhenFeedbackIsSent();
        initializeOnClickListener();
        setFeedbackAlertDialogBuilder();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_user_berifeedback_detail_givefeedback_layout, container, false);
        initializeViews(view);
        setupViews();
        setInputValidationOnTextChanged();
        return view;
    }
    private void initializeConstants()
    {
        feedback = new HashMap<>();
    }
    private void initializeViews(View view)
    {
        ratingBar_rateStar = (RatingBar) view.findViewById(R.id.fragment_user_berifeedback_givefeedback_rating);
        editText_rateMessage = (EditText) view.findViewById(R.id.fragment_user_berifeedback_givefeedback_ulasan);
        textInputLayout_rateMessage = (TextInputLayout) view.findViewById(R.id.fragment_user_berifeedback_givefeedback_ulasan_text_layout);
        button_simpan = (Button) view.findViewById(R.id.fragment_user_berifeedback_givefeedback_simpan);
    }
    private void initializeDataReceiverWhenFeedbackIsSent()
    {
        dataFeedbackReceiver = new DataReceiver() {
            @Override
            public void dataReceived(Object output) {
                String response = output.toString();
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    if (jsonResponse.getString("status").equals("success")) {
                        feedbackProgressDialog.dismiss();
                        Intent intent = new Intent(getActivity(), BeriFeedbackActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        getActivity().finish();
                        startActivity(intent);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
    }
    private void initializeOnClickListener()
    {
        onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputValidationWhenButtonClicked();
                if (checkInputValidation())
                {
                    setFeedbackData();
                    showFeedbackAlertDialog();
                }
            }
        };
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
        button_simpan.setOnClickListener(onClickListener);
    }
    public void setGivenFeedback(FeedbackResources feedback)
    {
        this.feedbackResources = feedback;
    }
    public void setStatusUser()
    {
        statusUser = getActivity().getIntent().getExtras().getString("status_user");
    }
    private void setFeedbackData()
    {
        String rateMessage = editText_rateMessage.getText().toString().trim();
        String rateGiven = String.valueOf((int) ratingBar_rateStar.getRating());

        Bundle bundleExtras = getActivity().getIntent().getExtras();
        String bidTime = String.valueOf(bundleExtras.getInt("item_bid_time"));
        if (statusUser.equals("auctioneer")) {
            feedback.put("id_auctioneer", bundleExtras.getString("user_id"));
            feedback.put("id_rater", bundleExtras.getString("rater_id"));
            feedback.put("id_item", bundleExtras.getString("item_id"));
            feedback.put("item_bidtime", bidTime);
            feedback.put("rate_given", rateGiven);
            feedback.put("rate_message", rateMessage);
        }
        else {
            feedback.put("id_bidder", bundleExtras.getString("user_id"));
            feedback.put("id_rater", bundleExtras.getString("rater_id"));
            feedback.put("id_item", bundleExtras.getString("item_id"));
            feedback.put("item_bidtime", bidTime);
            feedback.put("rate_given", rateGiven);
            feedback.put("rate_message", rateMessage);
        }
    }
    private void sendFeedbackToServer()
    {
        if (feedbackResources != null) {
            //update feedback
            if (statusUser.equals("auctioneer")) {
                BeriFeedbackAPI.UpdateFeedbackForAuctioneer update =
                        BeriFeedbackAPI.instanceUpdateFeedbackForAuctioneer(feedback, dataFeedbackReceiver);
                RequestController.getInstance(getActivity()).addToRequestQueue(update);
            }
            else {
                BeriFeedbackAPI.UpdateFeedbackForWinner update =
                        BeriFeedbackAPI.instanceUpdateFeedbackForWinner(feedback, dataFeedbackReceiver);
                RequestController.getInstance(getActivity()).addToRequestQueue(update);
            }
        }
        else {
            //insert feedback
            if (statusUser.equals("auctioneer")) {
                BeriFeedbackAPI.InsertFeedbackForAuctioneer update =
                        BeriFeedbackAPI.instanceInsertFeedbackForAuctioneer(feedback, dataFeedbackReceiver);
                RequestController.getInstance(getActivity()).addToRequestQueue(update);
            }
            else {
                BeriFeedbackAPI.InsertFeedbackForWinner update =
                        BeriFeedbackAPI.instanceInsertFeedbackForWinner(feedback, dataFeedbackReceiver);
                RequestController.getInstance(getActivity()).addToRequestQueue(update);
            }
        }
    }

    private void setFeedbackAlertDialogBuilder()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder
                .setTitle(R.string.BERIFEEDBACK_ALERTDIALOGTITLE)
                .setMessage(R.string.BERIFEEDBACK_ALERTDIALOGMSG)
                .setPositiveButton(R.string.BERIFEEDBACK_ALERTDIALOGOKBUTTON, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        showProgressBar();
                        sendFeedbackToServer();
                    }
                })
                .setNegativeButton(R.string.BERIFEEDBACK_ALERTDIALOGCANCELBUTTON, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        feedbackAlertDialog = builder.create();
    }
    private void showFeedbackAlertDialog()
    {
        feedbackAlertDialog.show();
    }
    private void showProgressBar()
    {
        feedbackProgressDialog = ProgressDialog.show(getActivity(), "Menyimpan", "Harap tunggu...");
    }
    private void setInputValidationOnTextChanged()
    {
        editText_rateMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (editText_rateMessage.getText().toString().trim().equalsIgnoreCase("")){
                    textInputLayout_rateMessage.setErrorEnabled(true);
                    textInputLayout_rateMessage.setError("Ulasan tidak boleh kosong");
                }
                else
                {
                    textInputLayout_rateMessage.setErrorEnabled(false);
                    textInputLayout_rateMessage.setError(null);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
    }
    private void inputValidationWhenButtonClicked()
    {
        if (editText_rateMessage.getText().toString().trim().equalsIgnoreCase("")){
            textInputLayout_rateMessage.setErrorEnabled(true);
            textInputLayout_rateMessage.setError("Ulasan tidak boleh kosong");
        }
        else
        {
            textInputLayout_rateMessage.setErrorEnabled(false);
            textInputLayout_rateMessage.setError(null);
        }
    }
    private boolean checkInputValidation()
    {
        if (!textInputLayout_rateMessage.isErrorEnabled()) return true;
        return false;
    }
}
