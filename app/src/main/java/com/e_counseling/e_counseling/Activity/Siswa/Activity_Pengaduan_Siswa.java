package com.e_counseling.e_counseling.Activity.Siswa;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.e_counseling.e_counseling.Model.Koneksi_Change_Listener;
import com.e_counseling.e_counseling.Model.PengaduanClass;
import com.e_counseling.e_counseling.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import dmax.dialog.SpotsDialog;

public class Activity_Pengaduan_Siswa extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Button btnSubmit, dateButton, timeButton;
    private RelativeLayout rlfoto;
    private DatePickerDialog datePicker;
    private EditText etDeskripsiPelanggaran, etSaranTindakan;
    private Spinner spinnerLokasi, spinnerPelanggaran;
    private AutoCompleteTextView autoComplete;
    private TextView tv_pilihFoto;
    private AlertDialog loading;
    private DatabaseReference dbRef;
    private StorageReference storageFoto;
    private StorageTask mUploadTask;
    private ImageView fotoPelanggaran;
    private ArrayAdapter<CharSequence> adapterLokasi;
    private ArrayList<String> arrayList = new ArrayList<>();
    private ArrayList<String> arrayListPelanggar = new ArrayList<>();
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri mImageUri;
    public boolean poin = false;
    String DeskripsiPelanggaran, Pelapor, Pelanggar, SaranTindakan, NamaPelanggaran = "",
            date = "", time = "", Poin = "", Key, LokasiPelanggaran, url,pelanggarKey, tipePelanggaran;
    int hour, minute;
    long maxID = 0, maxChildren = 0;
    Koneksi_Change_Listener koneksi_change_listener = new Koneksi_Change_Listener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pengaduan_siswa);

        dbRef = FirebaseDatabase
                .getInstance("https://e-counseling-61974-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference();

        storageFoto = FirebaseStorage.getInstance().getReference();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initDatePicker();

        getId();
        spinnerFunction();
        getLokasi();
        getPelapor();
        setIDPengaduan();
        autoCompletListener();

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickAnimation(view);
                checkEmpty();
                //uploadFile();
                IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
                registerReceiver(koneksi_change_listener, filter);
            }
        });

        rlfoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadFoto(view);
            }
        });
    }

    @Override
    protected void onStart() {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(koneksi_change_listener, filter);
        super.onStart();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(koneksi_change_listener);
        super.onStop();
    }

    private void getId() {
        btnSubmit = findViewById(R.id.btnSubmit);
        autoComplete = findViewById(R.id.autoComplete);
        etDeskripsiPelanggaran = findViewById(R.id.etDeskripsiPelanggaran);
        etSaranTindakan = findViewById(R.id.etSaranTindakan);
        dateButton = findViewById(R.id.datePickerButton);
        timeButton = findViewById(R.id.timeButton);
        loading = new SpotsDialog.Builder().setContext(this).build();
        spinnerLokasi = findViewById(R.id.lokasiPelanggaran);
        spinnerPelanggaran = findViewById(R.id.jenisPelanggaran);
        rlfoto = findViewById(R.id.rlfoto);
        fotoPelanggaran = findViewById(R.id.fotoPelanggaran);
        tv_pilihFoto = findViewById(R.id.tv_pilihFoto);
        /*timeButton = findViewById(R.id.timeButton);*/
    }

    private void getString() {
        Pelanggar = autoComplete.getText().toString();
        DeskripsiPelanggaran = etDeskripsiPelanggaran.getText().toString();
        SaranTindakan = etSaranTindakan.getText().toString();
    }

    private void autoCompletListener() {
        dbRef.child("Users").orderByChild("Status").equalTo("Siswa").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayListPelanggar.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    arrayListPelanggar.add(dataSnapshot.child("Nama").getValue(String.class));
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(Activity_Pengaduan_Siswa.this, android.R.layout.simple_list_item_1, arrayListPelanggar);
                autoComplete.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void cekArrayPelanggar() {
        if (!arrayListPelanggar.contains(Pelanggar)) {
            Toast.makeText(this, "Nama Siswa Tidak Terdaftar", Toast.LENGTH_SHORT).show();
        } else {
            inputData();
        }
    }

    private void checkEmpty() {
        getString();
        if (Pelanggar.isEmpty() || DeskripsiPelanggaran.isEmpty() || SaranTindakan.isEmpty() || NamaPelanggaran == "" || date == "" || time == "") {
            Toast.makeText(this, "Form Tidak Boleh Kosong", Toast.LENGTH_SHORT).show();
        } else {
            AlertDialog alertDialog = new AlertDialog.Builder(Activity_Pengaduan_Siswa.this)
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

    private void inputData() {
        getPoint(new FirebaseCallback() {
            @Override
            public void onCallback(String Uid) {
                if (poin == true) {
                    getPoin(Uid);
                }
            }
        });

    }

    private void getPoin(String KEY) {
        uploadFile(new CallbackFoto() {
            @Override
            public void onCallback(String URL) {
                tipePelanggaran();
                dbRef.child("Pelanggaran").child(KEY).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Poin = snapshot.child("Poin").getValue().toString();
                        String id = "P" + (maxID + 1);
                        String waktu = date + "/" + time;
                        String Status = "Belum diproses";
                        maxChildren++;
                        PengaduanClass pengaduanClass = new PengaduanClass(NamaPelanggaran, DeskripsiPelanggaran, Pelapor, SaranTindakan, waktu, id, Status, Pelanggar, Poin, LokasiPelanggaran, URL, tipePelanggaran);
                        dbRef.child("Pengaduan").child(String.valueOf(maxChildren)).setValue(pengaduanClass).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    loading.show();
                                    loadingTimer();
                                } else {
                                    loading.dismiss();
                                    Toast.makeText(Activity_Pengaduan_Siswa.this, "Input data gagal!\nMohon cek koneksi internet anda...", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });


    }

    private void getPoint(FirebaseCallback firebaseCallback) {
        dbRef.child("Pelanggaran").orderByChild("NamaPelanggaran").equalTo(NamaPelanggaran).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    if (dataSnapshot.exists()) {
                        Key = dataSnapshot.getKey();
                        poin = true;
                    }
                    break;
                }
                firebaseCallback.onCallback(Key);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getPelapor() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String UID = firebaseUser.getUid();
        dbRef.child("Users").child(UID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Pelapor = snapshot.child("Nama").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setIDPengaduan() {
        dbRef.child("Pengaduan").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    maxChildren = (snapshot.getChildrenCount());
                    maxID = (snapshot.getChildrenCount());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void tipePelanggaran(){
        if (Key.equals("XVIII") || Key.equals("XX") || Key.equals("XXI") || Key.equals("XXII") || Key.equals("XXIII") ||
                Key.equals("XXIV") || Key.equals("XXV") || Key.equals("XXVI") || Key.equals("XXVII") || Key.equals("XXVIII") ||
                Key.equals("XXX") || Key.equals("XXXI") || Key.equals("XXXII") || Key.equals("XXXIII") || Key.equals("XXXIV") ||
                Key.equals("XXXV") || Key.equals("XXXVI") || Key.equals("XXXVII") || Key.equals("XXXVIII")){
            tipePelanggaran = "Pelanggaran Berat";
        }else{
            tipePelanggaran = "Pelanggaran Ringan";
        }
    }

    private void loadingTimer() {
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (loading.isShowing()) {
                    loading.dismiss();
                    Toast.makeText(Activity_Pengaduan_Siswa.this, "Pengaduan berhasil dikirim", Toast.LENGTH_SHORT).show();
                }
            }
        };

        loading.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                handler.removeCallbacks(runnable);
            }
        });

        handler.postDelayed(runnable, 3000);
    }

    private void clickAnimation(View view) {
        Context context = this;
        view.startAnimation(AnimationUtils.loadAnimation(context, R.anim.animation_click));
    }

   /* private String getTodaysDate(){
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month, year);
    }*/

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                dateButton.setText("TESTING");
                month = month + 1;
                date = makeDateString(day, month, year);
                dateButton.setText(date);
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
                time = String.format(Locale.getDefault(), "%02d:%02d", hour, minute);
                timeButton.setText(time);
            }
        };

        int style = AlertDialog.THEME_HOLO_LIGHT;

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, style, onTimeSetListener, hour, minute, true);

        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        spinnerLokasi = (Spinner) adapterView;
        spinnerPelanggaran = (Spinner) adapterView;

        if (spinnerLokasi.getId() == R.id.lokasiPelanggaran) {
            if (!adapterView.getItemAtPosition(i).equals("Pilih Lokasi...")) {
                LokasiPelanggaran = adapterView.getItemAtPosition(i).toString();
            }
        } else if (spinnerPelanggaran.getId() == R.id.jenisPelanggaran) {
            if (!adapterView.getItemAtPosition(i).equals("Pilih Pelanggaran")) {
                NamaPelanggaran = adapterView.getItemAtPosition(i).toString();
            }
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void spinnerFunction() {
        spinnerPelanggaran.setPrompt("Pilih Pelanggaran");
        dbRef.child("Pelanggaran").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayList.clear();
                arrayList.add(0, "Pilih Pelanggaran");
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    arrayList.add(dataSnapshot.child("NamaPelanggaran").getValue(String.class));
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(Activity_Pengaduan_Siswa.this, android.R.layout.simple_spinner_item, arrayList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerPelanggaran.setAdapter(adapter);
                spinnerPelanggaran.setOnItemSelectedListener(Activity_Pengaduan_Siswa.this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getLokasi() {
        adapterLokasi = ArrayAdapter.createFromResource(this, R.array.Lokasi, android.R.layout.simple_spinner_item);
        adapterLokasi.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLokasi.setAdapter(adapterLokasi);
        spinnerLokasi.setOnItemSelectedListener(this);
    }

    public void uploadFoto(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();

            Picasso.get().load(mImageUri);
            tv_pilihFoto.setText(String.valueOf(mImageUri));
            Toast.makeText(this, String.valueOf(mImageUri), Toast.LENGTH_SHORT).show();
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile(CallbackFoto callbackFoto) {
        if (mImageUri != null) {
            StorageReference fileReference = storageFoto.child(System.currentTimeMillis()
                    + "." + getFileExtension(mImageUri));

            mUploadTask = fileReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    url = uri.toString();
                                    callbackFoto.onCallback(url);
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Activity_Pengaduan_Siswa.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }else{
            url = "Tidak ada";
            callbackFoto.onCallback(url);
        }
    }

    private interface FirebaseCallback {
        void onCallback(String Uid);
    }

    private interface CallbackFoto{
        void onCallback(String URL);
    }
}
