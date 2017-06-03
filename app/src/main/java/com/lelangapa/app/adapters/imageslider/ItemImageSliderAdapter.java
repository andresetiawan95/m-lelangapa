package com.lelangapa.app.adapters.imageslider;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.lelangapa.app.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by andre on 05/04/17.
 */

public class ItemImageSliderAdapter extends PagerAdapter {
    private Activity activity;
    private ArrayList<String> listImagesURL;

    public ItemImageSliderAdapter(Activity activity, ArrayList<String> list)
    {
        this.activity = activity;
        this.listImagesURL = list;
    }
    @Override
    public View instantiateItem(ViewGroup container, int position)
    {
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.fragment_detail_barang_gambar_layout_imageslider, container, false);

        ImageView imageView = (ImageView) view.findViewById(R.id.fragment_detail_barang_gambar_layout_img);
        //imageView.setImageResource(listImagesURL.get(position));
        if (listImagesURL.size() == 0) {
            imageView.setImageResource(R.drawable.ic_insert_photo_grey_128dp);
        }
        else {
            if (listImagesURL.get(position) != null) {
                Picasso.with(activity).load(listImagesURL.get(position)).into(imageView);
            }
        }
        container.addView(view);
        return view;
    }
    @Override
    public int getCount() {
        return listImagesURL.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
