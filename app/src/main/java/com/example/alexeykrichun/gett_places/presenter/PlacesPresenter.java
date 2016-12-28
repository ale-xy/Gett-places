package com.example.alexeykrichun.gett_places.presenter;

import com.example.alexeykrichun.gett_places.PlacesViewContract;
import com.example.alexeykrichun.gett_places.model.AutocompleteResult;
import com.example.alexeykrichun.gett_places.model.Place;
import com.example.alexeykrichun.gett_places.model.PlacesModel;
import com.example.alexeykrichun.gett_places.places.PlacesApi;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alexeykrichun on 26/12/2016.
 */

public class PlacesPresenter implements PlacesViewContract.Presenter {
    private PlacesViewContract.View view;
    private final PlacesApi placesApi;

    private final PlacesModel placesModel;


    public PlacesPresenter(PlacesViewContract.View view, PlacesApi placesApi) {
        this.view = view;
        this.placesApi = placesApi;
        placesModel = new PlacesModel();
        placesModel.setPlaces(new ArrayList<Place>());
    }

    @Override
    public void getAutocompleteSuggestions(String text) {
        placesApi.getAutocompletePredictions(text, new PlacesApi.GetAutocompleteCallback() {
            @Override
            public void autocompleteResult(List<AutocompleteResult> places) {
                view.showAutocompleteResults(places);
            }
        });
    }

    @Override
    public void selectPlaceFromCoords(double lat, double lon) {
        placesApi.getReverseGeocoding(lat, lon, new PlacesApi.ReverseGeocodingCallback() {
            @Override
            public void reverseGeocodingResult(Place place) {
                placesModel.clearPlaces();
                placesModel.setCurrentPlace(place);
                view.updateMap(placesModel);
                getNearbyPlaces();
            }
        });
    }

    private void getNearbyPlaces() {
        placesApi.getNearbyPlaces(placesModel.getCurrentPlace().lat,
                placesModel.getCurrentPlace().lon,
                placesModel.getRadius(),
                new PlacesApi.GetNearbyPlacesCallback() {
            @Override
            public void nearbyPlacesResult(List<Place> places) {
                placesModel.setPlaces(places);
                view.updateMap(placesModel);
            }
        });
    }

    @Override
    public void getPlaceDetails(String placeId) {
        placesApi.getPlaceDetails(placeId, new PlacesApi.GetPlaceDetailsCallback() {
            @Override
            public void placeDetailsResult(Place place) {
                //todo show place details
            }
        });
    }

    @Override
    public void setCurrentPlace(Place place) {
        placesModel.setCurrentPlace(place);
        getNearbyPlaces();
    }

    @Override
    public void setRadius(int radius) {
        if (placesModel.getRadius() != radius) {
            placesModel.setRadius(radius);
            if (placesModel.getCurrentPlace() != null) {
                getNearbyPlaces();
            }
        }
    }

    @Override
    public void selectPlaceFromAutocomplete(String placeId) {
        placesApi.getPlaceDetails(placeId, new PlacesApi.GetPlaceDetailsCallback() {
            @Override
            public void placeDetailsResult(Place place) {
                placesModel.clearPlaces();
                placesModel.setCurrentPlace(place);
                view.updateMap(placesModel);
                getNearbyPlaces();
            }
        });
    }
}
