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

    //todo add data


    public Place(double lat, double lon, String id, String name, String address) {
        this.lat = lat;
        this.lon = lon;
        this.id = id;
        this.name = name;
        this.address = address;
    }
}
