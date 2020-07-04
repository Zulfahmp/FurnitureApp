package com.zulfa.furnitureapp.Fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.zulfa.furnitureapp.Koneksi.Api;
import com.zulfa.furnitureapp.Login.LoginActivity;
import com.zulfa.furnitureapp.Login.PreferenceHelper;
import com.zulfa.furnitureapp.R;
import com.zulfa.furnitureapp.UpdateActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class AccountFragment extends Fragment {

    String REFRESHURL = "https://furnitureappta.000webhostapp.com/api/";

    private FloatingActionButton bEdit;
    private TextView tEmail, tvlogout;
    private CircleImageView circleImageView;
    private EditText mNama, mAlamat, mNoTelefon, mKodePos, mKota, mTgl;
    private PreferenceHelper preferenceHelper;
    private RadioButton radioSexButton;
    private RadioButton radioButtonL;
    private RadioButton radioButtonP;
    Calendar myCalendar = Calendar.getInstance();

    SwipeRefreshLayout swLayout;
    LinearLayout llayout;

    Bitmap bitmap;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        preferenceHelper = new PreferenceHelper(getContext());
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        radioButtonL = (RadioButton)view.findViewById(R.id.radioL);
        radioButtonP = (RadioButton)view.findViewById(R.id.radioP);

        inisialisasi(view);
        viewMode();
        onCLick();
        getNew();
        //CircleImage
//        new GetImageFromURL(circleImageView).execute(preferenceHelper.getPICTURE());

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.skipMemoryCache(true);
        requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
        requestOptions.placeholder(R.drawable.account);
        requestOptions.error(R.drawable.account);
        Glide.with(getContext())
                .load(preferenceHelper.getPICTURE())
                .apply(requestOptions)
                .into(circleImageView);

        //EditText
        tEmail.setText(preferenceHelper.getEmail());
        mNama.setText(preferenceHelper.getNama());
        mAlamat.setText(preferenceHelper.getAlamat());
        mNoTelefon.setText(preferenceHelper.getNO_TELEFON());
        mKodePos.setText(preferenceHelper.getKODE_POS());
        mKota.setText(preferenceHelper.getKOTA());
        mTgl.setText(preferenceHelper.getTANGGAL_LAHIR());

        if (preferenceHelper.getJENIS_KELAMIN().equals("laki-laki")){
            radioButtonL.setChecked(true);
        }else {
            radioButtonP.setChecked(true);
        }

        swLayout.setColorSchemeResources(R.color.colorAccent,R.color.colorPrimary);
        //refresh
        swLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getNew();
                    }
                },5000);
            }
        });
    }

    private void onCLick() {
        tvlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                alertDialog.setMessage("Keluar?");
                alertDialog.setPositiveButton("iya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        preferenceHelper.putIsLogin(false);
                        Intent intent = new Intent(getContext(), LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        getActivity().finish();
                    }
                });
                alertDialog.setNegativeButton("tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                alertDialog.show();
            }
        });

        bEdit.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), UpdateActivity.class);
                intent.putExtra("picture", preferenceHelper.getPICTURE());
                startActivity(intent);
            }
        });
    }

    private void getNew() {
        String email = preferenceHelper.getEmail();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(REFRESHURL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        Api api = retrofit.create(Api.class);
        Call<String> call = api.refresh(email);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.i("Responsestring", response.body().toString());

                if (response.isSuccessful()){
                    if (response.body() != null){
                        Log.i("onSuccess", response.body().toString());
                        String jsonresponse = response.body().toString();
                        parseLoginData(jsonresponse);
                    }else {
                        Log.i("onEmptyResponse", "Returned empty response");
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(getContext(), "Tidak Ada Koneksi Internet!!!", Toast.LENGTH_SHORT).show();
            }
        });
        swLayout.setRefreshing(false);
        //EditText
        mNama.setText(preferenceHelper.getNama());
        mAlamat.setText(preferenceHelper.getAlamat());
        mNoTelefon.setText(preferenceHelper.getNO_TELEFON());
        mKodePos.setText(preferenceHelper.getKODE_POS());
        mKota.setText(preferenceHelper.getKOTA());
        mTgl.setText(preferenceHelper.getTANGGAL_LAHIR());
        if (preferenceHelper.getJENIS_KELAMIN().equals("laki-laki")){
            radioButtonL.setChecked(true);
        }else {
            radioButtonP.setChecked(true);
        }
        //CircleImage
        new GetImageFromURL(circleImageView).execute(preferenceHelper.getPICTURE());
    }

    private void parseLoginData(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.getString("status").equals("true")){
                saveInfo(response);

            }else {
                jsonObject.getString("status").equals("false");
                Toast.makeText(getContext(), "Email Tidak Ada!!!", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void saveInfo(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.getString("status").equals("true")){
                JSONArray dataArray = jsonObject.getJSONArray("data");
                for (int i = 0; i < dataArray.length(); i++ ){
                    JSONObject dataobj = dataArray.getJSONObject(i);
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

    //load image
    public class GetImageFromURL extends AsyncTask<String, Void, Bitmap> {
        CircleImageView imgV;

        public GetImageFromURL(CircleImageView imgV){
            this.imgV = imgV;
        }

        @Override
        protected Bitmap doInBackground(String... url) {
            String urldisplay = url[0];
            bitmap = null;
            try {
                InputStream srt = new java.net.URL(urldisplay).openStream();
                bitmap = BitmapFactory.decodeStream(srt);

            }catch (Exception e){
                e.printStackTrace();
            }

            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            imgV.setImageBitmap(bitmap);

        }
    }

    @SuppressLint("RestrictedApi")
    private void viewMode(){
        mNama.setFocusableInTouchMode(false);
        mAlamat.setFocusableInTouchMode(false);
        mNoTelefon.setFocusableInTouchMode(false);
        mKodePos.setFocusableInTouchMode(false);
        mKota.setFocusableInTouchMode(false);
        mTgl.setFocusableInTouchMode(false);
        mTgl.setClickable(false);
        bEdit.setVisibility(View.VISIBLE);
    }

    private void inisialisasi(View view){
        tEmail = (TextView)view.findViewById(R.id.tvEmail);
        tvlogout = (TextView)view.findViewById(R.id.btn);
        bEdit = (FloatingActionButton) view.findViewById(R.id.btnEdit);
        circleImageView = (CircleImageView) view.findViewById(R.id.imgv);
        swLayout = (SwipeRefreshLayout)view.findViewById(R.id.swlayout);
        llayout = (LinearLayout)view.findViewById(R.id.ll_swiperefresh);

        mNama = (EditText)view.findViewById(R.id.etNama);
        mAlamat = (EditText)view.findViewById(R.id.etAlamat);
        mNoTelefon = (EditText)view.findViewById(R.id.etNoTelefon);
        mKodePos = (EditText)view.findViewById(R.id.etKodePos);
        mKota = (EditText)view.findViewById(R.id.etKota);
        mTgl = (EditText)view.findViewById(R.id.etTTL);

    }
}
