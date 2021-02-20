package com.example.currentweather.helper;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;


//Caller activity needs to handle the permissions
@SuppressWarnings({"MissingPermission"})
public class LocationUtil {

    private static final int LAST_KNOWN_LOCATION_DELAY = 5000;//5 seconds
    private static final int LOCATION_MIN_TIME = 0;
    private static final int LOCAITON_MIN_DISTANCE = 0;

    private Timer locationTimer;
    private LocationManager locationManager;
    private LocationResult locationResult;
    private boolean isGPSEnabled;
    private boolean isNetworkEnabled;

    public static void startUserCurrentLocationListener(final Context context,
                                                        final LocationUtilListener locationUtilListener) {
        try {
            Log.i("System out", "lat long call started with listener");

            if (!isLocationEnabled(context)) {
                Log.i("System out","Location is not enabled");
                if (locationUtilListener != null) {
                    locationUtilListener.onLocationResult(null);
                }
                return;
            }

            new LocationUtil().getLocation(context, new LocationUtil.LocationResult() {
                @Override
                public void gotLocation(Location location) {

                    if (location != null) {
                        Log.i("System out","lat long received with listener");
                        locationUtilListener.onLocationResult(location);
                    } else {
                        Log.i("System out","Unable to get lat long received with listener");
                        if (locationUtilListener != null) {
                            locationUtilListener.onLocationResult(null);
                        }
                    }
                }
            });
        } catch (Exception e) {
            Log.e("System out", e.toString());
        }
    }

    /**
     * utility method to return if location is enabled or not
     *
     * @param context
     * @return true if location enabled else false
     */
    public static boolean isLocationEnabled(Context context) {
        if (context == null)
            return false;

        int locationMode;
        String locationProviders;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);

            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
                return false;
            }

            return locationMode != Settings.Secure.LOCATION_MODE_OFF;

        } else {
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }
    }

    public static boolean isLocationPermissionGranted(Context context) {
        return ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }


    /**
     * This timer class will return last known location result
     * if location manager is not able to get the result in 5 seconds.
     */
    public interface LocationUtilListener {
        void onLocationResult(Location location);
    }

    public static abstract class LocationResult {
        public abstract void gotLocation(Location location);
    }

    class GetLastLocation extends TimerTask {
        @Override
        public void run() {
            Log.d("System out","GetLastLocation is running");
            locationManager.removeUpdates(locationListenerGps);
            locationManager.removeUpdates(locationListenerNetwork);

            Location networkLocation = null;
            Location gpsLocation = null;

            if (isGPSEnabled)
                gpsLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (isNetworkEnabled)
                networkLocation = locationManager
                        .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            // if there are both values use the latest one
            if (gpsLocation != null && networkLocation != null) {
                if (gpsLocation.getTime() > networkLocation.getTime()) {
                    locationResult.gotLocation(gpsLocation);
                } else {
                    locationResult.gotLocation(networkLocation);
                }
                return;
            }

            if (gpsLocation != null) {
                locationResult.gotLocation(gpsLocation);
                return;
            }
            if (networkLocation != null) {
                locationResult.gotLocation(networkLocation);
                return;
            }
            locationResult.gotLocation(null);
        }
    }

    /***
     * Getting user real locations
     */
    LocationListener locationListenerNetwork = new LocationListener() {
        public void onLocationChanged(Location location) {
            locationTimer.cancel();
            locationResult.gotLocation(location);
            locationManager.removeUpdates(this);
            locationManager.removeUpdates(locationListenerGps);

        }

        public void onProviderDisabled(String provider) {
            Log.i("System out","Network onProviderDisabled");
        }

        public void onProviderEnabled(String provider) {
            Log.i("System out","Network onProviderEnabled");
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.i("System out","Network onStatusChanged");
        }
    };
    LocationListener locationListenerGps = new LocationListener() {
        public void onLocationChanged(Location location) {
            locationTimer.cancel();
            locationResult.gotLocation(location);
            locationManager.removeUpdates(this);
            locationManager.removeUpdates(locationListenerNetwork);
        }

        public void onProviderDisabled(String provider) {
            Log.i("System out","GPS onProviderDisabled");
        }

        public void onProviderEnabled(String provider) {
            Log.i("System out","GPS onProviderEnabled");
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.i("System out","GPS onStatusChanged");
        }
    };


    /**
     * Method to get the real user location
     *
     * @param context
     * @param result
     * @return
     */
    private boolean getLocation(Context context, LocationResult result) {
        locationResult = result;
        if (locationManager == null)
            locationManager = (LocationManager) context
                    .getSystemService(Context.LOCATION_SERVICE);

        // exceptions will be thrown if provider is not permitted.
        try {
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
            Log.e("System out", ex.toString());
        }
        try {
            isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
            Log.e("System out", ex.toString());
        }

        // don't start listeners if no provider is enabled
        if (!isGPSEnabled && !isNetworkEnabled)
            return false;

        if (isGPSEnabled) {
            locationManager.getProvider(LocationManager.GPS_PROVIDER).supportsAltitude();
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_MIN_TIME, LOCAITON_MIN_DISTANCE,
                    locationListenerGps);
        }
        if (isNetworkEnabled) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                    LOCATION_MIN_TIME, LOCAITON_MIN_DISTANCE,
                    locationListenerNetwork);
        }
        locationTimer = new Timer();
        locationTimer.schedule(new GetLastLocation(), LAST_KNOWN_LOCATION_DELAY);
        return true;
    }

}