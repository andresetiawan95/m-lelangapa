package com.lelangapa.android.fragments.gerai;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.lelangapa.android.R;
import com.lelangapa.android.activities.UserGeraiActivity;
import com.lelangapa.android.activities.cropper.GalleryUtil;
import com.lelangapa.android.adapters.MultipleImageNewItemAdapter;
import com.lelangapa.android.apicalls.gerai.SubmitBarangAPI;
import com.lelangapa.android.apicalls.gerai.SubmitGambarBarangAPI;
import com.lelangapa.android.apicalls.singleton.RequestController;
import com.lelangapa.android.interfaces.DataReceiver;
import com.lelangapa.android.interfaces.ImagePicker;
import com.lelangapa.android.preferences.SessionManager;
import com.lelangapa.android.resources.DateTimeConverter;
import com.lelangapa.android.resources.ImageResources;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import static android.app.Activity.RESULT_OK;

/**
 * Created by andre on 02/12/16.
 */

public class UserLelangBarangFragment extends Fragment {
    private EditText editText_namabarang, editText_deskripsibarang, editText_hargabarang_awal, editText_hargabarang_target, editText_tanggalmulai;
    private EditText editText_jammulai, editText_tanggalselesai, editText_jamselesai;
    //private ImageView gambarBarang;
    private Button button_lelangBarang;
    private ProgressDialog progressDialog;
    private SessionManager sessionManager;
    private static HashMap<String, String> session;

    private Bitmap bitmap;
    int year, month, day, hour, minute;

    private static final String KEY_NAMABARANG = "name";
    private static final String KEY_DESCBARANG = "description";
    private static final String KEY_STARTINGPRICE = "starting_price";
    private static final String KEY_EXPECTEDPRICE = "expected_price";
    private static final String KEY_STARTTIME = "start_time";
    private static final String KEY_ENDTIME = "end_time";
    private static final String KEY_IMAGE = "image";
    public static final int MY_TIMEOUT = 100000;
    public static final int MY_RETRY = 2;
    public static final float MY_BACKOFF_MULT = 2f;

    private int PICK_IMAGE_REQUEST = 1;
    private int CROP_IMAGE_REQUEST = 2;
    private int IMAGE_ARRAY_INDEX = 0;
    private int IMAGE_ALREADY_UPLOADED_INDEX = 1;
    private static final int MAX_IMAGE= 8;
    private String itemID;

    private HashMap<String, String> data, dataImage;
    private ArrayList<ImageResources> listImages;
    private MultipleImageNewItemAdapter adapter;
    private RecyclerView recyclerView_imageList;
    private ImagePicker imagePicker;
    private DataReceiver imageReceiver;

    private Intent cropIntent;

    public UserLelangBarangFragment()
    {
        initializeConstants();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_user_lelang_barang_layout, container, false);
        initializeSession();
        initializeViews(view);
        initializeImageReceiver();
        setImagePicker();
        setupViews();
        initializeAdapter();
        setRecyclerViewProperties();
        return view;
    }
    private void initializeConstants()
    {
        data = new HashMap<>();
        dataImage = new HashMap<>();
        listImages = new ArrayList<>();
    }
    private void initializeImageReceiver()
    {
        imageReceiver = new DataReceiver() {
            @Override
            public void dataReceived(Object output) {
                String response = output.toString();
                if (response.equals("success")){
                    if (listImages.size() == MAX_IMAGE) {
                        if (IMAGE_ALREADY_UPLOADED_INDEX == listImages.size()) {
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), "Lelang telah disubmit", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getActivity(), UserGeraiActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            getActivity().finish();
                        }
                        else {
                            IMAGE_ALREADY_UPLOADED_INDEX+=1;
                            wrapImageData(itemID, IMAGE_ALREADY_UPLOADED_INDEX);
                            sendGambarBarangToServer();
                        }
                    }
                    else {
                        if (IMAGE_ALREADY_UPLOADED_INDEX == listImages.size() -1) {
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), "Lelang telah disubmit", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getActivity(), UserGeraiActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            getActivity().finish();
                        }
                        else {
                            IMAGE_ALREADY_UPLOADED_INDEX+=1;
                            wrapImageData(itemID, IMAGE_ALREADY_UPLOADED_INDEX);
                            sendGambarBarangToServer();
                        }
                    }
                }
            }
        };
    }
    private void initializeSession()
    {
        sessionManager = new SessionManager(getActivity());
        session = SessionManager.getSessionStatic();
    }
    private void initializeViews(View view)
    {
        button_lelangBarang = (Button) getActivity().findViewById(R.id.fragment_user_lelang_barang_jual_button);
        editText_namabarang = (EditText) view.findViewById(R.id.fragment_user_lelang_barang_nama_barang);
        editText_deskripsibarang = (EditText) view.findViewById(R.id.fragment_user_lelang_barang_deskripsi_barang);
        editText_hargabarang_awal = (EditText) view.findViewById(R.id.fragment_user_lelang_barang_harga_awal_barang);
        editText_hargabarang_target = (EditText) view.findViewById(R.id.fragment_user_lelang_barang_harga_target_barang);
        editText_tanggalmulai = (EditText) view.findViewById(R.id.fragment_user_lelang_barang_tanggal_mulai);
        editText_tanggalselesai = (EditText) view.findViewById(R.id.fragment_user_lelang_barang_tanggal_selesai);
        editText_jammulai = (EditText) view.findViewById(R.id.fragment_user_lelang_barang_jam_mulai);
        editText_jamselesai = (EditText) view.findViewById(R.id.fragment_user_lelang_barang_jam_selesai);
        //gambarBarang = (ImageView) view.findViewById(R.id.fragment_user_lelang_barang_gambar);
        recyclerView_imageList = (RecyclerView) view.findViewById(R.id.fragment_user_lelang_barang_image_recyclerview);
    }
    private void setupViews()
    {
        /*gambarBarang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });*/
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
        button_lelangBarang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.v("Masuk", "tombol ditekan dari fragment");
                wrapFilledItemData();
            }
        });
    }
    private void setImagePicker()
    {
        imagePicker = new ImagePicker() {
            @Override
            public void picked(Integer indexOfArray) {
                //Log.v("Index", Integer.toString(indexOfArray));
                IMAGE_ARRAY_INDEX = indexOfArray;
                chooseImage();
            }
        };
    }
    private void initializeAdapter()
    {
        ImageResources init = new ImageResources();
        listImages.add(init);
        adapter = new MultipleImageNewItemAdapter(getActivity(), listImages, imagePicker);
    }
    private void setRecyclerViewProperties()
    {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView_imageList.setLayoutManager(layoutManager);
        recyclerView_imageList.setAdapter(adapter);
    }
    private void wrapFilledItemData(){
        DateTimeConverter dateTimeConverter = new DateTimeConverter();
        data.put("id_user", session.get(sessionManager.getKEY_ID()));
        data.put(KEY_NAMABARANG, editText_namabarang.getText().toString());
        data.put(KEY_DESCBARANG, editText_deskripsibarang.getText().toString());
        data.put(KEY_STARTINGPRICE, editText_hargabarang_awal.getText().toString());
        data.put(KEY_EXPECTEDPRICE, editText_hargabarang_target.getText().toString());
        data.put(KEY_STARTTIME, dateTimeConverter.convertInputLocalTime(editText_tanggalmulai.getText().toString() + " " + editText_jammulai.getText().toString() + ":00"));
        data.put(KEY_ENDTIME, dateTimeConverter.convertInputLocalTime(editText_tanggalselesai.getText().toString() + " " + editText_jamselesai.getText().toString() + ":00"));
        //data.put(KEY_IMAGE, getStringImage(bitmap));
        progressDialog = ProgressDialog.show(getActivity(), "Sedang diproses..","Harap tunggu...");
        sendLelangData();
    }
    private void wrapImageData(String itemID, int index)
    {
        dataImage.put("image", getStringImage(listImages.get(index-1).getBitmap()));
        dataImage.put("ext", "jpg");
        dataImage.put("id_user", session.get(sessionManager.getKEY_ID()));
        dataImage.put("itemid", itemID);
    }
    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }
    private void chooseImage(){
        Intent galleryIntent = new Intent(getActivity(), GalleryUtil.class);
        startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST);

        /*Intent intent = new Intent();
        intent.setType("image*//*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        Intent chooserIntent = Intent.createChooser(intent, "Select Picture");
        //chooserIntent.putExtra("lol", "LOLOLOL");
        startActivityForResult(chooserIntent, PICK_IMAGE_REQUEST);*/
    }
    private void otherVersionOfChooseImage()
    {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST);
    }
    private void cropImageURI(Uri path){
        try {
            cropIntent = new Intent("com.android.camera.action.CROP");

            cropIntent.setDataAndType(path, "image/*");
            cropIntent.putExtra("crop", "true");
            cropIntent.putExtra("outputX", 180);
            cropIntent.putExtra("outputY", 180);
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            cropIntent.putExtra("return-data", true);

            startActivityForResult(cropIntent, CROP_IMAGE_REQUEST);
        } catch(ActivityNotFoundException e) {
            String errorMessage = "Perangkat Anda Tidak Mendukung Crop";
            Toast toast = Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }
    private void cropImage(String path){
        try {
            cropIntent = new Intent("com.android.camera.action.CROP");
            File f = new File(path);
            Uri content = Uri.fromFile(f);
            cropIntent.setDataAndType(content, "image/*");
            cropIntent.putExtra("crop", "true");
            cropIntent.putExtra("outputX", 180);
            cropIntent.putExtra("outputY", 180);
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            cropIntent.putExtra("return-data", true);

            startActivityForResult(cropIntent, CROP_IMAGE_REQUEST);
        } catch(ActivityNotFoundException e) {
            String errorMessage = "Perangkat Anda Tidak Mendukung Crop";
            Toast toast = Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null){
            //Uri filePath = data.getData();

            String picturePath = data.getStringExtra("picturePath");
            //cropImageURI(filePath);
            cropImage(picturePath);
            /*try {
                //Log.v("EXTRA SUCCESS", data.getStringExtra("lol"));
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filepath);
                if (IMAGE_ARRAY_INDEX == listImages.size() -1) {
                    listImages.get(IMAGE_ARRAY_INDEX).setIdImage(Integer.toString(IMAGE_ARRAY_INDEX));
                    listImages.get(IMAGE_ARRAY_INDEX).setBitmap(bitmap);
                    ImageResources newinit = new ImageResources();
                    listImages.add(newinit);

                    adapter.updateImageSet();
                }
                else {
                    listImages.get(IMAGE_ARRAY_INDEX).setIdImage(Integer.toString(IMAGE_ARRAY_INDEX));
                    listImages.get(IMAGE_ARRAY_INDEX).setBitmap(bitmap);
                    adapter.updateImageSet();
                }
                //gambarBarang.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }*/
        }
        else if (requestCode == CROP_IMAGE_REQUEST && resultCode == RESULT_OK) {
            if (data != null) {
                Bundle bundle = data.getExtras();
                bitmap = bundle.getParcelable("data");
                if (IMAGE_ARRAY_INDEX == listImages.size() -1 && listImages.size() < MAX_IMAGE) {
                    listImages.get(IMAGE_ARRAY_INDEX).setIdImage(Integer.toString(IMAGE_ARRAY_INDEX));
                    listImages.get(IMAGE_ARRAY_INDEX).setBitmap(bitmap);
                    ImageResources newinit = new ImageResources();
                    listImages.add(newinit);

                    adapter.updateImageSet();
                }
                else {
                    listImages.get(IMAGE_ARRAY_INDEX).setIdImage(Integer.toString(IMAGE_ARRAY_INDEX));
                    listImages.get(IMAGE_ARRAY_INDEX).setBitmap(bitmap);
                    adapter.updateImageSet();
                }
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

    private void sendLelangData()
    {
        DataReceiver dataReceiver = new DataReceiver() {
            @Override
            public void dataReceived(Object output) {
                String response = output.toString();
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    String successResp = jsonResponse.getString("status");
                    if (successResp.equals("success")){
                        String result = jsonResponse.getString("id_item");
                        itemID = result;
                        wrapImageData(itemID, IMAGE_ALREADY_UPLOADED_INDEX);
                        sendGambarBarangToServer();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        SubmitBarangAPI submitBarangAPI = new SubmitBarangAPI(data, dataReceiver);
        RequestController.getInstance(getActivity()).addToRequestQueue(submitBarangAPI);
    }

    private void sendGambarBarangToServer()
    {
        Log.v("Masuk ke upload gambar", "Sudah masuk ke upload gambar");
        SubmitGambarBarangAPI submitGambarBarangAPI = new SubmitGambarBarangAPI(dataImage, imageReceiver);
        submitGambarBarangAPI.setRetryPolicy(new DefaultRetryPolicy(MY_TIMEOUT, MY_RETRY, MY_BACKOFF_MULT));
        RequestController.getInstance(getActivity()).addToRequestQueue(submitGambarBarangAPI);
    }
}
