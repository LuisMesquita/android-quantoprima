///**
// *
// */
//package br.com.mobiclub.quantoprima.service.map;
//
//import android.Manifest;
//import android.app.Activity;
//import android.content.Context;
//import android.content.pm.PackageManager;
//import android.location.GpsStatus;
//import android.location.Location;
//import android.location.LocationListener;
//import android.location.LocationManager;
//import android.os.Build;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.LinearLayout;
//
////import com.google.android.gms.maps.model.LatLng;
//
//import br.com.mobiclub.quantoprima.core.service.api.entity.Localization;
//import br.com.mobiclub.quantoprima.facade.Facade;
////import br.com.mobiclub.quantoprima.ui.fragment.MapFragment;
//import br.com.mobiclub.quantoprima.util.Ln;
//
///**
// * @author Thales Ferreira / l.thales.x@gmail.com
// *
// */
//public class PositionListener implements LocationListener {
//
//    private GpsStatus gpsStatus = null;
//    private LocationManager locationManager;
////    private MapFragment mapFragment;
//    private Facade facade;
//
//    private static PositionListener instance;
//
//    private boolean isGPSEnabled = false;
//    private boolean isNetworkEnabled;
//
//    // The minimum time between updates in milliseconds
//    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 2; // 20s
//    // The minimum distance to change Updates in meters
//    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 100; // 10 meters
//
//    //localizacao, pode ser a ultima conhecida do usuario
//    private Location location;
//    private boolean gpsStarted;
//    private LinearLayout gpsBar;
//    private GpsStatus.Listener gpsStatusListener;
//    private boolean started = false;
//
//    private static final String[] GPS_PERMS = {
//            Manifest.permission.ACCESS_FINE_LOCATION,
//            Manifest.permission.ACCESS_COARSE_LOCATION
//    };
//
//    public static final int GPS_PERMS_REQUEST = 1337;
//
//    // Ativicada
//    private Activity mActivity = null;
//
//    private PositionListener(Activity activity) {
//        locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
//        this.facade = Facade.getInstance();
//        mActivity = activity;
//        initLocalizationService();
//    }
//
//    public static PositionListener getInstance() {
//        if (instance == null) {
//            throw new IllegalStateException("Nenhuma instancia criada execute createPositionListener");
//        }
//        return instance;
//    }
//
//    public static PositionListener createPositionListener(Activity activity) {
//        if (instance == null) {
//            instance = new PositionListener(activity);
//        }
//        return instance;
//    }
//
//    public void initLocalizationService() {
//        Ln.d("initLocalizationService");
//        boolean enabled = locationByGPS();
//        if (!enabled) {
//            locationByNetwork();
//        }
//
//        Location loc = getLocation();
//        if (loc != null)
//            Ln.d("Ultima localizacao conhecida %f %f.", loc.getLatitude(),
//                    loc.getLongitude());
//
//        //listener para quando iniciar gps
//        gpsStatusListener = new GpsStatus.Listener() {
//            @Override
//            public void onGpsStatusChanged(int event) {
//                gpsStatus = locationManager.getGpsStatus(gpsStatus);
//                switch (event) {
//                    case GpsStatus.GPS_EVENT_STARTED:
//                        if (!isGpsStarted()) {
//                            locationByGPS();
//                        }
//                        Ln.d("GPS inciado");
//                        if (gpsBar != null)
//                            gpsBar.setVisibility(View.GONE);
//                        break;
//
//                    case GpsStatus.GPS_EVENT_STOPPED:
//                        Ln.d("GPS parado");
////                        isGPSEnabled = false;
////                        stopUsingGPS();
//                        break;
//
//                    case GpsStatus.GPS_EVENT_FIRST_FIX:
//                        // Do Something with mStatus info
//                        break;
//
//                    case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
//                        // Do Something with mStatus info
//                        break;
//                }
//            }
//        };
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (mActivity.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && mActivity.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//                locationManager.addGpsStatusListener(gpsStatusListener);
//            } else {
//                mActivity.requestPermissions(GPS_PERMS, GPS_PERMS_REQUEST);
//            }
//        } else {
//            locationManager.addGpsStatusListener(gpsStatusListener);
//        }
//    }
//
//    private Location getLocation() {
//        return location;
//    }
//
//    private boolean locationByGPS() {
//        // getting GPS status
//        isGPSEnabled = isGPSActived();
//        // if GPS Enabled get lat/long using GPS Services
//        if (isGPSEnabled) {
//            Location loc = location;
//
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                Log.e("Location", "YEP");
//                if (mActivity.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && mActivity.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//                    locationManager.requestLocationUpdates(
//                            LocationManager.GPS_PROVIDER,
//                            MIN_TIME_BW_UPDATES,
//                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
//                    Ln.d("GPS pelo gps ligado");
//                    if (locationManager != null) {
//                        loc = locationManager
//                                .getLastKnownLocation(LocationManager.GPS_PROVIDER);
////                        setLocation(loc);
//                    }
//                    setGpsStarted(true);
//                } else {
//                    mActivity.requestPermissions(GPS_PERMS, GPS_PERMS_REQUEST);
//                }
//            } else {
//                locationManager.requestLocationUpdates(
//                        LocationManager.GPS_PROVIDER,
//                        MIN_TIME_BW_UPDATES,
//                        MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
//                Ln.d("GPS pelo gps ligado");
//                if (locationManager != null) {
//                    loc = locationManager
//                            .getLastKnownLocation(LocationManager.GPS_PROVIDER);
////                    setLocation(loc);
//                }
//                setGpsStarted(true);
//            }
//
//        }
//        return isGPSEnabled;
//    }
//
//    public synchronized boolean isGpsStarted() {
//        return gpsStarted;
//    }
//
//    public synchronized void setGpsStarted(boolean gpsStarted) {
//        this.gpsStarted = gpsStarted;
//    }
//
//    private boolean isGPSActived() {
//        return locationManager
//                .isProviderEnabled(LocationManager.GPS_PROVIDER);
//    }
//
//    private boolean locationByNetwork() {
//        // getting network status
//        isNetworkEnabled = locationManager
//                .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
//        if (isNetworkEnabled) {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                if (mActivity.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && mActivity.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//                    locationManager.requestLocationUpdates(
//                            LocationManager.NETWORK_PROVIDER,
//                            MIN_TIME_BW_UPDATES,
//                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
//                    Ln.d("GPS pela rede ligado");
//                    if (locationManager != null) {
//                        Location location = locationManager
//                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
////                        setLocation(location);
//                    }
//                } else {
//                    mActivity.requestPermissions(GPS_PERMS, GPS_PERMS_REQUEST);
//                }
//            } else {
//                locationManager.requestLocationUpdates(
//                        LocationManager.NETWORK_PROVIDER,
//                        MIN_TIME_BW_UPDATES,
//                        MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
//                Ln.d("GPS pela rede ligado");
//                if (locationManager != null) {
//                    Location location = locationManager
//                            .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
////                    setLocation(location);
//                }
//            }
//        }
//        return isNetworkEnabled;
//    }
//
////    private void setLocation(Location location) {
////        this.location = location;
////        if(location != null)
////            facade.updateUserPosition(new LatLng(this.location.getLatitude(),
////                    this.location.getLongitude()));
////    }
////
////    public void setMapFragment(MapFragment mapFragment) {
////        this.mapFragment = mapFragment;
////    }
////
////    /* (non-Javadoc)
////         * @see android.location.LocationListener#onLocationChanged(android.location.Location)
////         */
////    @Override
////    public void onLocationChanged(Location location) {
////        LatLng locationLatlng = new LatLng(location.getLatitude(), location.getLongitude());
////        Ln.d("Nova posicao recebida %s", locationLatlng);
////        location.setLatitude(locationLatlng.latitude);
////        location.setLongitude(locationLatlng.longitude);
////        facade.updateUserPosition(locationLatlng);
////        if(mapFragment != null)
////            mapFragment.center(locationLatlng);
////    }
////
////    public Localization getLocalization() {
////        if(location == null)
////            return null;
////        return new Localization(location.getLatitude(), location.getLongitude());
////    }
//
//    /* (non-Javadoc)
//     * @see android.location.LocationListener#onStatusChanged(java.lang.String, int, android.os.Bundle)
//     */
//    @Override
//    public void onStatusChanged(String provider, int status, Bundle extras) {
//    }
//
//    /* (non-Javadoc)
//     * @see android.location.LocationListener#onProviderEnabled(java.lang.String)
//     */
//    @Override
//    public void onProviderEnabled(String provider) {
//    }
//
//    /* (non-Javadoc)
//     * @see android.location.LocationListener#onProviderDisabled(java.lang.String)
//     */
//    @Override
//    public void onProviderDisabled(String provider) {
//    }
//
//    public void cancel() {
//        if(locationManager != null) {
//            locationManager.removeUpdates(this);
//            locationManager.removeGpsStatusListener(gpsStatusListener);
//            started = false;
//        }
//    }
//
//    public boolean isGPSEnabled() {
//        if(isGPSEnabled) //cache
//            return isGPSEnabled;
//        else
//            return isGPSActived();
//    }
//
//    public void setGpsBar(LinearLayout gpsBar) {
//        this.gpsBar = gpsBar;
//    }
//
//}
