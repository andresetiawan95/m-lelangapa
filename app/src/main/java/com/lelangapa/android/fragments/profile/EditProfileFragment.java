package com.lelangapa.android.fragments.profile;

import android.app.Activity;
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
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.lelangapa.android.R;
import com.lelangapa.android.activities.ProfileActivity;
import com.lelangapa.android.activities.cropper.GalleryUtil;
import com.lelangapa.android.apicalls.EditUserProfileAPI;
import com.lelangapa.android.apicalls.GetUserProfileAPI;
import com.lelangapa.android.apicalls.profile.AvatarAPI;
import com.lelangapa.android.apicalls.singleton.RequestController;
import com.lelangapa.android.asyncs.GetUserAvatarBMP;
import com.lelangapa.android.interfaces.DataReceiver;
import com.lelangapa.android.preferences.SessionManager;
import com.lelangapa.android.resources.AvatarResources;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;

/**
 * Created by Andre on 11/19/2016.
 */

public class EditProfileFragment extends Fragment {
    private EditText editText_editProfile_Nama, editText_editProfile_Email, editText_editProfile_Telepon;
    private String nama, telepon, email;
    private Button editProfile_simpan_btn;
    private SessionManager sessionManager;
    private DataReceiver getUserProfileData, uploadUserProfileData, whenAvatarUploaded, whenAvatarLoaded;
    private HashMap<String, String> session, userProfileData;
    private ProgressBar progressBar_infoakun, progressBar_infokontak, progressBar_avatar;

    private ImageView imageView_avatar;
    private AvatarResources avatar;

    private int CAMERA_REQUEST = 0;
    private int PICK_IMAGE_REQUEST = 1;
    private int CROP_IMAGE_REQUEST = 2;
    public static final int MY_TIMEOUT = 100000;
    public static final int MY_RETRY = 2;
    public static final float MY_BACKOFF_MULT = 2f;

    private static int OS_VERSION;
    private static final int PERMISSION_CAMERA_REQUEST = 10;
    private static final int PERMISSION_GALLERY_REQUEST = 11;

    private Intent cropIntent;
    private Uri cameraUri;

    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog alertDialog;

    private HashMap<String, String> imageData;

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_userprofile_editprofile_layout, container, false);
        initializeConstant();
        initializeViews(view);
        initializeDataReceiver();
        initializeViewVisibilities();
        setViewOnClickListener();
        initializeAlertDialogBuilder();
        sessionManager = new SessionManager(getActivity());
        session = sessionManager.getSession();
        return view;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        getUserProfileData();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            cropImageCamera();
        }
        else if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data!= null) {
            String imagePath = data.getStringExtra("picturePath");
            cropImage(imagePath);
        }
        else if (requestCode == CROP_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data!=null) {
            Bundle bundle = data.getExtras();
            Bitmap bitmap = bundle.getParcelable("data");
            avatar.setBitmap(bitmap);
            avatar.setImageChanged(true);
            imageView_avatar.setImageBitmap(avatar.getBitmap());
        }
    }

    private void initializeConstant()
    {
        OS_VERSION = Build.VERSION.SDK_INT;
        avatar = new AvatarResources();
    }
    private void initializeViews(View view)
    {
        editText_editProfile_Nama = (EditText) view.findViewById(R.id.fragment_usereditprofile_name);
        editText_editProfile_Telepon = (EditText) view.findViewById(R.id.fragment_usereditprofile_phone);
        editText_editProfile_Email = (EditText) view.findViewById(R.id.fragment_usereditprofile_email);
        editProfile_simpan_btn = (Button) view.findViewById(R.id.fragment_usereditprofile_simpan_button);
        progressBar_infoakun = (ProgressBar) view.findViewById(R.id.fragment_userprofile_editprofile_progress_bar_informasiakun);
        progressBar_infokontak = (ProgressBar) view.findViewById(R.id.fragment_userprofile_editprofile_progress_bar_informasikontak);
        imageView_avatar = (ImageView) view.findViewById(R.id.fragment_usereditprofile_avatar);
        progressBar_avatar = (ProgressBar) view.findViewById(R.id.fragment_usereditprofile_avatar_progress_bar);
    }
    private void initializeViewVisibilities()
    {
        progressBar_infoakun.setVisibility(View.VISIBLE);
        progressBar_infokontak.setVisibility(View.VISIBLE);
        editText_editProfile_Nama.setVisibility(View.INVISIBLE);
        editText_editProfile_Telepon.setVisibility(View.INVISIBLE);
        editText_editProfile_Email.setVisibility(View.INVISIBLE);
        imageView_avatar.setVisibility(View.GONE);
        imageView_avatar.setEnabled(false);
        progressBar_avatar.setVisibility(View.VISIBLE);
    }
    private void initializeDataReceiver()
    {
        whenAvatarLoaded = new DataReceiver() {
            @Override
            public void dataReceived(Object output) {
                Bitmap loadedAvatar = (Bitmap) output;
                avatar.setBitmap(loadedAvatar);
                progressBar_avatar.setVisibility(View.GONE);
                imageView_avatar.setEnabled(true);
                imageView_avatar.setVisibility(View.VISIBLE);
                imageView_avatar.setImageBitmap(loadedAvatar);
            }
        };
        getUserProfileData = new DataReceiver() {
            @Override
            public void dataReceived(Object output) {
                String response = output.toString();
                progressBar_infoakun.setVisibility(View.GONE);
                progressBar_infokontak.setVisibility(View.GONE);
                editText_editProfile_Nama.setVisibility(View.VISIBLE);
                editText_editProfile_Nama.setEnabled(false);
                editText_editProfile_Nama.setFocusable(false);
                editText_editProfile_Telepon.setVisibility(View.VISIBLE);
                editText_editProfile_Email.setVisibility(View.VISIBLE);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray responseData = jsonObject.getJSONArray("data");
                    JSONObject userDataObject = responseData.getJSONObject(0);
                    if (userDataObject!=null){
                        nama = userDataObject.getString("nama_user_return");
                        telepon = userDataObject.getString("phone_user_return");
                        email = userDataObject.getString("email_user_return");
                        editText_editProfile_Nama.setText(nama);
                        editText_editProfile_Telepon.setText(telepon);
                        editText_editProfile_Email.setText(email);

                        if (userDataObject.getString("avatar_url_return").equals("null")) {
                            imageView_avatar.setEnabled(true);
                            imageView_avatar.setVisibility(View.VISIBLE);
                            progressBar_avatar.setVisibility(View.GONE);
                        }
                        else {
                            //masukkan url ke asynctask class untuk ditampilkan\
                            GetUserAvatarBMP getUserAvatarBMP = new GetUserAvatarBMP(whenAvatarLoaded);
                            getUserAvatarBMP.execute("http://img-s7.lelangapa.com/" + userDataObject.getString("avatar_url_return"));
                        }
                    }
                    else {
                        Toast.makeText(getActivity(), "Tidak ada data", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        whenAvatarUploaded = new DataReceiver() {
            @Override
            public void dataReceived(Object output) {
                String response = output.toString();
                Log.v("upload-avatar", response);
                if (response.equals("success")) {
                    Toast.makeText(getActivity(), "Pengubahan profil telah berhasil", Toast.LENGTH_LONG).show();
                    /*Intent intent = new Intent (getActivity(), ProfileActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    getActivity().startActivity(intent);*/
                    getActivity().finish();
                }
                else {
                    Toast.makeText(getActivity(), "Upload gagal", Toast.LENGTH_SHORT).show();
                }
            }
        };
    }
    private void initializeAlertDialogBuilder()
    {
        alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle(R.string.PICK_IMAGE_FROM_TITLE)
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
        alertDialog = alertDialogBuilder.create();
    }
    private void showAlertDialog()
    {
        alertDialog.show();
    }
    private void setViewOnClickListener()
    {
        editProfile_simpan_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserProfileAPI();
            }
        });
        imageView_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialog();
            }
        });
    }
    private void chooseImageOnGallery()
    {
        Intent galleryIntent = new Intent(getActivity(), GalleryUtil.class);
        startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST);
    }
    private void chooseImageOnCamera()
    {
        Intent camIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        File file = new File(Environment.getExternalStorageDirectory(),
                "file" + String.valueOf(System.currentTimeMillis()) + ".jpg");
        cameraUri = Uri.fromFile(file);
        camIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, cameraUri);
        camIntent.putExtra("return-data", true);
        startActivityForResult(camIntent, CAMERA_REQUEST);
    }
    private void cropImage(String imagePath)
    {
        try {
            cropIntent = new Intent("com.android.camera.action.CROP");
            File f = new File(imagePath);
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
    private void cropImageCamera()
    {
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
    private String getStringImage(Bitmap bmp)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }
    private void putUserProfileData(String _userid, String _name, String _phone, String _email){
        userProfileData = new HashMap<>();
        userProfileData.put("userid", _userid);
        userProfileData.put("name", _name);
        userProfileData.put("phone", _phone);
        userProfileData.put("email", _email);
    }
    private void updateUserProfileAPI (){
        String editUserProfileNama = editText_editProfile_Nama.getText().toString();
        String editUserProfileTelepon = editText_editProfile_Telepon.getText().toString();
        final String editUserProfileEmail = editText_editProfile_Email.getText().toString();
        final String oldUserProfileEmail = session.get(sessionManager.getKEY_EMAIL());
        putUserProfileData(session.get(sessionManager.getKEY_ID()),editUserProfileNama, editUserProfileTelepon, editUserProfileEmail);
        uploadUserProfileData = new DataReceiver() {
            @Override
            public void dataReceived(Object output) {
                String response = output.toString();
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    String result = jsonResponse.getString("status");
                    if (result.equals("success")){
                        if (!(oldUserProfileEmail.equals(editUserProfileEmail))){
                            sessionManager.editEmailSessionPreference(editUserProfileEmail);
                        }
                        if (avatar.isImageChanged()) {
                            wrapImageAvatarData();
                            uploadImageToServer();
                        }
                        else {
                            Toast.makeText(getActivity(), "Pengubahan profil telah berhasil", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent (getActivity(), ProfileActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            getActivity().startActivity(intent);
                            getActivity().finish();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        editUserProfileData();
    }
    private void wrapImageAvatarData()
    {
        imageData = new HashMap<>();
        imageData.put("image", getStringImage(avatar.getBitmap()));
        imageData.put("id_user", SessionManager.getSessionStatic().get(SessionManager.KEY_ID));
        imageData.put("ext", "jpg");
    }
    private void getUserProfileData()
    {
        GetUserProfileAPI getUserProfileAPI = new GetUserProfileAPI(session.get(sessionManager.getKEY_ID()), getUserProfileData);
        RequestController.getInstance(getActivity()).addToRequestQueue(getUserProfileAPI);
    }
    private void editUserProfileData()
    {
        EditUserProfileAPI editUserProfileAPI = new EditUserProfileAPI(userProfileData, uploadUserProfileData);
        RequestController.getInstance(getActivity()).addToRequestQueue(editUserProfileAPI);
    }
    private void uploadImageToServer()
    {
        AvatarAPI.UploadAvatar uploadAvatarAPI = AvatarAPI.instanceUploadAvatar(imageData, whenAvatarUploaded);
        uploadAvatarAPI.setRetryPolicy(new DefaultRetryPolicy(MY_TIMEOUT, MY_RETRY, MY_BACKOFF_MULT));
        RequestController.getInstance(getActivity()).addToRequestQueue(uploadAvatarAPI);
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
