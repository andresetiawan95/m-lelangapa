package com.lelangapa.app.adapters;

import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lelangapa.app.R;
import com.lelangapa.app.fragments.detail.detailtawaran.BlockToggler;
import com.lelangapa.app.fragments.detail.detailtawaran.CancelToggler;
import com.lelangapa.app.fragments.detail.detailtawaran.ChooseWinnerToggler;
import com.lelangapa.app.resources.BiddingResources;

import java.util.ArrayList;

/**
 * Created by andre on 03/04/17.
 */

public class DaftarTawaranAdapter extends RecyclerView.Adapter<DaftarTawaranAdapter.ViewHolder> {
    private Context context;
    private ArrayList<BiddingResources> listOffers;
    private BlockToggler blockToggler;
    private CancelToggler cancelToggler;
    private ChooseWinnerToggler chooseWinnerToggler;

    private PopupMenu popupMenuOptions;
    private PopupMenu.OnMenuItemClickListener onMenuItemClickListener;
    public DaftarTawaranAdapter
            (Context context,
             ArrayList<BiddingResources> list,
             BlockToggler blockToggler,
             CancelToggler cancelToggler,
             ChooseWinnerToggler chooseWinnerToggler)
    {
        this.context = context;
        this.listOffers = list;
        this.blockToggler = blockToggler;
        this.cancelToggler = cancelToggler;
        this.chooseWinnerToggler = chooseWinnerToggler;
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView textView_namaBidder, textView_tawaran;
        public ImageView icon_options;
        public ViewHolder (View view)
        {
            super(view);
            textView_namaBidder = (TextView) view.findViewById(R.id.fragment_detail_barang_daftar_tawaran_noempty_name);
            textView_tawaran = (TextView) view.findViewById(R.id.fragment_detail_barang_daftar_tawaran_noempty_offer);
            icon_options = (ImageView) view.findViewById(R.id.fragment_detail_barang_daftar_tawaran_noempty_option);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_detail_barang_daftar_tawaran_layout_items, parent, false);
        return new DaftarTawaranAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final BiddingResources detailOffer = listOffers.get(position);
        holder.textView_namaBidder.setText(detailOffer.getNamaBidder());
        holder.textView_tawaran.setText(detailOffer.getHargaBid());
        holder.icon_options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupMenuOptions = new PopupMenu(context, v);
                popupMenuOptions.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.daftar_tawaran_block_user:
                                blockToggler.setBidderIDToBlock(detailOffer.getIdBidder());
                                blockToggler.showAlertDialog();
                                return true;
                            case R.id.daftar_tawaran_cancel_offer:
                                cancelToggler.setBidIDAndBidderID(detailOffer.getIdBid(), detailOffer.getIdBidder());
                                cancelToggler.showAlertDialog();
                                return true;
                            case R.id.daftar_tawaran_choose_winner:
                                chooseWinnerToggler.setBidID(detailOffer.getIdBid());
                                chooseWinnerToggler.showAlertDialog();
                                return true;
                            default:
                                return false;
                        }
                    }
                });
                popupMenuOptions.inflate(R.menu.daftar_tawaran_popup_menu);
                popupMenuOptions.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return listOffers.size();
    }

    public void updateDataset(ArrayList<BiddingResources> list)
    {
        this.listOffers = list;
        notifyDataSetChanged();
    }

    /*private void initializePopUpMenu(View view)
    {
        popupMenuOptions = new PopupMenu(context, view);
        popupMenuOptions
    }
    private void setOnMenuItemClickListener()
    {
        onMenuItemClickListener = new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.daftar_tawaran_block_user:
                        return true;
                    case R.id.daftar_tawaran_cancel_offer:
                        return true;
                    case R.id.daftar_tawaran_choose_winner:
                        return true;
                }
            }
        };
    }*/
}
