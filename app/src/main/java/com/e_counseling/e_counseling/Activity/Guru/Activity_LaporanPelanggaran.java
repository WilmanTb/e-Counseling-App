package com.e_counseling.e_counseling.Activity.Guru;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.e_counseling.e_counseling.Adapter.AdapterPengaduan;
import com.e_counseling.e_counseling.Model.PengaduanClass;
import com.e_counseling.e_counseling.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Activity_LaporanPelanggaran extends AppCompatActivity {

    private RecyclerView rcLaporanPelanggaran;
    AdapterPengaduan adapterPengaduan;
    ArrayList<PengaduanClass> list;
    DatabaseReference dbLaporanPelanggaran;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laporan_pelanggaran);

        Activity_DashboardGuruBK.i = 2;

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        rcLaporanPelanggaran = findViewById(R.id.rcLaporanPelanggaran);
        rcLaporanPelanggaran.setHasFixedSize(true);
        rcLaporanPelanggaran.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        adapterPengaduan = new AdapterPengaduan(this, list);
        rcLaporanPelanggaran.setAdapter(adapterPengaduan);

        dbLaporanPelanggaran = FirebaseDatabase
                .getInstance("https://e-counseling-61974-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("Pengaduan");

        getData();

    }

    private void getData() {
        dbLaporanPelanggaran.orderByChild("status").equalTo("Diterima")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        list.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            PengaduanClass pengaduanClass = dataSnapshot.getValue(PengaduanClass.class);
                            list.add(pengaduanClass);
                        }
                        adapterPengaduan.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}