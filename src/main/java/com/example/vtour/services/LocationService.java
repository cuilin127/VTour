package com.example.vtour.services;

import com.example.vtour.helpers.GeocodeHelper;
import com.google.maps.GeoApiContext;
import com.google.maps.PlacesApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.LatLng;
import com.google.maps.model.PlaceType;
import com.google.maps.model.PlacesSearchResponse;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class LocationService {
    private final GeoApiContext context;

    public LocationService() {
        // Initialize the Google Maps API context with your API key
        this.context = new GeoApiContext.Builder()
                .apiKey("AIzaSyBZL-OEdORxsMHeTiKVH8CobkncKQvNFDQ")
                .build();
    }

    public PlacesSearchResponse findPlacesNearby(String address, PlaceType type) throws InterruptedException, ApiException, IOException {
        // Geocode the address to get latitude and longitude

        LatLng location = GeocodeHelper.geocodeAddress(context, address); // Implement GeocodeHelper based on your needs

        if (location != null) {
            // Perform a Nearby Search for the specified type and return the response
            return PlacesApi.nearbySearchQuery(context, location)
                    .type(type)
                    .radius(5000) // 5 km radius
                    .await();
        } else {
            throw new IllegalArgumentException("Address could not be geocoded.");
        }
    }
}
