package com.example.alexeykrichun.gett_places.model;

import java.util.List;

/**
 * Created by alexeykrichun on 26/12/2016.
 */

public class PlacesModel {
    private Place currentPlace;
    private int radius;
    private List<Place> places;

    public Place getCurrentPlace() {
        return currentPlace;
    }

    public void setCurrentPlace(Place currentPlace) {
        this.currentPlace = currentPlace;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public List<Place> getPlaces() {
        return places;
    }

    public void setPlaces(List<Place> places) {
        this.places = places;
    }

    public void clearPlaces() {
        if(places != null) {
            places.clear();
        }
    }
}
