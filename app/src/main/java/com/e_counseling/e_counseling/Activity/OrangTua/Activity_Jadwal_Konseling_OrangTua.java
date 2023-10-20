package com.e_counseling.e_counseling.Activity.OrangTua;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.e_counseling.e_counseling.Activity.Guru.BuatJadwal;
import com.e_counseling.e_counseling.Adapter.AdapterJadwalKonselingSiswa;
import com.e_counseling.e_counseling.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Activity_Jadwal_Konseling_OrangTua extends AppCompatActivity {

    private DatabaseReference dbRef;
    private RecyclerView rcJadwalKonselingOrangTua;
    AdapterJadwalKonselingSiswa myAdapter;
    ArrayList<BuatJadwal> listJadwalKonseling;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jadwal_konseling_orang_tua);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dbRef = FirebaseDatabase
                .getInstance("https://e-counseling-61974-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference();

        setDataJadwal();

    }

    private void setDataJadwal() {
        rcJadwalKonselingOrangTua = findViewById(R.id.rcJadwalKonselingOrangTua);
        rcJadwalKonselingOrangTua.setHasFixedSize(true);
        rcJadwalKonselingOrangTua.setLayoutManager(new LinearLayoutManager(this));
        listJadwalKonseling = new ArrayList<>();
        myAdapter = new AdapterJadwalKonselingSiswa(this, listJadwalKonseling);
        rcJadwalKonselingOrangTua.setAdapter(myAdapter);

        getData();
    }

    private void getData(){
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String UID = firebaseUser.getUid();
        dbRef.child("JadwalKonseling").orderByChild("uidOrangTua").equalTo(UID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    listJadwalKonseling.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        BuatJadwal jadwalKonseling = dataSnapshot.getValue(BuatJadwal.class);
                        listJadwalKonseling.add(jadwalKonseling);
                    }

                    myAdapter.notifyDataSetChanged();

                }else{

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}