package com.example.alexeykrichun.gett_places.places;

import com.google.maps.errors.ApiException;
import com.google.maps.model.GeocodingResult;

/**
 * Created by alexeykrichun on 27/12/2016.
 */

public class GeocodingApiResponse {
    public String status;
    public String errorMessage;
    public GeocodingResult[] results;

    public boolean successful() {
        return "OK".equals(status) || "ZERO_RESULTS".equals(status);
    }

    public GeocodingResult[] getResult() {
        return results;
    }

    public ApiException getError() {
        if (successful()) {
            return null;
        }
        return ApiException.from(status, errorMessage);
    }
}
