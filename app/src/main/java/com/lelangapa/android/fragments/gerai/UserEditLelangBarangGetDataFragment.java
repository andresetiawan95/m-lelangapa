package com.lelangapa.android.fragments.gerai;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.lelangapa.android.R;
import com.lelangapa.android.activities.UserGeraiActivity;
import com.lelangapa.android.activities.cropper.GalleryUtil;
import com.lelangapa.android.adapters.MultipleImageEditItemAdapter;
import com.lelangapa.android.apicalls.gerai.UpdateGambarBarangAPI;
import com.lelangapa.android.apicalls.gerai.UpdateItemEditLelangBarangAPI;
import com.lelangapa.android.apicalls.singleton.RequestController;
import com.lelangapa.android.asyncs.GetItemImagesBMP;
import com.lelangapa.android.interfaces.DataReceiver;
import com.lelangapa.android.interfaces.ImageLoaderReceiver;
import com.lelangapa.android.interfaces.ImagePicker;
import com.lelangapa.android.interfaces.OnItemClickListener;
import com.lelangapa.android.listeners.RecyclerItemClickListener;
import com.lelangapa.android.preferences.SessionManager;
import com.lelangapa.android.resources.DateTimeConverter;
import com.lelangapa.android.resources.ItemImageResources;
import com.lelangapa.android.resources.UserGeraiResources;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import static android.app.Activity.RESULT_OK;

/**
 * Created by andre on 11/12/16.
 * UPDATING IMAGE : UNDONE
 * UPDATING PROGRESS HAS BEEN DONE IN FRAGMENT CLASS
 */

public class UserEditLelangBarangGetDataFragment extends Fragment {
    private ArrayList<UserGeraiResources> dataBarangReceived;
    private ArrayList<ItemImageResources> dataImageReceived, imageToUpload;
    private HashMap<String, String> dataInput, dataNewImage;
    private ProgressDialog loadingDialog;
    private static final String KEY_IDUSER = "id_user";
    private static final String KEY_IDBARANGUPDATE = "item_id";
    private static final String KEY_NAMABARANG = "name";
    private static final String KEY_DESCBARANG = "description";
    private static final String KEY_STARTINGPRICE = "starting_price";
    private static final String KEY_EXPECTEDPRICE = "expected_price";
    private static final String KEY_STARTTIME = "start_time";
    private static final String KEY_ENDTIME = "end_time";
    private static final String KEY_IDCATEGORY = "id_category";
    private static final String KEY_NAMAUSER = "nama_user";
    private static final String KEY_NAMACATEGORY = "nama_category";
    private static final String KEY_IMAGE = "image";
    private EditText editText_namabarang, editText_deskripsibarang, editText_hargabarang_awal, editText_hargabarang_target, editText_tanggalmulai,
            editText_jammulai, editText_tanggalselesai, editText_jamselesai ;

    private RecyclerView recyclerView_image;
    private ImagePicker imagePicker;
    private ImageLoaderReceiver imageLoaderReceiver;
    private DataReceiver whenAnImageAlreadyUploaded, whenMainImageAlreadyEdited;

    private MultipleImageEditItemAdapter adapter;

    private int PICK_IMAGE_REQUEST = 1;
    private int CROP_IMAGE_REQUEST = 2;
    private int IMAGE_ARRAY_INDEX = 0;
    private int IMAGE_ALREADY_UPLOADED_INDEX = 1;
    private static final int MAX_IMAGE = 8;
    private static int MAIN_IMAGE_INDEX = 0;
    private static String INITIAL_UNIQUE_ID_IMAGE = null;

    private Intent cropIntent;
    private Bitmap bitmap;
    private PopupMenu popupMenu_image;
    private Spinner spinner_kategori;

    public UserEditLelangBarangGetDataFragment() {
        initializeConstants();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_user_edit_lelang_barang_layout, container, false);
        Button btnEditBarang = (Button) getActivity().findViewById(R.id.fragment_user_edit_lelang_barang_jual_button);
        btnEditBarang.setVisibility(View.VISIBLE);
        initializeViews(view);
        takeMainImageAsFirstImage();
        setInitialUniqueIdImage();
        setImagePicker();
        setDataBarangInfo();
        initializeAdapter();
        initializeImageReceiver();
        setImageLoaderReceiver();
        setupRecyclerViewProperties();
        loadExistingImage();
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
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null){
            String picturePath = data.getStringExtra("picturePath");
            cropImage(picturePath);
        }
        else if (requestCode == CROP_IMAGE_REQUEST && resultCode == RESULT_OK) {
            if (data != null) {
                Bundle bundle = data.getExtras();
                bitmap = bundle.getParcelable("data");
                if (IMAGE_ARRAY_INDEX == dataImageReceived.size() -1 && dataImageReceived.size() < MAX_IMAGE) {
                    dataImageReceived.get(IMAGE_ARRAY_INDEX).setIdImage(Integer.toString(IMAGE_ARRAY_INDEX));
                    dataImageReceived.get(IMAGE_ARRAY_INDEX).setBitmap(bitmap);
                    dataImageReceived.get(IMAGE_ARRAY_INDEX).setImageChanged(true);
                    if (dataImageReceived.size() == 1) {
                        dataImageReceived.get(IMAGE_ARRAY_INDEX).setMainImage(true);
                    }
                    ItemImageResources newinit = new ItemImageResources();
                    dataImageReceived.add(newinit);

                    adapter.updateDataSet(dataImageReceived);
                }
                else {
                    dataImageReceived.get(IMAGE_ARRAY_INDEX).setIdImage(Integer.toString(IMAGE_ARRAY_INDEX));
                    dataImageReceived.get(IMAGE_ARRAY_INDEX).setBitmap(bitmap);
                    dataImageReceived.get(IMAGE_ARRAY_INDEX).setImageChanged(true);

                    adapter.updateDataSet(dataImageReceived);
                }
            }
        }
    }
    private void initializeConstants() {
        dataBarangReceived = new ArrayList<>();
        dataImageReceived = new ArrayList<>();
        imageToUpload = new ArrayList<>();
        dataInput = new HashMap<>();
        dataNewImage = new HashMap<>();
    }
    private void initializeViews(View view) {
        editText_namabarang = (EditText) view.findViewById(R.id.fragment_user_edit_lelang_barang_nama_barang);
        editText_deskripsibarang = (EditText) view.findViewById(R.id.fragment_user_edit_lelang_barang_deskripsi_barang);
        editText_hargabarang_awal = (EditText) view.findViewById(R.id.fragment_user_edit_lelang_barang_harga_awal_barang);
        editText_hargabarang_target = (EditText) view.findViewById(R.id.fragment_user_edit_lelang_barang_harga_target_barang);
        editText_tanggalmulai = (EditText) view.findViewById(R.id.fragment_user_edit_lelang_barang_tanggal_mulai);
        editText_tanggalselesai = (EditText) view.findViewById(R.id.fragment_user_edit_lelang_barang_tanggal_selesai);
        editText_jammulai = (EditText) view.findViewById(R.id.fragment_user_edit_lelang_barang_jam_mulai);
        editText_jamselesai = (EditText) view.findViewById(R.id.fragment_user_edit_lelang_barang_jam_selesai);
        spinner_kategori = (Spinner) view.findViewById(R.id.fragment_user_edit_lelang_barang_kategori);
        recyclerView_image = (RecyclerView) view.findViewById(R.id.fragment_user_edit_lelang_barang_image_recyclerview);
    }
    private void takeMainImageAsFirstImage() {
        if (dataImageReceived.size() > 0) {
            ItemImageResources temp1 = null, temp2;
            int positionMain = 0;
            for (int x=0;x<dataImageReceived.size();x++) {
                if (dataImageReceived.get(x).isMainImage()) {
                    temp1 = dataImageReceived.get(x);
                    positionMain = x;
                    break;
                }
            }
            temp2 = dataImageReceived.get(0);
            dataImageReceived.set(0, temp1);
            dataImageReceived.set(positionMain, temp2);
        }
    }
    private void setInitialUniqueIdImage()
    {
        if (dataImageReceived.size() > 1)
            INITIAL_UNIQUE_ID_IMAGE = dataImageReceived.get(0).getUniqueIDImage();
    }
    private void initializeAdapter() {
        adapter = new MultipleImageEditItemAdapter(getActivity(), dataImageReceived, imagePicker);
    }
    private void initializeImageReceiver() {
        whenAnImageAlreadyUploaded = new DataReceiver() {
            @Override
            public void dataReceived(Object output) {
                String response = output.toString();
                if (response.equals("success")) {
                    Log.v("UPLOADED", "IMAGE IDX " + IMAGE_ALREADY_UPLOADED_INDEX + " UPLOADED");
                    if (IMAGE_ALREADY_UPLOADED_INDEX == imageToUpload.size()) {
                        //finishActivity();
                        updateMainImage();
                    }
                    else {
                        IMAGE_ALREADY_UPLOADED_INDEX += 1;
                        wrapNewImageData(IMAGE_ALREADY_UPLOADED_INDEX);
                        uploadEditedImageToServer();
                    }
                }
            }
        };
        whenMainImageAlreadyEdited = new DataReceiver() {
            @Override
            public void dataReceived(Object output) {
                String response = output.toString();
                if (response.equals("success")) {
                    Log.v("KELAR", "KELARRRR");
                    finishActivity();
                }
            }
        };
    }
    private void setupRecyclerViewProperties() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView_image.setLayoutManager(layoutManager);
        recyclerView_image.setAdapter(adapter);
        recyclerView_image.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), recyclerView_image, new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {}

            @Override
            public void onLongItemClick(View view, int position) {
                chooseMainImagePopupMenu(view, position);
                showPopupMenu();
            }
        }));
    }
    public void setDataBarang(ArrayList<UserGeraiResources> dataBarang, ArrayList<ItemImageResources> dataImage){
        dataBarangReceived = dataBarang;
        dataImageReceived = dataImage;
    }
    private void setImagePicker()
    {
        imagePicker = new ImagePicker() {
            @Override
            public void picked(Integer indexOfArray) {
                IMAGE_ARRAY_INDEX = indexOfArray;
                chooseImage();
            }
        };
    }
    private void setImageLoaderReceiver()
    {
        imageLoaderReceiver = new ImageLoaderReceiver() {
            @Override
            public void loaded(Bitmap[] imageList) {
                if (dataImageReceived.size() == imageList.length) {
                    for (int x=0;x<dataImageReceived.size();x++) {
                        dataImageReceived.get(x).setBitmap(imageList[x]);
                    }
                }
                if (dataImageReceived.size() < MAX_IMAGE) {
                    ItemImageResources newInit = new ItemImageResources();
                    dataImageReceived.add(newInit);
                }
                //update adapter
                adapter.updateDataSet(dataImageReceived);
            }
        };
    }
    private void chooseMainImagePopupMenu(View view, final int position)
    {
        popupMenu_image = new PopupMenu(getActivity(), view);
        popupMenu_image.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.set_main_image_popup_menu :
                        dataImageReceived.get(MAIN_IMAGE_INDEX).setMainImage(false);
                        dataImageReceived.get(position).setMainImage(true);

                        ItemImageResources temp1 = dataImageReceived.get(MAIN_IMAGE_INDEX);
                        ItemImageResources temp2 = dataImageReceived.get(position);
                        dataImageReceived.set(MAIN_IMAGE_INDEX, temp2);
                        dataImageReceived.set(position, temp1);

                        adapter.updateDataSet(dataImageReceived);
                        return true;
                    default:
                        return false;
                }
            }
        });
        popupMenu_image.inflate(R.menu.item_image_popup_menu);
    }
    private void showPopupMenu()
    {
        popupMenu_image.show();
    }
    private void getBitmapListFromAWS()
    {
        GetItemImagesBMP getItemImagesBMP = new GetItemImagesBMP(imageLoaderReceiver);
        String[] listURL = new String[dataImageReceived.size()];
        for (int x=0;x<listURL.length;x++) {
            listURL[x] = "http://img-s7.lelangapa.com/" + dataImageReceived.get(x).getImageURL();
        }
        getItemImagesBMP.execute(listURL);
    }
    private void loadExistingImage()
    {
        if (dataImageReceived.size() > 0) {
            getBitmapListFromAWS();
        }
        else {
            ItemImageResources newInit = new ItemImageResources();
            dataImageReceived.add(newInit);
            adapter.updateDataSet(dataImageReceived);
        }
    }
    private void chooseImage(){
        Intent galleryIntent = new Intent(getActivity(), GalleryUtil.class);
        startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST);
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
    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
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
    private void generateAllNewImageToUpload()
    {
        for (int x=0;x<dataImageReceived.size();x++) {
            if (dataImageReceived.get(x).isImageChanged()) {
                imageToUpload.add(dataImageReceived.get(x));
            }
        }
    }
    private void wrapNewImageData(int index)
    {
        dataNewImage.put("image", getStringImage(imageToUpload.get(index-1).getBitmap()));
        dataNewImage.put("ext", "jpg");
        dataNewImage.put("id_user", SessionManager.getSessionStatic().get(SessionManager.KEY_ID));
        dataNewImage.put("itemid", dataBarangReceived.get(0).getIdbarang());
        dataNewImage.put("is_main_image", Boolean.toString(imageToUpload.get(index-1).isMainImage()));
        if (imageToUpload.get(index-1).getUniqueIDImage() == null) {
            dataNewImage.put("imageid", "null");
        }
        else {
            dataNewImage.put("imageid", imageToUpload.get(index-1).getUniqueIDImage());
        }
    }
    private void putFilledData(){
        DateTimeConverter dateTimeConverter = new DateTimeConverter();
        dataInput.put(KEY_IDUSER, SessionManager.getSessionStatic().get(SessionManager.KEY_ID));
        dataInput.put(KEY_IDBARANGUPDATE, dataBarangReceived.get(0).getIdbarang());
        dataInput.put(KEY_NAMABARANG, editText_namabarang.getText().toString());
        dataInput.put(KEY_DESCBARANG, editText_deskripsibarang.getText().toString());
        dataInput.put(KEY_STARTINGPRICE, editText_hargabarang_awal.getText().toString());
        dataInput.put(KEY_EXPECTEDPRICE, editText_hargabarang_target.getText().toString());
        dataInput.put(KEY_STARTTIME, dateTimeConverter.convertInputLocalTime(editText_tanggalmulai.getText().toString() + " "+ editText_jammulai.getText().toString()+ ":00"));
        dataInput.put(KEY_ENDTIME, dateTimeConverter.convertInputLocalTime(editText_tanggalselesai.getText().toString() + " "+ editText_jamselesai.getText().toString()+ ":00"));
        dataInput.put(KEY_IDCATEGORY, Integer.toString(spinner_kategori.getSelectedItemPosition() +1));
        dataInput.put(KEY_NAMACATEGORY, spinner_kategori.getSelectedItem().toString());
        dataInput.put(KEY_NAMAUSER, SessionManager.getSessionStatic().get(SessionManager.KEY_NAME));
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
                        generateAllNewImageToUpload();
                        Log.v("ImgToBeUploaded", Integer.toString(imageToUpload.size()));
                        if (imageToUpload.size() > 0) {
                            wrapNewImageData(IMAGE_ALREADY_UPLOADED_INDEX);
                            uploadEditedImageToServer();
                        }
                        else {
                            updateMainImage();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        UpdateItemEditLelangBarangAPI updateItemEditLelangBarang = new UpdateItemEditLelangBarangAPI(dataInput, updatedData);
        RequestController.getInstance(getActivity()).addToRequestQueue(updateItemEditLelangBarang);
    }
    private void uploadEditedImageToServer() {
        UpdateGambarBarangAPI.UpdateGambar updateGambarAPI = UpdateGambarBarangAPI.instanceUpdateGambar(dataNewImage, whenAnImageAlreadyUploaded);
        RequestController.getInstance(getActivity()).addToRequestQueue(updateGambarAPI);
    }
    private void finishActivity() {
        loadingDialog.dismiss();
        Toast.makeText(getActivity(), "Info barang berhasil diperbaharui", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getActivity(), UserGeraiActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        getActivity().startActivity(intent);
        getActivity().finish();
    }

    private void updateMainImage()
    {
        if (INITIAL_UNIQUE_ID_IMAGE != null && dataImageReceived.size() > 1) {
            String NEW_MAIN_IMAGE = dataImageReceived.get(0).getUniqueIDImage();
            if (NEW_MAIN_IMAGE == null || !NEW_MAIN_IMAGE.equals(INITIAL_UNIQUE_ID_IMAGE)) {
                Log.v("NEW IMAGE", "NEW IMAGE COYYY");
                if (NEW_MAIN_IMAGE == null) editMainImageInServer("null");
                else editMainImageInServer(NEW_MAIN_IMAGE);
            }
            else if (NEW_MAIN_IMAGE.equals(INITIAL_UNIQUE_ID_IMAGE) && dataImageReceived.get(0).isImageChanged()) {
                Log.v("NEW IMAGE", "NEW IMAGE COYYYZZZ");
                editMainImageInServer(NEW_MAIN_IMAGE);
            }
            else finishActivity();
        }
        else {
            finishActivity();
        }
    }
    private void editMainImageInServer(String newID) {
        HashMap<String, String> editMainImageData = new HashMap<>();
        editMainImageData.put("id_item", dataBarangReceived.get(0).getIdbarang());
        editMainImageData.put("old_image_id", INITIAL_UNIQUE_ID_IMAGE);
        editMainImageData.put("new_image_id", newID);

        //kirim ke server
        UpdateGambarBarangAPI.UpdateMain updateMainAPI = UpdateGambarBarangAPI.instanceUpdateMain(editMainImageData, whenMainImageAlreadyEdited);
        RequestController.getInstance(getActivity()).addToRequestQueue(updateMainAPI);
    }
    //logic untuk case dimana gambar utamanya dirubah dengan gambar yang sudah diupload sebelumnya, bukan gambar baru
    //cek apakah element ItemImageResources di index ke 0 isImageChange. jika true, maka biarkan. karena berarti itu gambar baru dan sudah pasti
    //di handle di update-aws.php

    //jika tidak, cek apakah uniqueID element di index ke 0 berbeda dengan initUniqueID (di declare di global)
    //jika berbeda, maka ada perubahan gambar utama -> request ke server untuk update
    //jika sama, berarti tidak ada perubahan
}
