package com.e_counseling.e_counseling.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.e_counseling.e_counseling.Activity.Siswa.Activity_Lengkapi_Data;
import com.e_counseling.e_counseling.Activity.Siswa.Activity_Verifikasi_Email;
import com.e_counseling.e_counseling.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Fragment_Profil_Siswa extends Fragment {

    private CardView cvData, cvVerifikasiEmail;
    private ViewGroup root;
    private TextView namaUser, emailUser;
    private DatabaseReference dbRef;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = (ViewGroup) inflater.inflate(R.layout.fragment_profil_siswa,container,false);

        getID();


        dbRef = FirebaseDatabase
                .getInstance("https://e-counseling-61974-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("Users");

        getData();

        cvData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animationClick(view);
                startActivity(new Intent(getActivity(), Activity_Lengkapi_Data.class));
            }
        });

        cvVerifikasiEmail.setOnClickListener(view -> {
            animationClick(view);
            startActivity(new Intent(getActivity(), Activity_Verifikasi_Email.class));
        });

        return root;
    }

    private void getID(){
        cvData = root.findViewById(R.id.cvData);
        cvVerifikasiEmail = root.findViewById(R.id.cvVerifikasiEmail);
        namaUser = root.findViewById(R.id.namaUser);
        emailUser = root.findViewById(R.id.emailUser);
    }

    private void getData(){
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String UID = firebaseUser.getUid();
        dbRef.child(UID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String Email = snapshot.child("Email").getValue().toString();
                String Nama = snapshot.child("Nama").getValue().toString();
                emailUser.setText(Email);
                namaUser.setText(Nama);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void animationClick(View view){
        Context context = getActivity();
        view.startAnimation(AnimationUtils.loadAnimation(context, R.anim.animation_click));
    }
}
