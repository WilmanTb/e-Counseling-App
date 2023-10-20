package com.e_counseling.e_counseling.Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.e_counseling.e_counseling.Activity.Siswa.Activity_Dashboard_Siswa;
import com.e_counseling.e_counseling.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import dmax.dialog.SpotsDialog;

public class SiswaTabFragment extends Fragment {

    EditText etNISN, etNama, etEmail, etAlamat, etPassword, etKonfirmasiPassword;
    Button btnRegister;
    ViewGroup root;
    String NISN,Nama, Email, Alamat, Password, KonfirmasiPassword, Uid;
    FirebaseAuth userAuth;
    AlertDialog progressDialog;
    TextView loginText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        root = (ViewGroup) inflater.inflate(R.layout.register_siswa_fragment, container, false);

        progressDialog = new SpotsDialog.Builder().setContext(getActivity()).build();

        Id();

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickAnimation(view);
                progressDialog.show();
                setString();
            }
        });

        return root;
    }

    private void setString() {
        NISN = etNISN.getText().toString();
        Nama = etNama.getText().toString();
        Email = etEmail.getText().toString();
        Alamat = etAlamat.getText().toString();
        Password = etPassword.getText().toString();
        KonfirmasiPassword = etKonfirmasiPassword.getText().toString();
        cekValue();
    }

    private void cekValue() {
        if (NISN.isEmpty() || Nama.isEmpty() || Email.isEmpty() || Alamat.isEmpty() || Password.isEmpty() || KonfirmasiPassword.isEmpty()) {
            Toast.makeText(getActivity(), "Form Tidak Boleh Kosong", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        } else {
            cekPassword();
        }
    }

    private void cekPassword() {
        if (Password.length() < 8) {
            progressDialog.dismiss();
            Toast.makeText(getActivity(), "Panjang Password Minimal 8 Karakter", Toast.LENGTH_SHORT).show();
        } else if (!KonfirmasiPassword.equals(Password)) {
            progressDialog.dismiss();
            Toast.makeText(getActivity(), "Password Yang Anda Masukkan Tidak Sesuai", Toast.LENGTH_SHORT).show();
        } else {
            registerUser(NISN, Nama, Email, Alamat, Password);
        }
    }

    private void registerUser(String nisn, String nama, String email, String alamat, String password) {
        userAuth = FirebaseAuth.getInstance();
        userAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                        Uid = currentUser.getUid();
                        DatabaseReference dbUser = FirebaseDatabase
                                .getInstance("https://e-counseling-61974-default-rtdb.asia-southeast1.firebasedatabase.app/")
                                .getReference().child("Users").child(Uid);
                        HashMap hashMap = new HashMap<>();
                        hashMap.put("NISN", nisn);
                        hashMap.put("Nama", nama);
                        hashMap.put("Email", email);
                        hashMap.put("Alamat", alamat);
                        hashMap.put("Password", password);
                        hashMap.put("Status", "Siswa");
                        hashMap.put("Kelas", "empty");
                        hashMap.put("Jurusan", "empty");
                        hashMap.put("NomorHp","empty");
                        dbUser.setValue(hashMap);
                        Toast.makeText(getActivity(), "Registrasi Berhasil", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getActivity(), Activity_Dashboard_Siswa.class));
                        getActivity().finish();
                        progressDialog.dismiss();
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), "Registrasi Gagal, Mohon Cek Koneksi Internet Anda", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void Id(){
        etNISN = root.findViewById(R.id.etNisn);
        etNama = root.findViewById(R.id.etNama);
        etAlamat = root.findViewById(R.id.etAlamat);
        etEmail = root.findViewById(R.id.etEmail);
        etPassword = root.findViewById(R.id.etPassword);
        etKonfirmasiPassword = root.findViewById(R.id.etKonfirmasiPassword);
        loginText = root.findViewById(R.id.loginText);
        btnRegister = root.findViewById(R.id.btnRegister);
    }

    private void clickAnimation(View view) {
        Context context = getActivity();
        view.startAnimation(AnimationUtils.loadAnimation(context, R.anim.animation_click));
    }
}
