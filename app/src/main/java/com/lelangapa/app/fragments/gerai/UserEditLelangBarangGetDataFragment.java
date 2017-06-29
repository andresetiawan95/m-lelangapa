package com.lelangapa.app.fragments.gerai;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.lelangapa.app.R;
import com.lelangapa.app.activities.cropper.GalleryUtil;
import com.lelangapa.app.adapters.MultipleImageEditItemAdapter;
import com.lelangapa.app.apicalls.gerai.UpdateGambarBarangAPI;
import com.lelangapa.app.apicalls.gerai.UpdateItemEditLelangBarangAPI;
import com.lelangapa.app.apicalls.singleton.RequestController;
import com.lelangapa.app.asyncs.GetItemImagesBMP;
import com.lelangapa.app.interfaces.DataReceiver;
import com.lelangapa.app.interfaces.ImageLoaderReceiver;
import com.lelangapa.app.interfaces.ImagePicker;
import com.lelangapa.app.interfaces.OnItemClickListener;
import com.lelangapa.app.listeners.RecyclerItemClickListener;
import com.lelangapa.app.preferences.SessionManager;
import com.lelangapa.app.resources.DateTimeConverter;
import com.lelangapa.app.resources.ErrorMessage;
import com.lelangapa.app.resources.ItemImageResources;
import com.lelangapa.app.resources.NumberTextWatcher;
import com.lelangapa.app.resources.PriceFormatter;
import com.lelangapa.app.resources.UserGeraiResources;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
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
    private static final String KEY_USERDOMAIN = "user_domain";
    private static final String KEY_IMAGE = "image";
    private EditText editText_namabarang, editText_deskripsibarang, editText_hargabarang_awal, editText_hargabarang_target;
    private TextView textView_tanggalmulai, textView_jammulai, textView_tanggalselesai, textView_jamselesai, textView_imageWarning, textView_dateWarning;
    private TextInputLayout til_namaBarang, til_deskripsiBarang, til_hargabarangawal, til_hargabarangtarget;
    private Button btnEditBarang;
    private ScrollView scrollView;

    private RecyclerView recyclerView_image;
    private ImagePicker imagePicker;
    private ImageLoaderReceiver imageLoaderReceiver;
    private DataReceiver whenAnImageAlreadyUploaded, whenMainImageAlreadyEdited;
    private DateTimeConverter dateTimeConverter;

    private MultipleImageEditItemAdapter adapter;

    private int PICK_IMAGE_REQUEST = 1;
    private int CROP_IMAGE_REQUEST = 2;
    private int IMAGE_ARRAY_INDEX = 0;
    private int IMAGE_ALREADY_UPLOADED_INDEX = 1;
    private static final int MAX_IMAGE = 8;
    private static int MAIN_IMAGE_INDEX = 0;
    private static String INITIAL_UNIQUE_ID_IMAGE = null;
    private static int year, month, day, hour, minute;

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
        initializeViews(view);
        initializeTextChangeListenerOnPrice();
        initializeDateTimeOnClickListeners();
        setupAllInputValidation();
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
                if (validateInput() && validatePriceInput() && validateDateInput() && validateImageInput()) {
                    loadingDialog = ProgressDialog.show(getActivity(), "Sedang diproses", "Harap menunggu..");
                    putFilledData();
                    sendUpdatedDataToServer();
                }
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
                removeImageError();
            }
        }
    }
    private void initializeConstants() {
        dataBarangReceived = new ArrayList<>();
        dataImageReceived = new ArrayList<>();
        imageToUpload = new ArrayList<>();
        dataInput = new HashMap<>();
        dataNewImage = new HashMap<>();
        dateTimeConverter = new DateTimeConverter();
    }
    private void initializeViews(View view) {
        editText_namabarang = (EditText) view.findViewById(R.id.fragment_user_edit_lelang_barang_nama_barang);
        editText_deskripsibarang = (EditText) view.findViewById(R.id.fragment_user_edit_lelang_barang_deskripsi_barang);
        editText_hargabarang_awal = (EditText) view.findViewById(R.id.fragment_user_edit_lelang_barang_harga_awal_barang);
        editText_hargabarang_target = (EditText) view.findViewById(R.id.fragment_user_edit_lelang_barang_harga_target_barang);
        textView_tanggalmulai = (TextView) view.findViewById(R.id.fragment_user_edit_lelang_barang_tanggal_mulai);
        textView_tanggalselesai = (TextView) view.findViewById(R.id.fragment_user_edit_lelang_barang_tanggal_selesai);
        textView_jammulai = (TextView) view.findViewById(R.id.fragment_user_edit_lelang_barang_jam_mulai);
        textView_jamselesai = (TextView) view.findViewById(R.id.fragment_user_edit_lelang_barang_jam_selesai);
        spinner_kategori = (Spinner) view.findViewById(R.id.fragment_user_edit_lelang_barang_kategori);
        recyclerView_image = (RecyclerView) view.findViewById(R.id.fragment_user_edit_lelang_barang_image_recyclerview);

        textView_imageWarning = (TextView) view.findViewById(R.id.fragment_user_edit_lelang_barang_image_warning);
        textView_dateWarning = (TextView) view.findViewById(R.id.fragment_user_edit_lelang_barang_jadwal_warning);
        scrollView = (ScrollView) view.findViewById(R.id.fragment_user_edit_lelang_barang_scroll);

        btnEditBarang = (Button) view.findViewById(R.id.fragment_user_edit_lelang_barang_jual_button);

        til_namaBarang = (TextInputLayout) view.findViewById(R.id.fragment_user_edit_lelang_barang_input_nama_barang);
        til_deskripsiBarang = (TextInputLayout) view.findViewById(R.id.fragment_user_edit_lelang_barang_input_deskripsi_barang);
        til_hargabarangawal = (TextInputLayout) view.findViewById(R.id.fragment_user_edit_lelang_barang_input_harga_awal);
        til_hargabarangtarget = (TextInputLayout) view.findViewById(R.id.fragment_user_edit_lelang_barang_input_harga_target);
    }
    private void initializeTextChangeListenerOnPrice() {
        editText_hargabarang_awal.addTextChangedListener(new NumberTextWatcher(editText_hargabarang_awal, til_hargabarangawal));
        editText_hargabarang_target.addTextChangedListener(new NumberTextWatcher(editText_hargabarang_target, til_hargabarangtarget));
    }
    private void initializeDateTimeOnClickListeners() {
        textView_tanggalmulai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectDate(textView_tanggalmulai);
            }
        });
        textView_tanggalselesai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectDate(textView_tanggalselesai);
            }
        });
        textView_jammulai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectTime(textView_jammulai);
            }
        });
        textView_jamselesai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectTime(textView_jamselesai);
            }
        });
    }
    private void setupAllInputValidation() {
        setupInputValidation(editText_namabarang, til_namaBarang);
        setupInputValidation(editText_deskripsibarang, til_deskripsiBarang);
    }
    private void setupInputValidation(EditText editText, final TextInputLayout textInputLayout) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equalsIgnoreCase("")) {
                    textInputLayout.setErrorEnabled(true);
                    textInputLayout.setError(ErrorMessage.EMPTY_ERROR_MESSAGE);
                }
                else {
                    textInputLayout.setErrorEnabled(false);
                    textInputLayout.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }
    private boolean validateInput() {
        if (!(til_namaBarang.isErrorEnabled() && til_deskripsiBarang.isErrorEnabled()
                && til_hargabarangawal.isErrorEnabled() && til_hargabarangtarget.isErrorEnabled())) {
            if (editText_namabarang.getText().toString().trim().equalsIgnoreCase("")) {
                til_namaBarang.setErrorEnabled(true);
                til_namaBarang.setError(ErrorMessage.EMPTY_ERROR_MESSAGE);
                scrollOnView(til_namaBarang, 0);
                return false;
            }
            if (editText_deskripsibarang.getText().toString().trim().equalsIgnoreCase("")) {
                til_deskripsiBarang.setErrorEnabled(true);
                til_deskripsiBarang.setError(ErrorMessage.EMPTY_ERROR_MESSAGE);
                scrollOnView(til_deskripsiBarang, 0);
                return false;
            }
            if (editText_hargabarang_awal.getText().toString().trim().equalsIgnoreCase("")) {
                til_hargabarangawal.setErrorEnabled(true);
                til_hargabarangawal.setError(ErrorMessage.EMPTY_ERROR_MESSAGE);
                scrollOnView(til_hargabarangawal, 0);
                return false;
            }
            if (editText_hargabarang_target.getText().toString().trim().equalsIgnoreCase("")) {
                til_hargabarangtarget.setErrorEnabled(true);
                til_hargabarangtarget.setError(ErrorMessage.EMPTY_ERROR_MESSAGE);
                scrollOnView(til_hargabarangtarget, 0);
                return false;
            }
            return true;
        }
        return false;
    }
    private boolean validateImageInput() {
        if (dataImageReceived.size()==1 && dataImageReceived.get(0).getBitmap()==null) {
            textView_imageWarning.setText(ErrorMessage.EMPTY_IMAGE_ERROR);
            textView_imageWarning.setVisibility(View.VISIBLE);
            scrollOnView(recyclerView_image, 0);
            return false;
        }
        else {
            textView_imageWarning.setVisibility(View.GONE);
            return true;
        }
    }
    private boolean validateDateInput() {
        String tanggalMulai = textView_tanggalmulai.getText().toString();
        String tanggalSelesai = textView_tanggalselesai.getText().toString();
        String jamMulai = textView_jammulai.getText().toString();
        String jamSelesai = textView_jamselesai.getText().toString();

        if (tanggalMulai.equalsIgnoreCase("DD-MM-YYYY") || tanggalSelesai.equalsIgnoreCase("DD-MM-YYYY")
                || jamMulai.equalsIgnoreCase("HH:MM") || jamSelesai.equalsIgnoreCase("HH:MM")) {
            textView_dateWarning.setVisibility(View.VISIBLE);
            textView_dateWarning.setText(ErrorMessage.EMPTY_DATE_ERROR);
            scrollOnView(textView_dateWarning, 1);
            return false;
        }
        else {
            textView_dateWarning.setVisibility(View.GONE);
            return true;
        }
    }
    private void scrollOnView(final View view, final int position) {
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                if (position == 0)
                    scrollView.smoothScrollTo(0, view.getTop());
                else scrollView.fullScroll(View.FOCUS_DOWN);
            }
        });
    }
    private boolean validatePriceInput() {
        String hargamulai = editText_hargabarang_awal.getText().toString().trim().replaceAll("[^0-9]","");
        String hargaselesai = editText_hargabarang_target.getText().toString().trim().replaceAll("[^0-9]","");
        long hargamulailong = Long.parseLong(hargamulai);
        long hargaselesailong = Long.parseLong(hargaselesai);
        if (hargamulailong > hargaselesailong) {
            til_hargabarangtarget.setErrorEnabled(true);
            til_hargabarangtarget.setError(ErrorMessage.ERROR_HARGA_TARGET);
            return false;
        }
        return true;
    }
    private void removeImageError() {
        textView_imageWarning.setVisibility(View.GONE);
    }
    private void removeDateError() {
        textView_dateWarning.setVisibility(View.GONE);
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
        //CHINESE BUG FIXED --hopefully
        if (dataImageReceived.size() > 0) {
            INITIAL_UNIQUE_ID_IMAGE = dataImageReceived.get(0).getUniqueIDImage();
            //DEBUG CHINESE BUG
            printInitiateUniqueID();
        }
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
                Log.v("Response image", response);
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
                if (!dataImageReceived.get(position).isMainImage() &&
                        (dataImageReceived.get(position).getIdImage() != null || dataImageReceived.get(position).getUniqueIDImage() != null)) {
                    chooseMainImagePopupMenu(view, position);
                    showPopupMenu();
                }
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
                        //DEBUG CHINESE BUG
                        printChangedUniqueID(dataImageReceived.get(MAIN_IMAGE_INDEX).getUniqueIDImage());
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
        String idcategory = dataBarangReceived.get(0).getIdkategori();
        String deskripsibarang = dataBarangReceived.get(0).getDeskripsibarang();
        String hargabarangawal = PriceFormatter.formatPrice(dataBarangReceived.get(0).getHargaawal());
        String hargabarangtarget = PriceFormatter.formatPrice(dataBarangReceived.get(0).getHargatarget());
        String tanggalmulai = dataBarangReceived.get(0).getTanggalmulai();
        String tanggalselesai = dataBarangReceived.get(0).getTanggalselesai();
        String jammulai = dataBarangReceived.get(0).getJammulai();
        String jamselesai = dataBarangReceived.get(0).getJamselesai();
        editText_namabarang.setText(namabarang);
        if (!idcategory.equals("null")) {
            spinner_kategori.setSelection(Integer.parseInt(idcategory) - 1);
        }
        editText_deskripsibarang.setText(deskripsibarang);
        editText_hargabarang_awal.setText(hargabarangawal);
        editText_hargabarang_target.setText(hargabarangtarget);
        textView_tanggalmulai.setText(tanggalmulai);
        textView_tanggalselesai.setText(tanggalselesai);
        textView_jammulai.setText(jammulai);
        textView_jamselesai.setText(jamselesai);
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
        dataInput.put(KEY_STARTINGPRICE, editText_hargabarang_awal.getText().toString().trim().replaceAll("[^0-9]",""));
        dataInput.put(KEY_EXPECTEDPRICE, editText_hargabarang_target.getText().toString().trim().replaceAll("[^0-9]",""));
        dataInput.put(KEY_STARTTIME, dateTimeConverter.convertInputLocalTime(textView_tanggalmulai.getText().toString() + " "+ textView_jammulai.getText().toString()+ ":00"));
        dataInput.put(KEY_ENDTIME, dateTimeConverter.convertInputLocalTime(textView_tanggalselesai.getText().toString() + " "+ textView_jamselesai.getText().toString()+ ":00"));
        dataInput.put(KEY_IDCATEGORY, Integer.toString(spinner_kategori.getSelectedItemPosition() +1));
        dataInput.put(KEY_NAMACATEGORY, spinner_kategori.getSelectedItem().toString());
        dataInput.put(KEY_NAMAUSER, SessionManager.getSessionStatic().get(SessionManager.KEY_NAME));
        dataInput.put(KEY_USERDOMAIN, SessionManager.getSessionStatic().get(SessionManager.KEY_USERDOMAIN));
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
                    else if (res.equals("error")) {
                        if (respObject.getString("errcode").equals("wrong-time-start")) {
                            loadingDialog.dismiss();
                            textView_dateWarning.setText(ErrorMessage.WRONG_TIME_START_ERROR);
                            textView_dateWarning.setVisibility(View.VISIBLE);
                        }
                        else if (respObject.getString("errcode").equals("wrong-time-format")) {
                            loadingDialog.dismiss();
                            textView_dateWarning.setText(ErrorMessage.MISMATCH_DATE_ERROR);
                            textView_dateWarning.setVisibility(View.VISIBLE);
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
    private void selectDate(final TextView textView) {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                textView.setText(dateTimeConverter.convertUserDateInput(dayOfMonth + "-" + (month+1) + "-" + year));
                removeDateError();
            }
        }, year, month, day);
        datePickerDialog.show();
    }
    private void selectTime(final TextView textView) {
        Calendar calendar = Calendar.getInstance();
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                textView.setText(String.format("%02d:%02d", hourOfDay, minute));
                removeDateError();
            }
        }, hour, minute, false);
        timePickerDialog.show();
    }
    private void finishActivity() {
        loadingDialog.dismiss();
        Toast.makeText(getActivity(), "Info barang berhasil diperbaharui", Toast.LENGTH_SHORT).show();
        /*Intent intent = new Intent(getActivity(), UserGeraiActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        getActivity().startActivity(intent);*/
        getActivity().finish();
    }
    /* bug gambar 1 doang ga ke update main image nya kayaknya disini */
    private void updateMainImage()
    {
        if (INITIAL_UNIQUE_ID_IMAGE != null && dataImageReceived.size() > 1) {
            String NEW_MAIN_IMAGE = dataImageReceived.get(0).getUniqueIDImage();
            if (NEW_MAIN_IMAGE == null || !NEW_MAIN_IMAGE.equals(INITIAL_UNIQUE_ID_IMAGE)) {
                Log.v("NEW IMAGE", "NEW IMAGE COYYY");
                if (NEW_MAIN_IMAGE == null) {
                    editMainImageInServer("null");
                    Log.v("NEW IMAGE NULL", "NEW IMAGE NULL COYYY");
                }
                else {
                    editMainImageInServer(NEW_MAIN_IMAGE);
                    Log.v("NEW IMAGE NOT NULL", "NEW IMAGE NOT NULL COYYY");
                }
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

        Log.v("OLDIMAGEID", INITIAL_UNIQUE_ID_IMAGE);
        Log.v("NEWIMAGEID", newID);

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

    //* INI COBA DEBUG CHINESE BUG *//
    private void printInitiateUniqueID() {
        Log.v("INITIALUNIQUEIDIMAGE", INITIAL_UNIQUE_ID_IMAGE);
    }
    private void printChangedUniqueID(String newID) {
        Log.v("INITIALUNIQUEIDIMAGE", INITIAL_UNIQUE_ID_IMAGE);
        if (newID == null) {
            Log.e("NEWUNIQUEIDIMAGE", "NULL");
        }
        else {
            Log.e("NEWUNIQUEIDIMAGE", newID);
        }
    }
}
