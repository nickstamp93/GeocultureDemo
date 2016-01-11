package com.geoculturedemo.nickstamp.geoculturedemo.Model;

import java.io.Serializable;

/**
 * Created by nickstamp on 1/6/2016.
 */
public class Location implements Serializable{
    private double longitude, latitude;
    private String country, city, area;

    public Location() {
        latitude = 0;
        longitude = 0;
    }


    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getFullName() {
        if (city.equals(area)) {
            return city + " , " + country;
        } else {
            return area + " , " + city + " , " + country;
        }
    }
}
