package com.geoculturedemo.nickstamp.geoculturedemo.Callback;

import com.geoculturedemo.nickstamp.geoculturedemo.Utils.GPSUtils;

/**
 * Created by nickstamp on 2/18/2016.
 */
public interface GPSUtilsCallback {

    void OnCoordinates(double latitude,double longitude);

    void onGPSError();

    void onNetworkError();
}