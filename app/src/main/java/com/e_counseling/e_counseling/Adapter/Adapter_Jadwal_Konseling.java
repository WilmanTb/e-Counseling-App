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

import com.e_counseling.e_counseling.Activity.Guru.Activity_Desc_Jadwal_Konseling;
import com.e_counseling.e_counseling.Activity.Guru.BuatJadwal;
import com.e_counseling.e_counseling.R;

import java.util.ArrayList;

public class Adapter_Jadwal_Konseling extends RecyclerView.Adapter<Adapter_Jadwal_Konseling.MyViewHolder> {

    Context context;
    ArrayList<BuatJadwal> list;
    private Adapter_Jadwal_Konseling.descJadwal DescJadwal;

    public interface descJadwal {
        void clickCardView(int position);
    }

    public void descJadwal(Adapter_Jadwal_Konseling.descJadwal DescJadwal) {
        this.DescJadwal = DescJadwal;
    }


    public Adapter_Jadwal_Konseling(Context context, ArrayList<BuatJadwal> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_jadwal_konseling, parent, false);
        return new Adapter_Jadwal_Konseling.MyViewHolder(view,DescJadwal);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        BuatJadwal buatJadwal = list.get(position);
        holder.jenisKonseling.setText(buatJadwal.getJenisKonseling());
        holder.metodeKonseling.setText(buatJadwal.getMetodeKonseling());
        holder.waktuKonseling.setText(buatJadwal.getTanggal());
        holder.cvListJadwalKonseling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, Activity_Desc_Jadwal_Konseling.class).putExtra("deskripsi", list.get(holder.getAdapterPosition())));
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView jenisKonseling, waktuKonseling, metodeKonseling;
        CardView cvListJadwalKonseling;

        public MyViewHolder(@NonNull View itemView, descJadwal descJadwal) {
            super(itemView);

            jenisKonseling = itemView.findViewById(R.id.jenisKonseling);
            waktuKonseling = itemView.findViewById(R.id.waktuKonseling);
            metodeKonseling = itemView.findViewById(R.id.metodeKonseling);
            cvListJadwalKonseling = itemView.findViewById(R.id.cvListJadwalKonseling);
        }
    }
}
