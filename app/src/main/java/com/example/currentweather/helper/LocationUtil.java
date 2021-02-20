package com.example.currentweather.helper;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static android.content.Context.LOCATION_SERVICE;


//Caller activity needs to handle the permissions
@SuppressWarnings({"MissingPermission"})
public class LocationUtil {

    private static final int LAST_KNOWN_LOCATION_DELAY = 1000;//5 seconds
    private static final int LOCATION_MIN_TIME = 1000;
    private static final int LOCAITON_MIN_DISTANCE = 500;

    private Timer locationTimer;
    private LocationManager locationManager;
    private LocationResult locationResult;

    public static void startUserCurrentLocationListener(final Context context, final LocationUtilListener locationUtilListener) {
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

    public interface LocationUtilListener {
        void onLocationResult(Location location);
    }

    public static abstract class LocationResult {
        public abstract void gotLocation(Location location);
    }

    class GetLastLocation extends TimerTask {
        @Override
        public void run() {

            locationManager.removeUpdates(locationListener);

            List<String> providers = locationManager.getProviders(true);
            Location bestLocation = null;
            for (String provider : providers) {
                Location l = locationManager.getLastKnownLocation(provider);
                if (l == null) {
                    continue;
                }
                if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                    bestLocation = l;

                }
            }

            if (bestLocation == null)
                locationResult.gotLocation(null);
            else
                locationResult.gotLocation(bestLocation);
        }
    }

    private LocationListener locationListener = new LocationListener() {

        public void onLocationChanged(Location location) {
            locationTimer.cancel();
            locationResult.gotLocation(location);
            locationManager.removeUpdates(this);
            locationManager.removeUpdates(locationListener);
        }

        public void onProviderDisabled(String provider) {
            Log.i("System out", "GPS onProviderDisabled");
        }

        public void onProviderEnabled(String provider) {
            Log.i("System out", "GPS onProviderEnabled");
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.i("System out", "GPS onStatusChanged");
        }
    };

    private boolean getLocation(Context context, LocationResult result) {

        locationResult = result;
        if (locationManager == null)
            locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            locationManager.getProvider(LocationManager.GPS_PROVIDER).supportsAltitude();
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    LOCATION_MIN_TIME,
                    LOCAITON_MIN_DISTANCE,
                    locationListener);
        } else if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                    LOCATION_MIN_TIME,
                    LOCAITON_MIN_DISTANCE,
                    locationListener);
        } else {
            locationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER,
                    LOCATION_MIN_TIME,
                    LOCAITON_MIN_DISTANCE,
                    locationListener);
        }

        locationTimer = new Timer();
        locationTimer.schedule(new GetLastLocation(), LAST_KNOWN_LOCATION_DELAY);
        return true;
    }

}