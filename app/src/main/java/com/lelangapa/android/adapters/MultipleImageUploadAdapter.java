package com.lelangapa.android.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.lelangapa.android.R;
import com.lelangapa.android.interfaces.ImagePicker;
import com.lelangapa.android.resources.ImageResources;

import java.util.ArrayList;

/**
 * Created by andre on 04/04/17.
 */

public class MultipleImageUploadAdapter extends RecyclerView.Adapter<MultipleImageUploadAdapter.ViewHolder> {
    private Context context;
    private ArrayList<ImageResources> listImages;
    private ImagePicker imagePicker;

    public MultipleImageUploadAdapter(Context context, ArrayList<ImageResources> list, ImagePicker imagePicker)
    {
        this.context = context;
        this.listImages = list;
        this.imagePicker = imagePicker;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_user_lelang_barang_layout_image_items, parent, false);
        return new MultipleImageUploadAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        ImageResources imageResources = listImages.get(position);
        if (imageResources.getBitmap() != null) {
            holder.imageView_image.setImageBitmap(imageResources.getBitmap());
        }
        else {
            holder.imageView_image.setImageResource(R.drawable.ic_add_box_grey_48dp);
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
        public ViewHolder(View view)
        {
            super(view);
            imageView_image = (ImageView) view.findViewById(R.id.fragment_user_lelang_barang_layout_image_items_gambar);
        }
    }

    public void updateImageSet()
    {
        notifyDataSetChanged();
    }
}
