package com.example.alexeykrichun.gett_places.places;

import com.google.maps.GeocodingApi;
import com.google.maps.GeocodingApiRequest;
import com.google.maps.NearbySearchRequest;
import com.google.maps.PlaceAutocompleteRequest;
import com.google.maps.model.AutocompletePrediction;
import com.google.maps.model.GeocodingResult;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by alexeykrichun on 26/12/2016.
 */

public interface RetrofitMapsApi {
    @GET("place/nearbysearch/json?sensor=true&type=establishment")
    Call<NearbySearchRequest.Response> getNearbyPlaces(@Query("key") String key,
                                                       @Query("location") String location,
                                                       @Query("radius") int radius);

    @GET("place/details/json?sensor=true")
    Call<NearbySearchRequest.Response> getPlaceDetails(@Query("key") String key,
                                                       @Query("placeid") String placeId);

    @GET("place/autocomplete/json?types=address")
    Call<PlaceAutocompleteRequest.Response> getAutocompletePredictions(@Query("key") String key, @Query("input") String input);

    @GET("geocode/json")
    Call<GeocodingApiResponse> reverseGeocoding(@Query("key") String key, @Query("latlng") String latlng);


}
