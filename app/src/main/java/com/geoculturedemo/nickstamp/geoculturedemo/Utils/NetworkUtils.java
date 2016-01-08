package com.geoculturedemo.nickstamp.geoculturedemo.Utils;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;

/**
 * Created by nickstamp on 1/4/2016.
 */
public class NetworkUtils {

    // Whether there is a Wi-Fi connection.
    private static boolean wifiConnected = false;
    // Whether there is a mobile connection.
    private static boolean mobileConnected = false;

    private Context context;

    public NetworkUtils(Context context) {

        this.context = context;
    }

    public boolean hasInternet() {
        checkConnections();
        return (wifiConnected || mobileConnected);
    }


    // Checks the network connection and sets the wifiConnected and mobileConnected
    // variables accordingly.
    private void checkConnections() {
        ConnectivityManager connMgr =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeInfo = connMgr.getActiveNetworkInfo();
        if (activeInfo != null && activeInfo.isConnected()) {
            wifiConnected = (activeInfo.getType() == ConnectivityManager.TYPE_WIFI);
            mobileConnected = (activeInfo.getType() == ConnectivityManager.TYPE_MOBILE);
        } else {
            wifiConnected = false;
            mobileConnected = false;
        }
    }

    public void showInternetErrorDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

        // Setting Dialog Title
        alertDialog.setTitle("Network Error");

        // Setting Dialog Message
        alertDialog.setMessage("No active internet connection found. Connect and try again");

        // Setting Icon to Dialog
        alertDialog.setIcon(android.R.drawable.stat_notify_error);

        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                context.startActivity(intent);
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }
}
