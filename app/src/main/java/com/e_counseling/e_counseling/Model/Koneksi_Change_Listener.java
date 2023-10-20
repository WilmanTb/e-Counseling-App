package com.e_counseling.e_counseling.Model;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.e_counseling.e_counseling.R;

public class Koneksi_Change_Listener extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (!Cek_Koneksi_Internet.isConnectedToInternet(context)){
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            View layout_dialog = LayoutInflater.from(context).inflate(R.layout.no_connection, null);
            builder.setView(layout_dialog);

            Button btnMuatUlang = layout_dialog.findViewById(R.id.btnMuatUlang);

            AlertDialog dialog = builder.create();
            dialog.show();
            dialog.setCancelable(true);
            dialog.getWindow().setGravity(Gravity.CENTER);

            btnMuatUlang.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    onReceive(context, intent);
                }
            });
        }
    }
}
