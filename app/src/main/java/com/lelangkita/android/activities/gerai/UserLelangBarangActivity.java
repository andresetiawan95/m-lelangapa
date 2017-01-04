package com.lelangkita.android.activities.gerai;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.lelangkita.android.R;
import com.lelangkita.android.activities.UserGeraiActivity;
import com.lelangkita.android.apicalls.gerai.SubmitBarangAPI;
import com.lelangkita.android.apicalls.gerai.SubmitGambarBarangAPI;
import com.lelangkita.android.fragments.gerai.UserLelangBarangFragment;
import com.lelangkita.android.interfaces.DataReceiver;
import com.lelangkita.android.interfaces.InputReceiver;
import com.lelangkita.android.preferences.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by andre on 01/12/16.
 */

public class UserLelangBarangActivity extends AppCompatActivity implements InputReceiver{
    private Toolbar toolbar;
    private Button lelangButton;
    private ProgressDialog loading;
    private RequestQueue queue;
    private SessionManager sessionManager;
    private HashMap<String, String> data;
    private HashMap<String, String> dataImage = new HashMap<>();
    public static final int MY_TIMEOUT = 10000;
    public static final int MY_RETRY = 2;
    public static final float MY_BACKOFF_MULT = 2f;
    private static final String KEY_IMAGE = "image";

    private String imageString;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lelang_barang);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Lelang Barang");
        sessionManager = new SessionManager(UserLelangBarangActivity.this);
        UserLelangBarangFragment lelangBarangFragment = new UserLelangBarangFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_user_lelang_barang_layout, lelangBarangFragment)
                .commit();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            super.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void inputReceived(HashMap<String, String> data) {
        this.data = data;
        /*final CharSequence[] datastream = this.data.toArray(new String[data.size()]);
        AlertDialog.Builder dialog = new AlertDialog.Builder(UserLelangBarangActivity.this);
        dialog.setTitle("Data lelang");
        dialog.setItems(datastream, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog object = dialog.create();
        object.show();*/
        imageString = data.get("image");
        this.data.remove("image");
        HashMap<String, String> userInfo = sessionManager.getSession();
        this.data.put("id_user", userInfo.get(sessionManager.getKEY_ID()));
        loading = ProgressDialog.show(UserLelangBarangActivity.this,"Sedang diproses..","Harap tunggu...",false,false);
        sendLelangData();
    }
    private void sendLelangData(){
        DataReceiver dataReceiver = new DataReceiver() {
            @Override
            public void dataReceived(Object output) {
                String response = output.toString();
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    String successResp = jsonResponse.getString("status");
                    if (successResp.equals("success")){
                        JSONArray respArray = jsonResponse.getJSONArray("id_item");
                        JSONObject resObject = respArray.getJSONObject(0);
                        String result = resObject.getString("id");
                        //data.put("itemid", result);
                        dataImage.put("image", imageString);
                        //Log.v("Imagestring", imageString);
                        dataImage.put("ext", "jpg");
                        //Log.v("Data id barang", result);
                        //data.put(KEY_IMAGE, )
                        dataImage.put("id_user", data.get("id_user"));
                        dataImage.put("itemid", result);
                        sendGambarBarangToServer();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        SubmitBarangAPI submitBarangAPI = new SubmitBarangAPI(data, dataReceiver);
        queue = Volley.newRequestQueue(UserLelangBarangActivity.this);
        queue.add(submitBarangAPI);
    }
    private void sendGambarBarangToServer(){
        Log.v("Masuk ke upload gambar", "Sudah masuk ke upload gambar");
        DataReceiver imageReceiver = new DataReceiver() {
            @Override
            public void dataReceived(Object output) {
                String response = output.toString();
                if (response.equals("success")){
                    loading.dismiss();
                    Toast.makeText(UserLelangBarangActivity.this, "Lelang telah disubmit", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(UserLelangBarangActivity.this, UserGeraiActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }
            }
        };
        SubmitGambarBarangAPI submitGambarBarangAPI = new SubmitGambarBarangAPI(dataImage, imageReceiver);
        submitGambarBarangAPI.setRetryPolicy(new DefaultRetryPolicy(MY_TIMEOUT, MY_RETRY, MY_BACKOFF_MULT));
        queue.add(submitGambarBarangAPI);
    }
}
