package com.e_counseling.e_counseling.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.e_counseling.e_counseling.Model.PengaduanClass;
import com.e_counseling.e_counseling.R;

import java.util.ArrayList;

public class Adapter_Kelakuan_Anak extends RecyclerView.Adapter<Adapter_Kelakuan_Anak.MyViewHolder> {

    Context context;
    ArrayList<PengaduanClass> list;

    public Adapter_Kelakuan_Anak(Context context,ArrayList<PengaduanClass> list){
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public Adapter_Kelakuan_Anak.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_kelakuan_siswa, parent, false);
        return new Adapter_Kelakuan_Anak.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_Kelakuan_Anak.MyViewHolder holder, int position) {

        PengaduanClass pengaduanClass = list.get(position);
        holder.jenisPelanggaran.setText(pengaduanClass.getJenisPelanggaran());
        holder.deskripsiPelanggaran.setText(pengaduanClass.getDeskripsi());
        holder.poinPelanggaran.setText(pengaduanClass.getPoin());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView deskripsiPelanggaran, poinPelanggaran, jenisPelanggaran;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            deskripsiPelanggaran = itemView.findViewById(R.id.deskripsiPelanggaran);
            poinPelanggaran = itemView.findViewById(R.id.poinPelangggaran);
            jenisPelanggaran = itemView.findViewById(R.id.jenisPelanggaran);
        }
    }
}
