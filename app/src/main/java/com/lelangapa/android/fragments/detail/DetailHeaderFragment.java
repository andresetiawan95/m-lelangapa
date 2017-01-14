package com.lelangapa.android.fragments.detail;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lelangapa.android.R;
import com.lelangapa.android.fragments.detail.statusbid.DetailHeaderStatusDoneFragment;
import com.lelangapa.android.fragments.detail.statusbid.DetailHeaderStatusLiveFragment;
import com.lelangapa.android.fragments.detail.statusbid.DetailHeaderStatusSoonFragment;
import com.lelangapa.android.resources.DetailItemResources;

/**
 * Created by Andre on 12/29/2016.
 */

public class DetailHeaderFragment extends Fragment {
    private DetailItemResources detailItem;
    private TextView textView_judulBarang;
    private TextView textView_namaAuctioneer;
    private ImageView imageView_avatarAuctioneer;


    public DetailHeaderFragment(){}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_detail_barang_header_layout, container, false);
        textView_judulBarang = (TextView) view.findViewById(R.id.fragment_detail_barang_header_judul);
        textView_namaAuctioneer = (TextView) view.findViewById(R.id.fragment_detail_barang_header_auctioneername);
        textView_judulBarang.setText(detailItem.getNamabarang());
        textView_namaAuctioneer.setText(detailItem.getNamaauctioneer());
        setStatusBiddingFragment();
        return view;
    }
    private void setStatusBiddingFragment()
    {
        if (detailItem.getItembidstatus() == 0)
        {
            DetailHeaderStatusSoonFragment detailHeaderStatusSoon = new DetailHeaderStatusSoonFragment();
            getFragmentManager().beginTransaction()
                    .replace(R.id.fragment_detail_barang_header_status_fragment, detailHeaderStatusSoon)
                    .commit();
        }
        else if (detailItem.getItembidstatus() == 1)
        {
            DetailHeaderStatusLiveFragment detailHeaderStatusLive = new DetailHeaderStatusLiveFragment();
            getFragmentManager().beginTransaction()
                    .replace(R.id.fragment_detail_barang_header_status_fragment, detailHeaderStatusLive)
                    .commit();
        }
        else if (detailItem.getItembidstatus() == -1)
        {
            DetailHeaderStatusDoneFragment detailHeaderStatusDone = new DetailHeaderStatusDoneFragment();
            getFragmentManager().beginTransaction()
                    .replace(R.id.fragment_detail_barang_header_status_fragment, detailHeaderStatusDone)
                    .commit();
        }
    }
    public void setDetailItem(DetailItemResources detailItem)
    {
        this.detailItem = detailItem;
    }
}
