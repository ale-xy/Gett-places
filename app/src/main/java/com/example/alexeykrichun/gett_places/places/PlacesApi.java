package com.example.alexeykrichun.gett_places.places;

import com.example.alexeykrichun.gett_places.model.AutocompleteResult;
import com.example.alexeykrichun.gett_places.model.Place;

import java.util.List;

/**
 * Created by alexeykrichun on 27/12/2016.
 */
public interface PlacesApi {


    public interface GetNearbyPlacesCallback {
        void nearbyPlacesResult(List<Place> places);
    }

    public interface GetPlaceDetailsCallback {
        void placeDetailsResult(Place place);
    }

    public interface GetAutocompleteCallback {
        void autocompleteResult(List<AutocompleteResult> places);
    }

    public interface ReverseGeocodingCallback {
        void reverseGeocodingResult(Place place);
    }

    void getNearbyPlaces(double lat, double lon, int radius, GetNearbyPlacesCallback callback);

    void getPlaceDetails(String placeId, GetPlaceDetailsCallback callback);

    void getAutocompletePredictions(String input, GetAutocompleteCallback callback);

    void getReverseGeocoding(double lat, double lon, ReverseGeocodingCallback callback);
}
