package com.geoculturedemo.nickstamp.geoculturedemo.Callback;

import com.geoculturedemo.nickstamp.geoculturedemo.Model.Location;

/**
 * Created by nickstamp on 1/6/2016.
 */
public interface OnLocationFound {

    void onLocationFound(Location location, boolean isCurrentLocation);
}
