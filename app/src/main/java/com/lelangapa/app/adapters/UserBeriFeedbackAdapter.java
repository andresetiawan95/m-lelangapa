package com.lelangapa.app.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lelangapa.app.R;
import com.lelangapa.app.resources.FeedbackResources;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by andre on 14/03/17.
 */

public class UserBeriFeedbackAdapter extends RecyclerView.Adapter<UserBeriFeedbackAdapter.FeedbackViewHolder> {
    private Context context;
    private ArrayList<FeedbackResources> listFeedback;
    public UserBeriFeedbackAdapter(Context context, ArrayList<FeedbackResources> list)
    {
        this.context = context;
        this.listFeedback = list;
    }

    public class FeedbackViewHolder extends RecyclerView.ViewHolder {
        public TextView textView_statusUser, textView_namaUser, textView_kaliBid,
                textView_namaBarang, textView_statusReview;
        public ImageView imageView_avatar;
        public FeedbackViewHolder(View view)
        {
            super(view);
            imageView_avatar = (ImageView) view.findViewById(R.id.fragment_berifeedback_noempty_layout_avatar);
            textView_statusUser = (TextView) view.findViewById(R.id.fragment_berifeedback_noempty_layout_status_user);
            textView_namaUser = (TextView) view.findViewById(R.id.fragment_berifeedback_noempty_layout_nama_user);
            textView_kaliBid = (TextView) view.findViewById(R.id.fragment_berifeedback_noempty_layout_status_bidkali);
            textView_namaBarang = (TextView) view.findViewById(R.id.fragment_berifeedback_noempty_layout_nama_barang);
            textView_statusReview = (TextView) view.findViewById(R.id.fragment_berifeedback_noempty_layout_status_review);
        }
    }

    @Override
    public FeedbackViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_user_berifeedback_noempty_layout_items, parent, false);
        return new UserBeriFeedbackAdapter.FeedbackViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(FeedbackViewHolder holder, int position) {
        FeedbackResources feedbackResources = listFeedback.get(position);
        if (feedbackResources.getStatusUser().equals("winner")) {
            holder.textView_statusUser.setText("Winner");
            holder.textView_statusUser.setBackgroundResource(R.color.feedbackStatusPemenang);
        }
        else {
            holder.textView_statusUser.setText("Auctioneer");
            holder.textView_statusUser.setBackgroundResource(R.color.feedbackStatusPelelang);
        }
        holder.textView_namaUser.setText(feedbackResources.getNamaUser());
        holder.textView_namaBarang.setText(feedbackResources.getNamaItem());
        holder.textView_kaliBid.setText(Integer.toString(feedbackResources.getBidTime()));
        if (feedbackResources.isStatusRating()) {
            holder.textView_statusReview.setText("SUDAH DIULAS");
            //holder.textView_statusReview.setBackgroundResource(R.color.feedbackStatusReviewDone);
            holder.textView_statusReview.setTextColor(ContextCompat.getColor(context, R.color.feedbackStatusReviewDone));
        }
        else {
            holder.textView_statusReview.setText("BELUM DIULAS");
            holder.textView_statusReview.setTextColor(ContextCompat.getColor(context, R.color.feedbackStatusReviewUndone));
        }
        if (!feedbackResources.getAvatarURLUser().equals("null"))
            Picasso.with(context).load("http://img-s7.lelangapa.com/" + feedbackResources.getAvatarURLUser())
                    .into(holder.imageView_avatar);
        else holder.imageView_avatar.setImageResource(R.drawable.ic_noavatar);
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
