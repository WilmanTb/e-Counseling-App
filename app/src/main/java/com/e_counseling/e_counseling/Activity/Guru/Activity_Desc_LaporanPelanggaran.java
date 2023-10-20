package com.e_counseling.e_counseling.Activity.Guru;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.TextView;

import com.e_counseling.e_counseling.Model.PengaduanClass;
import com.e_counseling.e_counseling.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Activity_Desc_LaporanPelanggaran extends AppCompatActivity {

    private TextView idPelanggaran, namaPelanggar, Kelas, jenisPelanggaran, skorPerilaku, deskripsiPelanggaran;
    private DatabaseReference dbRef;
    private String Pelanggar, nmPelanggar, kelasPelanggar, KelasPelanggar, Key;
    public boolean x = false;
    PengaduanClass pengaduanClass = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_desc_laporan_pelanggaran);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dbRef = FirebaseDatabase
                .getInstance("https://e-counseling-61974-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference();

        getID();
        setText();

    }

    private void getID() {
        idPelanggaran = findViewById(R.id.IdPelanggaran);
        namaPelanggar = findViewById(R.id.NamaPelanggar);
        Kelas = findViewById(R.id.kelasPelanggar);
        jenisPelanggaran = findViewById(R.id.jenisPelanggaran);
        skorPerilaku = findViewById(R.id.skorPerilaku);
        deskripsiPelanggaran = findViewById(R.id.deskripsiPelanggaran);
    }

    private void setText() {
        final Object object = getIntent().getSerializableExtra("deskripsi");
        if (object instanceof PengaduanClass) {
            pengaduanClass = (PengaduanClass) object;
        }
        if (pengaduanClass != null) {
            idPelanggaran.setText(pengaduanClass.getIdPengaduan());
            jenisPelanggaran.setText(pengaduanClass.getJenisPelanggaran());
            skorPerilaku.setText(pengaduanClass.getPoin());
            Pelanggar = pengaduanClass.getPelanggar();
            deskripsiPelanggaran.setText(pengaduanClass.getDeskripsi());
            getNamaPelanggar(new FirebaseCallback() {
                @Override
                public void onCallback(String Uid) {
                    if (x == true){
                        getKelas(Uid);
                    }
                }
            });
        }
    }

    private void getNamaPelanggar(FirebaseCallback firebaseCallback) {
        dbRef.child("Users").orderByChild("Nama").equalTo(Pelanggar).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                        Key = dataSnapshot.getKey();
                        x = true;
                        break;
                    }
                    firebaseCallback.onCallback(Key);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getKelas(String UID){
        dbRef.child("Users").child(UID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                nmPelanggar = snapshot.child("Nama").getValue().toString();
                kelasPelanggar = snapshot.child("Kelas").getValue().toString();
                namaPelanggar.setText(nmPelanggar);
                Kelas.setText(kelasPelanggar);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private interface FirebaseCallback{
        void onCallback(String Uid);
    }

}