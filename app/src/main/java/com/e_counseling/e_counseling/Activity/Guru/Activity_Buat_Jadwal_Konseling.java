package com.e_counseling.e_counseling.Activity.Guru;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.e_counseling.e_counseling.Activity.Siswa.Activity_Pengaduan_Siswa;
import com.e_counseling.e_counseling.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class Activity_Buat_Jadwal_Konseling extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private DatabaseReference dbRef;
    private EditText etPerihal;
    private DatePickerDialog datePicker;
    private Spinner jenisKonseling, konselingMethod;
    private Button btnSubmit, datePickerButton, timeButton;
    private AutoCompleteTextView etPelanggar;
    private String Perihal = "", Pelanggar = "", Date = "", Time = "", metodeKonseling = "", JenisKonseling = "", Key = "", OrangTua = "", NamaRuang, KeyOrangTua;
    private ArrayList<String> arrayListPelanggar = new ArrayList<>();
    private ArrayAdapter<CharSequence> arrayJenisKonseling;
    private ArrayAdapter<CharSequence> arrayKonselingMethod;
    private int hour, minute;
    public boolean x = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buat_jadwal_konseling);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dbRef = FirebaseDatabase
                .getInstance("https://e-counseling-61974-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference();


        getID();
        spinnerJenisKonseling();
        spinnerKonselingMethod();
        initDatePicker();
        autoCompletListener();

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animationClick(view);
                checkEmpty();

            }
        });


    }

    private void getID() {
        etPerihal = findViewById(R.id.etPerihalKonseling);
        btnSubmit = findViewById(R.id.btnSubmit);
        datePickerButton = findViewById(R.id.datePickerButton);
        timeButton = findViewById(R.id.timeButton);
        etPelanggar = findViewById(R.id.etPelanggar);
        jenisKonseling = findViewById(R.id.jenisKonsultasi);
        konselingMethod = findViewById(R.id.konselingMethod);
    }

    private void getString() {
        Perihal = etPerihal.getText().toString();
        Pelanggar = etPelanggar.getText().toString();
    }

    private void autoCompletListener() {
        dbRef.child("Users").orderByChild("Status").equalTo("Siswa").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayListPelanggar.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    arrayListPelanggar.add(dataSnapshot.child("Nama").getValue(String.class));
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(Activity_Buat_Jadwal_Konseling.this, android.R.layout.simple_list_item_1, arrayListPelanggar);
                etPelanggar.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void checkEmpty() {
        getString();
        if (Pelanggar.isEmpty() || Date.isEmpty() || Time.isEmpty() || Perihal.isEmpty() || metodeKonseling.isEmpty() || JenisKonseling.isEmpty()) {
            Toast.makeText(this, "Form Tidak Boleh Kosong", Toast.LENGTH_SHORT).show();
        } else {
            AlertDialog alertDialog = new AlertDialog.Builder(Activity_Buat_Jadwal_Konseling.this)
                    .setMessage("Apakah anda yakin membuat pengaduan?")
                    .setPositiveButton("Ya", null)
                    .setNegativeButton("Tidak", null)
                    .show();
            Button positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cekArrayPelanggar();
                    alertDialog.dismiss();
                }
            });

        }
    }

    private void cekArrayPelanggar() {
        if (!arrayListPelanggar.contains(Pelanggar)) {
            Toast.makeText(this, "Nama Siswa Tidak Terdaftar", Toast.LENGTH_SHORT).show();
        } else {
            getUidAnak();
        }
    }

    private void getUidAnak(){
        dbRef.child("Users").orderByChild("Nama").equalTo(Pelanggar).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        if (dataSnapshot.exists()){
                            Key = dataSnapshot.getKey();
                            x = true;
                            getUidOrangTua(new CallBackNama() {
                                @Override
                                public void onCallback(String NAMA) {
                                    inputData();
                                }
                            });
                        }
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void inputData(){
        String waktu = Date + "/" + Time;
        if (metodeKonseling.equals("Online")){
            NamaRuang = JenisKonseling + waktu;
        }else {
            NamaRuang = "empty";
        }
        BuatJadwal buatJadwal = new BuatJadwal(Pelanggar, waktu, Perihal, metodeKonseling, JenisKonseling, Key, KeyOrangTua, NamaRuang);
        dbRef.child("JadwalKonseling").push().setValue(buatJadwal).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(Activity_Buat_Jadwal_Konseling.this, "Jadwal konseling berhasil dibuat", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getUidOrangTua(CallBackNama callBackNama){
        dbRef.child("Users").orderByChild("NISNAnak").equalTo(Key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot:snapshot.getChildren()) {
                        KeyOrangTua = dataSnapshot.getKey();
                        break;
                    }
                    callBackNama.onCallback(KeyOrangTua);

                }else{
                    Toast.makeText(Activity_Buat_Jadwal_Konseling.this, "snapshot kosong", Toast.LENGTH_SHORT).show();
                    Toast.makeText(Activity_Buat_Jadwal_Konseling.this, Key, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    /*private void getNamaOrangTua(){
        Toast.makeText(this, KeyOrangTua, Toast.LENGTH_SHORT).show();
        dbRef.child("Users").child(KeyOrangTua).child("Nama").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                OrangTua = snapshot.getValue().toString();
                Toast.makeText(Activity_Buat_Jadwal_Konseling.this, OrangTua, Toast.LENGTH_SHORT).show();
                inputData();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }*/

    private interface CallBackNama{
        void onCallback(String NAMA);
    }

    private void spinnerJenisKonseling() {
        arrayJenisKonseling = ArrayAdapter.createFromResource(this, R.array.Jenis_Konseling, android.R.layout.simple_spinner_item);
        arrayJenisKonseling.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        jenisKonseling.setAdapter(arrayJenisKonseling);
        jenisKonseling.setOnItemSelectedListener(this);
    }

    private void spinnerKonselingMethod() {
        arrayKonselingMethod = ArrayAdapter.createFromResource(this, R.array.Metode_Konseling, android.R.layout.simple_spinner_item);
        arrayKonselingMethod.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        konselingMethod.setAdapter(arrayKonselingMethod);
        konselingMethod.setOnItemSelectedListener(this);
    }


    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                datePickerButton.setText("TESTING");
                month = month + 1;
                Date = makeDateString(day, month, year);
                datePickerButton.setText(Date);
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePicker = new DatePickerDialog(this, style, dateSetListener, year, month, day);
        datePicker.getDatePicker().setMaxDate(System.currentTimeMillis());

    }

    private String makeDateString(int day, int month, int year) {
        return getMonthFormat(month) + " " + day + " " + year;
    }

    private String getMonthFormat(int month) {
        if (month == 1)
            return "JAN";
        if (month == 2)
            return "FEB";
        if (month == 3)
            return "MAR";
        if (month == 4)
            return "APR";
        if (month == 5)
            return "MAY";
        if (month == 6)
            return "JUN";
        if (month == 7)
            return "JUL";
        if (month == 8)
            return "AUG";
        if (month == 9)
            return "SEP";
        if (month == 10)
            return "OCT";
        if (month == 11)
            return "NOV";
        if (month == 12)
            return "DEC";

        //default should never happen
        return "JAN";
    }

    public void onCLick(View view) {
        datePicker.show();
    }

    public void clickTime(View view) {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                hour = selectedHour;
                minute = selectedMinute;
                Time = String.format(Locale.getDefault(), "%02d:%02d", hour, minute);
                timeButton.setText(Time);
            }
        };

        int style = AlertDialog.THEME_HOLO_LIGHT;

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, style, onTimeSetListener, hour, minute, true);

        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();
    }


    private void animationClick(View view) {
        Context context = this;
        view.startAnimation(AnimationUtils.loadAnimation(context, R.anim.animation_click));
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        jenisKonseling = (Spinner) adapterView;
        konselingMethod = (Spinner) adapterView;
        if (jenisKonseling.getId() == R.id.jenisKonsultasi) {
            if (!adapterView.getItemAtPosition(i).equals("Pilih jenis konseling...")) {
                JenisKonseling = adapterView.getItemAtPosition(i).toString();
            }
        } else if (konselingMethod.getId() == R.id.konselingMethod) {
            if (!adapterView.getItemAtPosition(i).equals("Pilih metode konseling...")) {
                metodeKonseling = adapterView.getItemAtPosition(i).toString();
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private interface FirebaseCallback {
        void onCallback(String Uid);
    }
}