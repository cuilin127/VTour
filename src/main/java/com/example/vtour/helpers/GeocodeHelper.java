package com.example.vtour.helpers;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;

import java.io.IOException;

public class GeocodeHelper {

    /**
     * Geocodes an address string, converting it to a latitude and longitude.
     *
     * @param context The GeoApiContext used for making requests to the Google Maps API.
     * @param address The address to geocode.
     * @return A LatLng object containing the latitude and longitude of the address, or null if the address couldn't be geocoded.
     */
    public static LatLng geocodeAddress(GeoApiContext context, String address) {
        try {
            GeocodingResult[] results = GeocodingApi.geocode(context, address).await();
            if (results.length > 0) {
                return results[0].geometry.location;
            }
        } catch (ApiException | InterruptedException | IOException e) {
            e.printStackTrace();
        }
        return null; // Return null if the address could not be geocoded
    }
}