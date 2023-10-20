package com.e_counseling.e_counseling.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.e_counseling.e_counseling.R;
import com.e_counseling.e_counseling.Adapter.RegisterAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class Activity_Register extends AppCompatActivity {

    private ImageView btnBack;
    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private RegisterAdapter adapter;
    private String[] titles = new String[]{"Guru BK", "Orang Tua", "Siswa"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // <-- SET FULLSCREEN LAYOUT -->
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        btnBack = findViewById(R.id.btnBack);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        tabLayout.addTab(tabLayout.newTab().setText("Guru BK"));
        tabLayout.addTab(tabLayout.newTab().setText("Orang Tua"));
        tabLayout.addTab(tabLayout.newTab().setText("Siswa"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        adapter = new RegisterAdapter(this);
        viewPager.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPager, ((tab, position) -> tab.setText(titles[position]))).attach();

        // <-- START OF CLICK FUNCTION -->
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getAnimationClick(view);
                startActivity(new Intent(Activity_Register.this, Activity_Home.class));
                finish();
            }
        });

        // <-- START OF OTHER FUNCTION -->
    }

    private void getAnimationClick(View view) {
        Context context = this;
        view.startAnimation(AnimationUtils.loadAnimation(context, R.anim.animation_click));
    }

    // <-- END OF OTHER FUNCTION -->
}