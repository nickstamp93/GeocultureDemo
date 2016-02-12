package com.geoculturedemo.nickstamp.geoculturedemo.Utils;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;

import com.geoculturedemo.nickstamp.geoculturedemo.R;


/**
 * Created by nickstamp on 2/11/2016.
 */
public class GPSUtils {

    // calls back to calling thread, note this is for low grain: if you want higher precision, swap the
    // contents of the else and if. Also be sure to check gps permission/settings are allowed.
    // call usually takes <10ms
    public static void searchCurrentLocation(final Context context, final LocationCallback callback) {
        final LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean isNetworkEnabled = isNetworkEnabled(context);
        boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        //if this call is moved after the if statements, then on gps enabled there will be no auto find location
        singleLocationRequest(context, locationManager, callback);

        if (!isGPSEnabled) {
            callback.onGPSError();
            return;
        }

        if (!isNetworkEnabled) {
            callback.onNetworkError();
            return;
        }


    }

    private static void singleLocationRequest(Context context, LocationManager locationManager, final LocationCallback callback) {
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        if (ActivityCompat
                .checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        locationManager.requestSingleUpdate(criteria, new LocationListener() {

            @Override
            public void onLocationChanged(Location location) {
                callback.onNewLocationAvailable(new GPSCoordinates(location.getLatitude(), location.getLongitude()));
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            @Override
            public void onProviderEnabled(String provider) {
            }

            @Override
            public void onProviderDisabled(String provider) {
            }
        }, null);
    }

    public static boolean isNetworkEnabled(Context context) {

        ConnectivityManager connMgr =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeInfo = connMgr.getActiveNetworkInfo();
        boolean wifiConnected;
        boolean mobileConnected;
        if (activeInfo != null && activeInfo.isConnected()) {
            wifiConnected = (activeInfo.getType() == ConnectivityManager.TYPE_WIFI);
            mobileConnected = (activeInfo.getType() == ConnectivityManager.TYPE_MOBILE);
        } else {
            wifiConnected = false;
            mobileConnected = false;
        }

        return wifiConnected || mobileConnected;
    }

    public interface LocationCallback {

        void onNewLocationAvailable(GPSCoordinates location);

        void onGPSError();

        void onNetworkError();
    }

    public static void showInternetErrorDialog(final Context context) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

        // Setting Dialog Title
        alertDialog.setTitle(context.getString(R.string.network_error_title));

        // Setting Dialog Message
        alertDialog.setMessage(R.string.network_error_message);

        // Setting Icon to Dialog
        alertDialog.setIcon(android.R.drawable.stat_notify_error);

        // On pressing Settings button
        alertDialog.setPositiveButton(R.string.text_settings, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                context.startActivity(intent);
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    /**
     * Function to show settings alert dialog
     */
    public static void showGPSErrorDialog(final Context context) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

        // Setting Dialog Title
        alertDialog.setTitle(R.string.gps_error_title);

        // Setting Dialog Message
        alertDialog.setMessage(R.string.gps_error_message);

        // Setting Icon to Dialog
        alertDialog.setIcon(android.R.drawable.stat_notify_error);

        // On pressing Settings button
        alertDialog.setPositiveButton(R.string.text_settings, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                context.startActivity(intent);
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    // consider returning Location instead of this dummy wrapper class
    public static class GPSCoordinates {
        public double longitude = -1;
        public double latitude = -1;

        public GPSCoordinates(double theLatitude, double theLongitude) {
            longitude = theLongitude;
            latitude = theLatitude;
        }
    }

}
