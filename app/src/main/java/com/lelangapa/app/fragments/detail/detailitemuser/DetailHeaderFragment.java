package com.lelangapa.app.fragments.detail.detailitemuser;

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
        /*textView_judulBarang = (TextView) view.findViewById(R.id.fragment_detail_barang_header_judul);
        textView_namaAuctioneer = (TextView) view.findViewById(R.id.fragment_detail_barang_header_auctioneername);
        textView_judulBarang.setText(detailItem.getNamabarang());
        textView_namaAuctioneer.setText(detailItem.getNamaauctioneer());*/
        return view;
    }
    private void setStatusBiddingFragment(int status)
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
    public void setDetailItem(DetailItemResources detailItem)
    {
        this.detailItem = detailItem;
    }
    public void setStatusBiddingItem(Integer status)
    {
        setStatusBiddingFragment(status);
    }
}
