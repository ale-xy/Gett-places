package com.example.alexeykrichun.gett_places.places;

import com.example.alexeykrichun.gett_places.model.AutocompleteResult;
import com.example.alexeykrichun.gett_places.model.Place;

import java.util.List;
import java.util.concurrent.Exchanger;

/**
 * Created by alexeykrichun on 27/12/2016.
 */
public interface PlacesApi {

    interface GetNearbyPlacesCallback {
        void onSuccess(List<Place> places);
        void onError(Throwable t);
    }

    interface GetPlaceDetailsCallback {
        void onSuccess(Place place);
        void onError(Throwable t);
    }

    interface ReverseGeocodingCallback {
        void onSuccess(Place place);
        void onError(Throwable t);
    }

    void getNearbyPlaces(double lat, double lon, int radius, GetNearbyPlacesCallback callback);

    void getPlaceDetails(String placeId, GetPlaceDetailsCallback callback);

    List<AutocompleteResult> getAutocompletePredictionsSync(String input);

    void getReverseGeocoding(double lat, double lon, ReverseGeocodingCallback callback);
}
