package com.example.alexeykrichun.gett_places;

import com.example.alexeykrichun.gett_places.model.Place;

import java.util.List;

/**
 * Created by alexeykrichun on 26/12/2016.
 */

public interface PlacesViewContract {
    interface View {
        void showAutocompleteResults(List<String> results);
        void showAddress(Place place);
        void showPlaces(List<Place> places);
        void showLoading(boolean show);
    }

    interface Presenter {
        void getAutocompleteSuggestions(String text);
        void getReverseGeocoding(double lat, double lon);
        void getNearbyPlaces(Place base, int radius);
    }

}
