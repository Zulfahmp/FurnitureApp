package com.zulfa.furnitureapp.Login;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.leo.simplearcloader.ArcConfiguration;
import com.leo.simplearcloader.SimpleArcDialog;
import com.leo.simplearcloader.SimpleArcLoader;
import com.zulfa.furnitureapp.Koneksi.Api;
import com.zulfa.furnitureapp.Model.Value;
import com.zulfa.furnitureapp.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterActivity extends AppCompatActivity {

    public static final String URL = "https://furnitureappta.000webhostapp.com/api/";
    private ProgressDialog progress;
    Toast toast, toastada, toastgagal;

    private EditText mNamaReg, mEmailReg, mPasswordReg;
    private Button mRegister;
    private TextView Login, text, textada, textgagal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);

        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.lay_berhasil,(ViewGroup)findViewById(R.id.layout));
        View layout2 = inflater.inflate(R.layout.lay_ada,(ViewGroup)findViewById(R.id.layout_ada));
        View layout3 = inflater.inflate(R.layout.lay_gagal,(ViewGroup)findViewById(R.id.layout_gagal));

        text = layout.findViewById(R.id.tv);
        textada = layout2.findViewById(R.id.tv_ada);
        textgagal = layout3.findViewById(R.id.tv_gagal);

        toast = new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);

        toastada = new Toast(getApplicationContext());
        toastada.setDuration(Toast.LENGTH_LONG);
        toastada.setView(layout2);

        toastgagal = new Toast(getApplicationContext());
        toastgagal.setDuration(Toast.LENGTH_SHORT);
        toastgagal.setView(layout3);

        mNamaReg = (EditText)findViewById(R.id.etReg1);
        mEmailReg = (EditText)findViewById(R.id.etReg2);
        mPasswordReg = (EditText)findViewById(R.id.etReg3);
        Login = (TextView)findViewById(R.id.tvLog);
        mRegister = (Button)findViewById(R.id.btn_regis);

        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                daftar();
            }
        });

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });
    }

    private void daftar() {
        //membuat progress dialog
        final SimpleArcDialog mDialog = new SimpleArcDialog(this);
        ArcConfiguration configuration = new ArcConfiguration(this);
        configuration.setLoaderStyle(SimpleArcLoader.STYLE.COMPLETE_ARC);
        configuration.setText("Tunggu Sebentar....");
        mDialog.setConfiguration(configuration);
        mDialog.show();

//        progress = new ProgressDialog(this);
//        progress.setCancelable(false);
//        progress.setMessage("Loading ...");
//        progress.show();

        //mengambil data dari edittext
        String nama = mNamaReg.getText().toString();
        String email = mEmailReg.getText().toString();
        String password = mPasswordReg.getText().toString();

        if (nama.isEmpty()){
            mNamaReg.setError("Nama Tidak Boleh Kosong");
            mNamaReg.requestFocus();
            mDialog.dismiss();
            return;
        }

        if (email.isEmpty()) {
            mEmailReg.setError("Email tidak boleh kosong");
            mEmailReg.requestFocus();
            mDialog.dismiss();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mEmailReg.setError("Masukan email dengan benar");
            mEmailReg.requestFocus();
            mDialog.dismiss();
            return;
        }

        if (password.isEmpty()) {
            mPasswordReg.setError("Password tidak boleh kosong");
            mPasswordReg.requestFocus();
            mDialog.dismiss();
            return;
        }

        if (password.length() < 5) {
            mPasswordReg.setError("Password minimal 5 karakter");
            mPasswordReg.requestFocus();
            mDialog.dismiss();
            return;
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Api api = retrofit.create(Api.class);
        Call<Value> call = api.daftar(nama, email, password);
        call.enqueue(new Callback<Value>() {
            @Override
            public void onResponse(Call<Value> call, Response<Value> response) {
                String value = response.body().getValue();
                String message = response.body().getMessage();
                mDialog.dismiss();

                if (value.equals("1")){
                    text.setText(message);
                    toast.show();
                }else {
                    textada.setText(message);
                    toastada.show();
                }
            }

            @Override
            public void onFailure(Call<Value> call, Throwable t) {
                textgagal.setText("Jaringan Error!");
                toastgagal.show();
                mDialog.dismiss();
            }
        });
    }
}
