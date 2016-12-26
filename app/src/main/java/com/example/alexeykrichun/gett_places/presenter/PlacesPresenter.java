package com.example.alexeykrichun.gett_places.presenter;

import com.example.alexeykrichun.gett_places.PlacesViewContract;
import com.example.alexeykrichun.gett_places.model.Place;

/**
 * Created by alexeykrichun on 26/12/2016.
 */

public class PlacesPresenter implements PlacesViewContract.Presenter {
    private PlacesViewContract.View view;

    public PlacesPresenter(PlacesViewContract.View view) {
        this.view = view;
    }

    @Override
    public void getAutocompleteSuggestions(String text) {

    }

    @Override
    public void getReverseGeocoding(double lat, double lon) {

    }

    @Override
    public void getNearbyPlaces(Place base, int radius) {

    }
}
