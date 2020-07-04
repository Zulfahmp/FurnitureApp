package com.zulfa.furnitureapp.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.widget.Toast;

import com.zulfa.furnitureapp.Adapter.KursiAdapter;
import com.zulfa.furnitureapp.Koneksi.Api;
import com.zulfa.furnitureapp.LoadingDialog;
import com.zulfa.furnitureapp.Model.Result;
import com.zulfa.furnitureapp.Model.Value;
import com.zulfa.furnitureapp.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ListMejaActivity extends AppCompatActivity {

    public static final String URL = "https://furnitureappta.000webhostapp.com/api/";
    private List<Result> results = new ArrayList<>();
    private KursiAdapter viewAdapter;

    LoadingDialog loadingDialog;

    RecyclerView MrecyclerView;

    String jenis_barang = "meja";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_meja);

        loadingDialog = new LoadingDialog(ListMejaActivity.this);

        MrecyclerView = findViewById(R.id.recyclerViewMeja);

        viewAdapter = new KursiAdapter (ListMejaActivity.this, results);
        RecyclerView.LayoutManager mlayoutManager = new LinearLayoutManager(ListMejaActivity.this);
        mlayoutManager = new GridLayoutManager(this, 2);
        MrecyclerView.setLayoutManager(mlayoutManager);
        MrecyclerView.setHasFixedSize(true);
        MrecyclerView.setItemAnimator(new DefaultItemAnimator());
        MrecyclerView.setAdapter(viewAdapter);

        cek();
        load();
    }

    private void load() {
        loadingDialog.startLoadingDialog();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Api api = retrofit.create(Api.class);
        Call<Value> call = api.view(jenis_barang);
        call.enqueue(new Callback<Value>() {
            @Override
            public void onResponse(Call<Value> call, Response<Value> response) {
                String value = response.body().getValue();
                if (value.equals("1")){
                    results = response.body().getResult();
                    viewAdapter = new KursiAdapter(ListMejaActivity.this, results);
                    MrecyclerView.setAdapter(viewAdapter);
                    loadingDialog.dismissDialog();
                }
            }

            @Override
            public void onFailure(Call<Value> call, Throwable t) {
//                Toast.makeText(MainActivity.this, "Tidak ada koneksi!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        cek();
    }

    private boolean adaInternet(){
        ConnectivityManager koneksi = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return koneksi.getActiveNetworkInfo() != null;
    }

    public void cek(){
        if(adaInternet()){

        }else{
            // tampilkan pesan
            Toast.makeText(ListMejaActivity.this, "Tidak ada koneksi internet", Toast.LENGTH_LONG).show();
        }
    }
}
