package com.synerise.sdk.sample.service;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Looper;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.JobIntentService;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.synerise.sdk.event.Tracker;
import com.synerise.sdk.event.model.interaction.AppearedInLocationEvent;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class LocationService extends JobIntentService {

    private static final String GET_LOCATION_ACTION = "GET_LOCATION_ACTION";
    private static final int JOB_ID = 912;
    private LocationCallback mLocationCallback;
    private FusedLocationProviderClient mFusedLocationClient;

    public static void startLocation(Context context) {
        Intent intent = new Intent(context, LocationService.class);
        intent.setAction(GET_LOCATION_ACTION);
        enqueueWork(context, LocationService.class, JOB_ID, intent);
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        final String action = intent.getAction();
        if (GET_LOCATION_ACTION.equals(action)) {
            startLocationUpdates();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopLocationUpdates();
    }

    private void startLocationUpdates() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PERMISSION_GRANTED) {
            return;
        }

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(500);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        mLocationCallback = new LocationCallback();

        mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                                                    mLocationCallback,
                                                    Looper.getMainLooper() /* Looper */);

        Task<Location> lastLocation = mFusedLocationClient.getLastLocation();
        try {
            Tasks.await(lastLocation, 500, TimeUnit.MILLISECONDS);
            Location result = lastLocation.getResult();
            if (result != null)
                Tracker.send(new AppearedInLocationEvent("Location required from silent push", result.getLatitude(),
                                                         result.getLongitude()));
        } catch (ExecutionException | InterruptedException | TimeoutException e) {
            e.printStackTrace();
        }
    }

    private void stopLocationUpdates() {
        if (mFusedLocationClient != null && mLocationCallback != null)
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
    }
}