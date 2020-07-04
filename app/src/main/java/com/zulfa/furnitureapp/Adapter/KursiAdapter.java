package com.zulfa.furnitureapp.Adapter;

import android.content.Context;
import android.content.Intent;
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
import com.zulfa.furnitureapp.DetailActivity;
import com.zulfa.furnitureapp.Model.Result;
import com.zulfa.furnitureapp.R;

import java.util.List;

public class KursiAdapter extends RecyclerView.Adapter<KursiAdapter.ViewHolder> {

    private Context context;
    private List<Result> results;

    public KursiAdapter(Context context, List<Result> results){
        this.context = context;
        this.results = results;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_produk, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Result result = results.get(position);
        holder.nama.setText(result.getNama_barang());
        holder.harga.setText(result.getHarga());
        holder.link.setText(result.getGambar());

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.skipMemoryCache(true);
        requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
        requestOptions.placeholder(R.mipmap.ic_launcher);
        requestOptions.error(R.mipmap.ic_launcher);
        Glide.with(context)
                .load(result.getGambar())
                .apply(requestOptions)
                .into(holder.gmbr);
    }

    @Override
    public int getItemCount() {
        return results.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView nama, harga, link;
        ImageView gmbr;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nama = itemView.findViewById(R.id.NamaProduk);
            harga = itemView.findViewById(R.id.HargaProduk);
            gmbr = itemView.findViewById(R.id.GambarProduk);
            link = itemView.findViewById(R.id.linkGmbr);

            gmbr.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String NAMA = nama.getText().toString();
                    String Harga = harga.getText().toString();
                    String Link = link.getText().toString();
                    Intent intent = new Intent(context, DetailActivity.class);
                    intent.putExtra("nama_barang", NAMA);
                    intent.putExtra("harga_barang", Harga);
                    intent.putExtra("link", Link);
                    context.startActivity(intent);
                }
            });
        }
    }
}
