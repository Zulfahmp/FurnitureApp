package com.zulfa.furnitureapp.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.zulfa.furnitureapp.Koneksi.Api;
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

public class RecyclerViewAdapterPending extends RecyclerView.Adapter<RecyclerViewAdapterPending.ViewHolder> {

    public static final String URL = "https://furnitureappta.000webhostapp.com/api/";

    private Context context;
    private List<Result> results = new ArrayList<>();

    public RecyclerViewAdapterPending(Context context, List<Result> results){
        this.context = context;
        this.results = results;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_pending, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Result result = results.get(position);
        holder.order_id.setText(result.getOrder_id());
        holder.nama_barang.setText(result.getNama_barang());
        holder.tanggal_pesanan.setText(result.getTanggal_pesanan());
        holder.harga_pesanan.setText(result.getHarga_pesanan());
        holder.jumlah_pesanan.setText(result.getJumlah_pesanan());
        holder.status_pesanan.setText(result.getStatus());

        holder.bCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Apakah Anda Ingin Membatalkan Pesanan?");
                builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String id = result.getId();
                        String status = "cancel";

                        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl(URL)
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();
                        Api api = retrofit.create(Api.class);
                        Call<Value> call = api.status(id, status);
                        call.enqueue(new Callback<Value>() {
                            @Override
                            public void onResponse(Call<Value> call, Response<Value> response) {
                                String value = response.body().getValue();
                                String message = response.body().getMessage();

                                if (value.equals("1")){
                                    Toast.makeText(context, "Pesanan Di Batalkan", Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(context, "Gagal!!!", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Value> call, Throwable t) {

                            }
                        });
                    }
                });
                builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();
            }
        });

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.skipMemoryCache(true);
        requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
        requestOptions.placeholder(R.mipmap.ic_launcher);
        requestOptions.error(R.mipmap.ic_launcher);

        Glide.with(context)
                .load(result.getPicture())
                .apply(requestOptions)
                .into(holder.pic);

    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView order_id, nama_barang, tanggal_pesanan, harga_pesanan, jumlah_pesanan, status_pesanan;
        public ImageView pic;
        public Button bCancel;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            order_id = (TextView)itemView.findViewById(R.id.textOIpending);
            nama_barang = (TextView)itemView.findViewById(R.id.textNBpending);
            tanggal_pesanan = (TextView)itemView.findViewById(R.id.textTPpending);
            harga_pesanan = (TextView)itemView.findViewById(R.id.textHargapending);
            jumlah_pesanan = (TextView)itemView.findViewById(R.id.textJumlahpending);
            status_pesanan = (TextView)itemView.findViewById(R.id.textSPpending);
            pic = (ImageView)itemView.findViewById(R.id.imgPending);
            bCancel = (Button)itemView.findViewById(R.id.btnCancel);
        }
    }
}
