package com.lelangapa.app.preferences.sqlites;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by andre on 01/05/17.
 */

public class SQLiteHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "lelangapa_sqlite";
    private static final String TABLE_HISTORY_KEYWORD = "history_keyword";

    private static final String KEY_HISTORY_KEYWORD_ID = "id";
    private static final String KEY_HISTORY_KEYWORD_KEYWORD = "keyword";

    public SQLiteHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_HISTORY_KEYWORD + "("
                + KEY_HISTORY_KEYWORD_ID + " INTEGER PRIMARY KEY," + KEY_HISTORY_KEYWORD_KEYWORD + " TEXT)";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HISTORY_KEYWORD);
        onCreate(db);
    }

    public void addNewKeyword(String keyword) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_HISTORY_KEYWORD_KEYWORD, keyword);

        db.insert(TABLE_HISTORY_KEYWORD, null, values);
        db.close();
    }

    public ArrayList<String> getAllTopFiveKeywords() {
        ArrayList<String> keywordList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String getKeywordQuery = "SELECT * FROM " + TABLE_HISTORY_KEYWORD + " ORDER BY " +
                KEY_HISTORY_KEYWORD_ID + " DESC LIMIT 5";
        Cursor cursor = db.rawQuery(getKeywordQuery, null);

        if (cursor.moveToFirst()) {
            do {
                keywordList.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return keywordList;
    }
}
