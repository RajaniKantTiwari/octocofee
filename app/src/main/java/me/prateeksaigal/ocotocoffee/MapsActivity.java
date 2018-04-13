package me.prateeksaigal.ocotocoffee;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.lucasr.twowayview.TwoWayView;

import java.util.ArrayList;

import javax.inject.Inject;

import me.prateeksaigal.base.BaseActivity;
import me.prateeksaigal.base.BaseResponse;
import me.prateeksaigal.injector.presenter.CommonPresenter;
import me.prateeksaigal.network.request.LoginRequest;
import me.prateeksaigal.ocotocoffee.adapter.CoffeeAdapter;
import me.prateeksaigal.utils.CommonUtility;

public class MapsActivity extends BaseActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {


    private Handler mHandler;
    private Toolbar toolbar;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private Location mLastLocation;
    private Marker mCurrLocationMarker;

    private static final String TAG = MapsActivity.class.getSimpleName();
    private GoogleMap mMap;
    private CameraPosition mCameraPosition;

    // The entry points to the Places API.
    private GeoDataClient mGeoDataClient;
    private PlaceDetectionClient mPlaceDetectionClient;

    // The entry point to the Fused Location Provider.
    private FusedLocationProviderClient mFusedLocationProviderClient;

    // A default location (Sydney, Australia) and default zoom to use when location permission is
    // not granted.
    private final LatLng mDefaultLocation = new LatLng(-33.8523341, 151.2106085);
    private static final int DEFAULT_ZOOM = 15;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted;

    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private Location mLastKnownLocation;

    // Keys for storing activity state.
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";

    // Used for selecting the current place.
    private static final int M_MAX_ENTRIES = 5;
    private String[] mLikelyPlaceNames;
    private String[] mLikelyPlaceAddresses;
    private String[] mLikelyPlaceAttributions;
    private LatLng[] mLikelyPlaceLatLngs;

    private boolean machine_selected = false;
    private Marker selected_marker;
    private double userLat, userLon;
    private CoffeeAdapter adpCoffee;
    private TwoWayView lvTest;
    @Inject
    CommonPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        // Retrieve location and camera position from saved instance state.
        if (savedInstanceState != null) {
            mLastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            mCameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }

        LinearLayout maps_search_container = (LinearLayout) findViewById(R.id.search_maps_container);


        setContentView(R.layout.activity_maps);

        // Construct a GeoDataClient.
        mGeoDataClient = Places.getGeoDataClient(this, null);

        // Construct a PlaceDetectionClient.
        mPlaceDetectionClient = Places.getPlaceDetectionClient(this, null);

        // Construct a FusedLocationProviderClient.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);


        mHandler = new Handler();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.bringToFront();
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawer.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessary or else your hamburger icon wont show up
        //actionBarDrawerToggle.setDrawerIndicatorEnabled(false);
        //actionBarDrawerToggle.setHomeAsUpIndicator(R.drawable.ic_hamburger);
        actionBarDrawerToggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.black));
        for (int i = 0; i < toolbar.getChildCount(); i++) {
            if (toolbar.getChildAt(i) instanceof ImageButton) {
                toolbar.getChildAt(i).setScaleX(1.5f);
                toolbar.getChildAt(i).setScaleY(1.5f);
            }
        }
        actionBarDrawerToggle.syncState();

        handleNavItemClick();

        ArrayList<String> items = new ArrayList<String>();
        items.add("Add");

        ArrayList<Integer> drawables = new ArrayList<>();
        drawables.add(R.drawable.plus);

        ArrayList<Integer> coffee_ids = new ArrayList<>();
        coffee_ids.add(-1);

        CoffeeDBHelper coffeeDBHelper = new CoffeeDBHelper(getApplicationContext());
        ArrayList<Coffee> coffeeArrayList = new ArrayList<>();
        coffeeArrayList = coffeeDBHelper.getAll();
        if (coffeeArrayList != null) {
            if (coffeeArrayList.size() > 4) {
                for (int i = 4; i < coffeeArrayList.size(); i++) {
                    coffee_ids.add(coffeeArrayList.get(i).getId());
                    items.add(coffeeArrayList.get(i).getName());
                    drawables.add(R.drawable.coffee_cup);
                }
            }
        }


        adpCoffee = new CoffeeAdapter(MapsActivity.this, items, drawables, coffee_ids);

        lvTest = (TwoWayView) findViewById(R.id.scroll_bar);
        lvTest.setAdapter(adpCoffee);

        lvTest.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (!machine_selected) {
                    CommonUtility.showToastLong(getApplicationContext(),"Please select a machine first");
                } else {

                    Intent intent;
                    if (i != 0) {
                        intent = new Intent(getApplicationContext(), SelectedMapsActivity.class);
                    } else {
                        intent = new Intent(getApplicationContext(), BaseCoffeeActivity.class);
                        intent.putExtra("forwardtomaps2", true);
                    }

                    intent.putExtra("machine_lat", selected_marker.getPosition().latitude);
                    intent.putExtra("machine_lon", selected_marker.getPosition().longitude);
                    intent.putExtra("user_lat", userLat);
                    intent.putExtra("user_lon", userLon);

                    startActivity(intent);

                }
            }
        });

    }

    @Override
    public void attachView() {
        getActivityComponent().inject(this);
        presenter.attachView(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ArrayList<String> items = new ArrayList<String>();
        items.add("Add");

        ArrayList<Integer> drawables = new ArrayList<>();
        drawables.add(R.drawable.plus);

        ArrayList<Integer> coffee_ids = new ArrayList<>();
        coffee_ids.add(-1);

        CoffeeDBHelper coffeeDBHelper = new CoffeeDBHelper(getApplicationContext());
        ArrayList<Coffee> coffeeArrayList = new ArrayList<>();
        coffeeArrayList = coffeeDBHelper.getAll();

        if (coffeeArrayList != null) {
            if (coffeeArrayList.size() > 4) {
                for (int i = 4; i < coffeeArrayList.size(); i++) {
                    coffee_ids.add(coffeeArrayList.get(i).getId());
                    items.add(coffeeArrayList.get(i).getName());
                    drawables.add(R.drawable.coffee_cup);
                }
            }
        }


        lvTest.setAdapter(null);

        adpCoffee = new CoffeeAdapter(MapsActivity.this, items, drawables, coffee_ids);

        lvTest = (TwoWayView) findViewById(R.id.scroll_bar);
        lvTest.setAdapter(adpCoffee);

        presenter.loginMerchant(this, new LoginRequest("abcd@gmail.com", "12345678"));

    }

    private void handleNavItemClick() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {
                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.nav_profile:
                        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                        break;
                    case R.id.nav_about_us:
                        startActivity(new Intent(getApplicationContext(), AboutUsActivity.class));
                        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                        break;
                    case R.id.nav_orders:
                        startActivity(new Intent(getApplicationContext(), OrdersActivity.class));
                        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                        break;
                    case R.id.nav_contact:
                        startActivity(new Intent(getApplicationContext(), ContactActivity.class));
                        break;
                    case R.id.nav_payments:
                        startActivity(new Intent(getApplicationContext(), PaymentActivity.class));
                        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                        break;
                    case R.id.nav_settings:
                        startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                        break;

                }
                return true;
            }
        });
    }


    /**
     * Saves the state of the map when the activity is paused.
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (mMap != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, mMap.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, mLastKnownLocation);
            super.onSaveInstanceState(outState);
        }
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
        if (googleMap==null)
            return;
        mMap = googleMap;
        mMap.getUiSettings().setMapToolbarEnabled(false);


        // Use a custom info window adapter to handle multiple lines of text in the
        // info window contents.
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            // Return null here, so that getInfoContents() is called next.
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                // Inflate the layouts for the info window, title and snippet.
                View infoWindow = getLayoutInflater().inflate(R.layout.custom_info_contents,
                        (FrameLayout) findViewById(R.id.map), false);

                TextView title = ((TextView) infoWindow.findViewById(R.id.title));
                title.setText(marker.getTitle());

                TextView snippet = ((TextView) infoWindow.findViewById(R.id.snippet));
                snippet.setText(marker.getSnippet());

                return infoWindow;
            }
        });

        // Prompt the user for permission.
        getLocationPermission();

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();

        // Get the current location of the device and set the position of the map.
        getDeviceLocation();


        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if (selected_marker != null && marker.equals(selected_marker)) {
                    selected_marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.marker_logo));
                    marker.hideInfoWindow();
                    selected_marker = null;
                    machine_selected = false;
                } else {
                    marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.selected_marker_logo));
                    if (machine_selected) {
                        selected_marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.marker_logo));
                        selected_marker = marker;
                    } else {
                        machine_selected = true;
                        selected_marker = marker;
                    }
                }

                return false;
            }
        });


        //addMarkers();
    }


    private void addMarkers() {

        if(mLocationPermissionGranted) {

            LatLng machine1 = new LatLng(userLat - 0.0010, userLon - 0.0010);
            LatLng machine2 = new LatLng(userLat - 0.0030, userLon - 0.0040);
            LatLng machine3 = new LatLng(userLat + 0.0020, userLon + 0.0010);
            LatLng machine4 = new LatLng(userLat + 0.0020, userLon + 0.0040);
            LatLng machine5 = new LatLng(userLat + 0.0010, userLon - 0.0030);


            mMap.addMarker(new MarkerOptions().position(machine1).title("Machine 1").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_logo)));
            mMap.addMarker(new MarkerOptions().position(machine2).title("Machine 2").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_logo)));
            mMap.addMarker(new MarkerOptions().position(machine3).title("Machine 3").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_logo)));
            mMap.addMarker(new MarkerOptions().position(machine4).title("Machine 4").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_logo)));
            mMap.addMarker(new MarkerOptions().position(machine5).title("Machine 5").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_logo)));

            machine5 = new LatLng(52.520008, 13.40454);
            mMap.addMarker(new MarkerOptions().position(machine5).title("Machine 5").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_logo)));
            machine5 = new LatLng(52.56008, 13.40494);
            mMap.addMarker(new MarkerOptions().position(machine5).title("Machine 5").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_logo)));
            machine5 = new LatLng(52.5008, 13.20454);
            mMap.addMarker(new MarkerOptions().position(machine5).title("Machine 5").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_logo)));
            machine5 = new LatLng(52.51008, 13.60454);
            mMap.addMarker(new MarkerOptions().position(machine5).title("Machine 5").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_logo)));
            machine5 = new LatLng(52.58050, 13.604094);
            mMap.addMarker(new MarkerOptions().position(machine5).title("Machine 5").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_logo)));
            machine5 = new LatLng(52.82010, 13.4057);
            mMap.addMarker(new MarkerOptions().position(machine5).title("Machine 5").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_logo)));
            machine5 = new LatLng(52.420060, 13.0958);
            mMap.addMarker(new MarkerOptions().position(machine5).title("Machine 5").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_logo)));
            machine5 = new LatLng(52.32008, 13.40954);
            mMap.addMarker(new MarkerOptions().position(machine5).title("Machine 5").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_logo)));


            mMap.moveCamera(CameraUpdateFactory.newLatLng(machine1));
        }


    }


    /**
     * Gets the current location of the device, and positions the map's camera.
     */
    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (mLocationPermissionGranted) {
                Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            mLastKnownLocation = task.getResult();
                            if (mLastKnownLocation == null) {
                                mLocationPermissionGranted = false;
                                CommonUtility.showToastLong(getApplicationContext(),"Please grant Location settings from your mobile settings");

                            } else {
                                userLat = mLastKnownLocation.getLatitude();
                                userLon = mLastKnownLocation.getLongitude();

                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                        new LatLng(mLastKnownLocation.getLatitude(),
                                                mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                            }

                        } else {
                            Log.d(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());
                            mMap.moveCamera(CameraUpdateFactory
                                    .newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
                            userLat = mDefaultLocation.latitude;
                            userLon = mDefaultLocation.longitude;
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }

                        addMarkers();
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        } catch (Exception e) {
            Log.e("Exception: %s", e.getMessage());
            CommonUtility.showToastLong(getApplicationContext(),"Please grant Location settings from your mobile settings");

        }
    }


    /**
     * Prompts the user for permission to use the device location.
     */
    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {

            int permission = PermissionChecker.checkCallingOrSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION);//, android.os.Process.myPid(), android.os.Process.myUid(), getApplicationContext().getPackageName());

            if (permission == PermissionChecker.PERMISSION_GRANTED) {
                mLocationPermissionGranted = true;
            } else {
                CommonUtility.showToastLong(getApplicationContext(),"Please grant Location settings from your mobile settings");

                mLocationPermissionGranted = false;
            }
        } else if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    /**
     * Handles the result of the request for location permissions.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
        updateLocationUI();
    }

    /**
     * Updates the map's UI settings based on whether the user has granted location permission.
     */
    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mLastKnownLocation = null;
                try {
                    getLocationPermission();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }


    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }



    @SuppressLint("RestrictedApi")
    @Override
    public void onConnected(@Nullable Bundle bundle) {

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }

    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        //Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        mCurrLocationMarker = mMap.addMarker(markerOptions);

        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11));

        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onSuccess(BaseResponse response, int requestCode) {

    }
}
