package com.lelangapa.app.fragments.gerai;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
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

import com.android.volley.DefaultRetryPolicy;
import com.lelangapa.app.R;
import com.lelangapa.app.activities.UserGeraiActivity;
import com.lelangapa.app.activities.cropper.GalleryUtil;
import com.lelangapa.app.adapters.MultipleImageNewItemAdapter;
import com.lelangapa.app.apicalls.gerai.SubmitBarangAPI;
import com.lelangapa.app.apicalls.gerai.SubmitGambarBarangAPI;
import com.lelangapa.app.apicalls.singleton.RequestController;
import com.lelangapa.app.interfaces.DataReceiver;
import com.lelangapa.app.interfaces.ImagePicker;
import com.lelangapa.app.interfaces.OnItemClickListener;
import com.lelangapa.app.listeners.RecyclerItemClickListener;
import com.lelangapa.app.preferences.SessionManager;
import com.lelangapa.app.resources.DateTimeConverter;
import com.lelangapa.app.resources.ErrorMessage;
import com.lelangapa.app.resources.ItemImageResources;
import com.lelangapa.app.resources.NumberTextWatcher;

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
    private EditText editText_namabarang, editText_deskripsibarang, editText_hargabarang_awal, editText_hargabarang_target;
    private TextInputLayout til_namaBarang, til_deskripsiBarang, til_hargabarangawal, til_hargabarangtarget;
    private TextView textView_tanggalmulai, textView_jammulai, textView_tanggalselesai, textView_jamselesai, textView_imageWarning, textView_dateWarning;
    private Spinner spinner_kategori;
    private ScrollView scrollView;
    //private ImageView gambarBarang;
    private Button button_lelangBarang;
    private ProgressDialog progressDialog;
    private AlertDialog.Builder alertDialogImageBuilder;
    private AlertDialog alertDialogImage;
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
    private static final String KEY_IDCATEGORY = "id_category";
    private static final String KEY_NAMAUSER = "nama_user";
    private static final String KEY_NAMACATEGORY = "nama_category";
    private static final String KEY_USERDOMAIN = "user_domain";
    private static final String KEY_IMAGE = "image";
    public static final int MY_TIMEOUT = 100000;
    public static final int MY_RETRY = 2;
    public static final float MY_BACKOFF_MULT = 2f;

    private int PICK_IMAGE_GALLERY_REQUEST = 1;
    private int PICK_IMAGE_CAMERA_REQUEST = 2;
    private int CROP_IMAGE_REQUEST = 3;
    private int IMAGE_ARRAY_INDEX = 0;
    private int IMAGE_ALREADY_UPLOADED_INDEX = 1;
    private static final int MAX_IMAGE= 8;
    private static int MAIN_IMAGE_INDEX;
    private String itemID;
    private Uri cameraUri;
    private static int OS_VERSION;
    private static final int PERMISSION_CAMERA_REQUEST = 10;
    private static final int PERMISSION_GALLERY_REQUEST = 11;

    private HashMap<String, String> data, dataImage;
    private ArrayList<ItemImageResources> listImages;
    private MultipleImageNewItemAdapter adapter;
    private RecyclerView recyclerView_imageList;
    private ImagePicker imagePicker;
    private DataReceiver imageReceiver;
    private DateTimeConverter dateTimeConverter;

    private Intent cropIntent;
    private PopupMenu popupMenu_image;

    public UserLelangBarangFragment()
    {
        initializeConstants();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_user_lelang_barang_layout, container, false);
        initializeSession();
        initializeViews(view);
        initializeAlertDialogImageBuilder();
        initializeTextChangeListenerOnPrice();
        initializeImageReceiver();
        setupAllInputValidation();
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
        dateTimeConverter = new DateTimeConverter();
        OS_VERSION = Build.VERSION.SDK_INT;
    }
    private void initializeImageReceiver()
    {
        imageReceiver = new DataReceiver() {
            @Override
            public void dataReceived(Object output) {
                String response = output.toString();
                if (response.equals("success")){
                    if (listImages.size() == MAX_IMAGE && listImages.get(MAX_IMAGE-1).getBitmap() != null) {
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
        button_lelangBarang = (Button) view.findViewById(R.id.fragment_user_lelang_barang_jual_button);
        editText_namabarang = (EditText) view.findViewById(R.id.fragment_user_lelang_barang_nama_barang);
        editText_deskripsibarang = (EditText) view.findViewById(R.id.fragment_user_lelang_barang_deskripsi_barang);
        editText_hargabarang_awal = (EditText) view.findViewById(R.id.fragment_user_lelang_barang_harga_awal_barang);
        editText_hargabarang_target = (EditText) view.findViewById(R.id.fragment_user_lelang_barang_harga_target_barang);
        textView_tanggalmulai = (TextView) view.findViewById(R.id.fragment_user_lelang_barang_tanggal_mulai);
        textView_tanggalselesai = (TextView) view.findViewById(R.id.fragment_user_lelang_barang_tanggal_selesai);
        textView_jammulai = (TextView) view.findViewById(R.id.fragment_user_lelang_barang_jam_mulai);
        textView_jamselesai = (TextView) view.findViewById(R.id.fragment_user_lelang_barang_jam_selesai);

        textView_imageWarning = (TextView) view.findViewById(R.id.fragment_user_lelang_barang_image_warning);
        textView_dateWarning = (TextView) view.findViewById(R.id.fragment_user_lelang_barang_jadwal_warning);
        scrollView = (ScrollView) view.findViewById(R.id.fragment_user_lelang_barang_scroll);
        spinner_kategori = (Spinner) view.findViewById(R.id.fragment_user_lelang_barang_kategori);
        //gambarBarang = (ImageView) view.findViewById(R.id.fragment_user_lelang_barang_gambar);
        recyclerView_imageList = (RecyclerView) view.findViewById(R.id.fragment_user_lelang_barang_image_recyclerview);

        til_namaBarang = (TextInputLayout) view.findViewById(R.id.fragment_user_lelang_barang_input_nama_barang);
        til_deskripsiBarang = (TextInputLayout) view.findViewById(R.id.fragment_user_lelang_barang_input_deskripsi_barang);
        til_hargabarangawal = (TextInputLayout) view.findViewById(R.id.fragment_user_lelang_barang_input_harga_awal);
        til_hargabarangtarget = (TextInputLayout) view.findViewById(R.id.fragment_user_lelang_barang_input_harga_target);
    }
    private void initializeTextChangeListenerOnPrice() {
        editText_hargabarang_awal.addTextChangedListener(new NumberTextWatcher(editText_hargabarang_awal, til_hargabarangawal));
        editText_hargabarang_target.addTextChangedListener(new NumberTextWatcher(editText_hargabarang_target, til_hargabarangtarget));
    }
    private void initializeAlertDialogImageBuilder() {
        alertDialogImageBuilder = new AlertDialog.Builder(getActivity());
        alertDialogImageBuilder.setTitle(R.string.PICK_IMAGE_FROM_TITLE)
                .setMessage(R.string.PICK_IMAGE_FROM_MESSAGE)
                .setPositiveButton(R.string.PICK_IMAGE_FROM_CAMERA_NEUTRALBUTTON, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (OS_VERSION >= Build.VERSION_CODES.M) {
                            askForPermission(android.Manifest.permission.CAMERA, PERMISSION_CAMERA_REQUEST);
                        }
                        else {
                            Log.v("NO-PERMISSION", "NO NEED TO REQUEST PERMISSION");
                            chooseImageOnCamera();
                        }
                    }
                })
                .setNegativeButton(R.string.PICK_IMAGE_FROM_GALLERY_NEUTRALBUTTON, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        chooseImageOnGallery();
                    }
                });
    }
    private void showAlertDialogImage() {
        alertDialogImage = alertDialogImageBuilder.create();
        alertDialogImage.show();
    }
    private void setupViews()
    {
        /*gambarBarang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImageOnGallery();
            }
        });*/
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
                selectHour(textView_jammulai);
            }
        });
        textView_jamselesai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectHour(textView_jamselesai);
            }
        });
        button_lelangBarang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.v("Masuk", "tombol ditekan dari fragment");
                if (validateInput() && validatePriceInput() && validateDateInput() && validateImageInput()) wrapFilledItemData();
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
        if (listImages.size()==1 && listImages.get(0).getBitmap()==null) {
            textView_imageWarning.setText(ErrorMessage.EMPTY_IMAGE_ERROR);
            textView_imageWarning.setVisibility(View.VISIBLE);
            scrollOnView(recyclerView_imageList, 0);
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
    private void setImagePicker()
    {
        imagePicker = new ImagePicker() {
            @Override
            public void picked(Integer indexOfArray) {
                IMAGE_ARRAY_INDEX = indexOfArray;
                showAlertDialogImage();
            }
        };
    }
    private void initializeAdapter()
    {
        ItemImageResources init = new ItemImageResources();
        listImages.add(init);
        adapter = new MultipleImageNewItemAdapter(getActivity(), listImages, imagePicker);
    }
    private void setRecyclerViewProperties()
    {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView_imageList.setLayoutManager(layoutManager);
        recyclerView_imageList.setAdapter(adapter);
        recyclerView_imageList.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), recyclerView_imageList, new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {}

            @Override
            public void onLongItemClick(View view, int position) {
                if (!listImages.get(position).isMainImage() && listImages.get(position).getIdImage() != null) {
                    chooseMainImagePopupMenu(view, position);
                    showPopupMenu();
                }
            }
        }));
    }
    private void wrapFilledItemData(){
        data.put("id_user", session.get(sessionManager.getKEY_ID()));
        data.put(KEY_NAMABARANG, editText_namabarang.getText().toString());
        data.put(KEY_DESCBARANG, editText_deskripsibarang.getText().toString());
        data.put(KEY_STARTINGPRICE, editText_hargabarang_awal.getText().toString().trim().replaceAll("[^0-9]",""));
        data.put(KEY_EXPECTEDPRICE, editText_hargabarang_target.getText().toString().trim().replaceAll("[^0-9]",""));
        data.put(KEY_STARTTIME, dateTimeConverter.convertInputLocalTime(textView_tanggalmulai.getText().toString() + " " + textView_jammulai.getText().toString() + ":00"));
        data.put(KEY_ENDTIME, dateTimeConverter.convertInputLocalTime(textView_tanggalselesai.getText().toString() + " " + textView_jamselesai.getText().toString() + ":00"));
        data.put(KEY_IDCATEGORY, Integer.toString(spinner_kategori.getSelectedItemPosition() +1));
        data.put(KEY_NAMACATEGORY, spinner_kategori.getSelectedItem().toString());
        data.put(KEY_NAMAUSER, session.get(SessionManager.KEY_NAME));
        data.put(KEY_USERDOMAIN, session.get(SessionManager.KEY_USERDOMAIN));
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
        dataImage.put("is_main_image", Boolean.toString(listImages.get(index-1).isMainImage()));
    }
    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }
    private void chooseImageOnGallery(){
        Intent galleryIntent = new Intent(getActivity(), GalleryUtil.class);
        startActivityForResult(galleryIntent, PICK_IMAGE_GALLERY_REQUEST);

        /*Intent intent = new Intent();
        intent.setType("image*//*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        Intent chooserIntent = Intent.createChooser(intent, "Select Picture");
        //chooserIntent.putExtra("lol", "LOLOLOL");
        startActivityForResult(chooserIntent, PICK_IMAGE_GALLERY_REQUEST);*/
    }
    private void chooseImageOnCamera() {
        Intent camIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        File file = new File(Environment.getExternalStorageDirectory(),
                "file" + String.valueOf(System.currentTimeMillis()) + ".jpg");
        cameraUri = Uri.fromFile(file);
        camIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, cameraUri);
        camIntent.putExtra("return-data", true);
        startActivityForResult(camIntent, PICK_IMAGE_CAMERA_REQUEST);
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
    private void cropCameraImage() {
        try {
            cropIntent = new Intent("com.android.camera.action.CROP");
            cropIntent.setDataAndType(cameraUri, "image/*");
            cropIntent.putExtra("crop", "true");
            cropIntent.putExtra("outputX", 180);
            cropIntent.putExtra("outputY", 180);
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            cropIntent.putExtra("scaleUpIfNeeded", true);
            cropIntent.putExtra("return-data", true);
            startActivityForResult(cropIntent, CROP_IMAGE_REQUEST);
        } catch (ActivityNotFoundException e) {
            String errorMessage = "Perangkat Anda Tidak Mendukung Crop";
            Toast toast = Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }
    private void chooseMainImagePopupMenu(View view, final int position)
    {
        popupMenu_image = new PopupMenu(getActivity(), view);
        popupMenu_image.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.set_main_image_popup_menu :
                        listImages.get(MAIN_IMAGE_INDEX).setMainImage(false);
                        listImages.get(position).setMainImage(true);

                        ItemImageResources temp1 = listImages.get(MAIN_IMAGE_INDEX);
                        ItemImageResources temp2 = listImages.get(position);
                        listImages.set(MAIN_IMAGE_INDEX, temp2);
                        listImages.set(position, temp1);

                        adapter.updateImageSet();
                        return true;
                    default:
                        return false;
                }
            }
        });
    }
    private void showPopupMenu()
    {
        popupMenu_image.inflate(R.menu.item_image_popup_menu);
        popupMenu_image.show();
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
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode == PICK_IMAGE_GALLERY_REQUEST && resultCode == RESULT_OK && data != null){
            String picturePath = data.getStringExtra("picturePath");
            cropImage(picturePath);
        }
        else if (requestCode == PICK_IMAGE_CAMERA_REQUEST && resultCode == RESULT_OK) {
            cropCameraImage();
        }
        else if (requestCode == CROP_IMAGE_REQUEST && resultCode == RESULT_OK) {
            if (data != null) {
                Bundle bundle = data.getExtras();
                bitmap = bundle.getParcelable("data");
                if (IMAGE_ARRAY_INDEX == listImages.size() -1 && listImages.size() < MAX_IMAGE) {
                    listImages.get(IMAGE_ARRAY_INDEX).setIdImage(Integer.toString(IMAGE_ARRAY_INDEX));
                    listImages.get(IMAGE_ARRAY_INDEX).setBitmap(bitmap);
                    if (listImages.size() == 1) {
                        //ketika gambar pertama dipilih, langsung jadi gambar utama
                        listImages.get(IMAGE_ARRAY_INDEX).setMainImage(true);
                        MAIN_IMAGE_INDEX = IMAGE_ARRAY_INDEX;
                    }
                    ItemImageResources newinit = new ItemImageResources();
                    listImages.add(newinit);

                    adapter.updateImageSet();
                }
                else {
                    listImages.get(IMAGE_ARRAY_INDEX).setIdImage(Integer.toString(IMAGE_ARRAY_INDEX));
                    listImages.get(IMAGE_ARRAY_INDEX).setBitmap(bitmap);
                    adapter.updateImageSet();
                }
                removeImageError();
            }
        }
    }
    private void selectDate(final TextView textView){
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
    private void selectHour(final TextView textView){
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
                    else if (successResp.equals("error")) {
                        if (jsonResponse.getString("errcode").equals("wrong-time-start")) {
                            progressDialog.dismiss();
                            textView_dateWarning.setText(ErrorMessage.WRONG_TIME_START_ERROR);
                            textView_dateWarning.setVisibility(View.VISIBLE);
                        }
                        else if (jsonResponse.getString("errcode").equals("wrong-time-format")) {
                            progressDialog.dismiss();
                            textView_dateWarning.setText(ErrorMessage.MISMATCH_DATE_ERROR);
                            textView_dateWarning.setVisibility(View.VISIBLE);
                        }
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
    /*PERMISSION REQUEST FOR ANDROID 6.0 ABOVE*/
    private void askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(getActivity(), permission) != PackageManager.PERMISSION_GRANTED) {
            //system will show alert
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), permission)) {
                //ini akan ditampilkan ketika pengguna sebelumnya sudah men-denied permission
                ActivityCompat.requestPermissions(getActivity(), new String[]{permission}, requestCode);
            }
            else {
                ActivityCompat.requestPermissions(getActivity(), new String[]{permission}, requestCode);
            }
        }
        else {
            if (requestCode == PERMISSION_CAMERA_REQUEST) {
                chooseImageOnCamera();
            }
            else if (requestCode == PERMISSION_GALLERY_REQUEST) {
                chooseImageOnGallery();
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permission, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permission, grantResults);
        if (ActivityCompat.checkSelfPermission(getActivity(), permission[0]) == PackageManager.PERMISSION_GRANTED) {
            switch (requestCode) {
                case PERMISSION_CAMERA_REQUEST :
                    chooseImageOnCamera();
                    break;
                case PERMISSION_GALLERY_REQUEST :
                    chooseImageOnGallery();
                    break;
            }
        }
        else {
            Toast.makeText(getActivity(), "Permission tidak diberikan", Toast.LENGTH_SHORT).show();
        }
    }
}
