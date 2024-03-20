package com.example.vtour.services;

import com.example.vtour.helpers.GeocodeHelper;
import com.google.maps.DistanceMatrixApi;
import com.google.maps.GeoApiContext;
import com.google.maps.PlacesApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.*;
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

    public PlacesSearchResponse findSchool(String address) throws IOException, InterruptedException, ApiException {
        return this.findPlacesNearby(address,PlaceType.SCHOOL);
    }
    public PlacesSearchResponse findParks(String address) throws IOException, InterruptedException, ApiException {
        return this.findPlacesNearby(address,PlaceType.PARK);
    }
    public PlacesSearchResponse findTransits(String address) throws IOException, InterruptedException, ApiException {

        LatLng location = GeocodeHelper.geocodeAddress(context, address); // Implement GeocodeHelper based on your needs

        if (location != null) {
            return PlacesApi.nearbySearchQuery(context, location)
                    .type(PlaceType.TRANSIT_STATION)
                    .radius(1000) // 1 km radius
                    .await();
        } else {
            throw new IllegalArgumentException("Address could not be geocoded.");
        }
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
    public double getDistance(String from, String to){
        try {
            DistanceMatrix result = DistanceMatrixApi.newRequest(context)
                    .origins(from)
                    .destinations(to)
                    .mode(TravelMode.WALKING) // You can change this to other modes like WALKING, BICYCLING, TRANSIT
                    .await();

            // Assuming a single origin to destination request, get the distance
            if (result.rows.length > 0 && result.rows[0].elements.length > 0) {
                DistanceMatrixElement element = result.rows[0].elements[0];
                if (element.distance != null) {
                    System.out.println("Distance: " + element.distance.humanReadable);
                    return element.distance.inMeters/1000.0;
                } else {
                    System.out.println("Distance calculation failed.");
                    return -1.0;
                }
            } else {
                System.out.println("No results found.");
                return -1.0;
            }
        } catch (ApiException | InterruptedException | java.io.IOException e) {
            System.out.println(e.getMessage());
            return -1.0;
        }
    }
    public String getPlaceWebsite(String placeId){
        PlaceDetails placeDetails = getPlaceDetail(placeId);
        return placeDetails.website != null ? placeDetails.website.toString() : "No website available";
    }
    public String getFormattedAddress(String placeId){
        PlaceDetails placeDetails = getPlaceDetail(placeId);
        return placeDetails.formattedAddress != null ? placeDetails.formattedAddress : "No Address available";
    }
    public PlaceDetails getPlaceDetail(String placeId){
        try {
            // Requesting place details using the place ID
            return PlacesApi.placeDetails(context, placeId).await();
        } catch (ApiException | InterruptedException | java.io.IOException e) {
            System.out.println(e.getMessage());
            return new PlaceDetails();
        }
    }

}
