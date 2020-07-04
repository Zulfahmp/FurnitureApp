package com.zulfa.furnitureapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.leo.simplearcloader.ArcConfiguration;
import com.leo.simplearcloader.SimpleArcDialog;
import com.leo.simplearcloader.SimpleArcLoader;
import com.midtrans.sdk.corekit.callback.TransactionFinishedCallback;
import com.midtrans.sdk.corekit.core.LocalDataHandler;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.core.TransactionRequest;
import com.midtrans.sdk.corekit.core.themes.CustomColorTheme;
import com.midtrans.sdk.corekit.models.ItemDetails;
import com.midtrans.sdk.corekit.models.UserAddress;
import com.midtrans.sdk.corekit.models.UserDetail;
import com.midtrans.sdk.corekit.models.snap.CreditCard;
import com.midtrans.sdk.corekit.models.snap.TransactionResult;
import com.midtrans.sdk.uikit.SdkUIFlowBuilder;
import com.zulfa.furnitureapp.Koneksi.Api;
import com.zulfa.furnitureapp.Login.PreferenceHelper;
import com.zulfa.furnitureapp.Model.Result;
import com.zulfa.furnitureapp.Model.Value;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.zulfa.furnitureapp.App.ORDERAN_ID;

public class DetailActivity extends AppCompatActivity implements TransactionFinishedCallback {

    public static final String URL = "https://furnitureappta.000webhostapp.com/api/";

    TextView mNama, mHarga, mDeskripsi, tEmail, text, tTotal, tJuamlah;
    ImageView mGambar;
    Button mDwnld, mAr,bCheckout, bTambah, bKurang;
    private EditText mAlamat, mKodePos;
    private Toast toast;
    String NAMA, Thr, LinkGmbr;
    LoadingDialog loadingDialog;

    int jumlah = 0, total, regan;

    private PreferenceHelper preferenceHelper;
    private NotificationManagerCompat notificationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        preferenceHelper = new PreferenceHelper(this);
        notificationManager = NotificationManagerCompat.from(this);
        loadingDialog = new LoadingDialog(DetailActivity.this);

        mNama = findViewById(R.id.tvDetailNama);
        mHarga = findViewById(R.id.tvDetailHarga);
        mDeskripsi = findViewById(R.id.tvDetailDeskripsi);
        mGambar = findViewById(R.id.ivDetailGambar);
        mDwnld = findViewById(R.id.btnDetailDownload);
        mAr = findViewById(R.id.btnDetailAr);
        mAlamat = (EditText)findViewById(R.id.etAlamatPengiriman);
        mKodePos = (EditText)findViewById(R.id.etKodeposPengiriman);
        bCheckout = (Button)findViewById(R.id.btnCekOut);
        tEmail = findViewById(R.id.email_pengiriman);
        bKurang = findViewById(R.id.kurang);
        bTambah = findViewById(R.id.tambah);
        tJuamlah = findViewById(R.id.jumlah);
        tTotal = findViewById(R.id.tv_total);

        tEmail.setText(preferenceHelper.getEmail());
        //CustomToast
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast_berhasil,(ViewGroup)findViewById(R.id.ToastSuccess));
        text = (TextView)layout.findViewById(R.id.tvToast);
        text.setText("Pesanan Berhasil");
        toast = new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);

        mAlamat.setText(preferenceHelper.getAlamat());
        mKodePos.setText(preferenceHelper.getKODE_POS());
        mAlamat.setFocusableInTouchMode(false);
        mKodePos.setFocusableInTouchMode(false);

        Intent intent = getIntent();
        NAMA = intent.getStringExtra("nama_barang");
        Thr = intent.getStringExtra("harga_barang");
        LinkGmbr = intent.getStringExtra("link");
        mHarga.setText(Thr);
        regan = Integer.parseInt(Thr);

        load();
        OnClicknih();
        makePayment();
    }

    private void OnClicknih() {
        bTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jumlah = jumlah + 1;
                tJuamlah.setText("" + jumlah);
                total = jumlah*regan;

                tTotal.setText("Harga : Rp." + total);
            }
        });
        bKurang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (jumlah==1){
                    Toast.makeText(DetailActivity.this, "Pesanan Minimal 1", Toast.LENGTH_SHORT).show();
                    return;
                }
                jumlah = jumlah - 1;
                tJuamlah.setText("" + jumlah);
                total = jumlah*regan;

                tTotal.setText("Harga : Rp." + total);
            }
        });
        String alamat = mAlamat.getText().toString();
        bCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (alamat.isEmpty()){
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(DetailActivity.this);
                    alertDialog.setMessage("Edit Profil Dan Isi Alamat Terlebih Dahulu");
                    alertDialog.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(DetailActivity.this, MainActivity.class));
                        }
                    });
                    alertDialog.show();
                    mAlamat.requestFocus();
                    return;
                }
                if (jumlah == 0){
                    Toast.makeText(DetailActivity.this, "Mohon Tambah Jumlah", Toast.LENGTH_SHORT).show();
                    return;
                }

                MidtransSDK.getInstance().setTransactionRequest(transactionRequest("101", total, 1, NAMA, preferenceHelper));
                MidtransSDK.getInstance().startPaymentUiFlow(DetailActivity.this );
            }
        });
        mDwnld.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void makePayment() {
        SdkUIFlowBuilder.init()
                .setContext(this)
                .setMerchantBaseUrl(BuildConfig.BASE_URL)
                .setClientKey(BuildConfig.CLIENT_KEY)
                .setTransactionFinishedCallback(this)
                .enableLog(true)
                .setColorTheme(new CustomColorTheme("#777777","#f77474" , "#3f0d0d"))
                .buildSDK();
    }

    private void load() {
        loadingDialog.startLoadingDialog();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Api api = retrofit.create(Api.class);
        Call<List<Result>> call = api.detail(NAMA);
        call.enqueue(new Callback<List<Result>>() {
            @Override
            public void onResponse(Call<List<Result>> call, Response<List<Result>> response) {
                if (response.isSuccessful()){
                    for (int i=0; i<response.body().size(); i++){
                        mNama.setText(response.body().get(i).getNama_barang());
                        mDeskripsi.setText(response.body().get(i).getDeskripsi());

                        Glide.with(DetailActivity.this)
                                .load(response.body().get(i).getGambar())
                                .into(mGambar);

                        loadingDialog.dismissDialog();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Result>> call, Throwable t) {

            }
        });
    }

    @Override
    public void onTransactionFinished(TransactionResult result) {
        if (result.getResponse() != null){
            switch (result.getStatus()){
                case TransactionResult.STATUS_SUCCESS:
                    sukses(result);
                    break;
                case TransactionResult.STATUS_PENDING:
                    Intent intent = new Intent(DetailActivity.this, MainActivity.class);
                    PendingIntent pendingIntent = PendingIntent.getActivity(DetailActivity.this, 0, intent, 0);
                    Notification notification = new NotificationCompat.Builder(DetailActivity.this, ORDERAN_ID)
                            .setSmallIcon(R.drawable.store)
                            .setContentTitle("Pesanan Baru")
                            .setContentText("Pesanan Baru Modern Chair " + result.getStatus())
                            .setPriority(NotificationCompat.PRIORITY_HIGH)
                            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                            .setContentIntent(pendingIntent)
                            .build();

                    notificationManager.notify(1,notification);

                    pending(result);
//                    Status.setText(result.getStatus());
//                    Toast.makeText(this, "Pesanan Berhasil" + result.getResponse().getTransactionId(), Toast.LENGTH_SHORT).show();
                    break;
                case TransactionResult.STATUS_FAILED:
                    cancel(result);
                    break;
            }
            result.getResponse().getValidationMessages();
        }else if (result.isTransactionCanceled()){
            Toast.makeText(this, "Transaction Batal", Toast.LENGTH_LONG).show();
        }else {
            if (result.getStatus().equalsIgnoreCase((TransactionResult.STATUS_INVALID))){
                Toast.makeText(this, "Transaction Invalid" + result.getResponse().getTransactionId(), Toast.LENGTH_LONG).show();
            }else {
                Toast.makeText(this, "Something Wrong", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void cancel(TransactionResult result) {
        loadingDialog.startLoadingDialog();

        String email = tEmail.getText().toString();
        String order_id = result.getResponse().getOrderId();
        String nama_barang = mNama.getText().toString();
        String tanggal_pesanan = result.getResponse().getTransactionTime();
        String harga_pesanan = "Rp. " + result.getResponse().getGrossAmount();
        String jumlah_pesanan = tJuamlah.getText().toString();
        String status = result.getResponse().getTransactionStatus();
        String picture = LinkGmbr;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Api api = retrofit.create(Api.class);
        Call<Value> call = api.order(email, order_id, nama_barang, tanggal_pesanan, harga_pesanan, jumlah_pesanan, status, picture);
        call.enqueue(new Callback<Value>() {
            @Override
            public void onResponse(Call<Value> call, Response<Value> response) {
                String value = response.body().getValue();
                String message = response.body().getMessage();
                loadingDialog.dismissDialog();

                if (value.equals("1")){
                    toast.show();
                }else {
                    Toast.makeText(DetailActivity.this, "gagal", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Value> call, Throwable t) {
                loadingDialog.dismissDialog();
                Toast.makeText(DetailActivity.this, "Gagal!!!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void pending(TransactionResult result) {
        loadingDialog.startLoadingDialog();

        SimpleArcDialog mDialog = new SimpleArcDialog(this);
        ArcConfiguration configuration = new ArcConfiguration(this);
        configuration.setLoaderStyle(SimpleArcLoader.STYLE.COMPLETE_ARC);
        configuration.setText("Tunggu Sebentar....");
        mDialog.setConfiguration(configuration);
        mDialog.show();

        String email = tEmail.getText().toString();
        String order_id = result.getResponse().getOrderId();
        String nama_barang = mNama.getText().toString();
        String tanggal_pesanan = result.getResponse().getTransactionTime();
        String harga_pesanan = "Rp. " + result.getResponse().getGrossAmount();
        String jumlah_pesanan = tJuamlah.getText().toString();;
        String status = result.getStatus();
        String picture = LinkGmbr;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Api api = retrofit.create(Api.class);
        Call<Value> call = api.order(email, order_id, nama_barang, tanggal_pesanan, harga_pesanan, jumlah_pesanan, status, picture);
        call.enqueue(new Callback<Value>() {
            @Override
            public void onResponse(Call<Value> call, Response<Value> response) {
                String value = response.body().getValue();
                String message = response.body().getMessage();
                loadingDialog.dismissDialog();

                if (value.equals("1")){
                    toast.show();
                    mDialog.dismiss();
                }else {
                    Toast.makeText(DetailActivity.this, "gagal", Toast.LENGTH_SHORT).show();
                    mDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<Value> call, Throwable t) {
                loadingDialog.dismissDialog();
                Toast.makeText(DetailActivity.this, "Gagal!!!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sukses(TransactionResult result) {
        loadingDialog.startLoadingDialog();

        String email = tEmail.getText().toString();
        String order_id = result.getResponse().getOrderId();
        String nama_barang = mNama.getText().toString();
        String tanggal_pesanan = result.getResponse().getTransactionTime();
        String harga_pesanan = "Rp. " + result.getResponse().getGrossAmount();
        String jumlah_pesanan = tJuamlah.getText().toString();
        String status = result.getResponse().getTransactionStatus();
        String picture = LinkGmbr;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Api api = retrofit.create(Api.class);
        Call<Value> call = api.order(email, order_id, nama_barang, tanggal_pesanan, harga_pesanan, jumlah_pesanan, status, picture);
        call.enqueue(new Callback<Value>() {
            @Override
            public void onResponse(Call<Value> call, Response<Value> response) {
                String value = response.body().getValue();
                String message = response.body().getMessage();
                loadingDialog.dismissDialog();

                if (value.equals("1")){
                    toast.show();
                }else {
                    Toast.makeText(DetailActivity.this, "gagal", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Value> call, Throwable t) {
                loadingDialog.dismissDialog();
                Toast.makeText(DetailActivity.this, "Gagal!!!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static TransactionRequest transactionRequest(String id, int total, int qty, String name, PreferenceHelper preferenceHelper){
        TransactionRequest request =  new TransactionRequest(System.currentTimeMillis() + " " ,total );
//        request.setCustomerDetails(customerDetails());
        UserDetail userDetail = new UserDetail();
        userDetail.setUserFullName(preferenceHelper.getNama());
        userDetail.setEmail(preferenceHelper.getEmail());
        userDetail.setPhoneNumber("+62"+preferenceHelper.getNO_TELEFON());
        // set user ID as identifier of saved card (can be anything as long as unique),
        // randomly generated by SDK if not supplied
        userDetail.setUserId(preferenceHelper.getId());

        ArrayList<UserAddress> userAddresses = new ArrayList<>();
        UserAddress userAddress = new UserAddress();
        userAddress.setAddress(preferenceHelper.getAlamat());
        userAddress.setCity(preferenceHelper.getKOTA());
        userAddress.setAddressType(com.midtrans.sdk.corekit.core.Constants.ADDRESS_TYPE_BOTH);
        userAddress.setZipcode(preferenceHelper.getKODE_POS());
        userAddress.setCountry("IDN");
        userAddresses.add(userAddress);
        userDetail.setUserAddresses(userAddresses);
        LocalDataHandler.saveObject("user_details", userDetail);

        ItemDetails details = new ItemDetails(id, total, qty, name);

        ArrayList<ItemDetails> itemDetails = new ArrayList<>();
        itemDetails.add(details);
        request.setItemDetails(itemDetails);

        CreditCard creditCard = new CreditCard();
        creditCard.setSaveCard(false);
        creditCard.setAuthentication(CreditCard.AUTHENTICATION_TYPE_RBA);
        request.setCreditCard(creditCard);
        return request;
    }
}
