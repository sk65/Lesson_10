package com.example.lesson_10.viewmodel;

import android.location.Location;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.lesson_10.repostory.LocationRepository;
import com.example.lesson_10.util.Resource;

public class LocationViewModel extends ViewModel {
    private final LocationRepository locationRepository;

    public LocationViewModel() {
        locationRepository = new LocationRepository();
    }

    public LiveData<Resource<Location>> getLocation() {
        return locationRepository.getLocationLiveData();
    }

    public void removeLocationUpdate() {
        locationRepository.removeLocationUpdate();
    }

    public void updateLocation() {
        Log.i("dev","LocationViewModel updateLocation()");
        locationRepository.updateLocation();
    }
}
