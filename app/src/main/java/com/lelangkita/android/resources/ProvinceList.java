package com.lelangkita.android.resources;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Andre on 11/13/2016.
 * STILL CONFUSED
 */

public class ProvinceList {

    private Map<String, String> JawaTimur;
    private Map<String, String> JawaBarat;
    private Map<String, String> Banten;
    public ProvinceList() {
        JawaTimur = new HashMap<>();
        JawaBarat = new HashMap<>();
        Banten = new HashMap<>();
        JawaTimur.put("1", "Surabaya");
        JawaTimur.put("2", "Malang");
//        JawaTimur.put("3","Mojokerto");
        JawaBarat.put("3", "Bandung");
        Banten.put("4", "Serang");
    }
}
