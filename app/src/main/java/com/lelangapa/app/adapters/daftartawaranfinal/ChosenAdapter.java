package com.lelangapa.app.adapters.daftartawaranfinal;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lelangapa.app.R;
import com.lelangapa.app.resources.BiddingResources;
import com.lelangapa.app.resources.PriceFormatter;

import java.util.ArrayList;

/**
 * Created by andre on 11/05/17.
 */

public class ChosenAdapter extends RecyclerView.Adapter<ChosenAdapter.ViewHolder> {
    private Context context;
    private ArrayList<BiddingResources> listOffer;
    private static final int TYPE_SELECTED = 1;
    private static final int TYPE_NOT_SELECTED = 0;

    public ChosenAdapter(Context context, ArrayList<BiddingResources> list) {
        this.context = context;
        this.listOffer = list;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder (View view) {
            super(view);
        }
    }
    class SelectedViewHolder extends ViewHolder {
        private TextView textView_namaBidder, textView_tawaran;
        public SelectedViewHolder(View view) {
            super(view);
            textView_namaBidder = (TextView) view.findViewById(R.id.fragment_daftar_tawaran_final_chosen_selected_name);
            textView_tawaran =(TextView) view.findViewById(R.id.fragment_daftar_tawaran_final_chosen_selected_offer);
        }
    }
    class NotSelectedViewHolder extends ViewHolder {
        private TextView textView_namaBidder, textView_tawaran;
        public NotSelectedViewHolder(View view) {
            super(view);
            textView_namaBidder = (TextView) view.findViewById(R.id.fragment_daftar_tawaran_final_chosen_notselected_name);
            textView_tawaran =(TextView) view.findViewById(R.id.fragment_daftar_tawaran_final_chosen_notselected_offer);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case TYPE_SELECTED:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.fragment_daftar_tawaran_final_items_layout_chosen_selected, parent, false);
                return new SelectedViewHolder(view);
            case TYPE_NOT_SELECTED:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.fragment_daftar_tawaran_final_items_layout_chosen_notselected, parent, false);
                return new NotSelectedViewHolder(view);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        BiddingResources resources = listOffer.get(position);
        int viewType = listOffer.get(position).isWinnerStatus() ? TYPE_SELECTED : TYPE_NOT_SELECTED;
        if (viewType == TYPE_SELECTED) {
            SelectedViewHolder selectedViewHolder = (SelectedViewHolder) holder;
            selectedViewHolder.textView_namaBidder.setText(resources.getNamaBidder());
            selectedViewHolder.textView_tawaran.setText(PriceFormatter.formatPrice(resources.getHargaBid()));
        }
        else {
            NotSelectedViewHolder notSelectedViewHolder = (NotSelectedViewHolder) holder;
            notSelectedViewHolder.textView_namaBidder.setText(resources.getNamaBidder());
            notSelectedViewHolder.textView_tawaran.setText(PriceFormatter.formatPrice(resources.getHargaBid()));
        }
    }

    @Override
    public int getItemCount() {
        return listOffer.size();
    }

    @Override
    public int getItemViewType(int position) {
        return listOffer.get(position).isWinnerStatus() ? TYPE_SELECTED : TYPE_NOT_SELECTED;
    }
}
