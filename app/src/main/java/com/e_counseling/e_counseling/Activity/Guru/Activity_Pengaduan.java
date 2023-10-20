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

public class Activity_Pengaduan extends AppCompatActivity {

    RecyclerView rcPengaduan;
    AdapterPengaduan myAdapter;
    ArrayList<PengaduanClass> list;
    DatabaseReference dbPengaduan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pengaduan);

        Activity_DashboardGuruBK.i = 1;

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        rcPengaduan = findViewById(R.id.rcPengaduan);
        rcPengaduan.setHasFixedSize(true);
        rcPengaduan.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        myAdapter = new AdapterPengaduan(this, list);
        rcPengaduan.setAdapter(myAdapter);

        dbPengaduan = FirebaseDatabase
                .getInstance("https://e-counseling-61974-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("Pengaduan");

        getData();

    }

    private void getData(){
        dbPengaduan.orderByChild("status").equalTo("Belum diproses").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){

                    PengaduanClass pengaduanClass = dataSnapshot.getValue(PengaduanClass.class);
                    list.add(pengaduanClass);
                }
                myAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}