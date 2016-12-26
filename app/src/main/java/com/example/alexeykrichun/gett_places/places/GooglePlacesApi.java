package com.example.alexeykrichun.gett_places.places;

import android.util.Log;

import com.example.alexeykrichun.gett_places.model.AutocompleteResult;
import com.example.alexeykrichun.gett_places.model.Place;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.maps.NearbySearchRequest;
import com.google.maps.PlaceAutocompleteRequest;
import com.google.maps.internal.DateTimeAdapter;
import com.google.maps.internal.DistanceAdapter;
import com.google.maps.internal.DurationAdapter;
import com.google.maps.internal.SafeEnumAdapter;
import com.google.maps.model.AddressComponentType;
import com.google.maps.model.AddressType;
import com.google.maps.model.AutocompletePrediction;
import com.google.maps.model.Distance;
import com.google.maps.model.Duration;
import com.google.maps.model.LocationType;
import com.google.maps.model.PlacesSearchResult;
import com.google.maps.model.TravelMode;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by alexeykrichun on 26/12/2016.
 */

public class GooglePlacesApi {
    private static final String BASE_URL = "https://maps.googleapis.com/maps/api";

    private final String googleMapsKey;

    private final Retrofit retrofit;
    private final RetrofitMapsApi mapsApi;

    public interface GetNearbyPlacesCallback {
        void nearbyPlacesResult(List<Place> places);
    }

    public interface GetPlaceDetailsCallback {
        void placeDetailsResult(Place place);
    }

    public interface GetAutocompleteCallback {
        void autocompleteResult(List<AutocompleteResult> places);
    }

    public GooglePlacesApi(String googleMapsKey) {
        this.googleMapsKey = googleMapsKey;

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(DateTime.class, new DateTimeAdapter())
                .registerTypeAdapter(Distance.class, new DistanceAdapter())
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .registerTypeAdapter(AddressComponentType.class,
                        new SafeEnumAdapter<AddressComponentType>(AddressComponentType.UNKNOWN))
                .registerTypeAdapter(AddressType.class, new SafeEnumAdapter<AddressType>(AddressType.UNKNOWN))
                .registerTypeAdapter(TravelMode.class, new SafeEnumAdapter<TravelMode>(TravelMode.UNKNOWN))
                .registerTypeAdapter(LocationType.class, new SafeEnumAdapter<LocationType>(LocationType.UNKNOWN))
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        mapsApi = retrofit.create(RetrofitMapsApi.class);

    }

    public void getNearbyPlaces(double lat, double lon, int radius, final GetNearbyPlacesCallback callback) {
        String location = String.valueOf(lat) + "," + String.valueOf(lon);

        mapsApi.getNearbyPlaces(googleMapsKey, location, radius).enqueue(new Callback<NearbySearchRequest.Response>() {
            @Override
            public void onResponse(Call<NearbySearchRequest.Response> call, Response<NearbySearchRequest.Response> response) {
                ArrayList<Place> places = new ArrayList<Place>(response.body().results.length);

                for(PlacesSearchResult result:response.body().results) {
                    Place place = new Place(result.geometry.location.lat, result.geometry.location.lng, result.placeId, result.name, result.formattedAddress);
                    places.add(place);
                }
                callback.nearbyPlacesResult(places);
            }

            @Override
            public void onFailure(Call<NearbySearchRequest.Response> call, Throwable t) {
                Log.e("GooglePlacesApi", t.getLocalizedMessage());
            }
        });
    }

    public void getPlaceDetails(String placeId, final GetPlaceDetailsCallback callback) {
        mapsApi.getPlaceDetails(googleMapsKey, placeId).enqueue(new Callback<NearbySearchRequest.Response>() {
            @Override
            public void onResponse(Call<NearbySearchRequest.Response> call, Response<NearbySearchRequest.Response> response) {

            }

            @Override
            public void onFailure(Call<NearbySearchRequest.Response> call, Throwable t) {
                Log.e("GooglePlacesApi", t.getLocalizedMessage());
            }
        });
    }

    public void getAutocompletePredictions(String input, final GetAutocompleteCallback callback) {
        mapsApi.getAutocompletePredictions(googleMapsKey, input).enqueue(new Callback<PlaceAutocompleteRequest.Response>() {
            @Override
            public void onResponse(Call<PlaceAutocompleteRequest.Response> call, Response<PlaceAutocompleteRequest.Response> response) {
                ArrayList<AutocompleteResult> results = new ArrayList<AutocompleteResult>(response.body().predictions.length);

                for(AutocompletePrediction prediction:response.body().predictions) {
                    AutocompleteResult result = new AutocompleteResult(prediction.description, prediction.placeId);
                    results.add(result);
                }

                callback.autocompleteResult(results);
            }

            @Override
            public void onFailure(Call<PlaceAutocompleteRequest.Response> call, Throwable t) {
                Log.e("GooglePlacesApi", t.getLocalizedMessage());
            }
        });
    }
}
