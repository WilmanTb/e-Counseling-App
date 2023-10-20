package com.e_counseling.e_counseling.Activity.OrangTua;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.e_counseling.e_counseling.Adapter.Adapter_Kelakuan_Anak;
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

public class Activity_Kelakuan_Anak extends AppCompatActivity {

    RecyclerView rcKelakuanAnak;
    Adapter_Kelakuan_Anak myAdapter;
    ArrayList<PengaduanClass> list;
    DatabaseReference dbRef;
    String UID, NamaAnak = "", Key = "", Status, UidAnak;
    FirebaseUser firebaseUser;
    public boolean status = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kelakuan_anak);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        rcKelakuanAnak = findViewById(R.id.rcKelakuanAnak);
        rcKelakuanAnak.setHasFixedSize(true);
        rcKelakuanAnak.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        myAdapter = new Adapter_Kelakuan_Anak(this, list);
        rcKelakuanAnak.setAdapter(myAdapter);

        dbRef = FirebaseDatabase
                .getInstance("https://e-counseling-61974-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        getUIDANAK();


    }

    private void getUIDANAK() {
        UID = firebaseUser.getUid();
        dbRef.child("Users").child(UID).child("NISNAnak").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UidAnak = snapshot.getValue().toString();
                getNamaAnak(new CallBackNama() {
                    @Override
                    public void onCalbak(String NAMA) {
                        getData();
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getNamaAnak(CallBackNama callBackNama){
        dbRef.child("Users").child(UidAnak).child("Nama").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    NamaAnak = snapshot.getValue().toString();
                    callBackNama.onCalbak(NamaAnak);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getData() {
        dbRef.child("Pengaduan").orderByChild("status").equalTo("Diterima").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    if (dataSnapshot.child("pelanggar").getValue().equals(NamaAnak)) {
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

    private interface CallBackNama{
        void onCalbak(String NAMA);
    }

}