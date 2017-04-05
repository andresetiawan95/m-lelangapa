package com.lelangapa.android.adapters.imageslider;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.lelangapa.android.R;

import java.util.ArrayList;

/**
 * Created by andre on 05/04/17.
 */

public class ImageSliderAdapter extends PagerAdapter {
    private Activity activity;
    private ArrayList<Integer> listImagesFromResources;

    public ImageSliderAdapter(Activity activity, ArrayList<Integer> list)
    {
        this.activity = activity;
        this.listImagesFromResources = list;
    }
    @Override
    public View instantiateItem(ViewGroup container, int position)
    {
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.fragment_beranda_home_layout_imageslider, container, false);

        ImageView imageView = (ImageView) view.findViewById(R.id.image_display);
        imageView.setImageResource(listImagesFromResources.get(position));
        container.addView(view);
        return view;
    }
    @Override
    public int getCount() {
        return listImagesFromResources.size();
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
