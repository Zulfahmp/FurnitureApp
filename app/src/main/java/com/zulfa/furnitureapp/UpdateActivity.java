package com.zulfa.furnitureapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.leo.simplearcloader.ArcConfiguration;
import com.leo.simplearcloader.SimpleArcDialog;
import com.leo.simplearcloader.SimpleArcLoader;
import com.zulfa.furnitureapp.Koneksi.Api;
import com.zulfa.furnitureapp.Login.PreferenceHelper;
import com.zulfa.furnitureapp.Model.Value;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UpdateActivity extends AppCompatActivity {

    public static final String URLUPDATE = "https://furnitureappta.000webhostapp.com/api/";

    private EditText mEmail, mNama, mAlamat, mTTL, mNoTelefon, mKodePos, mKota;
    private CircleImageView mPicture;
    private FloatingActionButton mFabChoosePic;
    private Button update;
    private RadioButton radioSexButton;
    PreferenceHelper preferenceHelper;
    Calendar myCalendar = Calendar.getInstance();
    LoadingDialog loadingDialog;

    private String picture;

    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        ActionBar actionBar = getSupportActionBar(); // or getActionBar();
        getSupportActionBar().setTitle("Update Profil"); // set the top title
        String title = actionBar.getTitle().toString(); // get the title
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        preferenceHelper = new PreferenceHelper(this);
        loadingDialog = new LoadingDialog(UpdateActivity.this);

        mEmail = (EditText)findViewById(R.id.etEmail);
        mNama = (EditText)findViewById(R.id.etNama);
        mAlamat = (EditText)findViewById(R.id.etAlamat);
        mTTL = (EditText)findViewById(R.id.etTTL);
        final RadioGroup radioGroup = (RadioGroup)findViewById(R.id.radioJK);
        RadioButton radioButtonL = (RadioButton)findViewById(R.id.radioL);
        RadioButton radioButtonP = (RadioButton)findViewById(R.id.radioP);
        update = (Button)findViewById(R.id.btnUpdate);
        mNoTelefon = (EditText)findViewById(R.id.etNTF);
        mKodePos = (EditText)findViewById(R.id.etKP);
        mKota = (EditText)findViewById(R.id.etKT);
        mPicture = (CircleImageView)findViewById(R.id.imgvUpdate);
        mFabChoosePic = findViewById(R.id.fabChoosePic);

        Intent intent = getIntent();
        picture = intent.getStringExtra("picture");

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.skipMemoryCache(true);
        requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
        requestOptions.placeholder(R.drawable.account);
        requestOptions.error(R.drawable.account);
        Glide.with(UpdateActivity.this)
                .load(preferenceHelper.getPICTURE())
                .apply(requestOptions)
                .into(mPicture);

        mFabChoosePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
            }
        });

        mEmail.setText(preferenceHelper.getEmail());
        mTTL.setText(preferenceHelper.getTANGGAL_LAHIR());

        //jenis kelamin
        if (preferenceHelper.getJENIS_KELAMIN().equals("laki-laki")){
            radioButtonL.setChecked(true);
        }else {
            radioButtonP.setChecked(true);
        }

        //tanggal lahir
        mTTL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(UpdateActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        mNama.setText(preferenceHelper.getNama());
        mAlamat.setText(preferenceHelper.getAlamat());
        mNoTelefon.setText(preferenceHelper.getNO_TELEFON());
        mKodePos.setText(preferenceHelper.getKODE_POS());
        mKota.setText(preferenceHelper.getKOTA());

        mEmail.setFocusableInTouchMode(false);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingDialog.startLoadingDialog();

                //mengambil data dari edittext
                String email = mEmail.getText().toString();
                String nama = mNama.getText().toString();
                String alamat = mAlamat.getText().toString();
                int selectedId = radioGroup.getCheckedRadioButtonId();
                //mencari id radio button
                radioSexButton = (RadioButton)findViewById(selectedId);
                String jeniskelamin = radioSexButton.getText().toString();
                String tanggallahir = mTTL.getText().toString();
                String notelefon = mNoTelefon.getText().toString();
                String kodepos = mKodePos.getText().toString();
                String kota = mKota.getText().toString();
                String picture = null;
                if (bitmap == null){
                    picture = "";
                }else{
                    picture = getStringImage(bitmap);
                }
                if (nama.isEmpty()){
                    mNama.setError("Nama Tidak Boleh Kosong");
                    mNama.requestFocus();
                    loadingDialog.dismissDialog();
                    return;
                }
                if (alamat.isEmpty()){
                    mAlamat.setError("Alamat Tidak Boleh Kosong");
                    mAlamat.requestFocus();
                    loadingDialog.dismissDialog();
                    return;
                }
                if (notelefon.isEmpty()){
                    mNoTelefon.setError("NoTelefon Tidak Boleh Kosong");
                    mNoTelefon.requestFocus();
                    loadingDialog.dismissDialog();
                    return;
                }
                if (kodepos.isEmpty()){
                    mKodePos.setError("Kode Pos Tidak Boleh Kosong");
                    mKodePos.requestFocus();
                    loadingDialog.dismissDialog();
                    return;
                }
                if (kota.isEmpty()){
                    mKota.setError("Kode Pos Tidak Boleh Kosong");
                    mKota.requestFocus();
                    loadingDialog.dismissDialog();
                    return;
                }

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(URLUPDATE)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                Api api = retrofit.create(Api.class);
                Call<Value> call = api.ubah(email, nama, alamat, jeniskelamin, tanggallahir, notelefon, kodepos, kota, picture);
                call.enqueue(new Callback<Value>() {
                    @Override
                    public void onResponse(Call<Value> call, Response<Value> response) {
                        String value = response.body().getValue();
                        String message = response.body().getMessage();
                        loadingDialog.dismissDialog();
                        if (value.equals("1")){
                            Toast.makeText(UpdateActivity.this, "Update Berhasil", Toast.LENGTH_LONG).show();
//                            preferenceHelper.putIsLogin(false);
//                            Intent intent = new Intent(UpdateActivity.this, LoginActivity.class);
//                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                            startActivity(intent);
//                            finish();
                        }else {
                            Toast.makeText(UpdateActivity.this, "update gagal!!!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Value> call, Throwable t) {
                        Toast.makeText(UpdateActivity.this, "Tidak ada koneksi internet!!!", Toast.LENGTH_SHORT).show();
                        loadingDialog.dismissDialog();
                    }
                });

            }
        });
    }

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            setTTL();
        }

    };

    private void setTTL() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        mTTL.setText(sdf.format(myCalendar.getTime()));
    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {

                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);

                mPicture.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
