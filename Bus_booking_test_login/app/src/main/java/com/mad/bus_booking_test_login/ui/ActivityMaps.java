package com.mad.bus_booking_test_login.ui;

import androidx.fragment.app.FragmentActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;
import com.mad.bus_booking_test_login.FetchURL;
import com.mad.bus_booking_test_login.R;
import com.mad.bus_booking_test_login.databinding.ActivityMapsBinding;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

public class ActivityMaps extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private LatLng startLatLng, endLatLng;
    private TextView tv_duration;
    private Button btnOpenGoogleMaps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        tv_duration = findViewById(R.id.tv_duration);
        btnOpenGoogleMaps = findViewById(R.id.btn_open_google_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        btnOpenGoogleMaps.setOnClickListener(v -> openInGoogleMaps(startLatLng, endLatLng));
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
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //enable Google Map GUI settings

        // Enable zoom gestures
        mMap.getUiSettings().setZoomGesturesEnabled(true);

        // Enable other scroll gestures, zoom gestures, rotate gestures and tilt gestures
        mMap.getUiSettings().setScrollGesturesEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true); // Adds + and - buttons for zooming
        mMap.getUiSettings().setRotateGesturesEnabled(true);
        mMap.getUiSettings().setTiltGesturesEnabled(true);

        // Get Starting and Ending Points
        String startingPoint = getIntent().getStringExtra("starting_point");
        String endingPoint = getIntent().getStringExtra("ending_point");

        // Convert to LatLng
        startLatLng = getLatLngFromAddress(this, startingPoint);
        endLatLng = getLatLngFromAddress(this, endingPoint);

        //if the starting LatLng and ending LatLng is not empty, add markers
        if (startLatLng != null && endLatLng != null) {
            // Add markers
            mMap.addMarker(new MarkerOptions().position(startLatLng).title("Starting Point"));
            mMap.addMarker(new MarkerOptions().position(endLatLng).title("Ending Point"));

            //focus the camera a bit on the starting point
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(startLatLng, 15));

            // Fetch and draw the route
            drawRoute(startLatLng, endLatLng);
        }
    }

    //method to retrieve the Latitudes and Longitudes of an Address name which is a String and return LatLng object
    private LatLng getLatLngFromAddress(Context context, String address) {
        Geocoder geocoder = new Geocoder(context);
        try {
            List<Address> addresses = geocoder.getFromLocationName(address, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address location = addresses.get(0);
                return new LatLng(location.getLatitude(), location.getLongitude());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Draw PolyLine Routes on the map from JSON response
    @SuppressLint("SetTextI18n")
    private void drawRoute(LatLng start, LatLng end) {
        String directionsURL = getDirectionsUrl(start, end);
        new FetchURL(data -> {
            try {
                JSONObject jsonObject = new JSONObject(data);
                JSONArray routes = jsonObject.optJSONArray("routes");

                // Check if the routes array is not empty
                if (routes == null || routes.length() == 0) {
                    Log.e("ActivityMaps", "No routes found in the response.");
                    return;
                }

                // Proceed with parsing the first route
                JSONObject route = routes.getJSONObject(0);
                JSONObject overviewPolyline = route.getJSONObject("overview_polyline");
                String encodedPolyline = overviewPolyline.getString("points");

                // Decode the polyline and add it to the map
                List<LatLng> path = PolyUtil.decode(encodedPolyline);
                mMap.addPolyline(new PolylineOptions().addAll(path).color(Color.BLUE).width(10));

                JSONArray legs = route.optJSONArray("legs");
                if (legs != null && legs.length() > 0) {
                    JSONObject leg = legs.getJSONObject(0);
                    JSONObject durationObject = leg.getJSONObject("duration");
                    String durationText = durationObject.getString("text");

                    // Update duration in the TextView
                    tv_duration.setText("Duration: " + durationText);
                }
                Log.d("ActivityMaps", "Directions API Response: " + data);
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("ActivityMaps", "Error parsing the Directions API response: " + e.getMessage());
            }
        }).execute(directionsURL);
    }


    // Get Directions API URL
    private String getDirectionsUrl(LatLng origin, LatLng dest) {
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        String parameters = str_origin + "&" + str_dest + "&key=AIzaSyB7hhzcYvYYCPcrof2y5M-I40JNjzq4ExE";

        String url = "https://maps.googleapis.com/maps/api/directions/json?" + parameters;
        Log.d("ActivityMaps", "Directions API URL: " + url);
        return url;
    }

    private void openInGoogleMaps(LatLng start, LatLng end) {
        String uri = "https://www.google.com/maps/dir/?api=1" +
                "&origin=" + start.latitude + "," + start.longitude +
                "&destination=" + end.latitude + "," + end.longitude +
                "&travelmode=driving";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        intent.setPackage("com.google.android.apps.maps");
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Toast.makeText(this, "Google Maps is not installed.", Toast.LENGTH_SHORT).show();
        }
    }

}