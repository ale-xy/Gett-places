package com.example.alexeykrichun.gett_places.model;

/**
 * Created by alexeykrichun on 27/12/2016.
 */

public class AutocompleteResult {
    public final String address;
    public final String placeId;
    
    //todo add terms

    public AutocompleteResult(String address, String placeId) {
        this.address = address;
        this.placeId = placeId;
    }
}
