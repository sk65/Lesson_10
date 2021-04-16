package com.example.lesson_10.repostory;

import android.location.Location;
import android.os.Looper;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.lesson_10.util.App;
import com.example.lesson_10.util.Resource;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import org.jetbrains.annotations.NotNull;

import static com.example.lesson_10.util.Constants.FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS;
import static com.example.lesson_10.util.Constants.UPDATE_INTERVAL_IN_MILLISECONDS;


public class LocationRepository {
    private final FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private LocationRequest locationRequest;
    private final MutableLiveData<Resource<Location>> locationLiveData;


    public LocationRepository() {
        locationLiveData = new MutableLiveData<>();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(App.contextWeakReference.get());
        createLocationCallback();
        createLocationRequest();
    }

    public void updateLocation() {
        Log.i("dev", "LocationRepository updateLocation()");
        locationLiveData.setValue(new Resource.Loading<>());
        try {
            fusedLocationClient.requestLocationUpdates(locationRequest,
                    locationCallback,
                    Looper.getMainLooper());
        } catch (SecurityException e) {
            locationLiveData.setValue(new Resource.Error<>(null, "no permissions"));
        }
    }

    public LiveData<Resource<Location>> getLocationLiveData() {
        return locationLiveData;
    }

    public void removeLocationUpdate() {
        fusedLocationClient.removeLocationUpdates(locationCallback);
        locationLiveData.setValue(new Resource.Error<>(null, "you remove location update"));
    }

    private void createLocationCallback() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NotNull LocationResult locationResult) {
                super.onLocationResult(locationResult);
                Log.i("dev", "LocationRepo callback " + locationResult.getLastLocation());
                locationLiveData.postValue(new Resource.Success<>(locationResult.getLastLocation()));
                fusedLocationClient.removeLocationUpdates(locationCallback);
            }
        };
    }

    private void createLocationRequest() {
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        locationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

}
