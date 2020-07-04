package com.zulfa.furnitureapp.Model;

public class Result {

    String id;
    String nama_barang;
    String harga;
    String deskripsi;
    String jenis_barang;
    String gambar;
    String ar;

    String order_id;
    String email;
    String tanggal_pesanan;
    String harga_pesanan;
    String jumlah_pesanan;
    String status;
    String picture;

    public Result(String id, String nama_barang, String harga, String deskripsi, String jenis_barang, String gambar, String ar, String order_id, String email, String tanggal_pesanan, String harga_pesanan, String jumlah_pesanan, String status, String picture) {
        this.id = id;
        this.nama_barang = nama_barang;
        this.harga = harga;
        this.deskripsi = deskripsi;
        this.jenis_barang = jenis_barang;
        this.gambar = gambar;
        this.ar = ar;
        this.order_id = order_id;
        this.email = email;
        this.tanggal_pesanan = tanggal_pesanan;
        this.harga_pesanan = harga_pesanan;
        this.jumlah_pesanan = jumlah_pesanan;
        this.status = status;
        this.picture = picture;
    }

    public String getId() {
        return id;
    }

    public String getNama_barang() {
        return nama_barang;
    }

    public String getHarga() {
        return harga;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public String getJenis_barang() {
        return jenis_barang;
    }

    public String getGambar() {
        return gambar;
    }

    public String getAr() {
        return ar;
    }

    public String getOrder_id() {
        return order_id;
    }

    public String getEmail() {
        return email;
    }

    public String getTanggal_pesanan() {
        return tanggal_pesanan;
    }

    public String getHarga_pesanan() {
        return harga_pesanan;
    }

    public String getJumlah_pesanan() {
        return jumlah_pesanan;
    }

    public String getStatus() {
        return status;
    }

    public String getPicture() {
        return picture;
    }
}
