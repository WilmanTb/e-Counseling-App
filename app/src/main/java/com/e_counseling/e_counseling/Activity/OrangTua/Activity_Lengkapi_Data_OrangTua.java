package com.e_counseling.e_counseling.Activity.OrangTua;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.e_counseling.e_counseling.Activity.Siswa.Activity_Pengaduan_Siswa;
import com.e_counseling.e_counseling.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Activity_Lengkapi_Data_OrangTua extends AppCompatActivity {

    private EditText etNama, etEmail, etAlamat, etNoHp;
    private Button btnSubmit;
    private DatabaseReference dbRef;
    private String UID, Nama, Email, Alamat, NoHp;
    private String nomorHp, NisnAnak, UidAnak, NAnak,nisnanak;
    private ArrayList<String> arrayNisnAnak = new ArrayList<>();
    private AutoCompleteTextView nisnAnak;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lengkapi_data_orang_tua);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dbRef = FirebaseDatabase
                .getInstance("https://e-counseling-61974-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference();

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        UID = firebaseUser.getUid();

        getID();
        setHint();
        cekNoHp();
        autoCompletListener();
        cekNISNAnak();

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animationClick(view);
                checkEnabled();
            }
        });

    }

    private void getID() {
        etNama = findViewById(R.id.etNama);
        etEmail = findViewById(R.id.etEmail);
        etAlamat = findViewById(R.id.etAlamat);
        etNoHp = findViewById(R.id.etNoHp);
        btnSubmit = findViewById(R.id.btnSubmit);
        nisnAnak = findViewById(R.id.nisnAnak);

    }

    private void setHint() {
        dbRef.child("Users").child(UID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Nama = snapshot.child("Nama").getValue().toString();
                Email = snapshot.child("Email").getValue().toString();
                Alamat = snapshot.child("Alamat").getValue().toString();
                //set Hint
                etNama.setHint(Nama);
                etEmail.setHint(Email);
                etAlamat.setHint(Alamat);
                disableEditTex();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void disableEditTex() {
        etNama.setEnabled(false);
        etEmail.setEnabled(false);
        etAlamat.setEnabled(false);
    }

    private void cekNoHp() {
        dbRef.child("Users").child(UID).child("NomorHp").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                NoHp = snapshot.getValue().toString();

                if (NoHp.equals("empty")) {
                    etNoHp.setHint("Masukkan Nomor Hp");
                    etNoHp.setEnabled(true);
                } else {
                    etNoHp.setHint(NoHp);
                    etNoHp.setEnabled(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void cekNISNAnak() {
        dbRef.child("Users").child(UID).child("NISNAnak").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    NisnAnak = snapshot.getValue().toString();
                    if (NisnAnak.equals("empty")) {
                        nisnAnak.setHint("Masukkan NISN Anak");
                        nisnAnak.setEnabled(true);
                    } else {
                        setNisnAnak(new NisnCallBack() {
                            @Override
                            public void onCallback(String NISN) {
                                nisnAnak.setHint(NISN);
                                nisnAnak.setEnabled(false);
                            }
                        });
                       /* Toast.makeText(Activity_Lengkapi_Data_OrangTua.this, NAnak, Toast.LENGTH_SHORT).show();
                        nisnAnak.setHint(NAnak);
                        nisnAnak.setEnabled(false);*/
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void autoCompletListener() {
        dbRef.child("Users").orderByChild("Status").equalTo("Siswa").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayNisnAnak.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    arrayNisnAnak.add(dataSnapshot.child("NISN").getValue(String.class));
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(Activity_Lengkapi_Data_OrangTua.this, android.R.layout.simple_list_item_1, arrayNisnAnak);
                nisnAnak.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getUIDAnak(FirebaseCallback firebaseCallback) {
        String x = nisnAnak.getText().toString();
        dbRef.child("Users").orderByChild("NISN").equalTo(x).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        UidAnak = dataSnapshot.getKey();
                        break;
                    }
                    firebaseCallback.onCallback(UidAnak);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void animationClick(View view) {
        Context context = this;
        view.startAnimation(AnimationUtils.loadAnimation(context, R.anim.animation_click));
    }

    private void checkEnabled() {
        nomorHp = etNoHp.getText().toString();
        nisnanak = nisnAnak.getText().toString();
        if (!etNoHp.isEnabled() || !nisnAnak.isEnabled()) {
            Toast.makeText(this, "Data anda sudah lengkap", Toast.LENGTH_SHORT).show();
        } else if (etNoHp.isEnabled() || nisnAnak.isEnabled()) {
            if (!nomorHp.isEmpty() || !nisnanak.isEmpty()) {
                getUIDAnak(new FirebaseCallback() {
                    @Override
                    public void onCallback(String Uid) {
                        dbRef.child("Users").child(UID).child("NISNAnak").setValue(Uid);
                        dbRef.child("Users").child(UID).child("NomorHp").setValue(nomorHp);
                    }
                });

            } else {
                Toast.makeText(this, "Nomor HP dan NISN Anak Tidak Boleh Kosong", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void setNisnAnak(NisnCallBack nisnCallBack) {
        dbRef.child("Users").child(NisnAnak).child("NISN").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    NAnak = snapshot.getValue().toString();
                    nisnCallBack.onCallback(NAnak);
                    //Toast.makeText(Activity_Lengkapi_Data_OrangTua.this, NAnak, Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(Activity_Lengkapi_Data_OrangTua.this, "FUCKCKCKCKKCKC", Toast.LENGTH_SHORT).show();
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

    private interface NisnCallBack {
        void onCallback(String NISN);
    }

}
