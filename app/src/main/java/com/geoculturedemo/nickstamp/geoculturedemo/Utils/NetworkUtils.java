package com.geoculturedemo.nickstamp.geoculturedemo.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

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

}
