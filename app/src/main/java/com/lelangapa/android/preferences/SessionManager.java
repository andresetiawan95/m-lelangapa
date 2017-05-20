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
    private static SharedPreferences sessionPreferences;
    private static SharedPreferences.Editor editor;
    private static final String IS_LOGGED_IN = "isLoggedIn";
    private static final String SESSION_PREFERENCES = "sessionPreference";
    private static final String KEY_USERNAME = "username";
    public static final String KEY_NAME = "name";
    private static final String KEY_EMAIL = "email";
    public static final String KEY_USERDOMAIN = "domain";
    public static final String KEY_ID ="id";
    private static final String DUMMY_ID = "0";
    private static final String DUMMY_USERNAME = "dummy";
    private static final String DUMMY_NAME = "Dummy";
    private static final String DUMMY_EMAIL = "dummy@lelangkita.com";
    private static final String DUMMY_USERDOMAIN = "null";

    public static final String KEY_TOKEN = "token";
    public static final String DUMMY_TOKEN = "no_token";
    public SessionManager(Context context){
        this.context = context;
        sessionPreferences = this.context.getSharedPreferences(SESSION_PREFERENCES, Context.MODE_PRIVATE);
        editor = sessionPreferences.edit();
    }
    public void createSession(String id, String username, String name, String email, String domain, String token){
        editor.putBoolean(IS_LOGGED_IN, true);
        editor.putString(KEY_ID, id);
        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_USERDOMAIN, domain);
        editor.putString(KEY_TOKEN, token);
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
        sessionMap.put(KEY_USERDOMAIN, sessionPreferences.getString(KEY_USERDOMAIN, DUMMY_USERDOMAIN));
        sessionMap.put(KEY_TOKEN, sessionPreferences.getString(KEY_TOKEN, DUMMY_TOKEN));
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
    public static HashMap<String, String> getSessionStatic()
    {
        if (sessionMap == null)
        {
            sessionMap = new HashMap<>();
        }
        sessionMap.put(KEY_ID, sessionPreferences.getString(KEY_ID, DUMMY_ID));
        sessionMap.put(KEY_USERNAME, sessionPreferences.getString(KEY_USERNAME, DUMMY_USERNAME));
        sessionMap.put(KEY_NAME, sessionPreferences.getString(KEY_NAME, DUMMY_NAME));
        sessionMap.put(KEY_EMAIL, sessionPreferences.getString(KEY_EMAIL, DUMMY_EMAIL));
        sessionMap.put(KEY_USERDOMAIN, sessionPreferences.getString(KEY_USERDOMAIN, DUMMY_USERDOMAIN));
        sessionMap.put(KEY_TOKEN, sessionPreferences.getString(KEY_TOKEN, DUMMY_TOKEN));
        return sessionMap;
    }
    public String getKEY_NAME(){
        return KEY_NAME;
    }
    public String getKEY_EMAIL(){
        return KEY_EMAIL;
    }
    public String getKEY_ID() { return KEY_ID; }

    /* SOME NEW STATIC METHODS */
    public static boolean isLoggedInStatic() {
        return sessionPreferences.getBoolean(IS_LOGGED_IN, false);
    }
    public static String getUserTokenStatic() {
        return sessionPreferences.getString(KEY_TOKEN, DUMMY_TOKEN);
    }
}
