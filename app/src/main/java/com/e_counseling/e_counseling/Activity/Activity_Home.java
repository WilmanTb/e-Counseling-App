package com.e_counseling.e_counseling.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Toast;

import com.e_counseling.e_counseling.Activity.Guru.Activity_DashboardGuruBK;
import com.e_counseling.e_counseling.Activity.OrangTua.Activity_Dashboard_OrangTua;
import com.e_counseling.e_counseling.Activity.Siswa.Activity_Dashboard_Siswa;
import com.e_counseling.e_counseling.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Activity_Home extends AppCompatActivity {

    private Button btnDaftar, btnLogin;
    private DatabaseReference dbUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        dbUser = FirebaseDatabase
                .getInstance("https://e-counseling-61974-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("Users");

        btnDaftar = findViewById(R.id.btnDaftar);
        btnLogin = findViewById(R.id.btnLogin);

        btnDaftar.setOnClickListener(view -> {
            getAnimationClick(view);
            startActivity(new Intent(Activity_Home.this, Activity_Register.class));

        });

        btnLogin.setOnClickListener(view -> {
            getAnimationClick(view);
            startActivity(new Intent(Activity_Home.this, Activity_Login.class));

        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            String a = firebaseUser.getUid();
            dbUser.child(a).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String x = snapshot.child("Status").getValue().toString();
                    if (x.equals("Guru BK")) {
                        startActivity(new Intent(Activity_Home.this, Activity_DashboardGuruBK.class));
                        finish();
                    }else if (x.equals("Siswa")){
                        startActivity(new Intent(Activity_Home.this, Activity_Dashboard_Siswa.class));
                        finish();
                    }else if (x.equals("Orang tua")){
                        startActivity(new Intent(Activity_Home.this, Activity_Dashboard_OrangTua.class));
                        finish();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }else {
            Toast.makeText(Activity_Home.this, "User Kosong", Toast.LENGTH_SHORT).show();
        }
    }

    private void getAnimationClick(View view){
        Context context = this;
        view.startAnimation(AnimationUtils.loadAnimation(context, R.anim.animation_click));
    }

}