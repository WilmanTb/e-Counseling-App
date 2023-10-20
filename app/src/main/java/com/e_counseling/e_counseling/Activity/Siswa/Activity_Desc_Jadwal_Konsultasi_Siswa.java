package com.e_counseling.e_counseling.Activity.Siswa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import com.e_counseling.e_counseling.Activity.Guru.BuatJadwal;
import com.e_counseling.e_counseling.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;

import java.net.MalformedURLException;
import java.net.URL;

public class Activity_Desc_Jadwal_Konsultasi_Siswa extends AppCompatActivity {

    private TextView konselingSiswa, jadwalKonseling, perihalKonseling,
            metodeKonseling, orangTuaSiswa, namaSiswa;
    BuatJadwal jadwalKonselingClass = new BuatJadwal();
    private Button btnMulaiKonseling;
    String uidAnak, nmSiswa, nmAnak, Key,UidOrangTua, nmOrangTua;
    private DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_desc_jadwal_konsultasi_siswa);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dbRef = FirebaseDatabase
                .getInstance("https://e-counseling-61974-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference();

        getId();
        getData();

        btnMulaiKonseling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String NamaRoom = jadwalKonselingClass.getNamaRuang();
                    JitsiMeetConferenceOptions options = new JitsiMeetConferenceOptions.Builder()
                            .setServerURL(new URL("https://meet.jit.si"))
                            .setRoom(NamaRoom.replaceAll("\\s+",""))
                            .setAudioMuted(false)
                            .setVideoMuted(false)
                            .setAudioOnly(false)
                            .setWelcomePageEnabled(false)
                            .setConfigOverride("requireDisplayName", true)
                            .build();
                    JitsiMeetActivity.launch(Activity_Desc_Jadwal_Konsultasi_Siswa.this, options);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void getId(){
        konselingSiswa = findViewById(R.id.konselingSiswa);
        jadwalKonseling = findViewById(R.id.jadwalKonseling);
        perihalKonseling = findViewById(R.id.perihalKonseling);
        metodeKonseling = findViewById(R.id.metodeKonseling);
        orangTuaSiswa = findViewById(R.id.orangTuaSiswa);
        namaSiswa = findViewById(R.id.namaSiswa);
        btnMulaiKonseling = findViewById(R.id.btnMulaiKonseling);
    }

    public void getData(){
        final Object object = getIntent().getSerializableExtra("deskripsi_konseling");
        if (object instanceof BuatJadwal){
            jadwalKonselingClass = (BuatJadwal) object;
        }

        if (jadwalKonselingClass != null){
            konselingSiswa.setText(jadwalKonselingClass.getJenisKonseling());
            jadwalKonseling.setText(jadwalKonselingClass.getTanggal());
            perihalKonseling.setText(jadwalKonselingClass.getPerihal());
            metodeKonseling.setText(jadwalKonselingClass.getMetodeKonseling());
            UidOrangTua = jadwalKonselingClass.getUidOrangTua();
        }

        getNamaSiswa();
        getNamaOrangTua();
    }

    private void getNamaSiswa(){
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        uidAnak = firebaseUser.getUid();
        dbRef.child("Users").child(uidAnak).child("Nama").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               nmSiswa = snapshot.getValue().toString();
               namaSiswa.setText(nmSiswa);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getNamaOrangTua(){
        dbRef.child("Users").child(UidOrangTua).child("Nama").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                nmOrangTua = snapshot.getValue().toString();
                orangTuaSiswa.setText(nmOrangTua);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void animationClick(View view){
        Context context = this;
        view.startAnimation(AnimationUtils.loadAnimation(context, R.anim.animation_click));
    }
}