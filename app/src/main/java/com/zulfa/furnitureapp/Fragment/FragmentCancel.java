package com.zulfa.furnitureapp.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.leo.simplearcloader.ArcConfiguration;
import com.leo.simplearcloader.SimpleArcDialog;
import com.leo.simplearcloader.SimpleArcLoader;
import com.zulfa.furnitureapp.Adapter.RecyclerViewAdapterCancel;
import com.zulfa.furnitureapp.Koneksi.Api;
import com.zulfa.furnitureapp.LoadingDialog;
import com.zulfa.furnitureapp.Login.PreferenceHelper;
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

public class FragmentCancel extends Fragment {

    public static final String URL = "https://furnitureappta.000webhostapp.com/api/";
    private List<Result> results = new ArrayList<>();
    private RecyclerViewAdapterCancel viewAdapter;

    private RecyclerView recyclerView;
    private PreferenceHelper preferenceHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cancel, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        recyclerView = (RecyclerView)view.findViewById(R.id.recyclerViewCancel);

        LoadingDialog loadingDialog = new LoadingDialog(getActivity());
        preferenceHelper = new PreferenceHelper(getContext());

        viewAdapter = new RecyclerViewAdapterCancel(getContext(), results);
        RecyclerView.LayoutManager mlayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mlayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(viewAdapter);

        loadingDialog.startLoadingDialog();

        String email = preferenceHelper.getEmail();
        String status = "cancel";

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Api api = retrofit.create(Api.class);
        Call<Value> call = api.vieworderan(email, status);
        call.enqueue(new Callback<Value>() {
            @Override
            public void onResponse(Call<Value> call, Response<Value> response) {
                String value = response.body().getValue();
                if (value.equals("1")){
                    results = response.body().getResult();
                    viewAdapter = new RecyclerViewAdapterCancel(getContext(), results);
                    recyclerView.setAdapter(viewAdapter);
                    loadingDialog.dismissDialog();
                }
            }

            @Override
            public void onFailure(Call<Value> call, Throwable t) {
                Toast.makeText(getContext(), "Gagal!!!!", Toast.LENGTH_SHORT).show();
                loadingDialog.dismissDialog();
            }
        });
    }
}
