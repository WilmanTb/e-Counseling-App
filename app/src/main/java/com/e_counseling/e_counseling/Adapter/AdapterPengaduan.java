package com.e_counseling.e_counseling.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.e_counseling.e_counseling.Activity.Guru.Activity_DashboardGuruBK;
import com.e_counseling.e_counseling.Activity.Guru.Activity_DescPelanggaran;
import com.e_counseling.e_counseling.Activity.Guru.Activity_Desc_LaporanPelanggaran;
import com.e_counseling.e_counseling.Model.PengaduanClass;
import com.e_counseling.e_counseling.R;

import java.util.ArrayList;

public class AdapterPengaduan extends RecyclerView.Adapter<AdapterPengaduan.MyViewHolder> {

    Context context;
    ArrayList<PengaduanClass> list;
    private descPelanggaran DescPelanggaran;

    public interface descPelanggaran {
        void clickCardView(int position);
    }

    public void descPelanggaran(descPelanggaran DescPelanggaran) {
        this.DescPelanggaran = DescPelanggaran;
    }

    public AdapterPengaduan(Context context, ArrayList<PengaduanClass> list) {
        this.context = context;
        this.list = list;
    }


    @NonNull
    @Override
    public AdapterPengaduan.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_pelanggaran, parent, false);
        return new MyViewHolder(view, DescPelanggaran);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterPengaduan.MyViewHolder holder, int position) {
        PengaduanClass pengaduanClass = list.get(position);
        holder.jenisPelanggaran.setText(pengaduanClass.getJenisPelanggaran());
        holder.deskripsiPelanggaran.setText(pengaduanClass.getDeskripsi());
        holder.poinPelanggaran.setText(pengaduanClass.getPoin());
        holder.cvListPelanggaran.setOnClickListener(view -> {
            if (Activity_DashboardGuruBK.i == 1) {
                context.startActivity(new Intent(context, Activity_DescPelanggaran.class).putExtra("deskripsi", list.get(holder.getAdapterPosition())));
            }else if (Activity_DashboardGuruBK.i == 2){
                context.startActivity(new Intent(context, Activity_Desc_LaporanPelanggaran.class).putExtra("deskripsi", list.get(holder.getAdapterPosition())));
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView jenisPelanggaran, deskripsiPelanggaran, jenis, deskripsi, poinPelanggaran;
        CardView cvListPelanggaran;

        public MyViewHolder(@NonNull View itemView, descPelanggaran DescPelanggaran) {
            super(itemView);

            cvListPelanggaran = itemView.findViewById(R.id.cvListPelanggaran);
            jenisPelanggaran = itemView.findViewById(R.id.jenisPelanggaran);
            deskripsiPelanggaran = itemView.findViewById(R.id.deskripsiPelanggaran);
            jenis = itemView.findViewById(R.id.jenis);
            deskripsi = itemView.findViewById(R.id.deskripsi);
            poinPelanggaran = itemView.findViewById(R.id.poinPelangggaran);
        }
    }
}
