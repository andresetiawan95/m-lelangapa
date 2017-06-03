package com.lelangapa.app.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.lelangapa.app.R;
import com.lelangapa.app.interfaces.ImagePicker;
import com.lelangapa.app.resources.ItemImageResources;

import java.util.ArrayList;

/**
 * Created by andre on 04/04/17.
 */

public class MultipleImageNewItemAdapter extends RecyclerView.Adapter<MultipleImageNewItemAdapter.ViewHolder> {
    private Context context;
    private ArrayList<ItemImageResources> listImages;
    private ImagePicker imagePicker;

    public MultipleImageNewItemAdapter(Context context, ArrayList<ItemImageResources> list, ImagePicker imagePicker)
    {
        this.context = context;
        this.listImages = list;
        this.imagePicker = imagePicker;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_user_lelang_barang_layout_image_items, parent, false);
        return new MultipleImageNewItemAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        ItemImageResources imageResources = listImages.get(position);
        if (imageResources.getBitmap() != null) {
            holder.imageView_image.setImageBitmap(imageResources.getBitmap());
        }
        else {
            holder.imageView_image.setImageResource(R.drawable.ic_add_box_grey_48dp);
        }
        if (imageResources.isMainImage()) {
            holder.view_isMainImage.setEnabled(true);
            holder.view_isMainImage.setVisibility(View.VISIBLE);
        }
        else {
            holder.view_isMainImage.setEnabled(false);
            holder.view_isMainImage.setVisibility(View.GONE);
        }
        holder.imageView_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagePicker.picked(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return listImages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public ImageView imageView_image;
        public View view_isMainImage;
        public ViewHolder(View view)
        {
            super(view);
            imageView_image = (ImageView) view.findViewById(R.id.fragment_user_lelang_barang_layout_image_items_gambar);
            view_isMainImage = view.findViewById(R.id.fragment_user_lelang_barang_layout_image_items_main_image);
        }
    }

    public void updateImageSet()
    {
        notifyDataSetChanged();
    }
}
