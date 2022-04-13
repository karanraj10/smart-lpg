package com.example.lpgcontroller;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;

public class ProgressAlertDialog {

    Activity activity;
    AlertDialog alertDialog;

    ProgressAlertDialog(Activity activity) {
        this.activity = activity;
    }

    void startProgressAlertDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.loading_alert_dialog,null));
        builder.setCancelable(false);

        alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.BLACK));
        alertDialog.show();
    }

    void stopProgressAlertDialog()
    {
        alertDialog.dismiss();
    }
}
