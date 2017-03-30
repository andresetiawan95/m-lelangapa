package com.lelangapa.android.fragments.detail.detailitemuser;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lelangapa.android.R;
import com.lelangapa.android.activities.userpublic.DetailUserPublicActivity;
import com.lelangapa.android.resources.DetailItemResources;

/**
 * Created by Andre on 12/26/2016.
 */

public class DetailAuctioneerFragment extends Fragment {
    private DetailItemResources detailItemResources;
    private String auctioneerID, auctioneerName;
    private TextView textView_namaAuctioneer;
    //private Button button_checkProfile;

    private View.OnClickListener onClickListener;
    public DetailAuctioneerFragment(){}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_detail_barang_auctioneer_layout, container, false);
        initializeViews(view);
        setOnClickListener();
        setTextViewAuctioneerInformation();
        return view;
    }
    private void initializeViews(View view)
    {
        textView_namaAuctioneer = (TextView) view.findViewById(R.id.fragment_detail_barang_auctioneer_layout_nama);
        //button_checkProfile = (Button) view.findViewById(R.id.fragment_detail_barang_auctioneer_layout_button);
    }
    private void setOnClickListener()
    {
        onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DetailUserPublicActivity.class);
                intent.putExtra("id_user", auctioneerID);
                intent.putExtra("nama_user", auctioneerName);
                startActivity(intent);
            }
        };

        textView_namaAuctioneer.setOnClickListener(onClickListener);
    }
    public void setAuctioneerInfo(String auctioneerID, String auctioneerName)
    {
        this.auctioneerID = auctioneerID;
        this.auctioneerName = auctioneerName;
    }
    public void setAuctioneerInfo(DetailItemResources detailItemResources)
    {
        this.detailItemResources = detailItemResources;
    }
    private void setTextViewAuctioneerInformation()
    {
        textView_namaAuctioneer.setText(auctioneerName);
    }
}
