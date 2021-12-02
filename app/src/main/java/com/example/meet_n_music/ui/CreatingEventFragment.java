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
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.meet_n_music.R;
import com.example.meet_n_music.api.GeoLocationManager;
import com.example.meet_n_music.model.Event;
import com.example.meet_n_music.model.EventGeographicalLocation;
import com.example.meet_n_music.model.User;
import com.example.meet_n_music.repository.AuthRepository;
import com.example.meet_n_music.viewmodel.CreateEventViewModel;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class CreatingEventFragment extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 234;
    private static final String TAG = "CreatingEventFragment";

    public CreatingEventFragment() {
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


    private Spinner genreSelector;
    private ListView covidRestrictions;
    private EditText eventName, eventDescription, locationInput;
    private View createEvent, picUploadButton;
    private Button startDate, searchLocation;
    private DatePickerDialog startDateEventDialog;
    private ProgressBar progressBar3;
    private ImageView imagePlaceholder;
    private TextView tvTextLocation;
    private CreateEventViewModel createEventViewModel;
    private MutableLiveData<EventGeographicalLocation> geographicalLocationLiveData;
    private MutableLiveData<Uri> filePathLiveData;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_creating_event, container, false);

        createEventViewModel = new ViewModelProvider(this).get(CreateEventViewModel.class);

        eventName = rootView.findViewById(R.id.eventName);
        eventDescription = rootView.findViewById(R.id.EventDescription);
        locationInput = rootView.findViewById(R.id.locationInputSearch);
        progressBar3 = rootView.findViewById(R.id.progressBar3);
        startDate = rootView.findViewById(R.id.eventStartDate);
        genreSelector = rootView.findViewById(R.id.genreSelection);
        covidRestrictions = rootView.findViewById(R.id.CovidRestrictions);
        picUploadButton = rootView.findViewById(R.id.uploadPhotoIcon);
        imagePlaceholder = rootView.findViewById(R.id.uploadPhotoImage);
        tvTextLocation = rootView.findViewById(R.id.textLocation);
        searchLocation = rootView.findViewById(R.id.btnSearchLocation);
        createEvent = rootView.findViewById(R.id.createEvent);


        initDatePicker();
        startDate.setOnClickListener(v -> startDateEventDialog.show());
        startDate.setText(getTodaysDate());

        ArrayAdapter<String> genreAdaptor = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.Genres));
        genreAdaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genreSelector.setAdapter(genreAdaptor);

        ArrayAdapter<String> covidAdaptor = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_multiple_choice, getResources().getStringArray(R.array.CovidRestrictions));
        covidRestrictions.setAdapter(covidAdaptor);

        //Images

        picUploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFileChooser();
            }
        });

        searchLocation.setOnClickListener(new View.OnClickListener() {
                                              @Override
                                              public void onClick(View view) {
                                                  String searchString = locationInput.getText().toString().trim();
                                                  geographicalLocationLiveData = GeoLocationManager.geoLocate(searchString);
                                                  geographicalLocationLiveData.observe(getViewLifecycleOwner(), new Observer<EventGeographicalLocation>() {
                                                      @Override
                                                      public void onChanged(EventGeographicalLocation eventGeographicalLocation) {
                                                          if (eventGeographicalLocation != null) {
                                                              //Toast.makeText(getActivity(), eventGeographicalLocation.getName(), Toast.LENGTH_SHORT).show();
                                                              tvTextLocation.setText(eventGeographicalLocation.getName());
                                                          } else {
                                                              locationInput.setError("Cannot find given location.");
                                                              locationInput.requestFocus();
                                                          }
                                                      }
                                                  });

                                              }
                                          }
        );

        createEvent.setOnClickListener(v -> {
            newEvent();
        });


        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
//            filePath = data.getData();
            filePathLiveData = new MutableLiveData<>(data.getData());

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePathLiveData.getValue());
                imagePlaceholder.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
                filePathLiveData.setValue(null);
                Toast.makeText(getActivity(), "Can't load image! Try again.", Toast.LENGTH_SHORT).show();
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

    private void openStartDate() {
        startDateEventDialog.show();
    }

    private void newEvent() {

        String eventNameString = eventName.getText().toString().trim();
        String eventDescriptionString = eventDescription.getText().toString().trim();
        String startEventDateString = startDate.getText().toString().trim();
        String eventGenre;
        String eventCovidRestrictionString = "";

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

        if (geographicalLocationLiveData == null || geographicalLocationLiveData.getValue() == null) {
            locationInput.setError("Location is required!");
            locationInput.requestFocus();
            return;
        }

        if (filePathLiveData == null || filePathLiveData.getValue() == null) {
            Toast.makeText(getActivity(), "Can't load image! Try again.", Toast.LENGTH_SHORT).show();
            return;
        }

        String locationString = geographicalLocationLiveData.getValue().getName();


        eventGenre = genreSelector.getSelectedItem().toString();
        for (int i = 0; i < covidRestrictions.getCount(); i++) {
            if (covidRestrictions.isItemChecked(i)) {
                eventCovidRestrictionString += covidRestrictions.getItemAtPosition(i) + ", ";
            }
        }
        eventCovidRestrictionString = eventCovidRestrictionString.replaceAll(", $", "");

        Log.d("CreatingEventFragment", "1");
        progressBar3.setVisibility(View.VISIBLE);
        MutableLiveData<User> userMutableLiveData = AuthRepository.getAuthRepository().getCurrentUser();
        //Event(String id, String name, String description, String location, String startDate, String genre, String covid, String ownerId, int totalAttendants)
        Event event = new Event("null",
                eventNameString,
                eventDescriptionString,
                locationString,
                startEventDateString,
                eventGenre,
                eventCovidRestrictionString,
                userMutableLiveData.getValue().id,
                0);
        MutableLiveData<Event> eventMutableLiveData = new MutableLiveData<>();
        eventMutableLiveData.setValue(event);

        createEventViewModel.setEvent(eventMutableLiveData, userMutableLiveData).observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean setEventFinished) {
                if (setEventFinished != null && setEventFinished) {
//                    Log.d("CreatingEventFragment", event.getId());
                    Log.d(TAG, event.getName());
                    String imagePath = event.getId() + "/" + event.getId() + ".jpg";
                    createEventViewModel.setEventImage(imagePath, filePathLiveData).observe(getViewLifecycleOwner(), new Observer<Boolean>() {
                        @Override
                        public void onChanged(Boolean setImageFinished) {
                            if (setImageFinished != null && setImageFinished) {
                                createEventViewModel.setGeoLocation(event.getId(), geographicalLocationLiveData).observe(getViewLifecycleOwner(), new Observer<Boolean>() {
                                    @Override
                                    public void onChanged(Boolean setGeoLocFinished) {
                                        if (setGeoLocFinished != null && setGeoLocFinished) {
                                            Log.d(TAG, "Event with id " + event.getId() + " has been registered successfully.");
                                            Toast.makeText(getActivity(), "Event has been registered successfully!", Toast.LENGTH_LONG).show();
                                            progressBar3.setVisibility(View.GONE);
                                            Navigation.findNavController(getView()).navigate(R.id.action_creatingEventFragment_to_feedFragment);
                                        } else {
                                            Log.d(TAG, "setting geolocation has failed!");
                                            Toast.makeText(getActivity(), "Failed to register! Try again!", Toast.LENGTH_LONG).show();
                                            progressBar3.setVisibility(View.GONE);
                                        }
                                    }
                                });
                            } else {
                                Log.d(TAG, "setting image has failed!");
                                Toast.makeText(getActivity(), "Failed to register! Try again!", Toast.LENGTH_LONG).show();
                                progressBar3.setVisibility(View.GONE);
                            }
                        }
                    });
                } else {
                    Log.d(TAG, "setting event has returned null!");
                    Toast.makeText(getActivity(), "Failed to register! Try again!", Toast.LENGTH_LONG).show();
                    progressBar3.setVisibility(View.GONE);
                }
            }
        });
    }
}