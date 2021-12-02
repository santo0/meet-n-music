package com.example.meet_n_music.ui;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.navigation.Navigation;

import com.example.meet_n_music.R;
import com.example.meet_n_music.api.GeoLocationManager;
import com.example.meet_n_music.model.Event;
import com.example.meet_n_music.model.EventGeographicalLocation;
import com.example.meet_n_music.repository.EventRepository;
import com.example.meet_n_music.repository.GeoLocationRepository;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private EditText mSearchText;

    private View rootView;
    private GoogleMap mMap;
    private static final String TAG = "MapFragment";
    private static final int ERROR_DIALOG_REQUEST = 9001;
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 15f;
    private Boolean mLocationPermissionsGranted = false;

    private FusedLocationProviderClient mFusedLocationProviderClient;

    //    MutableLiveData<ArrayList<MarkerOptions>> markerOptionsMutableLiveData;
//    MutableLiveData<HashMap<Marker, EventGeographicalLocation>> hashMapMutableLiveData;
    MutableLiveData<HashMap<Marker, String>> idHashMapMutableLiveData;

    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).lockDrawerMenu();
        getActivity().findViewById(R.id.bottom_navigation).setVisibility(View.VISIBLE);
        getActivity().findViewById(R.id.appbar_top).setVisibility(View.VISIBLE);
        getActivity().findViewById(R.id.btn_create_event).setVisibility(View.GONE);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        rootView = view;
        mSearchText = (EditText) view.findViewById(R.id.input_search);

        getLocationPermission();

        if (mLocationPermissionsGranted) {
            initMap();
        }
        init();

        return view;
    }

    public void init() {
        Log.d(TAG, "init");
        mSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || keyEvent.getAction() == KeyEvent.ACTION_DOWN
                        || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER) {
                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
                    GeoLocationManager.geoLocate(mSearchText.getText().toString().trim()).observe(getViewLifecycleOwner(), new Observer<EventGeographicalLocation>() {
                        @Override
                        public void onChanged(EventGeographicalLocation eventGeographicalLocation) {
                            if (eventGeographicalLocation != null) {
                                Toast.makeText(getActivity(), eventGeographicalLocation.getName(), Toast.LENGTH_SHORT).show();
                                moveCamera(new LatLng(eventGeographicalLocation.getLat(), eventGeographicalLocation.getLng()), DEFAULT_ZOOM);
                            } else {
                                Toast.makeText(getActivity(), "Can't find location", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                return false;

            }
        });

    }
    private void initMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        GeoLocationRepository.getInstance().getAllEventGeoLocations().observe(getViewLifecycleOwner(), new Observer<ArrayList<Pair<String, EventGeographicalLocation>>>() {
            @Override
            public void onChanged(ArrayList<Pair<String, EventGeographicalLocation>> eventGeographicalLocations) {
                Log.d(TAG, "We have geolocations!");
                if (eventGeographicalLocations != null) {
                    if (idHashMapMutableLiveData == null) {
                        Log.d(TAG, "Initialize hashmap for markers");
                        idHashMapMutableLiveData = new MutableLiveData<>();
                    } else {
                        for (Marker marker : idHashMapMutableLiveData.getValue().keySet()) {
                            marker.remove();
                        }
                    }
                    HashMap<Marker, String> idHashMapMarkers = new HashMap<>();
                    Log.d(TAG, "starting to get pairs");
                    for (Pair<String, EventGeographicalLocation> geoLocPair : eventGeographicalLocations) {
                        Log.d(TAG, "we have pair with id " + geoLocPair.first);
                        String eventId = geoLocPair.first;
                        EventGeographicalLocation geoLoc = geoLocPair.second;
                        Log.d(TAG, eventId);
                        Log.d(TAG, geoLoc.getName());
                        EventRepository.getInstance().getEventById(eventId).observe(getViewLifecycleOwner(), new Observer<Event>() {
                            @Override
                            public void onChanged(Event event) {
                                if(event != null){
                                    Log.d(TAG, "Created market of event with id " + eventId);
                                    MarkerOptions markerOptions = new MarkerOptions();
                                    markerOptions.position(new LatLng(geoLoc.getLat(), geoLoc.getLng()));
                                    markerOptions.title(event.getName());
                                    idHashMapMarkers.put(mMap.addMarker(markerOptions), eventId);
                                } else {
                                  Log.e(TAG, "can't find event with id " + eventId);
                                }
                            }
                        });

                    }
                    idHashMapMutableLiveData.setValue(idHashMapMarkers);
                }
            }
        });
    }

    private void getDeviceLocation() {
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());

        try {
            if (mLocationPermissionsGranted) {
                @SuppressLint("MissingPermission") Task location = mFusedLocationProviderClient.getLastLocation();//suppress due bug
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "onComplete: found location");
                            Location currentLocation = (Location) task.getResult();
                            if (currentLocation != null) {
                                moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), DEFAULT_ZOOM);
                            } else {
                                Log.d(TAG, "onComplete: current location is null");
                                Toast.makeText(getActivity(), "Unable to get current location", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Log.d(TAG, "onComplete: current location is null");
                            Toast.makeText(getActivity(), "Unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        } catch (SecurityException e) {

        }
    }

    private void moveCamera(LatLng latLng, float zoom) {
        Log.d(TAG, "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }

    private void getLocationPermission() {
        Log.d(TAG, "getLocationPermission: getting location permissions");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "YES");
            if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),
                    COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "init: YESYES");
                mLocationPermissionsGranted = true;
                initMap();
            } else {
                Log.d(TAG, "init: YESNO");
                requestPermissions(permissions,
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        } else {
            Log.d(TAG, "init: NO");
            requestPermissions(permissions,
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: called.");
        mLocationPermissionsGranted = false;

        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            mLocationPermissionsGranted = false;
                            Log.d(TAG, "onRequestPermissionsResult: permission failed");
                            return;
                        }
                    }
                    Log.d(TAG, "onRequestPermissionsResult: permission granted");
                    mLocationPermissionsGranted = true;
                    //initialize our map
                    initMap();
                }
            }
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        Log.d(TAG, "onMapReady: permissions: " + Boolean.toString(mLocationPermissionsGranted));
        mMap = googleMap;
        if (mLocationPermissionsGranted) {
            getDeviceLocation();
        }
        mMap.getUiSettings().setZoomControlsEnabled(true);


        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                Log.d(TAG, "You have clicked a marker");
                if (idHashMapMutableLiveData != null && idHashMapMutableLiveData.getValue() != null && idHashMapMutableLiveData.getValue().get(marker) != null) {
                    String eventId = idHashMapMutableLiveData.getValue().get(marker);
                    Log.d(TAG, "You have clicked marker with event id " + eventId);
                    MapFragmentDirections.ActionMapFragmentToViewEventFragment action = MapFragmentDirections.actionMapFragmentToViewEventFragment();
                    action.setEventId(eventId);
                    Navigation.findNavController(rootView).navigate(action);
                } else {
                    Log.d(TAG, "Can't find referenced marker");
                }
                return false;
            }
        });

    }
}