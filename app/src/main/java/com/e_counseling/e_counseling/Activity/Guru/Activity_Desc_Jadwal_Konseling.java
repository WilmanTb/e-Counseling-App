package com.e_counseling.e_counseling.Activity.Guru;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.e_counseling.e_counseling.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;

import java.net.MalformedURLException;
import java.net.URL;

public class Activity_Desc_Jadwal_Konseling extends AppCompatActivity {

    private TextView jenisKonseling, metodeKonseling, namaSiswa, perihalKonseling, tanggalKonseling, orangTuaSiswa;
    private Button btnMulaiKonseling;
    private String NamaOrangTua ="", OrangTua, test = "1";
    private DatabaseReference dbRef;
    BuatJadwal buatJadwal = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_desc_jadwal_konseling);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dbRef = FirebaseDatabase
                .getInstance("https://e-counseling-61974-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference();

        getID();
        setText();

        btnMulaiKonseling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animationClick(view);
                try {
                    String NamaRoom = buatJadwal.getNamaRuang();
                    Toast.makeText(Activity_Desc_Jadwal_Konseling.this, NamaRoom, Toast.LENGTH_SHORT).show();
                    JitsiMeetConferenceOptions options = new JitsiMeetConferenceOptions.Builder()
                            .setServerURL(new URL("https://meet.jit.si"))
                            .setRoom(NamaRoom.replaceAll("\\s+",""))
                            .setAudioMuted(false)
                            .setVideoMuted(false)
                            .setAudioOnly(false)
                            .setWelcomePageEnabled(false)
                            .setConfigOverride("requireDisplayName", true)
                            .build();
                    JitsiMeetActivity.launch(Activity_Desc_Jadwal_Konseling.this, options);
                } catch (MalformedURLException e) {
                    /*e.printStackTrace();*/
                    Toast.makeText(Activity_Desc_Jadwal_Konseling.this, String.valueOf(e), Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
    private void getID(){
        jenisKonseling = findViewById(R.id.jenisKonseling);
        metodeKonseling = findViewById(R.id.metodeKonseling);
        namaSiswa = findViewById(R.id.namaSiswa);
        perihalKonseling = findViewById(R.id.perihalKonseling);
        tanggalKonseling = findViewById(R.id.tanggalKonseling);
        orangTuaSiswa = findViewById(R.id.orangTuaSiswa);
        btnMulaiKonseling = findViewById(R.id.btnMulaiKonseling);
    }

    private void setText(){
        final Object object = getIntent().getSerializableExtra("deskripsi");
        if (object instanceof BuatJadwal) {
            buatJadwal = (BuatJadwal) object;
        }
        if (buatJadwal != null) {
           jenisKonseling.setText(buatJadwal.getJenisKonseling());
           metodeKonseling.setText(buatJadwal.getMetodeKonseling());
           namaSiswa.setText(buatJadwal.getPelanggar());
           perihalKonseling.setText(buatJadwal.getPerihal());
           tanggalKonseling.setText(buatJadwal.getTanggal());
           NamaOrangTua = buatJadwal.getUidOrangTua();
           getNamaOrangTua();
        }
    }

    private void getNamaOrangTua(){
        dbRef.child("Users").child(NamaOrangTua).child("Nama").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    OrangTua = snapshot.getValue().toString();
                    orangTuaSiswa.setText(OrangTua);
                }else{
                    Toast.makeText(Activity_Desc_Jadwal_Konseling.this, "snapshot kosong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void animationClick(View view){
        Context context = this;
        view.startAnimation(AnimationUtils.loadAnimation(context,R.anim.animation_click));
    }


    /*private void createRoomName(){
        dbRef.child()
    }
*/
}