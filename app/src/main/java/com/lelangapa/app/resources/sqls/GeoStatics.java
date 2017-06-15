package com.lelangapa.app.resources.sqls;

import java.util.ArrayList;

/**
 * Created by andre on 15/06/17.
 */

public class GeoStatics {
    private static GeoStatics geoStatics;
    private static ArrayList<Provinces> PROVINCES_LIST;
    private static ArrayList<Cities> CITIES_LIST;

    private GeoStatics() {
        PROVINCES_LIST = new ArrayList<>();
        CITIES_LIST = new ArrayList<>();
    }
    public static synchronized GeoStatics getInstance() {
        if (geoStatics == null) geoStatics = new GeoStatics();
        return geoStatics;
    }
    public ArrayList<Provinces> getProvincesList() {
        return PROVINCES_LIST;
    }
    public ArrayList<Cities> getCitiesList() {
        return CITIES_LIST;
    }
}
