package com.example.alexeykrichun.gett_places.model;

/**
 * Created by alexeykrichun on 26/12/2016.
 */

public class Place {
    public final double lat;
    public final double lon;
    public final String id;
    public final String name;
    public final String address;

    private String website;
    private String phone;
    private double rating;



    public Place(double lat, double lon, String id, String name, String address) {
        this.lat = lat;
        this.lon = lon;
        this.id = id;
        this.name = name;
        this.address = address;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }
}
