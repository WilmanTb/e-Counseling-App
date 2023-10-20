package com.e_counseling.e_counseling.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.e_counseling.e_counseling.Model.SkorSiswa;
import com.e_counseling.e_counseling.R;

import java.util.List;

public class AdapterSkorSiswa extends RecyclerView.Adapter<AdapterSkorSiswa.ViewHolder>{

    Context context;
    List<SkorSiswa> list;

    public AdapterSkorSiswa(Context context, List<SkorSiswa> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.skor_siswa, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (list != null && list.size() > 0){
            SkorSiswa skorSiswa = list.get(position);
            holder.tv_NamaSiswa.setText(skorSiswa.getNama());
            holder.tv_Skor.setText(skorSiswa.getSkorPerilaku());
            holder.tv_Status.setText(skorSiswa.getStatus());
        }else {
            return;
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_NamaSiswa, tv_Skor, tv_Status;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_NamaSiswa = itemView.findViewById(R.id.tv_NamaSiswa);
            tv_Skor = itemView.findViewById(R.id.tv_Skor);
            tv_Status = itemView.findViewById(R.id.tv_Status);
        }
    }
}
