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

import java.util.List;

public class RecyclerViewAdapterCancel extends RecyclerView.Adapter<RecyclerViewAdapterCancel.ViewHolder> {

    private Context context;
    private List<Result> results;

    public RecyclerViewAdapterCancel(Context context, List<Result> results){
        this.context = context;
        this.results = results;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_cancel, parent, false);
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
                .into(holder.picCancel);
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView order_id, nama_barang, tanggal_pesanan, harga_pesanan, jumlah_pesanan, status_pesanan;
        public ImageView picCancel;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            order_id = (TextView)itemView.findViewById(R.id.textOIcancel);
            nama_barang = (TextView)itemView.findViewById(R.id.textNBcancel);
            tanggal_pesanan = (TextView)itemView.findViewById(R.id.textTPcancel);
            harga_pesanan = (TextView)itemView.findViewById(R.id.textHargacancel);
            jumlah_pesanan = (TextView)itemView.findViewById(R.id.textJumlahcancel);
            status_pesanan = (TextView)itemView.findViewById(R.id.textSPcancel);
            picCancel = (ImageView)itemView.findViewById(R.id.imgCancel);
        }
    }
}
