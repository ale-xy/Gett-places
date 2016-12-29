package com.example.alexeykrichun.gett_places;

import com.example.alexeykrichun.gett_places.model.AutocompleteResult;
import com.example.alexeykrichun.gett_places.model.Place;
import com.example.alexeykrichun.gett_places.model.PlacesModel;

import java.util.List;

/**
 * Created by alexeykrichun on 26/12/2016.
 */

public interface PlacesViewContract {
    interface View {
        void updateMap(PlacesModel placesModel);
        void showLoading(boolean show);
        void showPlaceDetails(Place place);
    }

    interface Presenter {
        void setCurrentPlace(Place place);
        void setRadius(int radius);
        List<AutocompleteResult> getAutocompleteSuggesionsSync(String text);
        void selectPlaceFromCoords(double lat, double lon);
        void selectPlaceFromAutocomplete(String placeId);
        void getPlaceDetails(String placeId);
    }

}
