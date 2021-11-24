package com.example.meet_n_music.ui;

import android.database.DataSetObserver;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.meet_n_music.R;
import com.example.meet_n_music.model.Event;
import com.example.meet_n_music.model.EventGeographicalLocation;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Arrays;

public class ViewEventFragment extends Fragment implements OnMapReadyCallback {

    private static final String TAG = "ViewEventFragment";
    private static final float DEFAULT_ZOOM = 15f;


    public ViewEventFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity)getActivity()).lockDrawerMenu();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {super.onCreate(savedInstanceState);}

    private TextView eventName, eventDescription, eventDate, eventLocation;
    private String eventId, eventCovid;
    private Button eventEdit;
    private ListView covidRestrictions;
    private GoogleMap mMap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewEventFragmentArgs args = ViewEventFragmentArgs.fromBundle(getArguments());
        String eventId = args.getEventId();
        View view = inflater.inflate(R.layout.fragment_view_event, container, false);

        initMap();

        eventName = (TextView) view.findViewById(R.id.namePlaceholder);
        eventDescription = (TextView) view.findViewById(R.id.descriptionPlaceholder);
        eventDate = (TextView) view.findViewById(R.id.datePlaceHolder);
        eventLocation = (TextView) view.findViewById(R.id.locationPlaceholder);
        covidRestrictions = (ListView) view.findViewById(R.id.covidPlaceholder);

        eventDescription.setMovementMethod(new ScrollingMovementMethod());

        Query query = FirebaseDatabase.getInstance().getReference().child("Events").child(eventId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Event event = snapshot.getValue(Event.class);

                Log.d("debugFS", event.getName());
                Log.d("debugFS", event.getDescription());
                Log.d("debugFS", event.getStartDate());
                Log.d("debugFS", event.getLocation());

                eventName.setText(event.getName());
                eventDescription.setText(event.getDescription());
                eventDate.setText(event.getStartDate());
                eventLocation.setText(event.getLocation());
                StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("images/"+event.getImagePath());
                Glide.with(getContext())
                        .load(storageReference)
                        .into((ImageView) view.findViewById(R.id.imagePlaceholder));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        FirebaseDatabase.getInstance().getReference().child("EventGeographicalLocation").child(eventId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                EventGeographicalLocation geoloc = snapshot.getValue(EventGeographicalLocation.class);

                LatLng location = new LatLng(geoloc.getLat(), geoloc.getLng());
                moveCamera(location, DEFAULT_ZOOM);
                mMap.addMarker(new MarkerOptions()
                        .position(location)
                        .title(geoloc.getName()));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


//        ArrayList<String> covidArray = getCovidRestrictions(eventCovid);
        return view;
    }

    public ArrayList<String> getCovidRestrictions(String eventCovid){
        return new ArrayList<String>(Arrays.asList(eventCovid.split(", ")));
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
    }

    private void moveCamera(LatLng latLng, float zoom) {
        Log.d(TAG, "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }

    private void initMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }
}