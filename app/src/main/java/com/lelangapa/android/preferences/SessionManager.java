package com.lelangapa.android.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

/**
 * Created by Andre on 11/12/2016.
 */

public class SessionManager {
    private static HashMap<String, String> sessionMap;
    private Context context;
    private SharedPreferences sessionPreferences;
    private SharedPreferences.Editor editor;
    private static final String IS_LOGGED_IN = "isLoggedIn";
    private static final String SESSION_PREFERENCES = "sessionPreference";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_ID ="id";
    private static final String DUMMY_ID = "0";
    private static final String DUMMY_USERNAME = "dummy";
    private static final String DUMMY_NAME = "Dummy";
    private static final String DUMMY_EMAIL = "dummy@lelangkita.com";
    public SessionManager(Context context){
        this.context = context;
        sessionPreferences = this.context.getSharedPreferences(SESSION_PREFERENCES, Context.MODE_PRIVATE);
        editor = sessionPreferences.edit();
    }
    public void createSession(String id, String username, String name, String email){
        editor.putBoolean(IS_LOGGED_IN, true);
        editor.putString(KEY_ID, id);
        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_EMAIL, email);
        editor.commit();
    }
    public void editEmailSessionPreference(String email){
        editor = sessionPreferences.edit();
        editor.putString(KEY_EMAIL,email);
        editor.commit();
    }
    public HashMap<String, String> getSession(){
        if (sessionMap == null)
        {
            sessionMap = new HashMap<>();
        }
        sessionMap.put(KEY_ID, sessionPreferences.getString(KEY_ID, DUMMY_ID));
        sessionMap.put(KEY_USERNAME, sessionPreferences.getString(KEY_USERNAME, DUMMY_USERNAME));
        sessionMap.put(KEY_NAME, sessionPreferences.getString(KEY_NAME, DUMMY_NAME));
        sessionMap.put(KEY_EMAIL, sessionPreferences.getString(KEY_EMAIL, DUMMY_EMAIL));
        return sessionMap;
    }
    public void destroySession(){
        editor.clear();
        editor.commit();
    }
    public boolean isLoggedIn() {
        boolean isLogin = sessionPreferences.getBoolean(IS_LOGGED_IN, false);
        if (isLogin)return true;
        return false;
    }
    public String getKEY_NAME(){
        return KEY_NAME;
    }
    public String getKEY_EMAIL(){
        return KEY_EMAIL;
    }
    public String getKEY_ID() { return KEY_ID; }
}
