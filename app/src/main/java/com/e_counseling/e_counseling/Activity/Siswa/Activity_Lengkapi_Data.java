package com.e_counseling.e_counseling.Activity.Siswa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.e_counseling.e_counseling.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.net.Proxy;
import java.util.ArrayList;

public class Activity_Lengkapi_Data extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private TextInputLayout iNisn, iNama, iEmail, iAlamat, iNoHp;
    private EditText etNoHp, etNisn, etNama, etEmail, etAlamat;
    private Button btnSubmit;
    String Jurusan, Kelas, Nama = "", Alamat = "", Email = "", NISN = "", noHp, UID, jurusan, kelas, nohp;
    private ArrayList<String> arrayListKelas = new ArrayList<>();
    private Spinner SpinnerJurusan, SpinnerDataKelas;
    private DatabaseReference dbRef;
    private ArrayAdapter<CharSequence> adapterJurusan;
    private ArrayAdapter<CharSequence> adapterKelas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lengkapi_data);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dbRef = FirebaseDatabase
                .getInstance("https://e-counseling-61974-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference();

        getID();
        spinnerJurusan();
        spinnerKelas();
        getData();
        cekData();



        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animationClick(view);
                checkEmpty();
            }
        });

        noHp = etNoHp.getText().toString();

    }

    private void getID(){
        iNisn = findViewById(R.id.nisn);
        iNama = findViewById(R.id.nama);
        iEmail = findViewById(R.id.email);
        iAlamat = findViewById(R.id.alamat);
        iNoHp = findViewById(R.id.hp);
        etNisn = findViewById(R.id.etNisn);
        etNama = findViewById(R.id.etNama);
        etAlamat = findViewById(R.id.etAlamat);
        etEmail = findViewById(R.id.etEmail);
        etNoHp = findViewById(R.id.etNoHp);
        btnSubmit = findViewById(R.id.btnSubmit);
        SpinnerJurusan = findViewById(R.id.jurusanSiswa);
        SpinnerDataKelas = findViewById(R.id.kelasSiswa);
    }

    private void checkEmpty(){
        if (noHp.isEmpty() || Jurusan.equals("") || Kelas.equals("")){
            Toast.makeText(this, "Form tidak boleh kosong", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "Form terisi semua", Toast.LENGTH_SHORT).show();
        }
    }

    private void spinnerJurusan() {
        adapterJurusan = ArrayAdapter.createFromResource(this, R.array.Jurusan, android.R.layout.simple_spinner_item);
        adapterJurusan.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SpinnerJurusan.setAdapter(adapterJurusan);
        SpinnerJurusan.setOnItemSelectedListener(this);

    }

    private void spinnerKelas(){
        /*dbRef.child("DaftarKelas").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayListKelas.clear();
                arrayListKelas.add(0, "Pilih Kelas");
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    arrayListKelas.add(dataSnapshot.child("Kelas").getValue(String.class));
                }
                adapterKelas = new ArrayAdapter(Activity_Lengkapi_Data.this, android.R.layout.simple_spinner_item, arrayListKelas);
                adapterKelas.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                SpinnerDataKelas.setAdapter(adapterKelas);
                SpinnerDataKelas.setOnItemSelectedListener(Activity_Lengkapi_Data.this);
                String x = "X-1";
                int a = adapterKelas.getPosition(x);
                SpinnerDataKelas.setSelection(a);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/
        adapterKelas = ArrayAdapter.createFromResource(this, R.array.Kelas, android.R.layout.simple_spinner_item);
        adapterKelas.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SpinnerDataKelas.setAdapter(adapterKelas);
        SpinnerDataKelas.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        SpinnerJurusan = (Spinner)adapterView;
        SpinnerDataKelas = (Spinner)adapterView;
        if (SpinnerJurusan.getId() == R.id.jurusanSiswa){
            if (!adapterView.getItemAtPosition(i).equals("Pilih Jurusan")) {
                Jurusan = adapterView.getItemAtPosition(i).toString();
            }
        }else if (SpinnerDataKelas.getId() == R.id.kelasSiswa){
            if (!adapterView.getItemAtPosition(i).equals("Pilih Jurusan")) {
                Kelas = adapterView.getItemAtPosition(i).toString();
            }
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void getData(){
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        UID = firebaseUser.getUid();
        dbRef.child("Users").child(UID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Nama = snapshot.child("Nama").getValue().toString();
                Alamat = snapshot.child("Alamat").getValue().toString();
                Email = snapshot.child("Email").getValue().toString();
                NISN = snapshot.child("NISN").getValue().toString();
                setHint();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setHint(){
        etNama.setEnabled(false);
        etNisn.setEnabled(false);
        etAlamat.setEnabled(false);
        etEmail.setEnabled(false);
        iNama.setHint(Nama);
        iNisn.setHint(NISN);
        iAlamat.setHint(Alamat);
        iEmail.setHint(Email);
    }

    private void cekData(){
        dbRef.child("Users").child(UID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                jurusan = snapshot.child("Jurusan").getValue().toString();
                kelas = snapshot.child("Kelas").getValue().toString();
                nohp = snapshot.child("NomorHp").getValue().toString();
                disableInputData();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void disableInputData(){
        if (!nohp.equals("empty") || !jurusan.equals("empty") || !kelas.equals("empty")){
            etNoHp.setEnabled(false);
            iNoHp.setHint(nohp);
            int spinnerPosition1 = adapterJurusan.getPosition(jurusan);
            int spinnerPosition2 = adapterKelas.getPosition(kelas);
            SpinnerJurusan.setSelection(spinnerPosition1);
            SpinnerJurusan.setEnabled(false);
            SpinnerDataKelas.setSelection(spinnerPosition2);
            SpinnerDataKelas.setEnabled(false);
        }
    }

    private void animationClick(View view){
        Context context = this;
        view.startAnimation(AnimationUtils.loadAnimation(context, R.anim.animation_click));
    }
}