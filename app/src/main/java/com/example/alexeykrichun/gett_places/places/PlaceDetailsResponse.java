package com.example.alexeykrichun.gett_places.places;

import com.google.maps.errors.ApiException;
import com.google.maps.internal.ApiResponse;
import com.google.maps.model.PlaceDetails;

/**
 * Created by alexeykrichun on 28/12/2016.
 */

public class PlaceDetailsResponse implements ApiResponse<PlaceDetails> {
    public String status;
    public PlaceDetails result;
    public String[] htmlAttributions;
    public String errorMessage;

    @Override
    public boolean successful() {
        return "OK".equals(status) || "ZERO_RESULTS".equals(status);
    }

    @Override
    public PlaceDetails getResult() {
        if (result != null) {
            result.htmlAttributions = htmlAttributions;
        }
        return result;
    }

    @Override
    public ApiException getError() {
        if (successful()) {
            return null;
        }
        return ApiException.from(status, errorMessage);
    }
}
