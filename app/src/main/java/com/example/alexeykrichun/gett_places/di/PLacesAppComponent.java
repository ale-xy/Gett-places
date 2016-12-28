package com.example.alexeykrichun.gett_places.di;

import com.example.alexeykrichun.gett_places.PlacesViewContract;
import com.example.alexeykrichun.gett_places.places.PlacesApi;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by alexeykrichun on 27/12/2016.
 */

@Component (modules = { PlacesAppModule.class })
@Singleton
public interface PlacesAppComponent {
    void inject(PlacesViewContract.View view);

    PlacesApi getPlacesApi();
    PlacesViewContract.Presenter getPresenter();
}
