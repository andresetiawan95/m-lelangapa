package com.lelangapa.android.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lelangapa.android.R;
import com.lelangapa.android.resources.FavoriteResources;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by andre on 27/02/17.
 */

public class UserFavoriteAdapter extends RecyclerView.Adapter<UserFavoriteAdapter.FavoriteViewHolder> {
    private ArrayList<FavoriteResources> listFavorite;
    private Context context;
    public UserFavoriteAdapter (Context context, ArrayList<FavoriteResources> listFavorite)
    {
        this.listFavorite = listFavorite;
        this.context = context;
    }
    public class FavoriteViewHolder extends RecyclerView.ViewHolder {
        public TextView textView_judulItem, textView_user, textView_harga;
        public ImageView imageView_imageItem, imageView_trash;
        public CardView cardView;
        public FavoriteViewHolder (View view){
            super(view);
            cardView = (CardView) view.findViewById(R.id.fragment_user_favorite_layout_cardview);
            imageView_imageItem = (ImageView) view.findViewById(R.id.fragment_user_favorite_layout_imgview);
            textView_judulItem = (TextView) view.findViewById(R.id.fragment_user_favorite_layout_namabarang);
            textView_user = (TextView) view.findViewById(R.id.fragment_user_favorite_layout_user);
            textView_harga = (TextView) view.findViewById(R.id.fragment_user_favorite_layout_harga);
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
    public void onBindViewHolder(FavoriteViewHolder viewHolder, int position)
    {
        final FavoriteResources itemFavorite = listFavorite.get(position);
        if (itemFavorite.getImageURLItem() != null) {
            Picasso.with(context).load(itemFavorite.getImageURLItem()).into(viewHolder.imageView_imageItem);
        }
        viewHolder.textView_judulItem.setText(itemFavorite.getNamaItemFavorite());
        viewHolder.textView_user.setText(itemFavorite.getNamaUserAuctioneerItemFavorite());
        viewHolder.imageView_trash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, itemFavorite.getNamaItemFavorite(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return listFavorite.size();
    }
}
