package com.example.lesson_10.util;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.example.lesson_10.R;

import static com.example.lesson_10.util.Constants.LOCATION_SETTINGS_REQUEST_CODE;


public class DialogUtils {

    public static ProgressDialog buildNetworkProgressDialog(Context context) {
        ProgressDialog networkProgressDialog = new ProgressDialog(context);
        networkProgressDialog.setMessage(context.getString(R.string.getting_data_from_server));
        networkProgressDialog.setCancelable(false);
        return networkProgressDialog;
    }

    public static ProgressDialog buildLocationProgressDialog(Context context) {
        ProgressDialog locationProgressDialog = new ProgressDialog(context);
        locationProgressDialog.setMessage(context.getString(R.string.getting_location));
        locationProgressDialog.setCancelable(false);
        return locationProgressDialog;
    }


    public static AlertDialog.Builder buildLocationSettingsDialog(Activity context) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle(R.string.location_settings);
        alertDialog.setMessage(R.string.location_settings_message);
        alertDialog.setPositiveButton(R.string.location_settings_button, (dialog, which) -> {
            context.startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), LOCATION_SETTINGS_REQUEST_CODE);
        });
        alertDialog.setNegativeButton(R.string.dialog_cancel, (dialog, which) -> {
            dialog.cancel();
            Toast.makeText(context, R.string.explanation_for_location_dialogue, Toast.LENGTH_LONG);
        });
        return alertDialog;
    }


}
