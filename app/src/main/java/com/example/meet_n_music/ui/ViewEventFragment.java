package com.example.meet_n_music.ui;

import android.net.Uri;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.example.meet_n_music.R;
import com.example.meet_n_music.api.WeatherManager;
import com.example.meet_n_music.model.Event;
import com.example.meet_n_music.model.EventGeographicalLocation;
import com.example.meet_n_music.model.User;
import com.example.meet_n_music.repository.AuthRepository;
import com.example.meet_n_music.repository.ImageRepository;
import com.example.meet_n_music.viewmodel.ViewEventViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ViewEventFragment extends Fragment implements OnMapReadyCallback {

    private static final String TAG = "ViewEventFragment";
    private static final float DEFAULT_ZOOM = 15f;


    public ViewEventFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().findViewById(R.id.bottom_navigation).setVisibility(View.VISIBLE);
        getActivity().findViewById(R.id.appbar_top).setVisibility(View.VISIBLE);
        getActivity().findViewById(R.id.btn_create_event).setVisibility(View.GONE);
        ((MainActivity)getActivity()).lockDrawerMenu();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    MutableLiveData<Event> eventLiveData;
    LiveData<String> eventIdLiveData;
    private TextView eventName, eventDescription, eventDate, eventLocation;
    private String eventCovid;
    private Button eventEdit;
    private ListView covidRestrictions;
    private GoogleMap mMap;
    ViewEventViewModel viewEventViewModel;
    MutableLiveData<User> userMutableLiveData;
    MutableLiveData<Boolean> userAttendsEvent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewEventFragmentArgs args = ViewEventFragmentArgs.fromBundle(getArguments());
        eventIdLiveData = new MutableLiveData<>(args.getEventId());
        View view = inflater.inflate(R.layout.fragment_view_event, container, false);

        initMap();

        viewEventViewModel = new ViewModelProvider(this).get(ViewEventViewModel.class);

        userMutableLiveData = AuthRepository.getAuthRepository().getCurrentUser();

        userAttendsEvent = new MutableLiveData<>();
        eventLiveData = new MutableLiveData<>();

        userAttendsEvent.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean attends) {
                if (attends) {
                    view.findViewById(R.id.btnAttend).setBackgroundTintList(getContext().getResources().getColorStateList(R.color.red));
                    ((Button) view.findViewById(R.id.btnAttend)).setText("Leave event");

                    view.findViewById(R.id.btnAttend).setOnClickListener(v -> {
                        view.findViewById(R.id.btnAttend).setOnClickListener(null);
                        Event event = eventLiveData.getValue();
                        FirebaseDatabase.getInstance().getReference().child("Events").child(event.getId()).runTransaction(new Transaction.Handler() {
                            @NonNull
                            @Override
                            public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                                Event e = currentData.getValue(Event.class);
                                if (e == null) {
                                    return Transaction.success(currentData);
                                }
                                e.setTotalAttendants(e.getTotalAttendants() - 1);
                                currentData.setValue(e);
                                return Transaction.success(currentData);
                            }

                            @Override
                            public void onComplete(@Nullable DatabaseError error, boolean committed, @Nullable DataSnapshot currentData) {
                                Event e = currentData.getValue(Event.class);
                                FirebaseDatabase
                                        .getInstance()
                                        .getReference()
                                        .child("Users")
                                        .child(userMutableLiveData.getValue().id)
                                        .child("attendingEventsIds")
                                        .child(event.getId()).removeValue()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                ((TextView) view.findViewById(R.id.tvTotalAttendees)).setText(e.getTotalAttendants() + " are attending the event!");

                                                userAttendsEvent.setValue(false);
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                    }
                                });
                            }
                        });
                    });
                } else {
                    view.findViewById(R.id.btnAttend).setBackgroundTintList(getContext().getResources().getColorStateList(R.color.secondary_variant));
                    ((Button) view.findViewById(R.id.btnAttend)).setText("Join event");
                    view.findViewById(R.id.btnAttend).setOnClickListener(v -> {
                        view.findViewById(R.id.btnAttend).setOnClickListener(null);
                        Event event = eventLiveData.getValue();
                        FirebaseDatabase.getInstance().getReference().child("Events").child(event.getId()).runTransaction(new Transaction.Handler() {
                            @NonNull
                            @Override
                            public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                                Event e = currentData.getValue(Event.class);
                                if (e == null) {
                                    return Transaction.success(currentData);
                                }
                                e.setTotalAttendants(e.getTotalAttendants() + 1);
                                currentData.setValue(e);
                                return Transaction.success(currentData);
                            }

                            @Override
                            public void onComplete(@Nullable DatabaseError error, boolean committed, @Nullable DataSnapshot currentData) {
                                Event e = currentData.getValue(Event.class);
                                FirebaseDatabase
                                        .getInstance()
                                        .getReference()
                                        .child("Users")
                                        .child(userMutableLiveData.getValue().id)
                                        .child("attendingEventsIds")
                                        .child(event.getId())
                                        .setValue(event.getId())
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                ((TextView) view.findViewById(R.id.tvTotalAttendees)).setText(e.getTotalAttendants() + " are attending the event!");
                                                userAttendsEvent.setValue(true);
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                    }
                                });
                            }
                        });
                    });
                }
            }
        });


        eventName = (TextView) view.findViewById(R.id.namePlaceholder);
        eventDescription = (TextView) view.findViewById(R.id.descriptionPlaceholder);
        eventDate = (TextView) view.findViewById(R.id.datePlaceHolder);
        eventLocation = (TextView) view.findViewById(R.id.locationPlaceholder);
        covidRestrictions = (ListView) view.findViewById(R.id.covidPlaceholder);

        eventDescription.setMovementMethod(new ScrollingMovementMethod());


        Query query = FirebaseDatabase.getInstance().getReference().child("Events").child(eventIdLiveData.getValue());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Event event = snapshot.getValue(Event.class);

                Log.d("debugFS", event.getName());
                Log.d("debugFS", event.getDescription());
                Log.d("debugFS", event.getStartDate());
                Log.d("debugFS", event.getLocation());

                showUserButton(view, userMutableLiveData.getValue(), event);

                eventName.setText(event.getName());
                eventDescription.setText(event.getDescription());
                eventDate.setText(event.getStartDate());
                eventLocation.setText(event.getLocation());
                ((TextView) view.findViewById(R.id.tvTotalAttendees)).setText(event.getTotalAttendants() + " are attending the event!");
                Log.d(TAG, "Image in: " + "images/" + event.getImagePath());
                ImageRepository.getInstance().getImageUri(event.getImagePath()).observe(getViewLifecycleOwner(), new Observer<Uri>() {
                    @Override
                    public void onChanged(Uri uri) {
                        if (uri != null) {
                            //     StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("images/" + eventIdLD.getValue() + "/" + eventIdLD.getValue() + ".jpg");
                            Glide.with(getContext())
                                    .load(uri)
                                    .centerCrop()
                                    .into((ImageView) view.findViewById(R.id.imagePlaceholder));
                        }
                    }
                });

                eventLiveData.setValue(event);

                MutableLiveData<List<String>> eventIds = AuthRepository.getAuthRepository().getCurrentUserAttendingEvents();
                eventIds.observe(getViewLifecycleOwner(), new Observer<List<String>>() {
                    @Override
                    public void onChanged(List<String> strings) {
                        boolean isAttending = false;
                        if (strings != null) {
                            for (String eventId : strings) {
                                if (event.getId().equals(eventId)) {
                                    isAttending = true;
                                    break;
                                }
                            }
                        }
                        userAttendsEvent.setValue(isAttending);
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        FirebaseDatabase.getInstance().getReference().child("EventGeographicalLocation").child(eventIdLiveData.getValue()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                EventGeographicalLocation geoloc = snapshot.getValue(EventGeographicalLocation.class);

                WeatherManager.getWeatherByCoords(geoloc.getLat(), geoloc.getLng());
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


    private void showUserButton(View view, User user, Event event) {
        Button btnAttend = view.findViewById(R.id.btnAttend);
        if (user.id.equals(event.getOwnerId())) {
            //User is owner
            btnAttend.setVisibility(View.GONE);
            view.findViewById(R.id.editLayout).setVisibility(View.VISIBLE);
            view.findViewById(R.id.editEventButton).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    ViewEventFragmentDirections.ActionViewEventFragmentToEditEventFragment action = ViewEventFragmentDirections.actionViewEventFragmentToEditEventFragment();
                    action.setEventId(event.getId());
                    Navigation.findNavController(view).navigate(action);
//                    Navigation.findNavController(getView()).navigate(R.id.action_viewEventFragment_to_editEventFragment);
                }
            });
        } else {
            //User is not owner
            view.findViewById(R.id.editLayout).setVisibility(View.GONE);
            btnAttend.setVisibility(View.VISIBLE);
        }
    }


    public ArrayList<String> getCovidRestrictions(String eventCovid) {
        return new ArrayList<String>(Arrays.asList(eventCovid.split(", ")));
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
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