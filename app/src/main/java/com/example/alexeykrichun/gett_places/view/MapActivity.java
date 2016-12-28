package com.example.alexeykrichun.gett_places.view;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;

import com.example.alexeykrichun.gett_places.R;
import com.example.alexeykrichun.gett_places.di.DaggerPlacesAppComponent;
import com.example.alexeykrichun.gett_places.di.PlacesAppComponent;
import com.example.alexeykrichun.gett_places.di.PlacesAppModule;
import com.example.alexeykrichun.gett_places.model.AutocompleteResult;
import com.example.alexeykrichun.gett_places.model.Place;
import com.example.alexeykrichun.gett_places.PlacesViewContract;
import com.example.alexeykrichun.gett_places.model.PlacesModel;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import javax.inject.Inject;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback, PlacesViewContract.View, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final String LOG_TAG = "MapActivity";
    private static final int REQUEST_CODE_LOCATION_PERMISSION = 9000;

    private PlacesViewContract.Presenter presenter;

    private GoogleMap googleMap;

    private AutoCompleteTextView autoCompleteTextView;

    private GoogleApiClient googleApiClient;

    private AutocompleteArrayAdapter autocompleteListAdapter;

    private int radius = 1000; //todo add control

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String googleMapsKey = getResources().getString(R.string.google_maps_key);
        PlacesAppComponent component = DaggerPlacesAppComponent.builder().
                placesAppModule(new PlacesAppModule(googleMapsKey, this)).
                build();

        presenter = component.getPresenter();
        presenter.setRadius(radius);

        setContentView(R.layout.activity_map);

        setupGoogleApiClient();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.address_autocomplete);
        autocompleteListAdapter = new AutocompleteArrayAdapter(this);
        autocompleteListAdapter.setNotifyOnChange(true);
//        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                if (charSequence.length() >= 3) {
//                    presenter.getAutocompleteSuggestions(charSequence.toString());
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//            }
//        });

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                AutocompleteResult result = autocompleteListAdapter.getItem(i);
                presenter.selectPlaceFromAutocomplete(result.placeId);
            }
        });
    }

    private void setupGoogleApiClient() {
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                String[] permissions = new String[2];
                permissions[0] = Manifest.permission.ACCESS_FINE_LOCATION;
                permissions[1] = Manifest.permission.ACCESS_COARSE_LOCATION;
                requestPermissions(permissions, REQUEST_CODE_LOCATION_PERMISSION);
            }
            return;
        } else {
            initMap(true);
        }
    }

    private void initMap(boolean locationPermissionsGranted) {
        if (locationPermissionsGranted) {
            try {
                googleMap.setMyLocationEnabled(true);
                googleMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
                    @Override
                    public boolean onMyLocationButtonClick() {
                        getCurrentLocationAddress();
                        return false;
                    }
                });
            } catch (SecurityException e) {
                Log.e(LOG_TAG, "Location permission error " + e.getMessage());
            }
        }
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                presenter.selectPlaceFromCoords(latLng.latitude, latLng.longitude);
            }
        });
    }

    private void getCurrentLocationAddress() {
        try {
            Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            if (location != null) {
                presenter.selectPlaceFromCoords(location.getLatitude(), location.getLongitude());
            }
        } catch (SecurityException e) {
            Log.e(LOG_TAG, "Location permission error " + e.getMessage());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_LOCATION_PERMISSION) {
            for (int i = 0; i< permissions.length; i++) {
                String permission = permissions[i];
                int result = grantResults[i];
                if (TextUtils.equals(Manifest.permission.ACCESS_FINE_LOCATION, permission) ||
                        TextUtils.equals(Manifest.permission.ACCESS_COARSE_LOCATION, permission)) {
                    if (result == PackageManager.PERMISSION_GRANTED) {
                        initMap(true);
                        return;
                    }
                }
            }
        }
        initMap(false);
    }

    @Override
    public void showAutocompleteResults(List<AutocompleteResult> results) {
        autocompleteListAdapter.clear();
        autocompleteListAdapter.addAll(results);
        autoCompleteTextView.showDropDown();
    }

    @Override
    public void updateMap(@NonNull PlacesModel placesModel) {
        autocompleteListAdapter.clear();
        Place currentPlace = placesModel.getCurrentPlace();
        autoCompleteTextView.setText(currentPlace.name, false);

        googleMap.clear();

        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        List<Place> places = placesModel.getPlaces();

        for (Place place:places) {
            LatLng latLng = new LatLng(place.lat, place.lon);

            builder.include(latLng);

            MarkerOptions markerOptions = new MarkerOptions().
                    position(latLng).
                    icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));

            googleMap.addMarker(markerOptions);
        }

        LatLng latLng = new LatLng(currentPlace.lat, currentPlace.lon);
        builder.include(latLng);

        MarkerOptions markerOptions = new MarkerOptions().
                position(latLng).
                icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));

        googleMap.addMarker(markerOptions);

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(builder.build(), 100);
        googleMap.animateCamera(cameraUpdate);
    }

    @Override
    public void showLoading(boolean show) {

    }

    protected void onStart() {
        googleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        googleApiClient.disconnect();
        super.onStop();
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
