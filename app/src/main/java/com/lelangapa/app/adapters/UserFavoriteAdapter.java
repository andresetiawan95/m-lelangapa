package com.lelangapa.app.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lelangapa.app.R;
import com.lelangapa.app.fragments.detail.detailitemfavorite.UnfavoriteTrashToggler;
import com.lelangapa.app.interfaces.DataReceiver;
import com.lelangapa.app.interfaces.OnItemClickListener;
import com.lelangapa.app.resources.FavoriteResources;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by andre on 27/02/17.
 */

public class UserFavoriteAdapter extends RecyclerView.Adapter<UserFavoriteAdapter.FavoriteViewHolder> {
    private UnfavoriteTrashToggler unfavoriteTrashToggler;
    private DataReceiver favoriteStatusReceived;
    private ArrayList<FavoriteResources> listFavorite;
    private String userID;
    private Context context;
    private OnItemClickListener onItemClickListener;
    public UserFavoriteAdapter
            (Context context,
            ArrayList<FavoriteResources> listFavorite,
            String userID,
            DataReceiver favoriteReceiver,
             OnItemClickListener onItemClickListener)
    {
        this.listFavorite = listFavorite;
        this.context = context;
        this.userID = userID;
        this.favoriteStatusReceived = favoriteReceiver;
        this.onItemClickListener = onItemClickListener;
        this.unfavoriteTrashToggler = new UnfavoriteTrashToggler((Activity) this.context, this.userID, this.favoriteStatusReceived);
    }
    public class FavoriteViewHolder extends RecyclerView.ViewHolder {
        public TextView textView_judulItem, textView_user;
        public ImageView imageView_imageItem, imageView_trash;
        public CardView cardView;
        public FavoriteViewHolder (View view){
            super(view);
            cardView = (CardView) view.findViewById(R.id.fragment_user_favorite_layout_cardview);
            imageView_imageItem = (ImageView) view.findViewById(R.id.fragment_user_favorite_layout_imgview);
            textView_judulItem = (TextView) view.findViewById(R.id.fragment_user_favorite_layout_namabarang);
            textView_user = (TextView) view.findViewById(R.id.fragment_user_favorite_layout_user);
            //textView_harga = (TextView) view.findViewById(R.id.fragment_user_favorite_layout_harga);
            imageView_trash = (ImageView) view.findViewById(R.id.fragment_user_favorite_layout_trash);
        }
    }

    @Override
    public FavoriteViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_user_favorite_noempty_layout_carditems, parent, false);
        return new UserFavoriteAdapter.FavoriteViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final FavoriteViewHolder viewHolder, int position)
    {
        final FavoriteResources itemFavorite = listFavorite.get(position);
        if (itemFavorite.getImageURLItem() != null) {
            Picasso.with(context).load(itemFavorite.getImageURLItem()).into(viewHolder.imageView_imageItem);
        }
        else {
            viewHolder.imageView_imageItem.setImageResource(R.drawable.ic_insert_photo_grey_128dp);
        }
        viewHolder.textView_judulItem.setText(itemFavorite.getNamaItemFavorite());
        viewHolder.textView_user.setText(itemFavorite.getNamaUserAuctioneerItemFavorite());
        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(v, viewHolder.getAdapterPosition());
            }
        });
        viewHolder.imageView_trash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context, itemFavorite.getNamaItemFavorite(), Toast.LENGTH_SHORT).show();
                unfavoriteTrashToggler.setFavoriteIDAndItemID(itemFavorite.getIdFavorite(), itemFavorite.getIdItemFavorite());
                unfavoriteTrashToggler.showAlertDialog();
            }
        });
    }

    public void updateDataSet(ArrayList<FavoriteResources> listFavorite)
    {
        this.listFavorite = listFavorite;
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount()
    {
        return listFavorite.size();
    }
}
