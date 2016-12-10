package com.lelangkita.android.resources;

/**
 * Created by andre on 06/12/16.
 */

public class UserGeraiResources {
    private String namabarang, namapengguna, hargaawal, statuslelang;
    public UserGeraiResources(){}
    public void setNamabarang(String nama){
        namabarang = nama;
    }
    public void setNamapengguna(String nama){
        namapengguna = nama;
    }
    public void setHargaawal(String harga){
        hargaawal = harga;
    }
    public void setStatuslelang(String status){
        statuslelang = status;
    }
    public String getNamabarang(){
        return namabarang;
    }
    public String getNamapengguna(){
        return namapengguna;
    }
    public String getHargaawal(){
        return hargaawal;
    }
    public String getStatuslelang(){
        return statuslelang;
    }
}
