package com.example.zooapp44;

import android.annotation.SuppressLint;
import android.app.Application;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import java.util.List;
//import java.util.Observer;
import java.util.Stack;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

//This class is provided from the starter code that is given from the example in class

public class LocationModel {


    private final MediatorLiveData<Coord> lastKnownCoords;

    private LiveData<Coord> locationProviderSource = null;
    private MutableLiveData<Coord> mockSource = null;
    private Observer observer;

    public LocationModel(Observer o) {
        lastKnownCoords = new MediatorLiveData<>();
        this.observer=o;
        // Create and add the mock source.
        mockSource = new MutableLiveData<>();
        lastKnownCoords.addSource(mockSource, lastKnownCoords::setValue);
    }

    public MediatorLiveData<Coord> getLastKnownCoords(){
        return lastKnownCoords;
    }

    void removeLocationProviderSource() {
        if (locationProviderSource == null) return;
        lastKnownCoords.removeSource(locationProviderSource);
    }

    @SuppressLint("MissingPermission")
    public void addLocationProviderSource(LocationManager locationManager, String provider) {
        // If a location provider source is already added, remove it.
        if (locationProviderSource != null) {
            removeLocationProviderSource();
        }

        // Create a new GPS source.
        var providerSource = new MutableLiveData<Coord>();
        var locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                var coord = Coord.fromLocation(location);
                Log.i("Location", String.format("Model received GPS location update: %s", coord));
                providerSource.postValue(coord);
            }
        };
        // Register for updates.
        locationManager.requestLocationUpdates(provider, 0, 0f, locationListener);

        locationProviderSource = providerSource;
        lastKnownCoords.addSource(locationProviderSource, lastKnownCoords::setValue);
    }


    public void mockLocation(Coord coords) {
        mockSource.postValue(coords);
    }

    /**
     *
     * @param route is the Coordinates of al mock locations
     * @param time is the time that passes between each
     * @return
     */
    public Future<?> mockRoute(List<Coord> route, List<Double> time){
        return Executors.newSingleThreadExecutor().submit(() ->{
                    int i = 1;
                    int n = route.size();
                    for (int j=0;j<route.size();j++) {
                        // Mock the location...
                        Log.i("Coordinates",route.get(j).toString());

                        mockLocation(route.get(j));

                        // Sleep for a while...
                        try {
                            Thread.sleep(Math.round(time.get(j)));
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
        }
        );
    }

}
