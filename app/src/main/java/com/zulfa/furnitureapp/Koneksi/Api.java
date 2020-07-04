package com.zulfa.furnitureapp.Koneksi;

import com.zulfa.furnitureapp.Model.Result;
import com.zulfa.furnitureapp.Model.Value;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface Api {

    @FormUrlEncoded
    @POST("register.php")
    Call<Value> daftar(@Field("nama") String nama,
                       @Field("email") String email,
                       @Field("password") String password);

    @FormUrlEncoded
    @POST("login.php")
    Call<String> login(@Field("email") String email,
                              @Field("password") String password);

    @GET("list_produk.php")
    Call<Value> view(@Query("jenis_barang") String jenis_barang);

    @GET("get_produk.php")
    Call<List<Result>> detail(@Query("nama_barang") String nama_barang);

    @FormUrlEncoded
    @POST("refresh.php")
    Call<String> refresh(@Field("email") String email);

    @FormUrlEncoded
    @POST("updateuser.php")
    Call<Value> ubah(@Field("email") String email,
                     @Field("nama") String nama,
                     @Field("alamat") String alamat,
                     @Field("jenis_kelamin") String jeniskelamin,
                     @Field("tanggal_lahir") String tanggallahir,
                     @Field("no_telefon") String notelefon,
                     @Field("kode_pos") String kodepos,
                     @Field("kota") String kota,
                     @Field("picture") String picture);

    @FormUrlEncoded
    @POST("update_status.php")
    Call<Value> status(@Field("id") String id,
                       @Field("status") String status);

    @GET("get_orderan.php")
    Call<Value> vieworderan(@Query("email") String email,
                     @Query("status") String status);

    @FormUrlEncoded
    @POST("pesanan.php")
    Call<Value> order(@Field("email") String email,
                      @Field("order_id") String order_id,
                      @Field("nama_barang") String nama_barang,
                      @Field("tanggal_pesanan") String tanggal_pesanan,
                      @Field("harga_pesanan") String harga_pesanan,
                      @Field("jumlah_pesanan") String jumlah_pesanan,
                      @Field("status") String status,
                      @Field("picture") String picture);
}
