package com.lelangapa.app.preferences.sqlites;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.lelangapa.app.resources.sqls.Cities;
import com.lelangapa.app.resources.sqls.GeoStatics;
import com.lelangapa.app.resources.sqls.Provinces;

import java.util.ArrayList;

/**
 * Created by andre on 01/05/17.
 */

public class SQLiteHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "lelangapa_sqlite";
    private static final String TABLE_HISTORY_KEYWORD = "history_keyword";
    private static final String TABLE_PROVINCE = "provinces";
    private static final String TABLE_CITIES = "cities";

    private static final String KEY_HISTORY_KEYWORD_ID = "id";
    private static final String KEY_HISTORY_KEYWORD_KEYWORD = "keyword";

    private static final String KEY_PROVINCE_ID ="id";
    private static final String KEY_PROVINCE_NAME = "name";

    private static final String KEY_CITIES_ID = "id";
    private static final String KEY_CITIES_NAME = "name";
    private static final String KEY_CITIES_ID_PROVINCE = "id_province";

    private static final String CREATE_HISTORY_KEYWORD_TABLE = "CREATE TABLE " + TABLE_HISTORY_KEYWORD + "("
            + KEY_HISTORY_KEYWORD_ID + " INTEGER PRIMARY KEY," + KEY_HISTORY_KEYWORD_KEYWORD + " TEXT)";
    private static final String CREATE_PROVINCE_TABLE = "CREATE TABLE " + TABLE_PROVINCE + "("
            + KEY_PROVINCE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_PROVINCE_NAME + " TEXT)";
    private static final String CREATE_CITIES_TABLE = "CREATE TABLE " + TABLE_CITIES + "("
            + KEY_CITIES_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_CITIES_NAME + " TEXT," +
            KEY_CITIES_ID_PROVINCE + " INTEGER)";

    public SQLiteHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_HISTORY_KEYWORD_TABLE);
        db.execSQL(CREATE_PROVINCE_TABLE);
        db.execSQL(CREATE_CITIES_TABLE);
        insertAllProvinces(db);
        insertAllCities(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //db.execSQL("DROP TABLE IF EXISTS " + TABLE_HISTORY_KEYWORD);
        switch (oldVersion) {
            case 1:
                db.execSQL(CREATE_PROVINCE_TABLE);
                db.execSQL(CREATE_CITIES_TABLE);
                insertAllProvinces(db);
                insertAllCities(db);
        }
        //onCreate(db);
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

    private void insertAllProvinces(SQLiteDatabase db) {
        String insertQuery = "INSERT INTO provinces(name) VALUES" +
                "  ('ACEH')," +
                "  ('SUMATERA UTARA')," +
                "  ('SUMATERA BARAT')," +
                "  ('RIAU')," +
                "  ('JAMBI')," +
                "  ('SUMATERA SELATAN')," +
                "  ('BENGKULU')," +
                "  ('LAMPUNG')," +
                "  ('KEPULAUAN BANGKA BELITUNG')," +
                "  ('KEPULAUAN RIAU')," +
                "  ('DKI JAKARTA')," +
                "  ('JAWA BARAT')," +
                "  ('JAWA TENGAH')," +
                "  ('DI YOGYAKARTA')," +
                "  ('JAWA TIMUR')," +
                "  ('BANTEN')," +
                "  ('BALI')," +
                "  ('NUSA TENGGARA BARAT')," +
                "  ('NUSA TENGGARA TIMUR')," +
                "  ('KALIMANTAN BARAT')," +
                "  ('KALIMANTAN TENGAH')," +
                "  ('KALIMANTAN SELATAN')," +
                "  ('KALIMANTAN TIMUR')," +
                "  ('KALIMANTAN UTARA')," +
                "  ('SULAWESI UTARA')," +
                "  ('SULAWESI TENGAH')," +
                "  ('SULAWESI SELATAN')," +
                "  ('SULAWESI TENGGARA')," +
                "  ('GORONTALO')," +
                "  ('SULAWESI BARAT')," +
                "  ('MALUKU')," +
                "  ('MALUKU UTARA')," +
                "  ('PAPUA BARAT')," +
                "  ('PAPUA')";
        db.execSQL(insertQuery);
    }
    private void insertAllCities(SQLiteDatabase db) {
        String insertQuery1 = "INSERT INTO cities(name, id_province) VALUES\n" +
                "  ('KABUPATEN SIMEULUE', '1'),\n" +
                "  ('KABUPATEN ACEH SINGKIL', '1'),\n" +
                "  ('KABUPATEN ACEH SELATAN', '1'),\n" +
                "  ('KABUPATEN ACEH TENGGARA', '1'),\n" +
                "  ('KABUPATEN ACEH TIMUR', '1'),\n" +
                "  ('KABUPATEN ACEH TENGAH', '1'),\n" +
                "  ('KABUPATEN ACEH BARAT', '1'),\n" +
                "  ('KABUPATEN ACEH BESAR', '1'),\n" +
                "  ('KABUPATEN PIDIE', '1'),\n" +
                "  ('KABUPATEN BIREUEN', '1'),\n" +
                "  ('KABUPATEN ACEH UTARA', '1'),\n" +
                "  ('KABUPATEN ACEH BARAT DAYA', '1'),\n" +
                "  ('KABUPATEN GAYO LUES', '1'),\n" +
                "  ('KABUPATEN ACEH TAMIANG', '1'),\n" +
                "  ('KABUPATEN NAGAN RAYA', '1'),\n" +
                "  ('KABUPATEN ACEH JAYA', '1'),\n" +
                "  ('KABUPATEN BENER MERIAH', '1'),\n" +
                "  ('KABUPATEN PIDIE JAYA', '1'),\n" +
                "  ('KOTA BANDA ACEH', '1'),\n" +
                "  ('KOTA SABANG', '1'),\n" +
                "  ('KOTA LANGSA', '1'),\n" +
                "  ('KOTA LHOKSEUMAWE', '1'),\n" +
                "  ('KOTA SUBULUSSALAM', '1')";
                String insertQuery2 =
                " INSERT INTO cities(name, id_province) VALUES\n" +
                " ('KABUPATEN NIAS', '2'),\n" +
                "  ('KABUPATEN MANDAILING NATAL', '2'),\n" +
                "  ('KABUPATEN TAPANULI SELATAN', '2'),\n" +
                "  ('KABUPATEN TAPANULI TENGAH', '2'),\n" +
                "  ('KABUPATEN TAPANULI UTARA', '2'),\n" +
                "  ('KABUPATEN TOBA SAMOSIR', '2'),\n" +
                "  ('KABUPATEN LABUHAN BATU', '2'),\n" +
                "  ('KABUPATEN ASAHAN', '2'),\n" +
                "  ('KABUPATEN SIMALUNGUN', '2'),\n" +
                "  ('KABUPATEN DAIRI', '2'),\n" +
                "  ('KABUPATEN KARO', '2'),\n" +
                "  ('KABUPATEN DELI SERDANG', '2'),\n" +
                "  ('KABUPATEN LANGKAT', '2'),\n" +
                "  ('KABUPATEN NIAS SELATAN', '2'),\n" +
                "  ('KABUPATEN HUMBANG HASUNDUTAN', '2'),\n" +
                "  ('KABUPATEN PAKPAK BHARAT', '2'),\n" +
                "  ('KABUPATEN SAMOSIR', '2'),\n" +
                "  ('KABUPATEN SERDANG BEDAGAI', '2'),\n" +
                "  ('KABUPATEN BATU BARA', '2'),\n" +
                "  ('KABUPATEN PADANG LAWAS UTARA', '2'),\n" +
                "  ('KABUPATEN PADANG LAWAS', '2'),\n" +
                "  ('KABUPATEN LABUHAN BATU SELATAN', '2'),\n" +
                "  ('KABUPATEN LABUHAN BATU UTARA', '2'),\n" +
                "  ('KABUPATEN NIAS UTARA', '2'),\n" +
                "  ('KABUPATEN NIAS BARAT', '2'),\n" +
                "  ('KOTA SIBOLGA', '2'),\n" +
                "  ('KOTA TANJUNG BALAI', '2'),\n" +
                "  ('KOTA PEMATANG SIANTAR', '2'),\n" +
                "  ('KOTA TEBING TINGGI', '2'),\n" +
                "  ('KOTA MEDAN', '2'),\n" +
                "  ('KOTA BINJAI', '2'),\n" +
                "  ('KOTA PADANGSIDIMPUAN', '2'),\n" +
                "  ('KOTA GUNUNGSITOLI', '2')";
        String insertQuery3=
                " INSERT INTO cities(name, id_province) VALUES\n" +
                " ('KABUPATEN KEPULAUAN MENTAWAI', '3'),\n" +
                "  ('KABUPATEN PESISIR SELATAN', '3'),\n" +
                "  ('KABUPATEN SOLOK', '3'),\n" +
                "  ('KABUPATEN SIJUNJUNG', '3'),\n" +
                "  ('KABUPATEN TANAH DATAR', '3'),\n" +
                "  ('KABUPATEN PADANG PARIAMAN', '3'),\n" +
                "  ('KABUPATEN AGAM', '3'),\n" +
                "  ('KABUPATEN LIMA PULUH KOTA', '3'),\n" +
                "  ('KABUPATEN PASAMAN', '3'),\n" +
                "  ('KABUPATEN SOLOK SELATAN', '3'),\n" +
                "  ('KABUPATEN DHARMASRAYA', '3'),\n" +
                "  ('KABUPATEN PASAMAN BARAT', '3'),\n" +
                "  ('KOTA PADANG', '3'),\n" +
                "  ('KOTA SOLOK', '3'),\n" +
                "  ('KOTA SAWAH LUNTO', '3'),\n" +
                "  ('KOTA PADANG PANJANG', '3'),\n" +
                "  ('KOTA BUKITTINGGI', '3'),\n" +
                "  ('KOTA PAYAKUMBUH', '3'),\n" +
                "  ('KOTA PARIAMAN', '3'),\n" +
                "  ('KABUPATEN KUANTAN SINGINGI', '4'),\n" +
                "  ('KABUPATEN INDRAGIRI HULU', '4'),\n" +
                "  ('KABUPATEN INDRAGIRI HILIR', '4'),\n" +
                "  ('KABUPATEN PELALAWAN', '4'),\n" +
                "  ('KABUPATEN SIAK', '4'),\n" +
                "  ('KABUPATEN KAMPAR', '4'),\n" +
                "  ('KABUPATEN ROKAN HULU', '4'),\n" +
                "  ('KABUPATEN BENGKALIS', '4'),\n" +
                "  ('KABUPATEN ROKAN HILIR', '4'),\n" +
                "  ('KABUPATEN KEPULAUAN MERANTI', '4'),\n" +
                "  ('KOTA PEKANBARU', '4'),\n" +
                "  ('KOTA DUMAI', '4'),\n" +
                "  ('KABUPATEN KERINCI', '5'),\n" +
                "  ('KABUPATEN MERANGIN', '5'),\n" +
                "  ('KABUPATEN SAROLANGUN', '5'),\n" +
                "  ('KABUPATEN BATANG HARI', '5'),\n" +
                "  ('KABUPATEN MUARO JAMBI', '5'),\n" +
                "  ('KABUPATEN TANJUNG JABUNG TIMUR', '5'),\n" +
                "  ('KABUPATEN TANJUNG JABUNG BARAT', '5'),\n" +
                "  ('KABUPATEN TEBO', '5'),\n" +
                "  ('KABUPATEN BUNGO', '5'),\n" +
                "  ('KOTA JAMBI', '5'),\n" +
                "  ('KOTA SUNGAI PENUH', '5'),\n" +
                "  ('KABUPATEN OGAN KOMERING ULU', '6'),\n" +
                "  ('KABUPATEN OGAN KOMERING ILIR', '6')";
        String insertQuery4 =
                "INSERT INTO cities (name, id_province) VALUES\n" +
                "  ('KABUPATEN MUARA ENIM', '6'),\n" +
                "  ('KABUPATEN LAHAT', '6'),\n" +
                "  ('KABUPATEN MUSI RAWAS', '6'),\n" +
                "  ('KABUPATEN MUSI BANYUASIN', '6'),\n" +
                "  ('KABUPATEN BANYU ASIN', '6'),\n" +
                "  ('KABUPATEN OGAN KOMERING ULU SELATAN', '6'),\n" +
                "  ('KABUPATEN OGAN KOMERING ULU TIMUR', '6'),\n" +
                "  ('KABUPATEN OGAN ILIR', '6'),\n" +
                "  ('KABUPATEN EMPAT LAWANG', '6'),\n" +
                "  ('KABUPATEN PENUKAL ABAB LEMATANG ILIR', '6'),\n" +
                "  ('KABUPATEN MUSI RAWAS UTARA', '6'),\n" +
                "  ('KOTA PALEMBANG', '6'),\n" +
                "  ('KOTA PRABUMULIH', '6'),\n" +
                "  ('KOTA PAGAR ALAM', '6'),\n" +
                "  ('KOTA LUBUKLINGGAU', '6'),\n" +
                "  ('KABUPATEN BENGKULU SELATAN', '7'),\n" +
                "  ('KABUPATEN REJANG LEBONG', '7'),\n" +
                "  ('KABUPATEN BENGKULU UTARA', '7'),\n" +
                "  ('KABUPATEN KAUR', '7'),\n" +
                "  ('KABUPATEN SELUMA', '7'),\n" +
                "  ('KABUPATEN MUKOMUKO', '7'),\n" +
                "  ('KABUPATEN LEBONG', '7'),\n" +
                "  ('KABUPATEN KEPAHIANG', '7'),\n" +
                "  ('KABUPATEN BENGKULU TENGAH', '7'),\n" +
                "  ('KOTA BENGKULU', '7'),\n" +
                "  ('KABUPATEN LAMPUNG BARAT', '8'),\n" +
                "  ('KABUPATEN TANGGAMUS', '8'),\n" +
                "  ('KABUPATEN LAMPUNG SELATAN', '8'),\n" +
                "  ('KABUPATEN LAMPUNG TIMUR', '8'),\n" +
                "  ('KABUPATEN LAMPUNG TENGAH', '8'),\n" +
                "  ('KABUPATEN LAMPUNG UTARA', '8'),\n" +
                "  ('KABUPATEN WAY KANAN', '8'),\n" +
                "  ('KABUPATEN TULANGBAWANG', '8'),\n" +
                "  ('KABUPATEN PESAWARAN', '8'),\n" +
                "  ('KABUPATEN PRINGSEWU', '8'),\n" +
                "  ('KABUPATEN MESUJI', '8'),\n" +
                "  ('KABUPATEN TULANG BAWANG BARAT', '8'),\n" +
                "  ('KABUPATEN PESISIR BARAT', '8'),\n" +
                "  ('KOTA BANDAR LAMPUNG', '8'),\n" +
                "  ('KOTA METRO', '8'),\n" +
                "  ('KABUPATEN BANGKA', '9'),\n" +
                "  ('KABUPATEN BELITUNG', '9'),\n" +
                "  ('KABUPATEN BANGKA BARAT', '9'),\n" +
                "  ('KABUPATEN BANGKA TENGAH', '9'),\n" +
                "  ('KABUPATEN BANGKA SELATAN', '9'),\n" +
                "  ('KABUPATEN BELITUNG TIMUR', '9'),\n" +
                "  ('KOTA PANGKAL PINANG', '9'),\n" +
                "  ('KABUPATEN KARIMUN', '10'),\n" +
                "  ('KABUPATEN BINTAN', '10'),\n" +
                "  ('KABUPATEN NATUNA', '10')";
        String insertQuery5 =
                "  INSERT INTO cities (name, id_province) VALUES\n" +
                "  ('KABUPATEN LINGGA', '10'),\n" +
                "  ('KABUPATEN KEPULAUAN ANAMBAS', '10'),\n" +
                "  ('KOTA BATAM', '10'),\n" +
                "  ('KOTA TANJUNG PINANG', '10'),\n" +
                "  ('KABUPATEN KEPULAUAN SERIBU', '11'),\n" +
                "  ('KOTA JAKARTA SELATAN', '11'),\n" +
                "  ('KOTA JAKARTA TIMUR', '11'),\n" +
                "  ('KOTA JAKARTA PUSAT', '11'),\n" +
                "  ('KOTA JAKARTA BARAT', '11'),\n" +
                "  ('KOTA JAKARTA UTARA', '11'),\n" +
                "  ('KABUPATEN BOGOR', '12'),\n" +
                "  ('KABUPATEN SUKABUMI', '12'),\n" +
                "  ('KABUPATEN CIANJUR', '12'),\n" +
                "  ('KABUPATEN BANDUNG', '12'),\n" +
                "  ('KABUPATEN GARUT', '12'),\n" +
                "  ('KABUPATEN TASIKMALAYA', '12'),\n" +
                "  ('KABUPATEN CIAMIS', '12'),\n" +
                "  ('KABUPATEN KUNINGAN', '12'),\n" +
                "  ('KABUPATEN CIREBON', '12'),\n" +
                "  ('KABUPATEN MAJALENGKA', '12'),\n" +
                "  ('KABUPATEN SUMEDANG', '12'),\n" +
                "  ('KABUPATEN INDRAMAYU', '12'),\n" +
                "  ('KABUPATEN SUBANG', '12'),\n" +
                "  ('KABUPATEN PURWAKARTA', '12'),\n" +
                "  ('KABUPATEN KARAWANG', '12'),\n" +
                "  ('KABUPATEN BEKASI', '12'),\n" +
                "  ('KABUPATEN BANDUNG BARAT', '12'),\n" +
                "  ('KABUPATEN PANGANDARAN', '12'),\n" +
                "  ('KOTA BOGOR', '12'),\n" +
                "  ('KOTA SUKABUMI', '12'),\n" +
                "  ('KOTA BANDUNG', '12'),\n" +
                "  ('KOTA CIREBON', '12'),\n" +
                "  ('KOTA BEKASI', '12'),\n" +
                "  ('KOTA DEPOK', '12'),\n" +
                "  ('KOTA CIMAHI', '12'),\n" +
                "  ('KOTA TASIKMALAYA', '12'),\n" +
                "  ('KOTA BANJAR', '12'),\n" +
                "  ('KABUPATEN CILACAP', '13'),\n" +
                "  ('KABUPATEN BANYUMAS', '13'),\n" +
                "  ('KABUPATEN PURBALINGGA', '13'),\n" +
                "  ('KABUPATEN BANJARNEGARA', '13'),\n" +
                "  ('KABUPATEN KEBUMEN', '13'),\n" +
                "  ('KABUPATEN PURWOREJO', '13'),\n" +
                "  ('KABUPATEN WONOSOBO', '13'),\n" +
                "  ('KABUPATEN MAGELANG', '13'),\n" +
                "  ('KABUPATEN BOYOLALI', '13'),\n" +
                "  ('KABUPATEN KLATEN', '13'),\n" +
                "  ('KABUPATEN SUKOHARJO', '13'),\n" +
                "  ('KABUPATEN WONOGIRI', '13'),\n" +
                "  ('KABUPATEN KARANGANYAR', '13')";
        String insertQuery6=
                "  INSERT INTO cities (name, id_province) VALUES\n" +
                "  ('KABUPATEN SRAGEN', '13'),\n" +
                "  ('KABUPATEN GROBOGAN', '13'),\n" +
                "  ('KABUPATEN BLORA', '13'),\n" +
                "  ('KABUPATEN REMBANG', '13'),\n" +
                "  ('KABUPATEN PATI', '13'),\n" +
                "  ('KABUPATEN KUDUS', '13'),\n" +
                "  ('KABUPATEN JEPARA', '13'),\n" +
                "  ('KABUPATEN DEMAK', '13'),\n" +
                "  ('KABUPATEN SEMARANG', '13'),\n" +
                "  ('KABUPATEN TEMANGGUNG', '13'),\n" +
                "  ('KABUPATEN KENDAL', '13'),\n" +
                "  ('KABUPATEN BATANG', '13'),\n" +
                "  ('KABUPATEN PEKALONGAN', '13'),\n" +
                "  ('KABUPATEN PEMALANG', '13'),\n" +
                "  ('KABUPATEN TEGAL', '13'),\n" +
                "  ('KABUPATEN BREBES', '13'),\n" +
                "  ('KOTA MAGELANG', '13'),\n" +
                "  ('KOTA SURAKARTA', '13'),\n" +
                "  ('KOTA SALATIGA', '13'),\n" +
                "  ('KOTA SEMARANG', '13'),\n" +
                "  ('KOTA PEKALONGAN', '13'),\n" +
                "  ('KOTA TEGAL', '13'),\n" +
                "  ('KABUPATEN KULON PROGO', '14'),\n" +
                "  ('KABUPATEN BANTUL', '14'),\n" +
                "  ('KABUPATEN GUNUNG KIDUL', '14'),\n" +
                "  ('KABUPATEN SLEMAN', '14'),\n" +
                "  ('KOTA YOGYAKARTA', '14'),\n" +
                "  ('KABUPATEN PACITAN', '15'),\n" +
                "  ('KABUPATEN PONOROGO', '15'),\n" +
                "  ('KABUPATEN TRENGGALEK', '15'),\n" +
                "  ('KABUPATEN TULUNGAGUNG', '15'),\n" +
                "  ('KABUPATEN BLITAR', '15'),\n" +
                "  ('KABUPATEN KEDIRI', '15'),\n" +
                "  ('KABUPATEN MALANG', '15'),\n" +
                "  ('KABUPATEN LUMAJANG', '15'),\n" +
                "  ('KABUPATEN JEMBER', '15'),\n" +
                "  ('KABUPATEN BANYUWANGI', '15'),\n" +
                "  ('KABUPATEN BONDOWOSO', '15'),\n" +
                "  ('KABUPATEN SITUBONDO', '15'),\n" +
                "  ('KABUPATEN PROBOLINGGO', '15'),\n" +
                "  ('KABUPATEN PASURUAN', '15'),\n" +
                "  ('KABUPATEN SIDOARJO', '15'),\n" +
                "  ('KABUPATEN MOJOKERTO', '15'),\n" +
                "  ('KABUPATEN JOMBANG', '15'),\n" +
                "  ('KABUPATEN NGANJUK', '15'),\n" +
                "  ('KABUPATEN MADIUN', '15'),\n" +
                "  ('KABUPATEN MAGETAN', '15'),\n" +
                "  ('KABUPATEN NGAWI', '15'),\n" +
                "  ('KABUPATEN BOJONEGORO', '15'),\n" +
                "  ('KABUPATEN TUBAN', '15')";
        String insertQuery7 =
                "  INSERT INTO cities (name, id_province) VALUES\n" +
                "  ('KABUPATEN LAMONGAN', '15'),\n" +
                "  ('KABUPATEN GRESIK', '15'),\n" +
                "  ('KABUPATEN BANGKALAN', '15'),\n" +
                "  ('KABUPATEN SAMPANG', '15'),\n" +
                "  ('KABUPATEN PAMEKASAN', '15'),\n" +
                "  ('KABUPATEN SUMENEP', '15'),\n" +
                "  ('KOTA KEDIRI', '15'),\n" +
                "  ('KOTA BLITAR', '15'),\n" +
                "  ('KOTA MALANG', '15'),\n" +
                "  ('KOTA PROBOLINGGO', '15'),\n" +
                "  ('KOTA PASURUAN', '15'),\n" +
                "  ('KOTA MOJOKERTO', '15'),\n" +
                "  ('KOTA MADIUN', '15'),\n" +
                "  ('KOTA SURABAYA', '15'),\n" +
                "  ('KOTA BATU', '15'),\n" +
                "  ('KABUPATEN PANDEGLANG', '16'),\n" +
                "  ('KABUPATEN LEBAK', '16'),\n" +
                "  ('KABUPATEN TANGERANG', '16'),\n" +
                "  ('KABUPATEN SERANG', '16'),\n" +
                "  ('KOTA TANGERANG', '16'),\n" +
                "  ('KOTA CILEGON', '16'),\n" +
                "  ('KOTA SERANG', '16'),\n" +
                "  ('KOTA TANGERANG SELATAN', '16'),\n" +
                "  ('KABUPATEN JEMBRANA', '17'),\n" +
                "  ('KABUPATEN TABANAN', '17'),\n" +
                "  ('KABUPATEN BADUNG', '17'),\n" +
                "  ('KABUPATEN GIANYAR', '17'),\n" +
                "  ('KABUPATEN KLUNGKUNG', '17'),\n" +
                "  ('KABUPATEN BANGLI', '17'),\n" +
                "  ('KABUPATEN KARANG ASEM', '17'),\n" +
                "  ('KABUPATEN BULELENG', '17'),\n" +
                "  ('KOTA DENPASAR', '17'),\n" +
                "  ('KABUPATEN LOMBOK BARAT', '18'),\n" +
                "  ('KABUPATEN LOMBOK TENGAH', '18'),\n" +
                "  ('KABUPATEN LOMBOK TIMUR', '18'),\n" +
                "  ('KABUPATEN SUMBAWA', '18'),\n" +
                "  ('KABUPATEN DOMPU', '18'),\n" +
                "  ('KABUPATEN BIMA', '18'),\n" +
                "  ('KABUPATEN SUMBAWA BARAT', '18'),\n" +
                "  ('KABUPATEN LOMBOK UTARA', '18'),\n" +
                "  ('KOTA MATARAM', '18'),\n" +
                "  ('KOTA BIMA', '18'),\n" +
                "  ('KABUPATEN SUMBA BARAT', '19'),\n" +
                "  ('KABUPATEN SUMBA TIMUR', '19'),\n" +
                "  ('KABUPATEN KUPANG', '19'),\n" +
                "  ('KABUPATEN TIMOR TENGAH SELATAN', '19'),\n" +
                "  ('KABUPATEN TIMOR TENGAH UTARA', '19'),\n" +
                "  ('KABUPATEN BELU', '19'),\n" +
                "  ('KABUPATEN ALOR', '19'),\n" +
                "  ('KABUPATEN LEMBATA', '19')";
        String insertQuery8 =
                "  INSERT INTO cities (name, id_province) VALUES\n" +
                "  ('KABUPATEN FLORES TIMUR', '19'),\n" +
                "  ('KABUPATEN SIKKA', '19'),\n" +
                "  ('KABUPATEN ENDE', '19'),\n" +
                "  ('KABUPATEN NGADA', '19'),\n" +
                "  ('KABUPATEN MANGGARAI', '19'),\n" +
                "  ('KABUPATEN ROTE NDAO', '19'),\n" +
                "  ('KABUPATEN MANGGARAI BARAT', '19'),\n" +
                "  ('KABUPATEN SUMBA TENGAH', '19'),\n" +
                "  ('KABUPATEN SUMBA BARAT DAYA', '19'),\n" +
                "  ('KABUPATEN NAGEKEO', '19'),\n" +
                "  ('KABUPATEN MANGGARAI TIMUR', '19'),\n" +
                "  ('KABUPATEN SABU RAIJUA', '19'),\n" +
                "  ('KABUPATEN MALAKA', '19'),\n" +
                "  ('KOTA KUPANG', '19'),\n" +
                "  ('KABUPATEN SAMBAS', '20'),\n" +
                "  ('KABUPATEN BENGKAYANG', '20'),\n" +
                "  ('KABUPATEN LANDAK', '20'),\n" +
                "  ('KABUPATEN MEMPAWAH', '20'),\n" +
                "  ('KABUPATEN SANGGAU', '20'),\n" +
                "  ('KABUPATEN KETAPANG', '20'),\n" +
                "  ('KABUPATEN SINTANG', '20'),\n" +
                "  ('KABUPATEN KAPUAS HULU', '20'),\n" +
                "  ('KABUPATEN SEKADAU', '20'),\n" +
                "  ('KABUPATEN MELAWI', '20'),\n" +
                "  ('KABUPATEN KAYONG UTARA', '20'),\n" +
                "  ('KABUPATEN KUBU RAYA', '20'),\n" +
                "  ('KOTA PONTIANAK', '20'),\n" +
                "  ('KOTA SINGKAWANG', '20'),\n" +
                "  ('KABUPATEN KOTAWARINGIN BARAT', '21'),\n" +
                "  ('KABUPATEN KOTAWARINGIN TIMUR', '21'),\n" +
                "  ('KABUPATEN KAPUAS', '21'),\n" +
                "  ('KABUPATEN BARITO SELATAN', '21'),\n" +
                "  ('KABUPATEN BARITO UTARA', '21'),\n" +
                "  ('KABUPATEN SUKAMARA', '21'),\n" +
                "  ('KABUPATEN LAMANDAU', '21'),\n" +
                "  ('KABUPATEN SERUYAN', '21'),\n" +
                "  ('KABUPATEN KATINGAN', '21'),\n" +
                "  ('KABUPATEN PULANG PISAU', '21'),\n" +
                "  ('KABUPATEN GUNUNG MAS', '21'),\n" +
                "  ('KABUPATEN BARITO TIMUR', '21'),\n" +
                "  ('KABUPATEN MURUNG RAYA', '21'),\n" +
                "  ('KOTA PALANGKA RAYA', '21'),\n" +
                "  ('KABUPATEN TANAH LAUT', '22'),\n" +
                "  ('KABUPATEN KOTA BARU', '22'),\n" +
                "  ('KABUPATEN BANJAR', '22'),\n" +
                "  ('KABUPATEN BARITO KUALA', '22'),\n" +
                "  ('KABUPATEN TAPIN', '22'),\n" +
                "  ('KABUPATEN HULU SUNGAI SELATAN', '22'),\n" +
                "  ('KABUPATEN HULU SUNGAI TENGAH', '22'),\n" +
                "  ('KABUPATEN HULU SUNGAI UTARA', '22')";
        String insertQuery9 =
                "INSERT INTO cities(name, id_province) VALUES\n" +
                "  ('KABUPATEN TABALONG', '22'),\n" +
                "  ('KABUPATEN TANAH BUMBU', '22'),\n" +
                "  ('KABUPATEN BALANGAN', '22'),\n" +
                "  ('KOTA BANJARMASIN', '22'),\n" +
                "  ('KOTA BANJAR BARU', '22'),\n" +
                "  ('KABUPATEN PASER', '23'),\n" +
                "  ('KABUPATEN KUTAI BARAT', '23'),\n" +
                "  ('KABUPATEN KUTAI KARTANEGARA', '23'),\n" +
                "  ('KABUPATEN KUTAI TIMUR', '23'),\n" +
                "  ('KABUPATEN BERAU', '23'),\n" +
                "  ('KABUPATEN PENAJAM PASER UTARA', '23'),\n" +
                "  ('KABUPATEN MAHAKAM HULU', '23'),\n" +
                "  ('KOTA BALIKPAPAN', '23'),\n" +
                "  ('KOTA SAMARINDA', '23'),\n" +
                "  ('KOTA BONTANG', '23'),\n" +
                "  ('KABUPATEN MALINAU', '24'),\n" +
                "  ('KABUPATEN BULUNGAN', '24'),\n" +
                "  ('KABUPATEN TANA TIDUNG', '24'),\n" +
                "  ('KABUPATEN NUNUKAN', '24'),\n" +
                "  ('KOTA TARAKAN', '24'),\n" +
                "  ('KABUPATEN BOLAANG MONGONDOW', '25'),\n" +
                "  ('KABUPATEN MINAHASA', '25'),\n" +
                "  ('KABUPATEN KEPULAUAN SANGIHE', '25'),\n" +
                "  ('KABUPATEN KEPULAUAN TALAUD', '25'),\n" +
                "  ('KABUPATEN MINAHASA SELATAN', '25'),\n" +
                "  ('KABUPATEN MINAHASA UTARA', '25'),\n" +
                "  ('KABUPATEN BOLAANG MONGONDOW UTARA', '25'),\n" +
                "  ('KABUPATEN SIAU TAGULANDANG BIARO', '25'),\n" +
                "  ('KABUPATEN MINAHASA TENGGARA', '25'),\n" +
                "  ('KABUPATEN BOLAANG MONGONDOW SELATAN', '25'),\n" +
                "  ('KABUPATEN BOLAANG MONGONDOW TIMUR', '25'),\n" +
                "  ('KOTA MANADO', '25'),\n" +
                "  ('KOTA BITUNG', '25'),\n" +
                "  ('KOTA TOMOHON', '25'),\n" +
                "  ('KOTA KOTAMOBAGU', '25'),\n" +
                "  ('KABUPATEN BANGGAI KEPULAUAN', '26'),\n" +
                "  ('KABUPATEN BANGGAI', '26'),\n" +
                "  ('KABUPATEN MOROWALI', '26'),\n" +
                "  ('KABUPATEN POSO', '26'),\n" +
                "  ('KABUPATEN DONGGALA', '26'),\n" +
                "  ('KABUPATEN TOLI-TOLI', '26'),\n" +
                "  ('KABUPATEN BUOL', '26'),\n" +
                "  ('KABUPATEN PARIGI MOUTONG', '26'),\n" +
                "  ('KABUPATEN TOJO UNA-UNA', '26'),\n" +
                "  ('KABUPATEN SIGI', '26'),\n" +
                "  ('KABUPATEN BANGGAI LAUT', '26'),\n" +
                "  ('KABUPATEN MOROWALI UTARA', '26'),\n" +
                "  ('KOTA PALU', '26'),\n" +
                "  ('KABUPATEN KEPULAUAN SELAYAR', '27'),\n" +
                "  ('KABUPATEN BULUKUMBA', '27')";
        String insertQuery10 =
                "  INSERT INTO cities(name, id_province) VALUES\n" +
                "  ('KABUPATEN BANTAENG', '27'),\n" +
                "  ('KABUPATEN JENEPONTO', '27'),\n" +
                "  ('KABUPATEN TAKALAR', '27'),\n" +
                "  ('KABUPATEN GOWA', '27'),\n" +
                "  ('KABUPATEN SINJAI', '27'),\n" +
                "  ('KABUPATEN MAROS', '27'),\n" +
                "  ('KABUPATEN PANGKAJENE DAN KEPULAUAN', '27'),\n" +
                "  ('KABUPATEN BARRU', '27'),\n" +
                "  ('KABUPATEN BONE', '27'),\n" +
                "  ('KABUPATEN SOPPENG', '27'),\n" +
                "  ('KABUPATEN WAJO', '27'),\n" +
                "  ('KABUPATEN SIDENRENG RAPPANG', '27'),\n" +
                "  ('KABUPATEN PINRANG', '27'),\n" +
                "  ('KABUPATEN ENREKANG', '27'),\n" +
                "  ('KABUPATEN LUWU', '27'),\n" +
                "  ('KABUPATEN TANA TORAJA', '27'),\n" +
                "  ('KABUPATEN LUWU UTARA', '27'),\n" +
                "  ('KABUPATEN LUWU TIMUR', '27'),\n" +
                "  ('KABUPATEN TORAJA UTARA', '27'),\n" +
                "  ('KOTA MAKASSAR', '27'),\n" +
                "  ('KOTA PAREPARE', '27'),\n" +
                "  ('KOTA PALOPO', '27'),\n" +
                "  ('KABUPATEN BUTON', '28'),\n" +
                "  ('KABUPATEN MUNA', '28'),\n" +
                "  ('KABUPATEN KONAWE', '28'),\n" +
                "  ('KABUPATEN KOLAKA', '28'),\n" +
                "  ('KABUPATEN KONAWE SELATAN', '28'),\n" +
                "  ('KABUPATEN BOMBANA', '28'),\n" +
                "  ('KABUPATEN WAKATOBI', '28'),\n" +
                "  ('KABUPATEN KOLAKA UTARA', '28'),\n" +
                "  ('KABUPATEN BUTON UTARA', '28'),\n" +
                "  ('KABUPATEN KONAWE UTARA', '28'),\n" +
                "  ('KABUPATEN KOLAKA TIMUR', '28'),\n" +
                "  ('KABUPATEN KONAWE KEPULAUAN', '28'),\n" +
                "  ('KABUPATEN MUNA BARAT', '28'),\n" +
                "  ('KABUPATEN BUTON TENGAH', '28'),\n" +
                "  ('KABUPATEN BUTON SELATAN', '28'),\n" +
                "  ('KOTA KENDARI', '28'),\n" +
                "  ('KOTA BAUBAU', '28'),\n" +
                "  ('KABUPATEN BOALEMO', '29'),\n" +
                "  ('KABUPATEN GORONTALO', '29'),\n" +
                "  ('KABUPATEN POHUWATO', '29'),\n" +
                "  ('KABUPATEN BONE BOLANGO', '29'),\n" +
                "  ('KABUPATEN GORONTALO UTARA', '29'),\n" +
                "  ('KOTA GORONTALO', '29'),\n" +
                "  ('KABUPATEN MAJENE', '30'),\n" +
                "  ('KABUPATEN POLEWALI MANDAR', '30'),\n" +
                "  ('KABUPATEN MAMASA', '30'),\n" +
                "  ('KABUPATEN MAMUJU', '30'),\n" +
                "  ('KABUPATEN MAMUJU UTARA', '30')";
        String insertQuery11 =
                "  INSERT INTO cities(name, id_province) VALUES\n" +
                "  ('KABUPATEN MAMUJU TENGAH', '30'),\n" +
                "  ('KABUPATEN MALUKU TENGGARA BARAT', '31'),\n" +
                "  ('KABUPATEN MALUKU TENGGARA', '31'),\n" +
                "  ('KABUPATEN MALUKU TENGAH', '31'),\n" +
                "  ('KABUPATEN BURU', '31'),\n" +
                "  ('KABUPATEN KEPULAUAN ARU', '31'),\n" +
                "  ('KABUPATEN SERAM BAGIAN BARAT', '31'),\n" +
                "  ('KABUPATEN SERAM BAGIAN TIMUR', '31'),\n" +
                "  ('KABUPATEN MALUKU BARAT DAYA', '31'),\n" +
                "  ('KABUPATEN BURU SELATAN', '31'),\n" +
                "  ('KOTA AMBON', '31'),\n" +
                "  ('KOTA TUAL', '31'),\n" +
                "  ('KABUPATEN HALMAHERA BARAT', '32'),\n" +
                "  ('KABUPATEN HALMAHERA TENGAH', '32'),\n" +
                "  ('KABUPATEN KEPULAUAN SULA', '32'),\n" +
                "  ('KABUPATEN HALMAHERA SELATAN', '32'),\n" +
                "  ('KABUPATEN HALMAHERA UTARA', '32'),\n" +
                "  ('KABUPATEN HALMAHERA TIMUR', '32'),\n" +
                "  ('KABUPATEN PULAU MOROTAI', '32'),\n" +
                "  ('KABUPATEN PULAU TALIABU', '32'),\n" +
                "  ('KOTA TERNATE', '32'),\n" +
                "  ('KOTA TIDORE KEPULAUAN', '32'),\n" +
                "  ('KABUPATEN FAKFAK', '33'),\n" +
                "  ('KABUPATEN KAIMANA', '33'),\n" +
                "  ('KABUPATEN TELUK WONDAMA', '33'),\n" +
                "  ('KABUPATEN TELUK BINTUNI', '33'),\n" +
                "  ('KABUPATEN MANOKWARI', '33'),\n" +
                "  ('KABUPATEN SORONG SELATAN', '33'),\n" +
                "  ('KABUPATEN SORONG', '33'),\n" +
                "  ('KABUPATEN RAJA AMPAT', '33'),\n" +
                "  ('KABUPATEN TAMBRAUW', '33'),\n" +
                "  ('KABUPATEN MAYBRAT', '33'),\n" +
                "  ('KABUPATEN MANOKWARI SELATAN', '33'),\n" +
                "  ('KABUPATEN PEGUNUNGAN ARFAK', '33'),\n" +
                "  ('KOTA SORONG', '33'),\n" +
                "  ('KABUPATEN MERAUKE', '34'),\n" +
                "  ('KABUPATEN JAYAWIJAYA', '34'),\n" +
                "  ('KABUPATEN JAYAPURA', '34'),\n" +
                "  ('KABUPATEN NABIRE', '34'),\n" +
                "  ('KABUPATEN KEPULAUAN YAPEN', '34'),\n" +
                "  ('KABUPATEN BIAK NUMFOR', '34'),\n" +
                "  ('KABUPATEN PANIAI', '34'),\n" +
                "  ('KABUPATEN PUNCAK JAYA', '34'),\n" +
                "  ('KABUPATEN MIMIKA', '34'),\n" +
                "  ('KABUPATEN BOVEN DIGOEL', '34'),\n" +
                "  ('KABUPATEN MAPPI', '34'),\n" +
                "  ('KABUPATEN ASMAT', '34'),\n" +
                "  ('KABUPATEN YAHUKIMO', '34'),\n" +
                "  ('KABUPATEN PEGUNUNGAN BINTANG', '34'),\n" +
                "  ('KABUPATEN TOLIKARA', '34')";
        String insertQuery12 =
                "  INSERT INTO cities(name, id_province) VALUES\n" +
                "  ('KABUPATEN SARMI', '34'),\n" +
                "  ('KABUPATEN KEEROM', '34'),\n" +
                "  ('KABUPATEN WAROPEN', '34'),\n" +
                "  ('KABUPATEN SUPIORI', '34'),\n" +
                "  ('KABUPATEN MAMBERAMO RAYA', '34'),\n" +
                "  ('KABUPATEN NDUGA', '34'),\n" +
                "  ('KABUPATEN LANNY JAYA', '34'),\n" +
                "  ('KABUPATEN MAMBERAMO TENGAH', '34'),\n" +
                "  ('KABUPATEN YALIMO', '34'),\n" +
                "  ('KABUPATEN PUNCAK', '34'),\n" +
                "  ('KABUPATEN DOGIYAI', '34'),\n" +
                "  ('KABUPATEN INTAN JAYA', '34'),\n" +
                "  ('KABUPATEN DEIYAI', '34'),\n" +
                "  ('KOTA JAYAPURA', '34')";

        db.execSQL(insertQuery1);
        db.execSQL(insertQuery2);
        db.execSQL(insertQuery3);
        db.execSQL(insertQuery4);
        db.execSQL(insertQuery5);
        db.execSQL(insertQuery6);
        db.execSQL(insertQuery7);
        db.execSQL(insertQuery8);
        db.execSQL(insertQuery9);
        db.execSQL(insertQuery10);
        db.execSQL(insertQuery11);
        db.execSQL(insertQuery12);
    }
    public void getAllProvinceList() {
        GeoStatics.getInstance().getProvincesList().clear();
        SQLiteDatabase db = this.getReadableDatabase();
        String getQuery = "SELECT * FROM provinces";
        Cursor cursor = db.rawQuery(getQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Provinces provinces = new Provinces();
                provinces.setProvinceID(cursor.getInt(cursor.getColumnIndex(KEY_PROVINCE_ID)));
                provinces.setProvinceName(cursor.getString(cursor.getColumnIndex(KEY_PROVINCE_NAME)));
                GeoStatics.getInstance().getProvincesList().add(provinces);
            } while(cursor.moveToNext());
        }
        cursor.close();
    }
    public void getAllCitiesList(int id_province) {
        GeoStatics.getInstance().getCitiesList().clear();
        SQLiteDatabase db = this.getReadableDatabase();
        String getQuery = "SELECT * FROM cities WHERE id_province = " + id_province;
        Cursor cursor = db.rawQuery(getQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Cities city = new Cities();
                city.setCityID(cursor.getInt(cursor.getColumnIndex(KEY_CITIES_ID)));
                city.setCityName(cursor.getString(cursor.getColumnIndex(KEY_CITIES_NAME)));
                GeoStatics.getInstance().getCitiesList().add(city);
            } while (cursor.moveToNext());
        }
        cursor.close();
    }
}
