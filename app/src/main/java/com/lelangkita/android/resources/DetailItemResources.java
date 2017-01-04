package com.lelangkita.android.resources;

/**
 * Created by Andre on 12/17/2016.
 */

public class DetailItemResources {
    private String namabarang, namapengguna, hargaawal, hargatarget, statuslelang, urlgambarbarang, idbarang, deskripsibarang, tanggalmulai, tanggalselesai, jammulai, jamselesai;
    private String tanggaljammulai, tanggaljamselesai;
    private Long tanggaljammulai_ms, tanggaljamselesai_ms;
    private Integer itembidstatus;
    public DetailItemResources(){}
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
    public void setTanggaljammulai(String waktu)
    {
        tanggaljammulai = waktu;
    }
    public void setTanggaljamselesai(String waktu)
    {
        tanggaljamselesai = waktu;
    }
    public String getIdbarang() {
        return idbarang;
    }
    public void setTanggaljammulai_ms(Long waktu_ms)
    {
        tanggaljammulai_ms = waktu_ms;
    }
    public void setTanggaljamselesai_ms(Long waktu_ms)
    {
        tanggaljamselesai_ms = waktu_ms;
    }
    public void setItembidstatus(Integer bidstatus)
    {
        itembidstatus = bidstatus;
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
    public Long getTanggaljammulai_ms(){
        return tanggaljammulai_ms;
    }
    public Long getTanggaljamselesai_ms(){
        return tanggaljamselesai_ms;
    }
    public String getTanggaljammulai() {
        return tanggaljammulai;
    }
    public String getTanggaljamselesai() {
        return tanggaljamselesai;
    }
    public Integer getItembidstatus()
    {
        return itembidstatus;
    }
}
