package com.lelangapa.android.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lelangapa.android.R;
import com.lelangapa.android.fragments.gerai.toggler.DeleteToggler;
import com.lelangapa.android.interfaces.OnItemClickListener;
import com.lelangapa.android.resources.PriceFormatter;
import com.lelangapa.android.resources.UserGeraiResources;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by andre on 06/12/16.
 */

public class UserGeraiBarangAdapter extends RecyclerView.Adapter<UserGeraiBarangAdapter.MyViewHolder> {
    private ArrayList<UserGeraiResources> dataBarang;
    private Context context;
    private OnItemClickListener onItemClickListener;
    private DeleteToggler deleteToggler;
    private PopupMenu popupMenuOptions;
    public UserGeraiBarangAdapter (Context context, ArrayList<UserGeraiResources> dataBarang,
                                   OnItemClickListener onItemClickListener, DeleteToggler deleteToggler){
        this.dataBarang = dataBarang;
        this.context = context;
        this.onItemClickListener = onItemClickListener;
        this.deleteToggler = deleteToggler;
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView namabarang, user, harga;
        private ImageView gambarbarang;
        private CardView cardview;
        private ImageView icon_option;
        private MyViewHolder(View view){
            super(view);
            cardview = (CardView) view.findViewById(R.id.fragment_user_gerai_layout_cardview);
            gambarbarang = (ImageView) view.findViewById(R.id.fragment_user_gerai_layout_imgview);
            namabarang = (TextView) view.findViewById(R.id.fragment_user_gerai_layout_namabarang);
            harga = (TextView) view.findViewById(R.id.fragment_user_gerai_layout_harga);
            icon_option = (ImageView) view.findViewById(R.id.fragment_user_gerai_layout_option);
        }
    }
    @Override
    public MyViewHolder onCreateViewHolder (ViewGroup parent, int viewType){
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_user_gerai_layout_notempty_carditems, parent, false);
        return new UserGeraiBarangAdapter.MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(final MyViewHolder viewHolder, int position){
        final UserGeraiResources resBarang = dataBarang.get(position);
        if (resBarang.getUrlgambarbarang()!=null) {
            Picasso.with(context).load(resBarang.getUrlgambarbarang()).into(viewHolder.gambarbarang);
        }
        else {
            viewHolder.gambarbarang.setImageResource(R.drawable.ic_insert_photo_grey_128dp);
        }
        viewHolder.namabarang.setText(resBarang.getNamabarang());
        viewHolder.harga.setText(PriceFormatter.formatPrice(resBarang.getHargaawal()));
        viewHolder.cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(v, viewHolder.getAdapterPosition());
            }
        });
        viewHolder.icon_option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupMenuOptions = new PopupMenu(context, v);
                popupMenuOptions.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.gerai_item_delete :
                                deleteToggler.setItemIDToBeDeleted(resBarang.getIdbarang());
                                deleteToggler.showAlertDialog();
                                return true;
                            default:
                                return false;
                        }
                    }
                });
                popupMenuOptions.inflate(R.menu.gerai_item_popup_menu);
                popupMenuOptions.show();
            }
        });
    }
    @Override
    public int getItemCount(){
        return dataBarang.size();
    }
}
