package com.lelangkita.android.fragments.detail;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.lelangkita.android.R;
import com.lelangkita.android.apicalls.detail.DetailItemAPI;
import com.lelangkita.android.interfaces.DataReceiver;
import com.lelangkita.android.resources.DetailItemResources;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Andre on 12/24/2016.
 */

public class DetailFragment extends Fragment {
    private DataReceiver detailReceived;
    private DetailHeaderFragment detailHeaderFragment;
    private DetailGambarFragment detailGambarFragment;
    private DetailBiddingFragment detailBiddingFragment;
    private DetailWaktuBidFragment detailWaktuBidFragment;
    private DetailDeskripsiFragment detailDeskripsiFragment;
    private DetailKomentarFragment detailKomentarFragment;
    private DetailAuctioneerFragment detailAuctioneerFragment;
    private DetailItemResources detailItem;
    public DetailFragment(){}
    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_detail_barang_layout, container, false);
        String itemID = getActivity().getIntent().getStringExtra("items_id");
        detailHeaderFragment = new DetailHeaderFragment();
        detailGambarFragment = new DetailGambarFragment();
        detailBiddingFragment = new DetailBiddingFragment();
        detailWaktuBidFragment = new DetailWaktuBidFragment();
        detailDeskripsiFragment = new DetailDeskripsiFragment();
        detailKomentarFragment = new DetailKomentarFragment();
        detailAuctioneerFragment = new DetailAuctioneerFragment();
        detailReceived = new DataReceiver() {
            @Override
            public void dataReceived(Object output) {
                if (output.toString().equals("done"))
                {
                    setDataToChildFragments(detailItem);
                    //untuk menampilkan fragment submit bid
                    setChildFragments();
                }
            }
        };
        getDetailItem(itemID);
        return view;
    }
    private void setDataToChildFragments(DetailItemResources detailItem)
    {
        detailHeaderFragment.setDetailItem(detailItem);
        detailGambarFragment.setDetailItem(detailItem);
        detailDeskripsiFragment.setDetailItem(detailItem);
        detailWaktuBidFragment.setDetailItem(detailItem);
    }
    private void setChildFragments()
    {
        getFragmentManager().beginTransaction()
                .replace(R.id.fragment_detail_barang_header_fragment, detailHeaderFragment)
                .replace(R.id.fragment_detail_barang_gambar_fragment, detailGambarFragment)
                .replace(R.id.fragment_detail_barang_bidding_fragment, detailBiddingFragment)
                .replace(R.id.fragment_detail_barang_waktubid_fragment, detailWaktuBidFragment)
                .replace(R.id.fragment_detail_barang_deskripsi_fragment, detailDeskripsiFragment)
                .replace(R.id.fragment_detail_barang_komentar_fragment, detailKomentarFragment)
                .replace(R.id.fragment_detail_barang_auctioneer_fragment, detailAuctioneerFragment)
                .commit();
    }
    private void getDetailItem(String itemID)
    {
        DataReceiver dataReceiver = new DataReceiver() {
            @Override
            public void dataReceived(Object output) {
                String response = output.toString();
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    String status = jsonResponse.getString("status");
                    if (status.equals("success"))
                    {
                        JSONArray responseData = jsonResponse.getJSONArray("data");
                        for (int i=0;i<responseData.length();i++)
                        {
                            detailItem = new DetailItemResources();
                            JSONObject itemDataObject = responseData.getJSONObject(i);
                            detailItem.setIdbarang(itemDataObject.getString("item_id"));
                            detailItem.setNamabarang(itemDataObject.getString("item_name"));
                            detailItem.setDeskripsibarang(itemDataObject.getString("item_description"));
                            detailItem.setHargaawal(itemDataObject.getString("starting_price"));
                            detailItem.setHargatarget(itemDataObject.getString("expected_price"));
                            String startTimeBeforeSplit = itemDataObject.getString("start_time");
                            String endTimeBeforeSplit = itemDataObject.getString("end_time");
                            String[] startTimePart = startTimeBeforeSplit.split("T");
                            String[] endTimePart = endTimeBeforeSplit.split("T");
                            String startDate = startTimePart[0];
                            String endDate = endTimePart[0];
                            String [] startHourPart = startTimePart[1].split("\\.");
                            String [] endHourPart = endTimePart[1].split("\\.");
                            String startHour = startHourPart[0];
                            String endHour = endHourPart[0];
                            detailItem.setTanggalmulai(startDate);
                            detailItem.setJammulai(startHour);
                            detailItem.setTanggalselesai(endDate);
                            detailItem.setJamselesai(endHour);
                            detailItem.setNamapengguna(itemDataObject.getString("user_name"));
                            JSONArray detailUrlGambarItemArray = itemDataObject.getJSONArray("url");
                            for (int j=0;j<detailUrlGambarItemArray.length();j++)
                            {
                                JSONObject detailUrlGambarItemObject = detailUrlGambarItemArray.getJSONObject(j);
                                detailItem.setUrlgambarbarang("http://es3.lelangkita.com/" +detailUrlGambarItemObject.getString("url"));
                            }
                        }
                        detailReceived.dataReceived("done");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        DetailItemAPI detailItemAPI = new DetailItemAPI(itemID, dataReceiver);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(detailItemAPI);
    }
}
