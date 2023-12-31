package com.e_counseling.e_counseling.Activity.Siswa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.e_counseling.e_counseling.Activity.Activity_Login;
import com.e_counseling.e_counseling.Activity.Guru.Activity_DashboardGuruBK;
import com.e_counseling.e_counseling.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Activity_Verifikasi_Email extends AppCompatActivity {
    private TextView infoEmail, statusEmail, infoVerifikasi, tvEmail;
    private Button btnVerifikasiEmail;
    private ImageView imgStatusEmail;
    private DatabaseReference dbRef;
    private Dialog dialog;
    String UID;
    FirebaseUser firebaseUser;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verifikasi_email);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();

        dbRef = FirebaseDatabase
                .getInstance("https://e-counseling-61974-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference();

        getID();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        UID = firebaseUser.getUid();

        getEmail();

        if (!mAuth.getCurrentUser().isEmailVerified()){
            btnVerifikasiEmail.setVisibility(View.VISIBLE);
            infoEmail.setText("Alamat email anda belum diverifikasi!");
            statusEmail.setText("UNVERIFIED");
            imgStatusEmail.setBackgroundResource(R.drawable.wrong);
            infoVerifikasi.setVisibility(View.VISIBLE);
        }else {
            btnVerifikasiEmail.setVisibility(View.INVISIBLE);
            infoEmail.setText("Alamat email anda sudah diverifikasi!");
            statusEmail.setText("VERIFIED");
            imgStatusEmail.setBackgroundResource(R.drawable.check);
            infoVerifikasi.setVisibility(View.INVISIBLE);
        }

        btnVerifikasiEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animationClick(view);
                mAuth.getCurrentUser().sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        setDialog();
                        mAuth.signOut();
                        startActivity(new Intent(Activity_Verifikasi_Email.this, Activity_Login.class));
                        finish();
                    }
                });
            }
        });

    }

    private void getID(){
        infoEmail = findViewById(R.id.infoEmail);
        statusEmail = findViewById(R.id.statusEmail);
        infoVerifikasi = findViewById(R.id.infoVerifikasi);
        tvEmail = findViewById(R.id.tvEmail);
        btnVerifikasiEmail = findViewById(R.id.btnVerifkasiEmail);
        imgStatusEmail = findViewById(R.id.imgStatusEmail);
        imgStatusEmail.setBackgroundResource(R.drawable.wrong);
    }

    private void getEmail(){
        dbRef.child("Users").child(UID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String emailUser = snapshot.child("Email").getValue().toString();
                tvEmail.setText(emailUser);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setDialog(){
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.popup_verifikasi_email);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private void animationClick(View view){
        Context context = this;
        view.startAnimation(AnimationUtils.loadAnimation(context, R.anim.animation_click));
    }
}