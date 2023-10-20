package com.e_counseling.e_counseling.Activity.Siswa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.e_counseling.e_counseling.Adapter.Adapter_Kelakuan_Siswa;
import com.e_counseling.e_counseling.Model.PengaduanClass;
import com.e_counseling.e_counseling.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Activity_Kelakuan_Siswa extends AppCompatActivity {

    RecyclerView rcKelakuanSiswa;
    Adapter_Kelakuan_Siswa myAdapter;
    ArrayList<PengaduanClass> list;
    DatabaseReference dbRef;
    String UID, Nama = "", Key = "", Status;
    FirebaseUser firebaseUser;
    public boolean status = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kelakuan_siswa);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        rcKelakuanSiswa = findViewById(R.id.rcKelakuanSiswa);
        rcKelakuanSiswa.setHasFixedSize(true);
        rcKelakuanSiswa.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        myAdapter = new Adapter_Kelakuan_Siswa(this, list);
        rcKelakuanSiswa.setAdapter(myAdapter);

        dbRef = FirebaseDatabase
                .getInstance("https://e-counseling-61974-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        getNama();
        getData1();

    }

    private void getNama(){
        UID = firebaseUser.getUid();
        dbRef.child("Users").child(UID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Nama = snapshot.child("Nama").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getData1(){
        dbRef.child("Pengaduan").orderByChild("status").equalTo("Diterima").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    if(dataSnapshot.child("pelanggar").getValue().equals(Nama)){
                        PengaduanClass pengaduanClass = dataSnapshot.getValue(PengaduanClass.class);
                        list.add(pengaduanClass);
                    }

                }
                myAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}

