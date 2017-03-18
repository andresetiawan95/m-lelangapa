package com.lelangapa.android.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.lelangapa.android.R;
import com.lelangapa.android.resources.FeedbackResources;

import java.util.ArrayList;

/**
 * Created by andre on 18/03/17.
 */

public class UserFeedbackAndaAdapter extends RecyclerView.Adapter<UserFeedbackAndaAdapter.FeedbackViewHolder> {
    private Context context;
    private ArrayList<FeedbackResources> listFeedback;

    public UserFeedbackAndaAdapter(Context context, ArrayList<FeedbackResources> list)
    {
        this.context = context;
        this.listFeedback = list;
    }

    public class FeedbackViewHolder extends RecyclerView.ViewHolder {
        public TextView textView_statusUser, textView_namaUser, textView_namaItem, textView_bidkali;
        public RatingBar ratingBar_rateGiven;
        public ImageView imageView_avatar;
        public FeedbackViewHolder(View view)
        {
            super(view);
            imageView_avatar = (ImageView) view.findViewById(R.id.fragment_feedbackanda_noempty_layout_avatar);
            textView_statusUser = (TextView) view.findViewById(R.id.fragment_feedbackanda_noempty_layout_status_user);
            textView_namaUser = (TextView) view.findViewById(R.id.fragment_feedbackanda_noempty_layout_nama_user);
            textView_namaItem = (TextView) view.findViewById(R.id.fragment_feedbackanda_noempty_layout_nama_barang);
            textView_bidkali = (TextView) view.findViewById(R.id.fragment_feedbackanda_noempty_layout_status_bidkali);
            ratingBar_rateGiven = (RatingBar) view.findViewById(R.id.fragment_feedbackanda_noempty_layout_rate_given);
        }
    }

    @Override
    public FeedbackViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_user_feedbackanda_noempty_layout_items, parent, false);
        return new UserFeedbackAndaAdapter.FeedbackViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(FeedbackViewHolder holder, int position) {
        FeedbackResources feedbackResources = listFeedback.get(position);
        String bidTime = Integer.toString(feedbackResources.getBidTime());
        if (feedbackResources.getStatusUser().equals("winner")) {
            holder.textView_statusUser.setText("Winner");
            holder.textView_statusUser.setBackgroundResource(R.color.feedbackStatusPemenang);
        }
        else {
            holder.textView_statusUser.setText("Auctioneer");
            holder.textView_statusUser.setBackgroundResource(R.color.feedbackStatusPelelang);
        }
        holder.textView_namaUser.setText(feedbackResources.getNamaUser());
        holder.textView_namaItem.setText(feedbackResources.getNamaItem());
        holder.textView_bidkali.setText(bidTime);
        holder.ratingBar_rateGiven.setRating((float) feedbackResources.getRateGiven());
    }

    @Override
    public int getItemCount() {
        return listFeedback.size();
    }

    public void updateDataset(ArrayList<FeedbackResources> list)
    {
        this.listFeedback = list;
        notifyDataSetChanged();
    }
}
