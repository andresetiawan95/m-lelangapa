package com.lelangapa.app.fragments.detail.detailtawaran;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lelangapa.app.R;
import com.lelangapa.app.adapters.DaftarTawaranAdapter;
import com.lelangapa.app.apicalls.detail.daftartawaran.DaftarTawaranAPI;
import com.lelangapa.app.apicalls.singleton.RequestController;
import com.lelangapa.app.apicalls.socket.BiddingSocket;
import com.lelangapa.app.decorations.DividerItemDecoration;
import com.lelangapa.app.interfaces.DataReceiver;
import com.lelangapa.app.interfaces.SocketReceiver;
import com.lelangapa.app.interfaces.SocketSender;
import com.lelangapa.app.resources.BiddingResources;
import com.lelangapa.app.resources.DetailItemResources;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.socket.client.Socket;

/**
 * Created by andre on 28/03/17.
 */

public class DaftarTawaranFragment extends Fragment {
    private Socket mSocket;
    private BiddingSocket biddingSocket;
    private ArrayList<BiddingResources> listOffer;
    private DetailItemResources detailItem;
    private DataReceiver dataReceiver;

    private SwipeRefreshLayout swipeRefreshLayout;
    private ImageView imageView_avatar;
    private TextView textView_nama, textView_offer, textView_noBidIndicator;
    private LinearLayout linearLayout_userProperties, linearLayout_priceProperties;
    private RecyclerView recyclerView;

    private DaftarTawaranAdapter daftarTawaranAdapter;

    private CancelToggler cancelToggler;
    private BlockToggler blockToggler;
    private ChooseWinnerToggler chooseWinnerToggler;

    private SocketSender socketSender;
    private SocketReceiver onBidSuccessDaftarTawaranReceiver, onBidCancelledDaftarTawaranReceiver, onWinnerSelectedDaftarTawaranReceiver;
    private DataReceiver onBlockUserReceiver;

    private boolean isNoBidIndicatorEnabled;
    //private boolean onPauseWhenSocketAlreadyConnected, onPauseActivity;

    public DaftarTawaranFragment()
    {
        initializeConstants();
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        initializeDataReceiver();
        setSocketSender();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_detail_barang_daftar_tawaran_layout, container, false);
        initializeViews(view);
        setSwipeRefreshLayoutProperties();
        initializeTogglers();
        initializeAdapter();
        setRecyclerViewProperties();
        if (mSocket.connected())
        {
            Log.v("CONNECTED", "SOCKET MASIH CONNECTED");
        }
        else
        {
            Log.v("DISCONNECT", "SOCKET SUDAH KOID");
        }
        return view;
    }
    @Override
    public void onResume()
    {
        super.onResume();
        onResumeConfiguration();
    }
    @Override
    public void onPause()
    {
        super.onPause();
    }
    @Override
    public void onDestroy()
    {
        super.onDestroy();
        onDestroyConfiguration();
    }
    private void initializeConstants()
    {
        listOffer = new ArrayList<>();
        isNoBidIndicatorEnabled = true;
        /*onPauseWhenSocketAlreadyConnected = false;
        onPauseActivity = false;*/
    }
    private void initializeTogglers()
    {
        blockToggler = new BlockToggler(getActivity(), onBlockUserReceiver, detailItem.getIdbarang(), detailItem.getIdauctioneer(), detailItem.getBidtime());
        cancelToggler = new CancelToggler(getActivity(), socketSender, detailItem.getIdbarang(), detailItem.getBidtime());
        chooseWinnerToggler = new ChooseWinnerToggler(getActivity(), socketSender, detailItem.getIdbarang());
    }
    private void initializeViews(View view)
    {
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.fragment_detail_barang_daftar_tawaran_layout_swipeRefreshLayout);
        textView_nama = (TextView) view.findViewById(R.id.fragment_detail_barang_daftar_tawaran_layout_name);
        textView_offer = (TextView) view.findViewById(R.id.fragment_detail_barang_daftar_tawaran_layout_bidprice);
        textView_noBidIndicator = (TextView) view.findViewById(R.id.fragment_detail_barang_daftar_tawaran_layout_nobidIndicator);
        imageView_avatar = (ImageView) view.findViewById(R.id.fragment_detail_barang_daftar_tawaran_layout_avatar);
        recyclerView = (RecyclerView) view.findViewById(R.id.fragment_detail_barang_daftar_tawaran_layout_recyclerview);

        linearLayout_priceProperties = (LinearLayout) view.findViewById(R.id.fragment_detail_barang_daftar_tawaran_layout_priceproperties);
        linearLayout_userProperties = (LinearLayout) view.findViewById(R.id.fragment_detail_barang_daftar_tawaran_layout_userproperties);
    }
    private void initializeDataReceiver()
    {
        dataReceiver = new DataReceiver() {
            @Override
            public void dataReceived(Object output) {
                String response = output.toString();
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    JSONArray responseArray = jsonResponse.getJSONArray("data");
                    listOffer.clear();
                    for (int i=0;i<responseArray.length();i++) {
                        JSONObject arrayObject = responseArray.getJSONObject(i);
                        BiddingResources biddingResources = new BiddingResources();
                        biddingResources.setIdBid(arrayObject.getString("bid_id"));
                        biddingResources.setNamaBidder(arrayObject.getString("user_name"));
                        biddingResources.setHargaBid(arrayObject.getString("bid_price"));
                        biddingResources.setIdBidder(arrayObject.getString("user_id"));
                        listOffer.add(biddingResources);
                    }
                    whenOfferListReceived();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };
        onBlockUserReceiver = new DataReceiver() {
            @Override
            public void dataReceived(Object output) {
                String response = output.toString();
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    if (jsonResponse.getString("status").equals("success")) {
                        whenUserBlocked();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };
    }
    private void whenUserBlocked()
    {
        blockToggler.unshowProgressDialog();
        swipeRefreshLayout.setEnabled(true);
        getOfferList();
    }
    private void whenOfferListReceived()
    {
        swipeRefreshLayout.setRefreshing(false);
        swipeRefreshLayout.setEnabled(false);
        if (!listOffer.isEmpty()) {
            setupViewsWhenBidExist();
            daftarTawaranAdapter.updateDataset(listOffer);
        }
        else {
            setupViewsWhenBidNotExist();
            daftarTawaranAdapter.updateDataset(listOffer);
        }
    }
    private void setSwipeRefreshLayoutProperties()
    {
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_orange_dark);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //Log.v("RefreshToggle", "Refresh Toggle on FavoriteFragment");
                getOfferList();
            }
        });
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                //Log.v("RefreshPost", "Refresh POST on FavoriteFragment");
                swipeRefreshLayout.setRefreshing(true);
                getOfferList();
            }
        });
    }
    private void initializeAdapter()
    {
        daftarTawaranAdapter = new DaftarTawaranAdapter(getActivity(), listOffer, blockToggler, cancelToggler, chooseWinnerToggler);
    }
    private void setRecyclerViewProperties()
    {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayout.VERTICAL));
        recyclerView.setAdapter(daftarTawaranAdapter);
    }
    private void onResumeConfiguration()
    {
        biddingSocket.IS_CHANGE_TO_DETAIL_TAWARAN_FRAGMENT = true;
    }
    private void onPauseConfiguration()
    {
        //masih mikir
        /*if (biddingSocket != null && biddingSocket.IS_JOINED_STATUS)
        {
            if (mSocket.connected())
            {
                mSocket.emit("leave-room", detailItem.getIdbarang());
                Log.v("Leaving room pause", "Leaving room pause");
                onPauseWhenSocketAlreadyConnected = true;
                biddingSocket.IS_JOINED_STATUS = false;
            }
        }
        onPauseActivity = true;*/
    }
    private void onDestroyConfiguration()
    {
        isNoBidIndicatorEnabled = true;
        biddingSocket.IS_CHANGE_TO_DETAIL_TAWARAN_FRAGMENT = false;
    }
    private void disableViews()
    {

    }
    private void enableViews()
    {

    }
    private void setupViewsWhenBidExist()
    {
        //BiddingResources topBidder = listOffer.get(0);
        if (linearLayout_userProperties.getVisibility() == View.GONE &&
                linearLayout_priceProperties.getVisibility() == View.GONE) {
            linearLayout_userProperties.setVisibility(View.VISIBLE);
            linearLayout_priceProperties.setVisibility(View.VISIBLE);
        }
        textView_nama.setText(listOffer.get(0).getNamaBidder());
        textView_offer.setText(listOffer.get(0).getHargaBid());
        if (isNoBidIndicatorEnabled) {
            textView_noBidIndicator.setVisibility(View.GONE);
            isNoBidIndicatorEnabled = false;
        }
    }
    private void setupViewsWhenBidNotExist()
    {
        if (textView_noBidIndicator.getVisibility() == View.GONE) {
            textView_noBidIndicator.setVisibility(View.VISIBLE);
        }
        linearLayout_userProperties.setVisibility(View.GONE);
        linearLayout_priceProperties.setVisibility(View.GONE);
        isNoBidIndicatorEnabled = true;
    }
    private void whenNewOfferReceived(JSONObject jsonObject)
    {
        try {
            int idx = findIndexOfPreviousOfferFromSameBidder(jsonObject.getString("bidder_id_return"));
            if (idx == -1) {
                BiddingResources newOffer = new BiddingResources();
                newOffer.setIdBid(jsonObject.getString("id_bid_return"));
                newOffer.setIdBidder(jsonObject.getString("bidder_id_return"));
                newOffer.setNamaBidder(jsonObject.getString("bidder_name_return"));
                newOffer.setHargaBid(jsonObject.getString("bid_price_return"));

                listOffer.add(0, newOffer);
            }
            else {
                BiddingResources updateOfferFromExistingBidder = listOffer.get(idx);
                listOffer.remove(idx);
                updateOfferFromExistingBidder.setHargaBid(jsonObject.getString("bid_price_return"));
                updateOfferFromExistingBidder.setIdBid(jsonObject.getString("id_bid_return"));

                listOffer.add(0, updateOfferFromExistingBidder);
            }
            daftarTawaranAdapter.updateDataset(listOffer);
            setupViewsWhenBidExist();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private int findIndexOfPreviousOfferFromSameBidder(String idBidder)
    {
        int idx = -1;
        for (int x=0;x<listOffer.size();x++)
        {
            if (listOffer.get(x).getIdBidder().equals(idBidder))
            {
                idx = x;
                break;
            }
        }
        return idx;
    }
    public SocketReceiver getSocketOnBidSuccess()
    {
        if (onBidSuccessDaftarTawaranReceiver == null) {
            onBidSuccessDaftarTawaranReceiver = new SocketReceiver() {
                @Override
                public void socketReceived(Object status, Object response) {
                    JSONObject jsonObject = (JSONObject) response;
                    whenNewOfferReceived(jsonObject);
                }
            };
        }
        return onBidSuccessDaftarTawaranReceiver;
    }
    public SocketReceiver getSocketOnBidCancelled()
    {
        if (onBidCancelledDaftarTawaranReceiver == null) {
            onBidCancelledDaftarTawaranReceiver = new SocketReceiver() {
                @Override
                public void socketReceived(Object status, Object response) {
                    //disable progressdialog
                    cancelToggler.unshowProgressDialog();
                    swipeRefreshLayout.setEnabled(true);
                    getOfferList();
                    //reload ranks
                }
            };
        }
        return onBidCancelledDaftarTawaranReceiver;
    }
    public SocketReceiver getSocketOnWinnerSelected()
    {
        if (onWinnerSelectedDaftarTawaranReceiver == null) {
            onWinnerSelectedDaftarTawaranReceiver = new SocketReceiver() {
                @Override
                public void socketReceived(Object status, Object response) {
                    //disable progressdialog
                    chooseWinnerToggler.unshowProgressDialog();
                    getActivity().onBackPressed();
                    //pop back fragment
                }
            };
        }
        return onWinnerSelectedDaftarTawaranReceiver;
    }
    private void setSocketSender()
    {
        socketSender = new SocketSender() {
            @Override
            public void sendSocketData(String emitMsg, String message) {
                mSocket.emit(emitMsg, message);
            }
        };
    }
    public void setSocket(Socket socket)
    {
        this.mSocket = socket;
    }
    public void setBiddingSocket(BiddingSocket biddingSocket)
    {
        this.biddingSocket = biddingSocket;
    }
    public void setDetailItem(DetailItemResources detailItem)
    {
        this.detailItem = detailItem;
    }
    private void getOfferList()
    {
        swipeRefreshLayout.setRefreshing(true);
        String urlparams = detailItem.getIdbarang() + "/offers?limit=50";
        DaftarTawaranAPI.GetOfferListAPI getOfferListAPI
                = DaftarTawaranAPI.instanceGetOfferList(urlparams, dataReceiver);
        RequestController.getInstance(getActivity()).addToRequestQueue(getOfferListAPI);
    }
}
