package com.e_counseling.e_counseling.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.e_counseling.e_counseling.Fragment.GuruBKTabFragment;
import com.e_counseling.e_counseling.Fragment.OrangTuaTabFragment;
import com.e_counseling.e_counseling.Fragment.SiswaTabFragment;

public class RegisterAdapter extends FragmentStateAdapter {

    private String[] titles = new String[]{"Guru BK", "Orang Tua", "Siswa"};

    public RegisterAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new GuruBKTabFragment();

            case 1:
                return new OrangTuaTabFragment();

            case 2:
                return new SiswaTabFragment();

            default:
                return null;
        }
    }

    @Override
    public int getItemCount() {
        return titles.length;
    }

}
