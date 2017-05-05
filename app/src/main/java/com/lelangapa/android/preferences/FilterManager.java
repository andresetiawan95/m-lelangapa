package com.lelangapa.android.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

/**
 * Created by andre on 05/05/17.
 */

public class FilterManager {
    private static HashMap<String, String> filterMap;
    private static SharedPreferences filterPreferences;
    private static SharedPreferences.Editor editor;

    private static final String FILTER_PREFERENCE_KEY = "filter_preference";
    public static final String KEY_ID_CATEGORY = "id_category";
    private static final String DUMMY_CATEGORY = "0";

    public static void initialize(Context context) {
        if (filterPreferences == null)
            filterPreferences = context.getSharedPreferences(FILTER_PREFERENCE_KEY, Context.MODE_PRIVATE);
    }

    public static void insertFilter(String category) {
        editor = filterPreferences.edit();
        editor.putString(KEY_ID_CATEGORY, category);
        editor.apply();
    }

    public static HashMap<String, String> getFilter() {
        if (filterMap==null) filterMap = new HashMap<>();
        filterMap.put(KEY_ID_CATEGORY, filterPreferences.getString(KEY_ID_CATEGORY, DUMMY_CATEGORY));
        return filterMap;
    }

    public static void destoryFilter() {
        editor = filterPreferences.edit();
        editor.clear();
        editor.apply();
    }
}
