package com.example.pagesss.ui.home;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.pagesss.MainActivity;
import com.example.pagesss.R;
import com.example.pagesss.databinding.FragmentHomeBinding;
import com.example.pagesss.ui.home.HomeViewModel;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.api.directions.v5.DirectionsCriteria;
import com.mapbox.api.directions.v5.MapboxDirections;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.api.directions.v5.models.LegStep;
import com.mapbox.core.constants.Constants;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.MapboxMapOptions;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.maps.SupportMapFragment;
import com.mapbox.mapboxsdk.style.layers.LineLayer;
import com.mapbox.mapboxsdk.style.layers.RasterLayer;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.mapboxsdk.style.sources.ImageSource;
import com.mapbox.mapboxsdk.style.sources.RasterSource;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

import static com.mapbox.api.directions.v5.DirectionsCriteria.GEOMETRY_POLYLINE;
import static com.mapbox.mapboxsdk.style.layers.Property.LINE_CAP_ROUND;
import static com.mapbox.mapboxsdk.style.layers.Property.LINE_JOIN_ROUND;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconOffset;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineCap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineJoin;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineOpacity;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineWidth;

public class HomeFragment extends Fragment implements OnMapReadyCallback,
        PermissionsListener {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;

    public Double latitude;
    public Double longitude;
    public android.location.LocationManager locationManager;
    public Criteria criteria;
    public String bestProvider;
    public boolean transfer = true;
    private Object lock = new Object();

    private static final float NAVIGATION_LINE_WIDTH = 6;
    private static final float NAVIGATION_LINE_OPACITY = .5f;
    private static final String DRIVING_ROUTE_POLYLINE_LINE_LAYER_ID = "DRIVING_ROUTE_POLYLINE_LINE_LAYER_ID";
    private static final String DRIVING_ROUTE_POLYLINE_SOURCE_ID = "DRIVING_ROUTE_POLYLINE_SOURCE_ID";
    private static final int DRAW_SPEED_MILLISECONDS = 0;
    // Origin point in Paris, France
    public Point ORIGIN_POINT;

    // Destination point in Lyon, France
    private static final Point RASANA_DESTINATION_POINT = Point.fromLngLat(107.806426, -7.244287);
    private PermissionsManager permissionsManager;
    private MapboxMap mapboxMap;
    private MapboxDirections mapboxDirectionsClient;
    private Handler handler = new Handler();
    private Runnable runnable;
    private static final int REQUEST_CODE_LOCATION_PERMISSION =1;
    public int getCurrentLocationDone = 0;
//    private TextView textLatLong;
//    private ProgressBar progressBar;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Mapbox.getInstance(com.example.pagesss.ui.home.HomeFragment.this.getContext(), getString(R.string.access_token));

        // Create supportMapFragment
        SupportMapFragment mapFragment;

        if (savedInstanceState == null) {

            // Create fragment
            final FragmentTransaction transaction = getParentFragmentManager().beginTransaction();

            // Build mapboxMap
            MapboxMapOptions options = MapboxMapOptions.createFromAttributes(getContext(), null);
            options.camera(new CameraPosition.Builder()
                    .target(new LatLng(-52.6885, -70.1395))
                    .zoom(9)
                    .build());

            // Create map fragment
            mapFragment = SupportMapFragment.newInstance(options);

            // Add map fragment to parent container
            transaction.add(R.id.container, mapFragment, "com.mapbox.map");
            transaction.commit();
        } else {
            mapFragment = (SupportMapFragment) getParentFragmentManager().findFragmentByTag("com.mapbox.map");
        }
        if (mapFragment != null) {
            mapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(@NonNull MapboxMap mapboxMap) {
                    getCurrentLocationDone=0;
                    HomeFragment.this.mapboxMap = mapboxMap;
//                    permissionsManager = new PermissionsManager(com.example.pagesss.ui.home.HomeFragment.this);
//                    permissionsManager.requestLocationPermissions(com.example.pagesss.ui.home.HomeFragment.this.getActivity());
//
//                    while (!PermissionsManager.areLocationPermissionsGranted(com.example.pagesss.ui.home.HomeFragment.this.getContext())) {
//                        permissionsManager.requestLocationPermissions(com.example.pagesss.ui.home.HomeFragment.this.getActivity());
//                        if (PermissionsManager.areLocationPermissionsGranted(com.example.pagesss.ui.home.HomeFragment.this.getContext())) {
//                            break;
//                        }
//                    }
//                    if (PermissionsManager.areLocationPermissionsGranted(com.example.pagesss.ui.home.HomeFragment.this.getContext())) {
//                        Toast.makeText(com.example.pagesss.ui.home.HomeFragment.this.getActivity(), "Location access granted!!", Toast.LENGTH_LONG).show();
//// Get an instance of the component
//                        LocationComponent locationComponent = mapboxMap.getLocationComponent();
//                        System.out.println("++++++++++++++++++++++++++++++++++++168");
//                        locationManager = (android.location.LocationManager) com.example.pagesss.ui.home.HomeFragment.this.getActivity().getSystemService(Context.LOCATION_SERVICE);
//                        System.out.println("+++++++++++++++++++++++++++++++++++++170");
//                        criteria = new Criteria();
//                        System.out.println("+++++++++++++++++++++++++++++++++++++172");
//                        bestProvider = String.valueOf(locationManager.getBestProvider(criteria, true)).toString();
//                        System.out.println("++++++++++++++++++++++++++++++++++++++++174");
//                        Location location = locationManager.getLastKnownLocation(bestProvider);
//                        System.out.println("+++++++++++++++++++++++++++++++++++++++++++176");
//                        if (location != null) {
//                            System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++178");
//                            Log.e("TAG", "GPS is on");
//                            latitude = location.getLatitude();
//                            longitude = location.getLongitude();
//                            System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++182");
//                            Location originLocation = location;
//                            ORIGIN_POINT = Point.fromLngLat(longitude, latitude);
//                            System.out.println("Latitude: "+latitude.toString());
//                            System.out.println("159 ORIGIN_POINT filled!!!");
//                        } else {
//                            //This is what you need:
//                            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                                // TODO: Consider calling
//                                //    ActivityCompat#requestPermissions
//                                LocationRequest request = LocationRequest.create();
//                                System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++191");
//                                locationManager.requestLocationUpdates(bestProvider, 1000, 0, (android.location.LocationListener) getActivity());
//                                // here to request the missing permissions, and then overriding
//                                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                                //                                          int[] grantResults)
//                                // to handle the case where the user grants the permission. See the documentation
//                                // for ActivityCompat#requestPermissions for more details.
//                                return;
//                            } else {
//                                System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++200");
//                            }
//                        }
//                    } else {
//                    }
//          mapboxMap.getStyle(new Style.OnStyleLoaded() {
//            @Override
//            public void onStyleLoaded(@NonNull Style style) {
//              enableLocationComponent(style);
//            }
//          });
//                    int counter = 0;
//                    while(ORIGIN_POINT==null && counter<200) {
//                        if(ORIGIN_POINT!=null){System.out.println("ORIGIN_POINT FILLEDDD !!!");}
//                            counter++;
                        if (ContextCompat.checkSelfPermission(
                                getContext(), Manifest.permission.ACCESS_FINE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED) {
                            System.out.println("upp00 !!!");
                            ActivityCompat.requestPermissions(
                                    getActivity(),
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    REQUEST_CODE_LOCATION_PERMISSION
                            );
                        } else {
                            getCurrentLocationDone=1;
                            LocationRequest locationRequest = new LocationRequest();
                            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                            LocationServices.getFusedLocationProviderClient(getActivity())
                                    .requestLocationUpdates(locationRequest, new LocationCallback() {
                                        @Override
                                        public void onLocationResult(@NonNull @NotNull LocationResult locationResult) {
                                            super.onLocationResult(locationResult);
                                            LocationServices.getFusedLocationProviderClient(getActivity())
                                                    .removeLocationUpdates(this);
//                                            if (locationResult !=null && locationResult.getLocations().size() > 0){
                                                int latestLocationIndex = locationResult.getLocations().size() -1;
                                                double latitude = locationResult.getLocations().get(latestLocationIndex).getLatitude();
                                                double longitude = locationResult.getLocations().get(latestLocationIndex).getLongitude();
                                                ORIGIN_POINT = Point.fromLngLat(longitude, latitude);
                                            if (ORIGIN_POINT != null) {
                                                System.out.println("ORIGIN_POINT EXIST !!!!!!!");
                                                //Isi variable origin point setelah minta izin
                                                mapboxMap.setStyle(new Style.Builder().fromUri(Style.SATELLITE_STREETS)
                                                                .withImage("icon-id", BitmapFactory.decodeResource(
                                                                        getResources(), R.drawable.pin))
                                                                .withSource(new GeoJsonSource("source-id",
                                                                        FeatureCollection.fromFeatures(new Feature[]{
                                                                                Feature.fromGeometry(Point.fromLngLat(ORIGIN_POINT.longitude(), ORIGIN_POINT.latitude())),
                                                                                Feature.fromGeometry(Point.fromLngLat(RASANA_DESTINATION_POINT.longitude(), RASANA_DESTINATION_POINT.latitude())),
                                                                        })))

                                                                .withLayer(new SymbolLayer("layer-id",
                                                                        "source-id").withProperties(
                                                                        iconImage("icon-id"),
                                                                        iconOffset(new Float[]{0f, -8f})
                                                                ))
                                                                .withSource(new GeoJsonSource(DRIVING_ROUTE_POLYLINE_SOURCE_ID))
                                                                .withLayerBelow(new LineLayer(DRIVING_ROUTE_POLYLINE_LINE_LAYER_ID,
                                                                        DRIVING_ROUTE_POLYLINE_SOURCE_ID)
                                                                        .withProperties(
                                                                                lineWidth(NAVIGATION_LINE_WIDTH),
                                                                                lineOpacity(NAVIGATION_LINE_OPACITY),
                                                                                lineCap(LINE_CAP_ROUND),
                                                                                lineJoin(LINE_JOIN_ROUND),
                                                                                lineColor(Color.parseColor("#07f2e0"))
                                                                        ), "layer-id"),
//                                        .withSource(new RasterSource("hanselkane.garut","https://api.mapbox.com/v1/hanselkane.garut/7/1/8.png?access_token=sk.eyJ1IjoiaGFuc2Vsa2FuZSIsImEiOiJja3FjZXcxOTYwamc3MndubTM5czV0MDcxIn0.B34iowDcxUmG2Yj1i4qb3w"))
//                                        .withLayerBelow(new RasterLayer("garut","hanselkane.garut"),"layer-id"),
                                                        new Style.OnStyleLoaded() {
                                                            @Override
                                                            public void onStyleLoaded(@NonNull Style style) {
                                                                enableLocationComponent(style);
                                                                getDirectionsRoute(ORIGIN_POINT, RASANA_DESTINATION_POINT);
                                                            }
                                                        });
//                            break;
                                            } else {
                                                System.out.println("Location null");
                                                getCurrentLocation();
                                            }
//                                            }
                                        }
                                    }, Looper.getMainLooper());
                            System.out.println("IN 264");
                        }
//                    }
//                    if(ORIGIN_POINT!=null){System.out.println("ORIGIN_POINT FILLEDDD !!!");}
                    System.out.println("ORIGIN_POINT FILLESSS !!!");


                    //Tunggu variable ORIGIN_POIN ada isinya....
//                    while(ORIGIN_POINT==null) {

//                    }
                }
            });
        }




        final TextView textView = binding.textHome;
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;

    }

    @Override
    public void onMapReady(@NonNull MapboxMap mapboxMap) {
        System.out.println("245 onCreate");
        mapboxMap.setStyle(new Style.Builder().fromUri(Style.SATELLITE_STREETS)
                        .withImage("icon-id", BitmapFactory.decodeResource(
                                getResources(), R.drawable.pin))
                        .withSource(new GeoJsonSource("source-id",
                                FeatureCollection.fromFeatures(new Feature[] {
                                        Feature.fromGeometry(Point.fromLngLat(ORIGIN_POINT.longitude(), ORIGIN_POINT.latitude())),
                                        Feature.fromGeometry(Point.fromLngLat(RASANA_DESTINATION_POINT.longitude(), RASANA_DESTINATION_POINT.latitude())),
                                })))
                        .withLayer(new SymbolLayer("layer-id",
                                "source-id").withProperties(
                                iconImage("icon-id"),
                                iconOffset(new Float[] {0f, -8f})
                        ))
                        .withSource(new GeoJsonSource(DRIVING_ROUTE_POLYLINE_SOURCE_ID))
                        .withLayerBelow(new LineLayer(DRIVING_ROUTE_POLYLINE_LINE_LAYER_ID,
                                DRIVING_ROUTE_POLYLINE_SOURCE_ID)
                                .withProperties(
                                        lineWidth(NAVIGATION_LINE_WIDTH),
                                        lineOpacity(NAVIGATION_LINE_OPACITY),
                                        lineCap(LINE_CAP_ROUND),
                                        lineJoin(LINE_JOIN_ROUND),
                                        lineColor(Color.parseColor("#d742f4"))
                                ),"layer-id"),
                new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {
//                enableLocationComponent(style);
                        getDirectionsRoute(ORIGIN_POINT, RASANA_DESTINATION_POINT);
                    }
                });
    }

    private void getDirectionsRoute(Point origin, Point destination) {
        mapboxDirectionsClient = MapboxDirections.builder()
                .origin(origin)
                .destination(destination)
                .overview(DirectionsCriteria.OVERVIEW_FULL)
                .profile(DirectionsCriteria.PROFILE_DRIVING)
                .geometries(GEOMETRY_POLYLINE)
                .alternatives(true)
                .steps(true)
                .accessToken(getString(R.string.access_token))
                .build();

        mapboxDirectionsClient.enqueueCall(new Callback<DirectionsResponse>() {
            @Override
            public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
// Create log messages in case no response or routes are present
                if (response.body() == null) {
                    Timber.d("No routes found, make sure you set the right user and access token.");
                    return;
                } else if (response.body().routes().size() < 1) {
                    Timber.d("No routes found");
                    return;
                }

// Get the route from the Mapbox Directions API response
                DirectionsRoute currentRoute = response.body().routes().get(0);

// Start the step-by-step process of drawing the route
                runnable = new DrawRouteRunnable(mapboxMap, currentRoute.legs().get(0).steps(), handler);
                handler.postDelayed(runnable, DRAW_SPEED_MILLISECONDS);
            }

            @Override
            public void onFailure(Call<DirectionsResponse> call, Throwable throwable) {
                Toast.makeText(com.example.pagesss.ui.home.HomeFragment.this.getActivity(),
                        "snaking_directions_activity_error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private static class DrawRouteRunnable implements Runnable {
        private MapboxMap mapboxMap;
        private List<LegStep> steps;
        private List<Feature> drivingRoutePolyLineFeatureList;
        private Handler handler;
        private int counterIndex;

        DrawRouteRunnable(MapboxMap mapboxMap, List<LegStep> steps, Handler handler) {
            this.mapboxMap = mapboxMap;
            this.steps = steps;
            this.handler = handler;
            this.counterIndex = 0;
            drivingRoutePolyLineFeatureList = new ArrayList<>();
        }

        @Override
        public void run() {
            if (counterIndex < steps.size()) {
                LegStep singleStep = steps.get(counterIndex);
                if (singleStep != null && singleStep.geometry() != null) {
                    LineString lineStringRepresentingSingleStep = LineString.fromPolyline(
                            singleStep.geometry(), Constants.PRECISION_5);
                    Feature featureLineString = Feature.fromGeometry(lineStringRepresentingSingleStep);
                    drivingRoutePolyLineFeatureList.add(featureLineString);
                }
                if (mapboxMap.getStyle() != null) {
                    GeoJsonSource source = mapboxMap.getStyle().getSourceAs(DRIVING_ROUTE_POLYLINE_SOURCE_ID);
                    if (source != null) {
                        source.setGeoJson(FeatureCollection.fromFeatures(drivingRoutePolyLineFeatureList));
                    }
                }
                counterIndex++;
                handler.postDelayed(this, DRAW_SPEED_MILLISECONDS);
            }
        }
    }

    @SuppressWarnings({"MissingPermission"})
    private void enableLocationComponent(@NonNull Style loadedMapStyle) {
        System.out.println("enableLocationComponent triggered !!!");
// Check if permissions are enabled and if not request
        if (PermissionsManager.areLocationPermissionsGranted(getContext())) {
            Toast.makeText(com.example.pagesss.ui.home.HomeFragment.this.getActivity(), "Location access granted!!", Toast.LENGTH_LONG).show();
// Get an instance of the component
            LocationComponent locationComponent = mapboxMap.getLocationComponent();
            System.out.println("++++++++++++++++++++++391");
            locationManager = (LocationManager) HomeFragment.this.getActivity().getSystemService(Context.LOCATION_SERVICE);
            System.out.println("++++++++++++++++++++++393");
            criteria = new Criteria();
            bestProvider = String.valueOf(locationManager.getBestProvider(criteria, true)).toString();

            Location location = locationManager.getLastKnownLocation(bestProvider);
            if (location != null) {
                Log.e("TAG", "GPS is on");
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                Location originLocation=location;
                ORIGIN_POINT= Point.fromLngLat(longitude, latitude);
                System.out.println("Latitude: "+latitude.toString());
                System.out.println("++++++++++++++++++++++404");
            }
            else{
                System.out.println("++++++++++++++++++++++407");
                //This is what you need:
                locationManager.requestLocationUpdates(bestProvider, 1000, 0, (android.location.LocationListener) com.example.pagesss.ui.home.HomeFragment.this.getActivity());
                System.out.println("++++++++++++++++++++++410");
            }

// Activate with options
            locationComponent.activateLocationComponent(
                    LocationComponentActivationOptions.builder(getContext(), loadedMapStyle).build());

// Enable to make component visible
            locationComponent.setLocationComponentEnabled(true);

// Set the component's camera mode
            locationComponent.setCameraMode(CameraMode.TRACKING);

// Set the component's render mode
            locationComponent.setRenderMode(RenderMode.COMPASS);
        } else {
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//    while(!transfer){
//      try{
//        System.out.println("Loop onRequest");
//        wait();
//      } catch (InterruptedException e) {
//        Thread.currentThread().interrupt();
//      }
//    }
//    transfer = true;
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        System.out.println("onRequestPermissionR triggered");
        //permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==REQUEST_CODE_LOCATION_PERMISSION && grantResults.length>0){
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
                getCurrentLocation();
            } else {

            }
        }
//        switch (requestCode) {
//            case MY_PERMISSIONS_REQUEST_LOCATION: {
//                // If request is cancelled, the result arrays are empty.
//                if (grantResults.length > 0
//                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    // permission was granted, yay! Do the
//                    // location-related task you need to do.
//                    if (ContextCompat.checkSelfPermission(getContext(),
//                            Manifest.permission.ACCESS_FINE_LOCATION)
//                            == PackageManager.PERMISSION_GRANTED) {
//                        LocationComponent locationComponent = mapboxMap.getLocationComponent();
//
//                        locationManager = (LocationManager) HomeFragment.this.getActivity().getSystemService(Context.LOCATION_SERVICE);
//                        criteria = new Criteria();
//                        bestProvider = String.valueOf(locationManager.getBestProvider(criteria, true)).toString();
//                        Location location = locationManager.getLastKnownLocation(bestProvider);
//                        if (location != null) {
//                            Log.e("TAG", "GPS is on");
//                            latitude = location.getLatitude();
//                            longitude = location.getLongitude();
//                            Location originLocation=location;
//                            ORIGIN_POINT= Point.fromLngLat(longitude, latitude);
//                            System.out.println("++++++++++++++++++++++465");
//                            System.out.println("Latitude: "+latitude.toString());
//                        }
//                        else{
//                            //This is what you need:
//                            locationManager.requestLocationUpdates(bestProvider, 1000, 0, (android.location.LocationListener) com.example.pagesss.ui.home.HomeFragment.this);
//                        }
//                    }
//
//                } else {
//
//                    // permission denied, boo! Disable the
//                    // functionality that depends on this permission.
//
//                }
//                return;
//            }
//
//        }
    }


    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {

    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            mapboxMap.getStyle(new Style.OnStyleLoaded() {
                @Override
                public void onStyleLoaded(@NonNull Style style) {
//                    enableLocationComponent(style);
                }
            });
        } else {
            getActivity().finish();
        }
    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(getContext())
                        .setTitle("Izinkan berbagi lokasi")
                        .setMessage("Fitur otomatis")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                //Loc.this 1
                                ActivityCompat.requestPermissions(HomeFragment.this.getActivity(),
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void getCurrentLocation(){
        getCurrentLocationDone=1;
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationServices.getFusedLocationProviderClient(getActivity())
                .requestLocationUpdates(locationRequest, new LocationCallback() {
                    @Override
                    public void onLocationResult(@NonNull @NotNull LocationResult locationResult) {
                        super.onLocationResult(locationResult);
                        LocationServices.getFusedLocationProviderClient(getActivity())
                                .removeLocationUpdates(this);
                        if (locationResult !=null && locationResult.getLocations().size() > 0){
                            int latestLocationIndex = locationResult.getLocations().size() -1;
                            double latitude = locationResult.getLocations().get(latestLocationIndex).getLatitude();
                            double longitude = locationResult.getLocations().get(latestLocationIndex).getLongitude();
                            ORIGIN_POINT = Point.fromLngLat(longitude, latitude);
                        }
                    }
                }, Looper.getMainLooper());
    }
}