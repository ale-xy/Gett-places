package com.example.alexeykrichun.gett_places.view;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.alexeykrichun.gett_places.PlacesViewContract;
import com.example.alexeykrichun.gett_places.R;
import com.example.alexeykrichun.gett_places.model.Place;

/**
 * Created by alexeykrichun on 28/12/2016.
 */

public class PlaceDetailsBottomSheet {
    private final ViewGroup baseView;
    private BottomSheetBehavior bottomSheetBehavior;

    TextView name;
    TextView address;
    TextView phone;
    TextView rating;
    TextView website;
    View ratingLine;

    public PlaceDetailsBottomSheet(@NonNull ViewGroup baseView) {
        this.baseView = baseView;
        bottomSheetBehavior = BottomSheetBehavior.from(baseView);

        name = (TextView)baseView.findViewById(R.id.details_title);
        address = (TextView)baseView.findViewById(R.id.details_address);
        phone = (TextView)baseView.findViewById(R.id.details_phone);
        rating = (TextView)baseView.findViewById(R.id.details_rating);
        website = (TextView)baseView.findViewById(R.id.details_website);
        ratingLine = baseView.findViewById(R.id.rating_line);
    }

    public void setPlace(Place place) {
        name.setText(place.name);
        address.setText(place.address);
        phone.setText(place.getPhone());

        double ratingValue = place.getRating();
        if (ratingValue > 0.001) {
            ratingLine.setVisibility(View.VISIBLE);
            rating.setText(String.format("%.2f", ratingValue));
        } else {
            ratingLine.setVisibility(View.GONE);
        }
        website.setText(place.getWebsite());
        //todo other fields
    }

    public void show() {
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    public void hide() {
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
    }

    public int getVisibilityState() {
        return bottomSheetBehavior.getState();
    }
}
