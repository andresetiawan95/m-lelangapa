package com.lelangapa.app.fragments.detail.detailitemowner;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lelangapa.app.R;
import com.lelangapa.app.fragments.detail.statusbid.DetailHeaderStatusDoneFragment;
import com.lelangapa.app.fragments.detail.statusbid.DetailHeaderStatusLiveFragment;
import com.lelangapa.app.fragments.detail.statusbid.DetailHeaderStatusSoonFragment;
import com.lelangapa.app.resources.DetailItemResources;

/**
 * Created by andre on 04/02/17.
 */

public class HeaderFragment extends Fragment {
    private TextView textView_judulBarang;
    private TextView textView_namaAuctioneer;
    private ImageView imageView_avatar;

    private DetailItemResources detailItem;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_detail_barang_header_layout, container, false);
        initializeViews(view);
        //setItemInformation();
        return view;
    }
    /*
    * Initialization method start here
    * */
    private void initializeViews(View view)
    {/*
        textView_judulBarang = (TextView) view.findViewById(R.id.fragment_detail_barang_header_judul);
        textView_namaAuctioneer = (TextView) view.findViewById(R.id.fragment_detail_barang_header_auctioneername);
        imageView_avatar = (ImageView) view.findViewById(R.id.fragment_detail_barang_header_profilepicture);*/
    }
    /*
    * Initialization method end here
    * */

    /*
    * Setter method start here
    * */
    public void setDetailItem(DetailItemResources detailItem)
    {
        this.detailItem = detailItem;
    }
    public void setStatusBiddingItem(Integer status)
    {
        setStatusBiddingFragment(status);
    }
    /*private void setItemInformation()
    {
        textView_judulBarang.setText(detailItem.getNamabarang());
        textView_namaAuctioneer.setText(detailItem.getNamaauctioneer());
    }*/
    /*
    * Setter method end here
    * */

    /*
    * Logic method start here
    * */
    private void setStatusBiddingFragment(Integer status)
    {
        if (status == 0)
        {
            DetailHeaderStatusSoonFragment detailHeaderStatusSoon = new DetailHeaderStatusSoonFragment();
            getFragmentManager().beginTransaction()
                    .replace(R.id.fragment_detail_barang_header_status_fragment, detailHeaderStatusSoon)
                    .commit();
        }
        else if (status == 1)
        {
            DetailHeaderStatusLiveFragment detailHeaderStatusLive = new DetailHeaderStatusLiveFragment();
            getFragmentManager().beginTransaction()
                    .replace(R.id.fragment_detail_barang_header_status_fragment, detailHeaderStatusLive)
                    .commit();
        }
        else if (status == -1)
        {
            DetailHeaderStatusDoneFragment detailHeaderStatusDone = new DetailHeaderStatusDoneFragment();
            getFragmentManager().beginTransaction()
                    .replace(R.id.fragment_detail_barang_header_status_fragment, detailHeaderStatusDone)
                    .commit();
        }
    }
    /*
    * Logic method end here
    * */
}
