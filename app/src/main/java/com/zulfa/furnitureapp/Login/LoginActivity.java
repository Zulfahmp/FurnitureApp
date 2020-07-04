package com.zulfa.furnitureapp.Login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.leo.simplearcloader.ArcConfiguration;
import com.leo.simplearcloader.SimpleArcDialog;
import com.leo.simplearcloader.SimpleArcLoader;
import com.zulfa.furnitureapp.Koneksi.Api;
import com.zulfa.furnitureapp.LoadingDialog;
import com.zulfa.furnitureapp.MainActivity;
import com.zulfa.furnitureapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String URL = "https://furnitureappta.000webhostapp.com/api/";

    private EditText editTextEmail;
    private EditText editTextPassword;
    private PreferenceHelper preferenceHelper;

    private ArcConfiguration configuration;
    LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        preferenceHelper = new PreferenceHelper(this);
        configuration = new ArcConfiguration(this);
        loadingDialog = new LoadingDialog(LoginActivity.this);

        if(preferenceHelper.getIsLogin()){
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            this.finish();
        }

        editTextEmail = findViewById(R.id.etLog1);
        editTextPassword = findViewById(R.id.etLog2);

        findViewById(R.id.btn_login).setOnClickListener(this);
        findViewById(R.id.tvReg).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                loginUser();
                break;
            case R.id.tvReg:
                startActivity(new Intent(this, RegisterActivity.class));
                break;
        }
    }

    private void loginUser() {
        loadingDialog.startLoadingDialog();

        final String email = editTextEmail.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();

        if (email.isEmpty()){
            editTextEmail.setError("Masukan email");
            editTextEmail.requestFocus();
            loadingDialog.dismissDialog();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Masukan email dengan benar");
            editTextEmail.requestFocus();
            loadingDialog.dismissDialog();
            return;
        }
        if (password.isEmpty()){
            editTextPassword.setError("Masukan password");
            editTextPassword.requestFocus();
            loadingDialog.dismissDialog();
            return;
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        Api api = retrofit.create(Api.class);
        Call<String> call = api.login(email, password);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.i("Responsestring", response.body().toString());

                if (response.isSuccessful()){
                    if (response.body() != null){
                        Log.i("onSuccess", response.body().toString());
                        String jsonresponse = response.body().toString();
                        parseLoginData(jsonresponse);
                        loadingDialog.dismissDialog();
                    }else {
                        Log.i("onEmptyResponse", "Returned empty response");
                        loadingDialog.dismissDialog();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Tidak Ada Koneksi Internet!!!", Toast.LENGTH_SHORT).show();
                loadingDialog.dismissDialog();
            }
        });
    }

    private void parseLoginData(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.getString("status").equals("true")){
                saveInfo(response);

                Toast.makeText(LoginActivity.this, "Login Berhasil!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                this.finish();
            }else {
                jsonObject.getString("status").equals("false");
                Toast.makeText(this, "Email Atau Password Salah!!!", Toast.LENGTH_SHORT).show();
                loadingDialog.dismissDialog();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void saveInfo(String response) {
        preferenceHelper.putIsLogin(true);
        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.getString("status").equals("true")){
                JSONArray dataArray = jsonObject.getJSONArray("data");
                for (int i = 0; i < dataArray.length(); i++ ){
                    JSONObject dataobj = dataArray.getJSONObject(i);
                    preferenceHelper.putId(dataobj.getString("id"));
                    preferenceHelper.putEmail(dataobj.getString("email"));
                    preferenceHelper.putNama(dataobj.getString("nama"));
                    preferenceHelper.putAlamat(dataobj.getString("alamat"));
                    preferenceHelper.putJenis_Kelamin(dataobj.getString("jenis_kelamin"));
                    preferenceHelper.putTanggal_lahir(dataobj.getString("tanggal_lahir"));
                    preferenceHelper.putPicture(dataobj.getString("picture"));
                    preferenceHelper.putNo_Telefon(dataobj.getString("no_telefon"));
                    preferenceHelper.putKode_Pos(dataobj.getString("kode_pos"));
                    preferenceHelper.putKota(dataobj.getString("kota"));
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
