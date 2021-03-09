package com.example.myapplication;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private TextView mLocationMarkerText;
    private LatLng mCenterLatLong;
    private AddressResultReceiver mResultReceiver;
    private String city;
    private FusedLocationProviderClient fusedLocationClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        Bundle extras = getIntent().getExtras();
        city = extras.getString("city");
        mapFragment.getMapAsync(this);
        mLocationMarkerText = (TextView) findViewById(R.id.locationMarkertext);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);


    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng cityLatLng = new LatLng(31.963158, 35.930359);
        switch (city) {

            case "إربد":
                cityLatLng = new LatLng(32.5568, 35.8469);
                ;
                break;
            case "عمان":
                cityLatLng = new LatLng(31.963158, 35.930359);
                ;
                break;
            case "الزرقاء":
                cityLatLng = new LatLng(32.0608, 36.0942);
                ;
                break;
            case "الطفيلة":
                cityLatLng = new LatLng(30.8337, 35.6161);
                ;
                break;
            case "جرش":
                cityLatLng = new LatLng(32.2747, 35.8961);
                ;
                break;
            case "معان":
                cityLatLng = new LatLng(30.1927, 35.7249);
                ;
                break;
            case "عجلون":
                cityLatLng = new LatLng(32.3326, 35.7517);
                ;
                break;
//            case "البلقاء":
//                cityLatLng = new LatLng(31.963158, 35.930359);
//                ;break;
            case "الكرك":
                cityLatLng = new LatLng(31.1853, 35.7048);
                ;
                break;
            case "المفرق":
                cityLatLng = new LatLng(32.20322, 36.12461);
                ;
                break;
            case "العقبة":
                cityLatLng = new LatLng(29.5321, 35.0063);
                ;
                break;
            case "السلط":
                cityLatLng = new LatLng(32.0346, 35.7269);
                ;
                break;

        }
        //LatLng amman = new LatLng(31.963158, 35.930359);
        mMap.addMarker(new MarkerOptions()
                .position(cityLatLng));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(cityLatLng, 15));
//        googleMap.animateCamera(CameraUpdateFactory.zoomIn());
//        // Zoom out to zoom level 10, animating with a duration of 2 seconds.
//        googleMap.animateCamera(CameraUpdateFactory.zoomTo(31), 2000, null);

        mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                mCenterLatLong = cameraPosition.target;
                mMap.clear();


                try {

                    Location mLocation = new Location("");
                    mLocation.setLatitude(mCenterLatLong.latitude);
                    mLocation.setLongitude(mCenterLatLong.longitude);

                    startIntentService(mLocation);
                    mLocationMarkerText.setText("Lat : " + mCenterLatLong.latitude + "," + "Long : " + mCenterLatLong.longitude);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        checkLocationPermission();
//        mMap.setMyLocationEnabled(true);
//        mMap.getUiSettings().setMyLocationButtonEnabled(true);
//
//        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }


    protected void startIntentService(Location mLocation) {
        // Create an intent for passing to the intent service responsible for fetching the address.
        Intent intent = new Intent(this, FetchAddressIntentService.class);

        // Pass the result receiver as an extra to the service.
        intent.putExtra(AppUtils.LocationConstants.RECEIVER, mResultReceiver);

        // Pass the location data as an extra to the service.
        intent.putExtra(AppUtils.LocationConstants.LOCATION_DATA_EXTRA, mLocation);

        // Start the service. If the service isn't already running, it is instantiated and started
        // (creating a process for it if needed); if it is running then it remains running. The
        // service kills itself automatically once all intents are processed.
        startService(intent);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        mMap = googleMap;
//
//        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
//    }
    protected String mAddressOutput;
    protected String mAreaOutput;
    protected String mCityOutput;
    protected String mStateOutput;

    class AddressResultReceiver extends ResultReceiver {
        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        /**
         * Receives data sent from FetchAddressIntentService and updates the UI in MainActivity.
         */
        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {

            // Display the address string or an error message sent from the intent service.
            mAddressOutput = resultData.getString(AppUtils.LocationConstants.RESULT_DATA_KEY);

            mAreaOutput = resultData.getString(AppUtils.LocationConstants.LOCATION_DATA_AREA);

            mCityOutput = resultData.getString(AppUtils.LocationConstants.LOCATION_DATA_CITY);
            mStateOutput = resultData.getString(AppUtils.LocationConstants.LOCATION_DATA_STREET);

            displayAddressOutput();

            // Show a toast message if an address was found.
            if (resultCode == AppUtils.LocationConstants.SUCCESS_RESULT) {
                //  showToast(getString(R.string.address_found));


            }


        }

    }

    EditText mLocationAddress;

    protected void displayAddressOutput() {
        //  mLocationAddressTextView.setText(mAddressOutput);
        try {
            if (mAreaOutput != null)
                // mLocationText.setText(mAreaOutput+ "");

                mLocationAddress.setText(mAddressOutput);
            //mLocationText.setText(mAreaOutput);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void backToLocation(View view) {
        SharedData.longitude = mCenterLatLong.longitude + "";
        SharedData.latitude = mCenterLatLong.latitude + "";
        String result = mCenterLatLong.longitude + " , " + mCenterLatLong.latitude;
        Intent returnIntent = new Intent();
        returnIntent.putExtra("result", result);
        returnIntent.putExtra("lang",mCenterLatLong.longitude);
        returnIntent.putExtra("lat",mCenterLatLong.latitude);

        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    public void getCurrentLocation(View view) {
        checkLocationPermission();
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            double latitude = location.getLatitude();
                            double longitude = location.getLongitude();
                            LatLng cityLatLng = new LatLng(latitude,longitude);
                            mMap.addMarker(new MarkerOptions()
                                    .position(cityLatLng));
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(cityLatLng, 15));

                            mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
                                @Override
                                public void onCameraChange(CameraPosition cameraPosition) {
                                    mCenterLatLong = cameraPosition.target;
                                    mMap.clear();


                                    try {

                                        Location mLocation = new Location("");
                                        mLocation.setLatitude(mCenterLatLong.latitude);
                                        mLocation.setLongitude(mCenterLatLong.longitude);

                                        startIntentService(mLocation);
                                        mLocationMarkerText.setText("Lat : " + mCenterLatLong.latitude + "," + "Long : " + mCenterLatLong.longitude);

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }
                    }
                });
    }

    private void checkLocationPermission(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            ActivityCompat.requestPermissions(MapsActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 109);

        }
    }
}