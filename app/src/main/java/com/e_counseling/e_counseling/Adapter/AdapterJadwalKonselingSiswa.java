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

import com.e_counseling.e_counseling.Activity.Guru.BuatJadwal;
import com.e_counseling.e_counseling.Activity.Siswa.Activity_Desc_Jadwal_Konsultasi_Siswa;
import com.e_counseling.e_counseling.R;

import java.util.ArrayList;

public class AdapterJadwalKonselingSiswa extends RecyclerView.Adapter<AdapterJadwalKonselingSiswa.MyViewHolder>{

    Context context;
    ArrayList<BuatJadwal> list;
    private descJadwal DescJadwal;

    public interface descJadwal {
        void clickCardView(int position);
    }

    public void descJadwal (descJadwal DescJadwal){
        this.DescJadwal = DescJadwal;
    }

    public AdapterJadwalKonselingSiswa(Context context,  ArrayList<BuatJadwal> list){
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public AdapterJadwalKonselingSiswa.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_jadwal_konseling_siswa,parent, false);
        return new MyViewHolder(view, DescJadwal);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterJadwalKonselingSiswa.MyViewHolder holder, int position) {
        BuatJadwal jadwalKonseling = list.get(position);
        holder.tvKonseling.setText(jadwalKonseling.getJenisKonseling());
        holder.jadwalKonseling.setText(jadwalKonseling.getTanggal());
        holder.perihalKonseling.setText(jadwalKonseling.getPerihal());
        holder.cvListJadwalKonseling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, Activity_Desc_Jadwal_Konsultasi_Siswa.class)
                        .putExtra("deskripsi_konseling", list.get(holder.getAdapterPosition())));

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvKonseling, jadwalKonseling, perihalKonseling, keteranganKonseling;
        CardView cvListJadwalKonseling;

        public MyViewHolder(@NonNull View itemView, descJadwal DescPelanggaran) {
            super(itemView);

            cvListJadwalKonseling = itemView.findViewById(R.id.cvListJadwalKonselingSiswa);
            tvKonseling = itemView.findViewById(R.id.tvKonseling);
            jadwalKonseling = itemView.findViewById(R.id.jadwalKonseling);
            perihalKonseling = itemView.findViewById(R.id.perihalKonseling);

        }
    }
}
