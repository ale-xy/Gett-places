package com.example.alexeykrichun.gett_places.places;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.alexeykrichun.gett_places.model.AutocompleteResult;
import com.example.alexeykrichun.gett_places.model.Place;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.maps.GeolocationApi;
import com.google.maps.NearbySearchRequest;
import com.google.maps.PlaceAutocompleteRequest;
import com.google.maps.internal.DateTimeAdapter;
import com.google.maps.internal.DayOfWeekAdaptor;
import com.google.maps.internal.DistanceAdapter;
import com.google.maps.internal.DurationAdapter;
import com.google.maps.internal.FareAdapter;
import com.google.maps.internal.GeolocationResponseAdapter;
import com.google.maps.internal.InstantAdapter;
import com.google.maps.internal.LatLngAdapter;
import com.google.maps.internal.LocalTimeAdapter;
import com.google.maps.internal.PriceLevelAdaptor;
import com.google.maps.internal.SafeEnumAdapter;
import com.google.maps.model.AddressComponentType;
import com.google.maps.model.AddressType;
import com.google.maps.model.AutocompletePrediction;
import com.google.maps.model.Distance;
import com.google.maps.model.Duration;
import com.google.maps.model.Fare;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import com.google.maps.model.LocationType;
import com.google.maps.model.OpeningHours;
import com.google.maps.model.PlaceDetails;
import com.google.maps.model.PlacesSearchResult;
import com.google.maps.model.PriceLevel;
import com.google.maps.model.TravelMode;

import org.joda.time.DateTime;
import org.joda.time.Instant;
import org.joda.time.LocalTime;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by alexeykrichun on 26/12/2016.
 */

public class GooglePlacesApi implements PlacesApi {
    private static final String BASE_URL = "https://maps.googleapis.com/maps/api/";

    private final String googleMapsKey;

    private final RetrofitMapsApi mapsApi;

    public GooglePlacesApi(String googleMapsKey) {
        this.googleMapsKey = googleMapsKey;

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(DateTime.class, new DateTimeAdapter())
                .registerTypeAdapter(Distance.class, new DistanceAdapter())
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .registerTypeAdapter(Fare.class, new FareAdapter())
                .registerTypeAdapter(LatLng.class, new LatLngAdapter())
                .registerTypeAdapter(AddressComponentType.class,
                        new SafeEnumAdapter<AddressComponentType>(AddressComponentType.UNKNOWN))
                .registerTypeAdapter(AddressType.class, new SafeEnumAdapter<AddressType>(AddressType.UNKNOWN))
                .registerTypeAdapter(TravelMode.class, new SafeEnumAdapter<TravelMode>(TravelMode.UNKNOWN))
                .registerTypeAdapter(LocationType.class, new SafeEnumAdapter<LocationType>(LocationType.UNKNOWN))
                .registerTypeAdapter(PlaceDetails.Review.AspectRating.RatingType.class, new SafeEnumAdapter<PlaceDetails.Review.AspectRating.RatingType>(PlaceDetails.Review.AspectRating.RatingType.UNKNOWN))
                .registerTypeAdapter(OpeningHours.Period.OpenClose.DayOfWeek.class, new DayOfWeekAdaptor())
                .registerTypeAdapter(PriceLevel.class, new PriceLevelAdaptor())
                .registerTypeAdapter(Instant.class, new InstantAdapter())
                .registerTypeAdapter(LocalTime.class, new LocalTimeAdapter())
                .registerTypeAdapter(GeolocationApi.Response.class, new GeolocationResponseAdapter())
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();

        mapsApi = retrofit.create(RetrofitMapsApi.class);

    }

    @Override
    public void getNearbyPlaces(double lat, double lon, int radius, final GetNearbyPlacesCallback callback) {
        String location = String.valueOf(lat) + "," + String.valueOf(lon);

        mapsApi.getNearbyPlaces(googleMapsKey, location, radius).enqueue(new Callback<NearbySearchRequest.Response>() {
            @Override
            public void onResponse(Call<NearbySearchRequest.Response> call, Response<NearbySearchRequest.Response> response) {
                ArrayList<Place> places = new ArrayList<>(response.body().results.length);

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

    @Override
    public void getPlaceDetails(String placeId, final GetPlaceDetailsCallback callback) {
        mapsApi.getPlaceDetails(googleMapsKey, placeId).enqueue(new Callback<PlaceDetailsResponse>() {
            @Override
            public void onResponse(Call<PlaceDetailsResponse> call, Response<PlaceDetailsResponse> response) {
                if (response.isSuccessful() && response.body().result != null) {
                    PlaceDetails placesSearchResult = response.body().result;
                    LatLng location = placesSearchResult.geometry.location;
                    String address = placesSearchResult.formattedAddress;
                    String id = placesSearchResult.placeId;
                    String name = placesSearchResult.name;
                    Place result = new Place(location.lat, location.lng, id, name, address);
                    result.setPhone(placesSearchResult.formattedPhoneNumber);
                    URL website = placesSearchResult.website;
                    if (website != null) {
                        result.setWebsite(website.toString());
                    }
                    result.setRating(placesSearchResult.rating);

                    callback.placeDetailsResult(result);
                }
                //todo not found

            }

            @Override
            public void onFailure(Call<PlaceDetailsResponse> call, Throwable t) {
                Log.e("GooglePlacesApi", t.getLocalizedMessage());
            }
        });
    }

    @Override
    public List<AutocompleteResult> getAutocompletePredictionsSync(String input) {
        try {
            Response<PlaceAutocompleteRequest.Response> response = mapsApi.getAutocompletePredictions(googleMapsKey, input).execute();
            ArrayList<AutocompleteResult> results = parseAutocompleteResults(response);
            return results;
        } catch (IOException e) {
            Log.e("GooglePlacesApi", e.getLocalizedMessage());
        }
        return null;
    }

    @NonNull
    private ArrayList<AutocompleteResult> parseAutocompleteResults(Response<PlaceAutocompleteRequest.Response> response) {
        ArrayList<AutocompleteResult> results = new ArrayList<>(response.body().predictions.length);

        for(AutocompletePrediction prediction:response.body().predictions) {
            AutocompleteResult result = new AutocompleteResult(prediction.description, prediction.placeId);
            results.add(result);
        }
        return results;
    }

    @Override
    public void getReverseGeocoding(double lat, double lon, final ReverseGeocodingCallback callback) {
        String location = String.valueOf(lat) + "," + String.valueOf(lon);
        mapsApi.reverseGeocoding(googleMapsKey, location).enqueue(new Callback<GeocodingApiResponse>() {
            @Override
            public void onResponse(Call<GeocodingApiResponse> call, Response<GeocodingApiResponse> response) {
                if (response.body().results.length > 0) {
                    GeocodingResult geocodingResult = response.body().results[0];
                    LatLng location = geocodingResult.geometry.location;
                    String address = geocodingResult.formattedAddress;
                    String id = geocodingResult.placeId;
                    Place result = new Place(location.lat, location.lng, id, address, address);

                    callback.reverseGeocodingResult(result);
                }
            }

            @Override
            public void onFailure(Call<GeocodingApiResponse> call, Throwable t) {
                Log.e("GooglePlacesApi", t.getLocalizedMessage());
            }
        });
    }
}
