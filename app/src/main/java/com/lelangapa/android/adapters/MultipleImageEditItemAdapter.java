package com.lelangapa.android.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.lelangapa.android.R;
import com.lelangapa.android.interfaces.ImagePicker;
import com.lelangapa.android.resources.ItemImageResources;

import java.util.ArrayList;

/**
 * Created by andre on 25/04/17.
 */

public class MultipleImageEditItemAdapter extends RecyclerView.Adapter<MultipleImageEditItemAdapter.ViewHolder>{
    private Context context;
    private ArrayList<ItemImageResources> listImages;
    private ImagePicker imagePicker;

    public MultipleImageEditItemAdapter(Context context, ArrayList<ItemImageResources> listImages, ImagePicker imagePicker)
    {
        this.context = context;
        this.listImages = listImages;
        this.imagePicker = imagePicker;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_user_edit_lelang_barang_layout_image_items, parent, false);
        return new MultipleImageEditItemAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ItemImageResources resources = listImages.get(position);
        if (resources.getImageURL() != null && resources.getBitmap() == null) {
            whenURLExistAndBitmapNULL(holder.imageView_image, holder.progressBar_loading);

            holder.view_isMainImage.setEnabled(false);
            holder.view_isMainImage.setVisibility(View.GONE);
        }
        else if (resources.getImageURL() != null && resources.getBitmap() != null) {
            whenURLExistAndBitmapExist(holder.imageView_image, holder.progressBar_loading, resources.getBitmap());
            setupImageViewListener(holder.imageView_image, holder.getAdapterPosition());
            if (resources.isMainImage()) {
                holder.view_isMainImage.setEnabled(true);
                holder.view_isMainImage.setVisibility(View.VISIBLE);
            }
            else {
                holder.view_isMainImage.setEnabled(false);
                holder.view_isMainImage.setVisibility(View.GONE);
            }
        }
        else if (resources.getImageURL() == null && resources.getBitmap() != null) {
            whenURLNULLAndBitmapExist(holder.imageView_image, holder.progressBar_loading, resources.getBitmap());
            setupImageViewListener(holder.imageView_image, holder.getAdapterPosition());
            if (resources.isMainImage()) {
                holder.view_isMainImage.setEnabled(true);
                holder.view_isMainImage.setVisibility(View.VISIBLE);
            }
            else {
                holder.view_isMainImage.setEnabled(false);
                holder.view_isMainImage.setVisibility(View.GONE);
            }
        }
        else if (resources.getImageURL() == null && resources.getBitmap() == null) {
            whenURLNULLAndBitmapNULL(holder.imageView_image, holder.progressBar_loading);
            setupImageViewListener(holder.imageView_image, holder.getAdapterPosition());

            holder.view_isMainImage.setEnabled(false);
            holder.view_isMainImage.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return listImages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public ImageView imageView_image;
        public ProgressBar progressBar_loading;
        public View view_isMainImage;
        public ViewHolder (View view)
        {
            super(view);
            imageView_image = (ImageView) view.findViewById(R.id.fragment_user_edit_lelang_barang_layout_image_items_gambar);
            progressBar_loading = (ProgressBar) view.findViewById(R.id.fragment_user_edit_lelang_barang_layout_image_items_loading);
            view_isMainImage = view.findViewById(R.id.fragment_user_edit_lelang_barang_layout_image_items_main_image);
        }
    }

    public void updateDataSet(ArrayList<ItemImageResources> list)
    {
        this.listImages = list;
        notifyDataSetChanged();
    }

    private void whenURLExistAndBitmapNULL(ImageView imageView, ProgressBar progressBar)
    {
        imageView.setEnabled(false);
        imageView.setVisibility(View.GONE);

        progressBar.setEnabled(true);
        progressBar.setVisibility(View.VISIBLE);
    }
    private void whenURLExistAndBitmapExist(ImageView imageView, ProgressBar progressBar, Bitmap bitmap)
    {
        imageView.setEnabled(true);
        imageView.setVisibility(View.VISIBLE);

        progressBar.setEnabled(false);
        progressBar.setVisibility(View.GONE);

        imageView.setImageBitmap(bitmap);
    }
    private void whenURLNULLAndBitmapExist(ImageView imageView, ProgressBar progressBar, Bitmap bitmap)
    {
        imageView.setEnabled(true);
        imageView.setVisibility(View.VISIBLE);

        progressBar.setEnabled(false);
        progressBar.setVisibility(View.GONE);

        imageView.setImageBitmap(bitmap);
    }
    private void whenURLNULLAndBitmapNULL(ImageView imageView, ProgressBar progressBar)
    {
        imageView.setEnabled(true);
        imageView.setVisibility(View.VISIBLE);

        progressBar.setEnabled(false);
        progressBar.setVisibility(View.GONE);

        imageView.setImageResource(R.drawable.ic_add_box_grey_48dp);
    }
    private void setupImageViewListener(ImageView imageView, final int position)
    {
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagePicker.picked(position);
            }
        });
    }
}
