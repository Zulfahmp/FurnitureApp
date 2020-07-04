package com.zulfa.furnitureapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.zulfa.furnitureapp.Model.Result;
import com.zulfa.furnitureapp.R;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapterSukses extends RecyclerView.Adapter<RecyclerViewAdapterSukses.ViewHolder> {

    private Context context;
    private List<Result> results = new ArrayList<>();

    public RecyclerViewAdapterSukses(Context context, List<Result> results){
        this.context = context;
        this.results = results;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_sukses, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Result result = results.get(position);
        holder.nama_barang.setText(result.getNama_barang());
        holder.tanggal_pesanan.setText(result.getTanggal_pesanan());
        holder.harga_pesanan.setText(result.getHarga_pesanan());
        holder.jumlah_pesanan.setText(result.getJumlah_pesanan());
        holder.status_pesanan.setText(result.getStatus());
        holder.order_id.setText(result.getOrder_id());

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.skipMemoryCache(true);
        requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
        requestOptions.placeholder(R.mipmap.ic_launcher);
        requestOptions.error(R.mipmap.ic_launcher);

        Glide.with(context)
                .load(result.getPicture())
                .apply(requestOptions)
                .into(holder.picSukses);
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView order_id, nama_barang, tanggal_pesanan, harga_pesanan, jumlah_pesanan, status_pesanan;
        public ImageView picSukses;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            order_id = (TextView)itemView.findViewById(R.id.textOIsukses);
            nama_barang = (TextView)itemView.findViewById(R.id.textNBsukses);
            tanggal_pesanan = (TextView)itemView.findViewById(R.id.textTPsukses);
            harga_pesanan = (TextView)itemView.findViewById(R.id.textHargasukses);
            jumlah_pesanan = (TextView)itemView.findViewById(R.id.textJumlahsukses);
            status_pesanan = (TextView)itemView.findViewById(R.id.textSPsukses);
            picSukses = (ImageView)itemView.findViewById(R.id.imgSukses);
        }
    }
}
