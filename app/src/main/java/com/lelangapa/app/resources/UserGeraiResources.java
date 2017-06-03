package com.lelangapa.app.resources;

/**
 * Created by andre on 06/12/16.
 */

public class UserGeraiResources {
    private String namabarang, namapengguna, hargaawal, hargatarget, statuslelang, urlgambarbarang, idbarang, deskripsibarang, tanggalmulai, tanggalselesai, jammulai, jamselesai;
    private String idkategori, namakategori;
    public UserGeraiResources(){}
    public void setIdbarang(String id){
        idbarang = id;
    }
    public void setUrlgambarbarang(String url){
        urlgambarbarang = url;
    }
    public void setNamabarang(String nama){
        namabarang = nama;
    }
    public void setNamapengguna(String nama){
        namapengguna = nama;
    }
    public void setHargaawal(String harga){
        hargaawal = harga;
    }
    public void setHargatarget(String harga) { hargatarget = harga; }
    public void setStatuslelang(String status){
        statuslelang = status;
    }
    public void setDeskripsibarang(String deskripsi) { deskripsibarang = deskripsi; }
    public void setTanggalmulai(String tanggal) {
        tanggalmulai = tanggal;
    }
    public void setTanggalselesai(String tanggal){
        tanggalselesai = tanggal;
    }
    public void setJammulai(String jam){
        jammulai = jam;
    }
    public void setJamselesai(String jam){
        jamselesai = jam;
    }
    public String getIdbarang() {
        return idbarang;
    }
    public String getUrlgambarbarang(){
        return urlgambarbarang;
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
    public String getHargatarget() { return hargatarget; }
    public String getStatuslelang(){
        return statuslelang;
    }
    public String getDeskripsibarang() {
        return deskripsibarang;
    }
    public String getTanggalmulai(){
        return tanggalmulai;
    }
    public String getTanggalselesai(){
        return tanggalselesai;
    }
    public String getJammulai(){
        return jammulai;
    }
    public String getJamselesai(){
        return jamselesai;
    }
    public String getIdkategori() {
        return idkategori;
    }

    public void setIdkategori(String idkategori) {
        this.idkategori = idkategori;
    }

    public String getNamakategori() {
        return namakategori;
    }

    public void setNamakategori(String namakategori) {
        this.namakategori = namakategori;
    }
}
