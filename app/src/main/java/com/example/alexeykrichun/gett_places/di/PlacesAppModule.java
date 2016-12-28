package com.example.alexeykrichun.gett_places.di;

import com.example.alexeykrichun.gett_places.PlacesViewContract;
import com.example.alexeykrichun.gett_places.places.GooglePlacesApi;
import com.example.alexeykrichun.gett_places.places.PlacesApi;
import com.example.alexeykrichun.gett_places.presenter.PlacesPresenter;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by alexeykrichun on 27/12/2016.
 */

@Module
public class PlacesAppModule {

    private final String googleMapsKey;
    private final PlacesViewContract.View view;

    public PlacesAppModule(String googlaMapsKey, PlacesViewContract.View view) {
        this.googleMapsKey = googlaMapsKey;
        this.view = view;
    }

    @Provides
    @Singleton
    PlacesViewContract.Presenter providesPresenter(PlacesApi placesApi) {
        return new PlacesPresenter(view, placesApi);
    }

    @Provides
    @Singleton
    PlacesApi providesPlacesApi() {
        return new GooglePlacesApi(googleMapsKey);
    }



}
