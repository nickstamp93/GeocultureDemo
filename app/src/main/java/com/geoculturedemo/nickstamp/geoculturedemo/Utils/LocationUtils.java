package com.geoculturedemo.nickstamp.geoculturedemo.Utils;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import com.geoculturedemo.nickstamp.geoculturedemo.Model.Location;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by nickstamp on 1/6/2016.
 */
public class LocationUtils {

    private Context context;
    private Location location;

    public LocationUtils(Context context) {

        this.context = context;
        location = new Location();
    }


    public Location getLocation(double latitude, double longitude) {

        location.setLatitude(latitude);
        location.setLongitude(longitude);


        if (location.getLatitude() == 0 || location.getLongitude() == 0) {
            return null;
        }

        Geocoder geocoder = new Geocoder(context, new Locale("en", "US"));

        try {

            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 10);

            boolean success = fetchLocationInfo(addresses);
            if (!success) {
                //location not found error
                return null;
            }


        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return location;

    }

    /**
     * Fetch the info for the current location
     *
     * @param addresses the list of addresses from the geocoder , based on the coordinates
     * @return whether the fetching was successful
     */
    public boolean fetchLocationInfo(List<Address> addresses) {

        if (addresses != null && addresses.size() > 0) {

            Address address = addresses.get(0);

            location.setArea(address.getLocality());
            location.setCountry(address.getCountryName());

            location.setCity(address.getLocality());
            for (Address a : addresses) {
                if (a.getSubAdminArea() != null) {
                    location.setCity(a.getSubAdminArea());
                    break;
                }
            }

            return true;
        } else {
            return false;
        }

    }

    public Location toGreekLocale(Location location) {
        Location l = new Location();

        l.setLatitude(location.getLatitude());
        l.setLongitude(location.getLongitude());

        Geocoder geocoder = new Geocoder(context, new Locale("el", "GR"));

        try {

            List<Address> addresses = geocoder.getFromLocationName(location.getArea(), 10);

            if (addresses != null && addresses.size() > 0) {

                Address address = addresses.get(0);

                l.setArea(address.getLocality());
                l.setCountry(address.getCountryName());

                l.setCity(address.getLocality());
                for (Address a : addresses) {
                    if (a.getSubAdminArea() != null) {
                        l.setCity(a.getSubAdminArea());
                        break;
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return l;

    }

}

