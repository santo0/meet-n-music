package com.example.meet_n_music.ui;

import static android.app.Activity.RESULT_OK;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.example.meet_n_music.R;
import com.example.meet_n_music.api.GeoLocationManager;
import com.example.meet_n_music.model.Event;
import com.example.meet_n_music.model.EventGeographicalLocation;
import com.example.meet_n_music.model.User;
import com.example.meet_n_music.repository.AuthRepository;
import com.example.meet_n_music.repository.ImageRepository;
import com.example.meet_n_music.viewmodel.CreateEventViewModel;
import com.example.meet_n_music.viewmodel.EditEventViewModel;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class EditEventFragment extends Fragment {


    private static final int PICK_IMAGE_REQUEST = 234;
    private static final String TAG = "EditEventFragment";

    private Spinner genreSelector;
    private ListView covidRestrictions;
    private EditText eventName, eventDescription, locationInput;
    private TextView tvLocation;
    private View createEvent, picUploadButton;
    private Button startDate, searchLocation;
    private DatePickerDialog startDateEventDialog;
    private ProgressBar progressBar3;
    private Uri filePath;
    private List<String> availableGenres;
    private List<String> availableRestrictions;
    private ImageView imagePlaceholder;

    private MutableLiveData<Boolean> hasChangedPicture;
    private LiveData<String> eventIdLD;
    private CreateEventViewModel createEventViewModel;
    private MutableLiveData<EventGeographicalLocation> geographicalLocationLiveData;
    private MutableLiveData<Uri> uriMutableLiveData;
    private MutableLiveData<Event> oldEventLiveData;
    private EditEventViewModel editEventViewModel;


    public void loadEvent() {
        Event event = oldEventLiveData.getValue();
        eventName.setText(event.getName());
        eventDescription.setText(event.getDescription());
        startDate.setText(event.getStartDate());

        tvLocation.setText(event.getLocation());

        GeoLocationManager.geoLocate(event.getLocation()).observe(getViewLifecycleOwner(), new Observer<EventGeographicalLocation>() {
            @Override
            public void onChanged(EventGeographicalLocation eventGeographicalLocation) {
                if(eventGeographicalLocation != null){
                    geographicalLocationLiveData.setValue(eventGeographicalLocation);
                    locationInput.setText(eventGeographicalLocation.getName());
                }
            }
        });

        ImageRepository.getInstance().getImageUri(event.getImagePath()).observe(getViewLifecycleOwner(), new Observer<Uri>() {
            @Override
            public void onChanged(Uri uri) {
                if(uri != null){

                    Glide.with(getContext())
                            .load(uri)
                            .into(imagePlaceholder);
                    uriMutableLiveData.setValue(uri);
                }
            }

        });

        genreSelector.setSelection(availableGenres.indexOf(event.getGenre()));

        List<String> restrictions = getCovidRestrictions(event.getCovid());

        for (String restriction : restrictions) {
            covidRestrictions.setItemChecked(availableRestrictions.indexOf(restriction), true);
        }




    }

    public ArrayList<String> getCovidRestrictions(String eventCovid) {
        return new ArrayList<>(Arrays.asList(eventCovid.split(", ")));
    }

    public EditEventFragment() {
        // Required empty public constructor
    }


    @Override
    public void onResume() {
        super.onResume();
        getActivity().findViewById(R.id.bottom_navigation).setVisibility(View.VISIBLE);
        getActivity().findViewById(R.id.appbar_top).setVisibility(View.VISIBLE);
        getActivity().findViewById(R.id.btn_create_event).setVisibility(View.GONE);
        ((MainActivity) getActivity()).lockDrawerMenu();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //View rootView = inflater.inflate(R.layout.fragment_edit_event, container, false);
        View rootView = inflater.inflate(R.layout.fragment_creating_event, container, false);
        ViewEventFragmentArgs args = ViewEventFragmentArgs.fromBundle(getArguments());
        eventIdLD = new MutableLiveData<>(args.getEventId());
        editEventViewModel = new ViewModelProvider(this).get(EditEventViewModel.class);

        //finishEditSemaphore = new MutableLiveData<>(3);

        //isPhotoChangedLD = new MutableLiveData<>(false);
        //isEventChangedLD = new MutableLiveData<>(false);
        //isLocationChangedLD = new MutableLiveData<>(false);

        tvLocation = rootView.findViewById(R.id.textLocation);
        createEventViewModel = new ViewModelProvider(this).get(CreateEventViewModel.class);

        geographicalLocationLiveData = new MutableLiveData<>();
        uriMutableLiveData = new MutableLiveData<>();
        hasChangedPicture = new MutableLiveData<>(false);

        eventName = rootView.findViewById(R.id.eventName);
        eventDescription = rootView.findViewById(R.id.EventDescription);
        locationInput = rootView.findViewById(R.id.locationInputSearch);
        progressBar3 = rootView.findViewById(R.id.progressBar3);
        startDate = rootView.findViewById(R.id.eventStartDate);
        genreSelector = rootView.findViewById(R.id.genreSelection);
        covidRestrictions = rootView.findViewById(R.id.CovidRestrictions);
        picUploadButton = rootView.findViewById(R.id.uploadPhotoIcon);
        imagePlaceholder = rootView.findViewById(R.id.uploadPhotoImage);
        searchLocation = rootView.findViewById(R.id.btnSearchLocation);
        createEvent = rootView.findViewById(R.id.createEvent);

        oldEventLiveData = editEventViewModel.getEventById(eventIdLD.getValue());

        oldEventLiveData.observe(getViewLifecycleOwner(), new Observer<Event>() {
            @Override
            public void onChanged(Event event) {
                if (event != null) {
                    loadEvent();
                }
            }
        });


        initDatePicker();
        startDate.setOnClickListener(v -> startDateEventDialog.show());
        startDate.setText(getTodaysDate());

        availableGenres = Arrays.asList(getResources().getStringArray(R.array.Genres));
        ArrayAdapter<String> genreAdaptor = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, availableGenres);
        genreAdaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genreSelector.setAdapter(genreAdaptor);


        availableRestrictions = Arrays.asList(getResources().getStringArray(R.array.CovidRestrictions));
        ArrayAdapter<String> covidAdaptor = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_multiple_choice, availableRestrictions);
        covidRestrictions.setAdapter(covidAdaptor);

        picUploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFileChooser();
            }
        });

        searchLocation.setOnClickListener(v -> {
            MutableLiveData<EventGeographicalLocation> geolocateld = GeoLocationManager.geoLocate(locationInput.getText().toString());
            geolocateld.observe(getViewLifecycleOwner(), new Observer<EventGeographicalLocation>() {
                @Override
                public void onChanged(EventGeographicalLocation eventGeographicalLocation) {
                    if (eventGeographicalLocation != null) {
                        geographicalLocationLiveData.setValue(eventGeographicalLocation);
                        Toast.makeText(getActivity(), eventGeographicalLocation.getName(), Toast.LENGTH_SHORT).show();
                        ((TextView) rootView.findViewById(R.id.textLocation)).setText(eventGeographicalLocation.getName());
                    } else {
                        Toast.makeText(getActivity(), "Did not found a location", Toast.LENGTH_SHORT).show();
                        ((TextView) rootView.findViewById(R.id.textLocation)).setText("Location not found");
                    }
                }
            });
        });

        ImageRepository.getInstance().getImageUri(eventIdLD.getValue() + "/" + eventIdLD.getValue() + ".jpg").observe(getViewLifecycleOwner(), new Observer<Uri>() {
            @Override
            public void onChanged(Uri uri) {
                if (uri != null) {
               //     StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("images/" + eventIdLD.getValue() + "/" + eventIdLD.getValue() + ".jpg");
                    Glide.with(getContext())
                            .load(uri)
                            .into((ImageView) rootView.findViewById(R.id.uploadPhotoImage));
                }
            }
        });

        createEvent.setOnClickListener(v -> {
            editEvent(rootView);
        });
        return rootView;
    }

    

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                imagePlaceholder.setImageBitmap(bitmap);
                uriMutableLiveData.setValue(filePath);
                hasChangedPicture.setValue(true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select an Image"), PICK_IMAGE_REQUEST);
    }

    private String getTodaysDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(calendar.getTime());
    }

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                String dateBuffer = "";
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, day);
                Log.d(TAG, calendar.toString());
                Log.d(TAG, calendar.getTime().toString());
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                dateBuffer = dateFormat.format(calendar.getTime());
                Log.d(TAG, dateBuffer);
                startDate.setText(dateBuffer);
            }
        };
        Calendar cal = Calendar.getInstance();

        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;
        startDateEventDialog = new DatePickerDialog(getActivity(), style, dateSetListener, year, month, day);
    }

    private void editEvent(View rootView) {

        String eventNameString = eventName.getText().toString().trim();
        String eventDescriptionString = eventDescription.getText().toString().trim();
        String startEventDateString = startDate.getText().toString().trim();
        String eventGenre = genreSelector.getSelectedItem().toString();
        EventGeographicalLocation geoloc = geographicalLocationLiveData.getValue();

        String eventCovidRestrictionString = "";
        for (int i = 0; i < covidRestrictions.getCount(); i++) {
            if (covidRestrictions.isItemChecked(i)) {
                eventCovidRestrictionString += covidRestrictions.getItemAtPosition(i) + ", ";
            }
        }
        eventCovidRestrictionString = eventCovidRestrictionString.replaceAll(", $", "");

        if (eventNameString.isEmpty()) {
            eventName.setError("Event name is required!");
            eventName.requestFocus();
            return;
        }

        if (eventDescriptionString.isEmpty()) {
            eventDescription.setError("Event description is required!");
            eventDescription.requestFocus();
            return;
        }

        if (geoloc == null) {
            locationInput.setError("Location is required!");
            locationInput.requestFocus();
            return;
        }


        Log.d(TAG, "1");
        progressBar3.setVisibility(View.VISIBLE);
        MutableLiveData<User> userMutableLiveData = AuthRepository.getAuthRepository().getCurrentUser();
        //Event(String id, String name, String description, String location, String startDate, String genre, String covid, String ownerId, int totalAttendants)
        Event newEvent = new Event(eventIdLD.getValue(),
                eventNameString,
                eventDescriptionString,
                geoloc.getName(),
                startEventDateString,
                eventGenre,
                eventCovidRestrictionString,
                userMutableLiveData.getValue().id,
                oldEventLiveData.getValue().getTotalAttendants());

        MutableLiveData<Event> newEventMutableLiveData = new MutableLiveData<>(newEvent);

        Log.d("CreatingEventFragment", "2");

        modifyEvent(newEventMutableLiveData).observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean modifyFinished) {
                if (modifyFinished) {
                    Toast.makeText(getActivity(), "Event has been modified successfully!", Toast.LENGTH_LONG).show();
                    progressBar3.setVisibility(View.GONE);


                    EditEventFragmentDirections.ActionEditEventFragmentToViewEventFragment action = EditEventFragmentDirections.actionEditEventFragmentToViewEventFragment();
                    action.setEventId(eventIdLD.getValue());
                    Navigation.findNavController(rootView).navigate(action);
                } else {
                    Toast.makeText(getActivity(), "Failed to modify event! Try again!", Toast.LENGTH_LONG).show();
                    progressBar3.setVisibility(View.GONE);

                }
            }
        });

        Log.d("CreatingEventFragment", "4");
    }


    public MutableLiveData<Boolean> modifyEvent(MutableLiveData<Event> newEventMutableLiveData) {
        MutableLiveData<Boolean> eventModifyMutableLiveData = new MutableLiveData<>();
        modifyEventDetails(newEventMutableLiveData).observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean detailsFinished) {
                if (detailsFinished) {
                    Log.d(TAG, "Modified event details! :D");
                    modifyEventPicture(newEventMutableLiveData, uriMutableLiveData).observe(getViewLifecycleOwner(), new Observer<Boolean>() {
                        @Override
                        public void onChanged(Boolean pictureFinished) {
                            if (pictureFinished) {
                                Log.d(TAG, "Modified picture! :D");
                                modifyEventLocation(newEventMutableLiveData, geographicalLocationLiveData).observe(getViewLifecycleOwner(), new Observer<Boolean>() {
                                    @Override
                                    public void onChanged(Boolean locationFinished) {
                                        if (locationFinished) {
                                            Log.d(TAG, "Modified location! :D");
                                            eventModifyMutableLiveData.setValue(true);
                                        } else {
                                            Log.d(TAG, "Did not modify location! :(");
                                            eventModifyMutableLiveData.setValue(false);
                                        }
                                    }
                                });
                            } else {
                                Log.d(TAG, "Did not modify picture! :(");
                                eventModifyMutableLiveData.setValue(false);
                            }
                        }
                    });
                } else {
                    Log.d(TAG, "Did not modify event details! :(!");
                    eventModifyMutableLiveData.setValue(false);
                }
            }
        });
        return eventModifyMutableLiveData;
    }

    public MutableLiveData<Boolean> modifyEventDetails(MutableLiveData<Event> newEventMutableLiveData) {
        MutableLiveData<Boolean> eventFinished = new MutableLiveData<>();
        editEventViewModel.modifyEventDetails(newEventMutableLiveData).observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean != null && aBoolean) {
                    eventFinished.setValue(true);
                } else {
                    eventFinished.setValue(false);
                }
            }
        });
        return eventFinished;
    }

    public MutableLiveData<Boolean> modifyEventPicture(MutableLiveData<Event> eventMutableLiveData, MutableLiveData<Uri> uriMutableLiveData) {
        MutableLiveData<Boolean> pictureFinished = new MutableLiveData<>();
        if(hasChangedPicture != null && hasChangedPicture.getValue() != null && hasChangedPicture.getValue()) {
            Log.d(TAG, "picture has to be changed");
            String filePath = eventMutableLiveData.getValue().getImagePath();
            editEventViewModel.modifyEventPicture(filePath, uriMutableLiveData).observe(getViewLifecycleOwner(), new Observer<Boolean>() {
                @Override
                public void onChanged(Boolean aBoolean) {
                    if (aBoolean != null && aBoolean) {
                        pictureFinished.setValue(true);
                    } else {
                        pictureFinished.setValue(false);
                    }
                }
            });
        }else{
            Log.d(TAG, "picture has NOT to be changed");
            pictureFinished.setValue(true);
        }
        return pictureFinished;
    }

    public MutableLiveData<Boolean> modifyEventLocation(MutableLiveData<Event> eventMutableLiveData, MutableLiveData<EventGeographicalLocation> eventGeographicalLocationMutableLiveData) {
        MutableLiveData<Boolean> locationFinished = new MutableLiveData<>();
        editEventViewModel.modifyEventGeoLocation(eventMutableLiveData.getValue().getId(), eventGeographicalLocationMutableLiveData).observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean != null && aBoolean) {
                    locationFinished.setValue(true);
                } else {
                    locationFinished.setValue(false);
                }
            }
        });
        return locationFinished;
    }

}