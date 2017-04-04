package com.lelangapa.android.fragments.gerai;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;

import com.lelangapa.android.R;
import com.lelangapa.android.interfaces.InputReceiver;
import com.lelangapa.android.resources.DateTimeConverter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;

import static android.app.Activity.RESULT_OK;

/**
 * Created by andre on 02/12/16.
 */

public class UserLelangBarangFragmentOld extends Fragment {
    private InputReceiver iReceiver;
    private EditText editText_namabarang;
    private EditText editText_deskripsibarang;
    private EditText editText_hargabarang_awal;
    private EditText editText_hargabarang_target;
    private EditText editText_tanggalmulai;
    private EditText editText_jammulai;
    private EditText editText_tanggalselesai;
    private EditText editText_jamselesai;
    private ImageView gambarBarang;

    private Bitmap bitmap;
    int year, month, day, hour, minute;
    String nama, deskripsi, harga, tanggalmulai, jammulai, tanggalselesai, jamselesai;

    private static final String KEY_NAMABARANG = "name";
    private static final String KEY_DESCBARANG = "description";
    private static final String KEY_STARTINGPRICE = "starting_price";
    private static final String KEY_EXPECTEDPRICE = "expected_price";
    private static final String KEY_STARTTIME = "start_time";
    private static final String KEY_ENDTIME = "end_time";
    private static final String KEY_IMAGE = "image";

    private int PICK_IMAGE_REQUEST = 1;
    public UserLelangBarangFragmentOld(){}
    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        try {
            iReceiver = (InputReceiver) getContext();
        }
        catch (ClassCastException e){
            throw new ClassCastException(context.toString()
                    + " must implement interface");
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_user_lelang_barang_layout, container, false);
        View buttonView = getActivity().findViewById(R.id.fragment_user_lelang_barang_jual_button);
        Button buttonLelang = (Button) buttonView;
        editText_namabarang = (EditText) view.findViewById(R.id.fragment_user_lelang_barang_nama_barang);
        editText_deskripsibarang = (EditText) view.findViewById(R.id.fragment_user_lelang_barang_deskripsi_barang);
        editText_hargabarang_awal = (EditText) view.findViewById(R.id.fragment_user_lelang_barang_harga_awal_barang);
        editText_hargabarang_target = (EditText) view.findViewById(R.id.fragment_user_lelang_barang_harga_target_barang);
        editText_tanggalmulai = (EditText) view.findViewById(R.id.fragment_user_lelang_barang_tanggal_mulai);
        editText_tanggalselesai = (EditText) view.findViewById(R.id.fragment_user_lelang_barang_tanggal_selesai);
        editText_jammulai = (EditText) view.findViewById(R.id.fragment_user_lelang_barang_jam_mulai);
        editText_jamselesai = (EditText) view.findViewById(R.id.fragment_user_lelang_barang_jam_selesai);
        gambarBarang = (ImageView) view.findViewById(R.id.fragment_user_lelang_barang_gambar);

        gambarBarang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });
        editText_tanggalmulai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            selectDate(editText_tanggalmulai);
            }
        });
        editText_tanggalselesai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectDate(editText_tanggalselesai);
            }
        });
        editText_jammulai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            selectHour(editText_jammulai);
            }
        });
        editText_jamselesai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectHour(editText_jamselesai);
            }
        });
        buttonLelang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.v("Masuk", "tombol ditekan dari fragment");
               checkFilledData();
            }
        });
        return view;
    }

    private void checkFilledData(){
        /*
        nama = editText_namabarang.getText().toString().trim();
        deskripsi = editText_deskripsibarang.getText().toString().trim();
        harga = editText_hargabarang_awal.getText().toString().trim();
        tanggalmulai = editText_tanggalmulai.getText().toString().trim();
        jammulai = editText_jammulai.getText().toString().trim();
        tanggalselesai = editText_tanggalselesai.getText().toString().trim();
        jamselesai = editText_jamselesai.getText().toString().trim();
        */
        DateTimeConverter dateTimeConverter = new DateTimeConverter();
        HashMap<String, String> data = new HashMap<>();
        data.put(KEY_NAMABARANG, editText_namabarang.getText().toString());
        data.put(KEY_DESCBARANG, editText_deskripsibarang.getText().toString());
        data.put(KEY_STARTINGPRICE, editText_hargabarang_awal.getText().toString());
        data.put(KEY_EXPECTEDPRICE, editText_hargabarang_target.getText().toString());
        data.put(KEY_STARTTIME, dateTimeConverter.convertInputLocalTime(editText_tanggalmulai.getText().toString() + " " + editText_jammulai.getText().toString() + ":00"));
        data.put(KEY_ENDTIME, dateTimeConverter.convertInputLocalTime(editText_tanggalselesai.getText().toString() + " " + editText_jamselesai.getText().toString() + ":00"));
        data.put(KEY_IMAGE, getStringImage(bitmap));
        //Log.v("Data nama", editText_namabarang.getText().toString());
        //Log.v("Data endtime", editText_tanggalselesai.getText().toString() + " " + editText_jamselesai.getText().toString() + ":00");
        iReceiver.inputReceived(data);
    }
    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }
    private void chooseImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            Uri filepath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filepath);
                gambarBarang.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private void selectDate(final EditText editText){
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                editText.setText(year + "-" + (month+1) + "-" + dayOfMonth);
            }
        }, year, month, day);
        datePickerDialog.show();
    }
    private void selectHour(final EditText editText){
        Calendar calendar = Calendar.getInstance();
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                editText.setText(String.format("%02d:%02d", hourOfDay, minute));
            }
        }, hour, minute, false);
        timePickerDialog.show();
    }
}
