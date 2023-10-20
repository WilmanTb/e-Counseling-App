package com.e_counseling.e_counseling.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.e_counseling.e_counseling.Activity.Guru.Activity_DashboardGuruBK;
import com.e_counseling.e_counseling.Activity.OrangTua.Activity_Dashboard_OrangTua;
import com.e_counseling.e_counseling.Activity.Siswa.Activity_Dashboard_Siswa;
import com.e_counseling.e_counseling.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;

import java.net.MalformedURLException;
import java.net.URL;

import dmax.dialog.SpotsDialog;

public class Activity_Login extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Button btnLogin;
    private TextView daftarText;
    private ImageView btnBack;
    private EditText etEmail, etPassword;
    private FirebaseAuth mAuth;
    private AlertDialog progressDialog;
    public static String Email;
    public boolean eMail = false;
    String Password, Status,Uid = "",getPassword = "";
    FirebaseDatabase dbReference;
    DatabaseReference dbUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // <-- SET FULLSCREEN LAYOUT -->
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mAuth = FirebaseAuth.getInstance();

        dbReference = FirebaseDatabase.getInstance("https://e-counseling-61974-default-rtdb.asia-southeast1.firebasedatabase.app/");
        dbUser = dbReference.getReference("Users");

        // <-- START OF CALLING FUNCTIONS -->
        spinnerFunction();
        getId();
        // <-- END OF CALLING FUNCTIONS -->


        //<-- START OF CLICK FUNCTION -->
        btnLogin.setOnClickListener(view -> {
            progressDialog.show();
            getAnimationClick(view);
            Email = etEmail.getText().toString();
            Password = etPassword.getText().toString();
            if (Email.isEmpty() || Password.isEmpty()){
                Toast.makeText(Activity_Login.this, "Form Tidak Boleh Kosong", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }else {
                getUid(new FirebaseCallback() {
                    @Override
                    public void onCallback(String Uid) {
                        if (eMail == true){
                            cekPassword(Uid);
                        }
                    }
                });
            }
        });

        btnBack.setOnClickListener(view -> {
            getAnimationClick(view);
            startActivity(new Intent(Activity_Login.this, Activity_Home.class));
            finish();
        });

        daftarText.setOnClickListener(view -> {
            getAnimationClick(view);
            startActivity(new Intent(Activity_Login.this, Activity_Register.class));
            finish();
        });
        //<-- END OF CLICK FUNCTION -->

    }

    // <-- START OF SPINNER FUNCTION -->
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Status = adapterView.getItemAtPosition(i).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }
    // <-- END OF SPINNER FUNCTION -->


    // <-- START OF SPINNER FUNCTION -->
    private void spinnerFunction(){
        Spinner spinner = findViewById(R.id.statusUser);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.Status, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    // <-- START OF OTHER FUNCTION -->
    private void getId(){
        btnLogin = findViewById(R.id.btnLogin);
        daftarText = findViewById(R.id.daftarText);
        btnBack = findViewById(R.id.btnBack);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        progressDialog = new SpotsDialog.Builder().setContext(this).build();
    }

    /*private void getText(){
        Email = etEmail.getText().toString();
        Password = etPassword.getText().toString();
        checkIsEmpty();
    }*/

    /*private void checkIsEmpty(){
        if (TextUtils.isEmpty(Email) || TextUtils.isEmpty(Password)){
            Toast.makeText(Activity_Login.this, "Form Tidak Boleh Kosong", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        }
    }*/

    private void loginUser(Class <? extends Activity> next){
        Intent intent = new Intent(Activity_Login.this, next);
        mAuth.signInWithEmailAndPassword(Email, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    progressDialog.dismiss();
                    startActivity(intent);
                    finish();
                    Toast.makeText(Activity_Login.this, "Login Berhasil", Toast.LENGTH_SHORT).show();
                }else {
                    progressDialog.dismiss();
                    Toast.makeText(Activity_Login.this, "Login Gagal, Mohon Cek Koneksi Internet Anda", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void cekStatus(){
        if (Status.equals("Guru BK")){
            loginUser(Activity_DashboardGuruBK.class);
        }else if (Status.equals("Orang Tua")){
            loginUser(Activity_Dashboard_OrangTua.class);
        }else if (Status.equals("Siswa")){
            loginUser(Activity_Dashboard_Siswa.class);
        }
    }

    private void getUid(FirebaseCallback firebaseCallback){
        Email = etEmail.getText().toString();
        dbUser.orderByChild("Email")
                .equalTo(Email)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            for (DataSnapshot snaps:snapshot.getChildren()){
                                if (snaps.exists()){
                                    Uid = snaps.getKey();
                                    eMail = true;
                                }
                                break;
                            }
                            firebaseCallback.onCallback(Uid);
                        }else {
                            progressDialog.dismiss();
                            eMail = false;
                            Toast.makeText(Activity_Login.this, "Email Tidak Terdaftar", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        throw error.toException();
                    }
                });
    }

    private void cekPassword(String UID){
        Password = etPassword.getText().toString();
        dbUser.child(UID).child("Password").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                getPassword = snapshot.getValue().toString();
                if (getPassword.equals(Password)){
                    cekStatus();
                }else {
                    progressDialog.dismiss();
                    Toast.makeText(Activity_Login.this, "Password Salah!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private interface FirebaseCallback{
        void onCallback(String Uid);
    }

    private void getAnimationClick(View view){
        Context context = this;
        view.startAnimation(AnimationUtils.loadAnimation(context, R.anim.animation_click));
    }

    // <-- END OF OTHER FUNCTION -->

}