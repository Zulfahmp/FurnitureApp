package com.zulfa.furnitureapp.Login;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceHelper {

    private final String INTRO = "intro";
    private final String ID = "id";
    private final String NAME = "nama";
    private final String ALAMAT = "alamat";
    private final String JENIS_KELAMIN = "jenis_kelamin";
    private final String TANGGAL_LAHIR = "tanggal_lahir";
    private final String PICTURE = "picture";
    private final String NO_TELEFON = "no_telefon";
    private final String KODE_POS = "kode_pos";
    private final String KOTA = "kota";
    private final String EMAIL = "email";
    private SharedPreferences app_prefs;
    private Context context;

    public PreferenceHelper(Context context) {
        app_prefs = context.getSharedPreferences("shared",
                Context.MODE_PRIVATE);
        this.context = context;
    }

    public void putIsLogin(boolean loginorout) {
        SharedPreferences.Editor edit = app_prefs.edit();
        edit.putBoolean(INTRO, loginorout);
        edit.commit();
    }
    public boolean getIsLogin() {
        return app_prefs.getBoolean(INTRO, false);
    }

    public void putId(String loginorout) {
        SharedPreferences.Editor edit = app_prefs.edit();
        edit.putString(ID, loginorout);
        edit.commit();
    }
    public String getId() {
        return app_prefs.getString(ID, "");
    }

    public void putNama(String loginorout) {
        SharedPreferences.Editor edit = app_prefs.edit();
        edit.putString(NAME, loginorout);
        edit.commit();
    }
    public String getNama() {
        return app_prefs.getString(NAME, "");
    }

    public void putEmail(String loginorout) {
        SharedPreferences.Editor edit = app_prefs.edit();
        edit.putString(EMAIL, loginorout);
        edit.commit();
    }
    public String getEmail() {
        return app_prefs.getString(EMAIL, "");
    }

    public void putAlamat(String loginorout) {
        SharedPreferences.Editor edit = app_prefs.edit();
        edit.putString(ALAMAT, loginorout);
        edit.commit();
    }
    public String getAlamat() {
        return app_prefs.getString(ALAMAT, "");
    }

    public void putJenis_Kelamin(String loginorout) {
        SharedPreferences.Editor edit = app_prefs.edit();
        edit.putString(JENIS_KELAMIN, loginorout);
        edit.commit();
    }
    public String getJENIS_KELAMIN() {
        return app_prefs.getString(JENIS_KELAMIN,"");
    }

    public void putTanggal_lahir(String loginorout) {
        SharedPreferences.Editor edit = app_prefs.edit();
        edit.putString(TANGGAL_LAHIR, loginorout);
        edit.commit();
    }
    public String getTANGGAL_LAHIR() {
        return app_prefs.getString(TANGGAL_LAHIR, "");
    }

    public void putPicture(String loginorout) {
        SharedPreferences.Editor edit = app_prefs.edit();
        edit.putString(PICTURE, loginorout);
        edit.commit();
    }
    public String getPICTURE() {
        return app_prefs.getString(PICTURE, "");
    }

    public void putNo_Telefon(String loginorout) {
        SharedPreferences.Editor edit = app_prefs.edit();
        edit.putString(NO_TELEFON, loginorout);
        edit.commit();
    }
    public String getNO_TELEFON() {
        return app_prefs.getString(NO_TELEFON, "");
    }

    public void putKode_Pos(String loginorout) {
        SharedPreferences.Editor edit = app_prefs.edit();
        edit.putString(KODE_POS, loginorout);
        edit.commit();
    }
    public String getKODE_POS() {
        return app_prefs.getString(KODE_POS, "");
    }

    public void putKota(String loginorout) {
        SharedPreferences.Editor edit = app_prefs.edit();
        edit.putString(KOTA, loginorout);
        edit.commit();
    }
    public String getKOTA() {
        return app_prefs.getString(KOTA, "");
    }

}
