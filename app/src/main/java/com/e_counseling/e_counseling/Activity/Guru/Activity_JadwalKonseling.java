package com.e_counseling.e_counseling.Activity.Guru;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.e_counseling.e_counseling.Adapter.Adapter_Jadwal_Konseling;
import com.e_counseling.e_counseling.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Activity_JadwalKonseling extends AppCompatActivity {

    private FloatingActionButton btnTambahJadwal;
    private RecyclerView rcJadwalKonseling;
    private Adapter_Jadwal_Konseling myAdapter;
    private ArrayList<BuatJadwal> list;
    private DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jadwal_konseling);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dbRef = FirebaseDatabase
                .getInstance("https://e-counseling-61974-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference();

        rcJadwalKonseling = findViewById(R.id.rcJadwalKonseling);
        rcJadwalKonseling.setHasFixedSize(true);
        rcJadwalKonseling.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        myAdapter = new Adapter_Jadwal_Konseling(this, list);
        rcJadwalKonseling.setAdapter(myAdapter);

        btnTambahJadwal = findViewById(R.id.btnTambahJadwal);

        btnTambahJadwal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_JadwalKonseling.this, Activity_Buat_Jadwal_Konseling.class));
            }
        });

        getData();

    }

    private void getData() {
        dbRef.child("JadwalKonseling").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    list.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
//                        Toast.makeText(Activity_JadwalKonseling.this, String.valueOf(dataSnapshot.getKey()), Toast.LENGTH_SHORT).show();
                        BuatJadwal buatJadwal = dataSnapshot.getValue(BuatJadwal.class);
                        list.add(buatJadwal);
                    }
                    myAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}