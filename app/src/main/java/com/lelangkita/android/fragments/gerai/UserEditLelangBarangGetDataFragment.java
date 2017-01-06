package com.lelangkita.android.fragments.gerai;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.lelangkita.android.R;
import com.lelangkita.android.activities.UserGeraiActivity;
import com.lelangkita.android.apicalls.gerai.UpdateItemEditLelangBarangAPI;
import com.lelangkita.android.interfaces.DataReceiver;
import com.lelangkita.android.resources.DateTimeConverter;
import com.lelangkita.android.resources.UserGeraiResources;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by andre on 11/12/16.
 * UPDATING IMAGE : UNDONE
 * UPDATING PROGRESS HAS BEEN DONE IN FRAGMENT CLASS
 */

public class UserEditLelangBarangGetDataFragment extends Fragment {
    private ArrayList<UserGeraiResources> dataBarangReceived = new ArrayList<>();
    private HashMap<String, String> data = new HashMap<>();
    private ProgressDialog loadingDialog;
    private static final String KEY_IDBARANGUPDATE = "item_id";
    private static final String KEY_NAMABARANG = "name";
    private static final String KEY_DESCBARANG = "description";
    private static final String KEY_STARTINGPRICE = "starting_price";
    private static final String KEY_EXPECTEDPRICE = "expected_price";
    private static final String KEY_STARTTIME = "start_time";
    private static final String KEY_ENDTIME = "end_time";
    private static final String KEY_IMAGE = "image";
    private EditText editText_namabarang;
    private EditText editText_deskripsibarang;
    private EditText editText_hargabarang_awal;
    private EditText editText_hargabarang_target;
    private EditText editText_tanggalmulai;
    private EditText editText_jammulai;
    private EditText editText_tanggalselesai;
    private EditText editText_jamselesai;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_user_edit_lelang_barang_layout, container, false);
        Button btnEditBarang = (Button) getActivity().findViewById(R.id.fragment_user_edit_lelang_barang_jual_button);
        btnEditBarang.setVisibility(View.VISIBLE);
        editText_namabarang = (EditText) view.findViewById(R.id.fragment_user_edit_lelang_barang_nama_barang);
        editText_deskripsibarang = (EditText) view.findViewById(R.id.fragment_user_edit_lelang_barang_deskripsi_barang);
        editText_hargabarang_awal = (EditText) view.findViewById(R.id.fragment_user_edit_lelang_barang_harga_awal_barang);
        editText_hargabarang_target = (EditText) view.findViewById(R.id.fragment_user_edit_lelang_barang_harga_target_barang);
        editText_tanggalmulai = (EditText) view.findViewById(R.id.fragment_user_edit_lelang_barang_tanggal_mulai);
        editText_tanggalselesai = (EditText) view.findViewById(R.id.fragment_user_edit_lelang_barang_tanggal_selesai);
        editText_jammulai = (EditText) view.findViewById(R.id.fragment_user_edit_lelang_barang_jam_mulai);
        editText_jamselesai = (EditText) view.findViewById(R.id.fragment_user_edit_lelang_barang_jam_selesai);
        setDataBarangInfo();
        btnEditBarang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingDialog = ProgressDialog.show(getActivity(), "Sedang diproses", "Harap menunggu..");
                putFilledData();
                sendUpdatedDataToServer();
            }
        });
        return view;
    }
    public void getDataBarang(ArrayList<UserGeraiResources> dataBarang){
        dataBarangReceived = dataBarang;
    }
    private void setDataBarangInfo(){
        String namabarang = dataBarangReceived.get(0).getNamabarang();
        String deskripsibarang = dataBarangReceived.get(0).getDeskripsibarang();
        String hargabarangawal = dataBarangReceived.get(0).getHargaawal();
        String hargabarangtarget = dataBarangReceived.get(0).getHargatarget();
        String tanggalmulai = dataBarangReceived.get(0).getTanggalmulai();
        String tanggalselesai = dataBarangReceived.get(0).getTanggalselesai();
        String jammulai = dataBarangReceived.get(0).getJammulai();
        String jamselesai = dataBarangReceived.get(0).getJamselesai();
        editText_namabarang.setText(namabarang);
        editText_deskripsibarang.setText(deskripsibarang);
        editText_hargabarang_awal.setText(hargabarangawal);
        editText_hargabarang_target.setText(hargabarangtarget);
        editText_tanggalmulai.setText(tanggalmulai);
        editText_tanggalselesai.setText(tanggalselesai);
        editText_jammulai.setText(jammulai);
        editText_jamselesai.setText(jamselesai);
    }
    private void putFilledData(){
        DateTimeConverter dateTimeConverter = new DateTimeConverter();
        data.put(KEY_IDBARANGUPDATE, dataBarangReceived.get(0).getIdbarang());
        data.put(KEY_NAMABARANG, editText_namabarang.getText().toString());
        data.put(KEY_DESCBARANG, editText_deskripsibarang.getText().toString());
        data.put(KEY_STARTINGPRICE, editText_hargabarang_awal.getText().toString());
        data.put(KEY_EXPECTEDPRICE, editText_hargabarang_target.getText().toString());
        data.put(KEY_STARTTIME, dateTimeConverter.convertInputLocalTime(editText_tanggalmulai.getText().toString() + " "+ editText_jammulai.getText().toString()+ ":00"));
        data.put(KEY_ENDTIME, dateTimeConverter.convertInputLocalTime(editText_tanggalselesai.getText().toString() + " "+ editText_jamselesai.getText().toString()+ ":00"));
    }
    private void sendUpdatedDataToServer(){
        DataReceiver updatedData = new DataReceiver() {
            @Override
            public void dataReceived(Object output) {
                String response = output.toString();
                try {
                    JSONObject respObject = new JSONObject(response);
                    String res = respObject.getString("status");
                    if (res.equals("success")){
                        loadingDialog.dismiss();
                        Toast.makeText(getActivity(), "Info barang berhasil diperbaharui", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getActivity(), UserGeraiActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        getActivity().startActivity(intent);
                        getActivity().finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        UpdateItemEditLelangBarangAPI updateItemEditLelangBarang = new UpdateItemEditLelangBarangAPI(data, updatedData);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(updateItemEditLelangBarang);
    }
}
