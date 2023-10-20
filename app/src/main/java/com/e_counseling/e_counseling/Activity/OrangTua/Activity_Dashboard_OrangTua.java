package com.e_counseling.e_counseling.Activity.OrangTua;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.e_counseling.e_counseling.Activity.Activity_Home;
import com.e_counseling.e_counseling.Activity.Activity_Login;
import com.e_counseling.e_counseling.Activity.Siswa.Activity_Dashboard_Siswa;
import com.e_counseling.e_counseling.Activity.Siswa.Activity_JadwalKonseling_Siswa;
import com.e_counseling.e_counseling.Activity.Siswa.Activity_Kelakuan_Siswa;
import com.e_counseling.e_counseling.Activity.Siswa.Activity_Pengaduan_Siswa;
import com.e_counseling.e_counseling.Activity.Siswa.Activity_Verifikasi_Email;
import com.e_counseling.e_counseling.Fragment.Fragment_Dashboard_OrangTua;
import com.e_counseling.e_counseling.Fragment.Fragment_Profil_OrangTua;
import com.e_counseling.e_counseling.Fragment.ProfileFragment;
import com.e_counseling.e_counseling.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import dmax.dialog.SpotsDialog;

public class Activity_Dashboard_OrangTua extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    private FirebaseAuth mAuth;
    private DatabaseReference dbUser;
    private Dialog dialog;
    private CardView cvVerifikasi;
    private FirebaseDatabase dbRef;
    private AlertDialog progressDialog;
    String Email, Nama;
    public static int i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_orang_tua);

        mAuth = FirebaseAuth.getInstance();

        progressDialog = new SpotsDialog.Builder().setContext(this).build();

        mAuth = FirebaseAuth.getInstance();
        dbRef = FirebaseDatabase.getInstance("https://e-counseling-61974-default-rtdb.asia-southeast1.firebasedatabase.app/");
        dbUser = dbRef.getReference("Users");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        TextView textView = (TextView) headerView.findViewById(R.id.userEmail);
        textView.setText(Email);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new Fragment_Dashboard_OrangTua()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser == null) {
            startActivity(new Intent(Activity_Dashboard_OrangTua.this, Activity_Home.class));
            finish();
        } else {
            getName();
        }
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case R.id.nav_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new Fragment_Dashboard_OrangTua()).commit();
                break;

            case R.id.nav_profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new Fragment_Profil_OrangTua()).commit();
                break;

            case R.id.nav_logout:
                progressDialog.show();
                mAuth.signOut();
                startActivity(new Intent(Activity_Dashboard_OrangTua.this, Activity_Login.class));
                finish();
                progressDialog.dismiss();
                break;

        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void onClickOrangTua(View view) {
        switch (view.getId()) {
            case R.id.cvLaporanKelakuanAnak:
                getAnimationClick(view);
               /* if (mAuth.getCurrentUser().isEmailVerified()) {*/
                    startActivity(new Intent(Activity_Dashboard_OrangTua.this, Activity_Kelakuan_Anak.class));
                /*}else{
                    setDialog();
                }*/
                break;

            case R.id.cvJadwalKonseling:
                getAnimationClick(view);
              /*  if (mAuth.getCurrentUser().isEmailVerified()) {*/
                    startActivity(new Intent(Activity_Dashboard_OrangTua.this, Activity_Jadwal_Konseling_OrangTua.class));
               /* }else{
                    setDialog();
                }*/
                break;
        }
    }

    private void getName() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String Uid = currentUser.getUid();
        dbUser.child(Uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Nama = snapshot.child("Nama").getValue().toString();
                    Email = snapshot.child("Email").getValue().toString();
                    drawer = findViewById(R.id.drawer_layout);
                    NavigationView navigationView = findViewById(R.id.nav_view);
                    navigationView.setNavigationItemSelectedListener(Activity_Dashboard_OrangTua.this);
                    View headerView = navigationView.getHeaderView(0);
                    TextView email = (TextView) headerView.findViewById(R.id.userEmail);
                    TextView nama = (TextView) headerView.findViewById(R.id.userName);
                    email.setText(Email);
                    nama.setText(Nama);
                } else {
                    Toast.makeText(Activity_Dashboard_OrangTua.this, "Snapshot tidak ada", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getAnimationClick(View view) {
        Context context = this;
        view.startAnimation(AnimationUtils.loadAnimation(context, R.anim.animation_click));
    }

    private void setDialog(){
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.popup_cek_verifikasi_email);
        cvVerifikasi = dialog.findViewById(R.id.cvVerifikasi);

        cvVerifikasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getAnimationClick(view);
                startActivity(new Intent(getApplicationContext(), Activity_Verifikasi_Email.class));
            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }
}