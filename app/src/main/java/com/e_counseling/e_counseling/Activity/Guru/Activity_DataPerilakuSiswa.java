package com.e_counseling.e_counseling.Activity.Guru;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.e_counseling.e_counseling.R;

public class Activity_DataPerilakuSiswa extends AppCompatActivity {

    private CardView cvKelasX, cvKelasXI, cvKelasXII;
    private Dialog dialogKelasX;
    private CardView cvKelasX1, cvKelasX2, cvKelasX3, cvKelasXI1, cvKelasXI2, cvKelasXI3, cvKelasXII1, cvKelasXII2, cvKelasXII3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_perilaku_siswa);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getID();

        cvKelasX.setOnClickListener(view -> {
            clickAnimation(view);
            setDialogKelasX();
        });

        cvKelasXI.setOnClickListener(view -> {
            clickAnimation(view);
            setDialogKelasXI();
        });

        cvKelasXII.setOnClickListener(view -> {
            clickAnimation(view);
            setDialogKelasXII();
        });

    }

    private void getID() {
        cvKelasX = findViewById(R.id.cvKelasX);
        cvKelasXI = findViewById(R.id.cvKelasXI);
        cvKelasXII = findViewById(R.id.cvKelasXII);
    }

    private void clickAnimation(View view) {
        Context context = this;
        view.startAnimation(AnimationUtils.loadAnimation(context, R.anim.animation_click));
    }

    private void setDialogKelasX() {

        dialogKelasX = new Dialog(this);
        dialogKelasX.setContentView(R.layout.popup_kelasx);
        cvKelasX1 = dialogKelasX.findViewById(R.id.cvKelasX1);
        cvKelasX2 = dialogKelasX.findViewById(R.id.cvKelasX2);
        cvKelasX3 = dialogKelasX.findViewById(R.id.cvKelasX3);

        cvKelasX1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickAnimation(view);
                Toast.makeText(getApplicationContext(), "TEST1", Toast.LENGTH_SHORT).show();
            }
        });

        cvKelasX2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickAnimation(view);
                Toast.makeText(getApplicationContext(), "TEST2", Toast.LENGTH_SHORT).show();
            }
        });

        cvKelasX3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickAnimation(view);
                Toast.makeText(getApplicationContext(), "TEST3", Toast.LENGTH_SHORT).show();
            }
        });

        dialogKelasX.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogKelasX.show();
    }

    private void setDialogKelasXI() {

        dialogKelasX = new Dialog(this);
        dialogKelasX.setContentView(R.layout.popup_kelasxi);
        cvKelasXI1 = dialogKelasX.findViewById(R.id.cvKelasXI1);
        cvKelasXI2 = dialogKelasX.findViewById(R.id.cvKelasXI2);
        cvKelasXI3 = dialogKelasX.findViewById(R.id.cvKelasXI3);

        cvKelasXI1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickAnimation(view);
                Toast.makeText(getApplicationContext(), "TEST1", Toast.LENGTH_SHORT).show();
            }
        });

        cvKelasXI2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickAnimation(view);
                Toast.makeText(getApplicationContext(), "TEST2", Toast.LENGTH_SHORT).show();
            }
        });

        cvKelasXI3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickAnimation(view);
                Toast.makeText(getApplicationContext(), "TEST3", Toast.LENGTH_SHORT).show();
            }
        });

        dialogKelasX.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogKelasX.show();
    }

    private void setDialogKelasXII() {

        dialogKelasX = new Dialog(this);
        dialogKelasX.setContentView(R.layout.popup_kelasxii);
        cvKelasXII1 = dialogKelasX.findViewById(R.id.cvKelasXII1);
        cvKelasXII2 = dialogKelasX.findViewById(R.id.cvKelasXII2);
        cvKelasXII3 = dialogKelasX.findViewById(R.id.cvKelasXII3);

        cvKelasXII1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickAnimation(view);
                Toast.makeText(getApplicationContext(), "TEST1", Toast.LENGTH_SHORT).show();
            }
        });

        cvKelasXII2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickAnimation(view);
                Toast.makeText(getApplicationContext(), "TEST2", Toast.LENGTH_SHORT).show();
            }
        });

        cvKelasXII3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickAnimation(view);
                Toast.makeText(getApplicationContext(), "TEST3", Toast.LENGTH_SHORT).show();
            }
        });

        dialogKelasX.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogKelasX.show();
    }

}