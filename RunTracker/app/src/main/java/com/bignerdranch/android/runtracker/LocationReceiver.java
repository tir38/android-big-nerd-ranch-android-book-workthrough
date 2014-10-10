package com.bignerdranch.android.runtracker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

public class LocationReceiver extends BroadcastReceiver {
    private static final String TAG = LocationReceiver.class.toString();

    public void onReceive(Context context, Intent intent) {
        // If you got a location extra, use it!
        Location location = (Location) intent.getParcelableExtra(LocationManager.KEY_LOCATION_CHANGED);

        if (location != null) {
            onLocationReceived(context, location);
            return;
        }

        // If you get here, something else has happened
        if (intent.hasExtra(LocationManager.KEY_PROVIDER_ENABLED)) {
            boolean enabled = intent.getBooleanExtra(LocationManager.KEY_PROVIDER_ENABLED, false);
            onProviderEnabledChanged(enabled);
        }
    }

    protected void onLocationReceived(Context context, Location location) {
        Log.d(TAG, this + " Got location from " + location.getProvider() + ": " + location.getLatitude() + ", " + location.getLongitude());
    }

    protected void onProviderEnabledChanged(boolean enabled) {
        Log.d(TAG, "Provider " + (enabled ? "enabled" : "disabled"));
    }
}
