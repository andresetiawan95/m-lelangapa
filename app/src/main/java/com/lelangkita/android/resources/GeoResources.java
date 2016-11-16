package com.lelangkita.android.resources;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Andre on 11/13/2016.
 * STILL CONFUSED
 */

public class GeoResources {
    private String[] Provinces = {"Jawa Timur", "Jawa Barat"};
    private String[] Cities = {"Surabaya", "Malang", "Bandung"};
    private HashMap<Integer, List<Integer> > Geo;
//    private HashMap<String, String> JawaTimur;
//    private HashMap<String, String> JawaBarat;
    private List<Integer> JawaTimur;
    private List<Integer> JawaBarat;
    public GeoResources(){
        Geo = new HashMap<>();
        JawaTimur = new ArrayList<>();
        JawaBarat = new ArrayList<>();
        JawaTimur.add(1);
        JawaTimur.add(2);
        JawaBarat.add(3);
        Geo.put(1, JawaTimur);
        Geo.put(2, JawaBarat);
    }
    public HashMap<Integer, List<Integer> > getGeo(){
        return Geo;
    }
    public List<Integer> getCityID(int position){
        List<Integer> cityID = Geo.get(position);
        return cityID;
    }
    public List<String> getCities(int position){
        List<Integer> geoCity = Geo.get(position);
        List<String> city = new ArrayList<>();
        for (int i = 0; i < geoCity.size(); i++){
            city.add(Cities[geoCity.get(i)-1]);
        }
        return city;
    }
    public List<String> getProvinces(){
        List<String> province = new ArrayList<>();
        for (String i : Provinces){
            province.add(i);
        }
        return province;
    }
}
