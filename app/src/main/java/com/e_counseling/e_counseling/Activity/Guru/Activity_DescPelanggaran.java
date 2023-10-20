package com.e_counseling.e_counseling.Activity.Guru;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.e_counseling.e_counseling.Model.PengaduanClass;
import com.e_counseling.e_counseling.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Activity_DescPelanggaran extends AppCompatActivity {

    private TextView deskripsiPelanggaran, jenisPelanggaran, pelaporPelanggaran, saranTindakan, waktuPelanggaran,
            idPengaduan, siswaPelanggar, tipePelanggaran;

    private Button btnTerima, btnTolak;

    private DatabaseReference dbPelanggaran;

    private ImageView fotoPelanggaran;

    String Node = "", ID;

    PengaduanClass pengaduanClass = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_desc_pelanggaran);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getId();
        getData();

        dbPelanggaran = FirebaseDatabase
                .getInstance("https://e-counseling-61974-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("Pengaduan");

        btnTerima.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getNode(new FirebaseCallback() {
                    @Override
                    public void onCallback(String Uid) {
                        clickAnimation(view);
                        AlertDialog alertDialog = new AlertDialog.Builder(Activity_DescPelanggaran.this)
                                .setMessage("Apakah anda yakin menerima pengaduan?")
                                .setPositiveButton("Ya", null)
                                .setNegativeButton("Tidak", null)
                                .show();
                        Button positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                        positiveButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dbPelanggaran.child(Node).child("status").setValue("Diterima");
                                startActivity(new Intent(Activity_DescPelanggaran.this, Activity_Pengaduan.class));
                                finish();
                                alertDialog.dismiss();
                            }
                        });

                    }
                });
            }
        });

        btnTolak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getNode(new FirebaseCallback() {
                    @Override
                    public void onCallback(String Uid) {
                        clickAnimation(view);
                        AlertDialog alertDialog = new AlertDialog.Builder(Activity_DescPelanggaran.this)
                                .setMessage("Apakah anda yakin menolak pengaduan?")
                                .setPositiveButton("Ya", null)
                                .setNegativeButton("Tidak", null)
                                .show();
                        Button positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                        positiveButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dbPelanggaran.child(Node).child("status").setValue("Ditolak");
                                startActivity(new Intent(Activity_DescPelanggaran.this, Activity_Pengaduan.class));
                                finish();
                                alertDialog.dismiss();
                            }
                        });
                    }
                });
            }
        });

    }

    private void getId() {
        deskripsiPelanggaran = findViewById(R.id.deskripsiPelanggaran);
        jenisPelanggaran = findViewById(R.id.jenisPelanggaran);
        pelaporPelanggaran = findViewById(R.id.pelaporPelanggaran);
        saranTindakan = findViewById(R.id.saranTindakan);
        waktuPelanggaran = findViewById(R.id.waktuPelanggaran);
        idPengaduan = findViewById(R.id.idPengaduan);
        siswaPelanggar = findViewById(R.id.siswaPelanggar);
        btnTerima = findViewById(R.id.btnTerima);
        btnTolak = findViewById(R.id.btnTolak);
        fotoPelanggaran = findViewById(R.id.fotoPelanggaran);
        tipePelanggaran = findViewById(R.id.tipePelanggaran);

    }

    public void getData() {
        final Object object = getIntent().getSerializableExtra("deskripsi");
        if (object instanceof PengaduanClass) {
            pengaduanClass = (PengaduanClass) object;
        }
        if (pengaduanClass != null) {
            deskripsiPelanggaran.setText(pengaduanClass.getDeskripsi());
            jenisPelanggaran.setText(pengaduanClass.getJenisPelanggaran());
            pelaporPelanggaran.setText(pengaduanClass.getPelapor());
            saranTindakan.setText(pengaduanClass.getSaranTindakan());
            waktuPelanggaran.setText(pengaduanClass.getWaktuPelanggaran());
            idPengaduan.setText(pengaduanClass.getIdPengaduan());
            siswaPelanggar.setText(pengaduanClass.getPelanggar());
            tipePelanggaran.setText(pengaduanClass.getTipePelanggaran());
            Glide.with(getApplicationContext()).load(pengaduanClass.getFoto()).into(fotoPelanggaran);
            ID = idPengaduan.getText().toString();
        }
    }

    private void getNode(FirebaseCallback firebaseCallback) {
        dbPelanggaran.orderByChild("idPengaduan").equalTo(ID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        if (snapshot.exists()) {
                            Node = dataSnapshot.getKey();
                        }
                        break;
                    }
                    firebaseCallback.onCallback(Node);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private interface FirebaseCallback {
        void onCallback(String Uid);
    }

    private void clickAnimation(View view) {
        Context context = this;
        view.startAnimation(AnimationUtils.loadAnimation(context, R.anim.animation_click));
    }
}