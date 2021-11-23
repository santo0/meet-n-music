package com.example.meet_n_music.ui;

import static android.app.Activity.RESULT_OK;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
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
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.meet_n_music.R;
import com.example.meet_n_music.model.Event;
import com.example.meet_n_music.model.EventGeographicalLocation;
import com.example.meet_n_music.viewmodel.CreateEventViewModel;
import com.google.android.material.navigation.NavigationView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class CreatingEventFragment extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 234;
    private static final String TAG = "CreatingEventFragment";

    public CreatingEventFragment() {
        // Required empty public constructor
    }


    @Override
    public void onResume() {
        super.onResume();
        getActivity().findViewById(R.id.bottom_navigation).setVisibility(View.GONE);
        getActivity().findViewById(R.id.appbar_top).setVisibility(View.VISIBLE);
        getActivity().findViewById(R.id.btn_create_event).setVisibility(View.GONE);
        ((DrawerLayout)((AppCompatActivity)getActivity()).findViewById(R.id.drawer_layout)).setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        //((Menu)((NavigationView)getActivity().findViewById(R.id.navigation_view)).getMenu()).findItem(R.id.);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    private Spinner genreSelector;
    private ListView covidRestrictions;
    private EditText eventName, eventDescription, locationInput;
    private View createEvent;
    private Button startDate;
    private DatePickerDialog startDateEventDialog;
    private ProgressBar progressBar3;
    private Uri filePath;
    ImageView imagePlaceholder;
    CreateEventViewModel createEventViewModel;
    MutableLiveData<EventGeographicalLocation> geographicalLocationLiveData;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_creating_event, container, false);

        createEventViewModel = new ViewModelProvider(this).get(CreateEventViewModel.class);

        eventName = (EditText) view.findViewById(R.id.eventName);
        eventDescription = (EditText) view.findViewById(R.id.EventDescription);
        locationInput = (EditText) view.findViewById(R.id.locationInputSearch);
        progressBar3 = (ProgressBar) view.findViewById(R.id.progressBar3);

        initDatePicker();
        startDate = (Button) view.findViewById(R.id.eventStartDate);
        startDate.setOnClickListener(v -> openStartDate(v));
        startDate.setText(getTodaysDate());

        genreSelector = (Spinner) view.findViewById(R.id.genreSelection);
        ArrayAdapter<String> genreAdaptor = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.Genres));
        genreAdaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genreSelector.setAdapter(genreAdaptor);

        covidRestrictions = (ListView) view.findViewById(R.id.CovidRestrictions);
        ArrayAdapter<String> covidAdaptor = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_multiple_choice, getResources().getStringArray(R.array.CovidRestrictions));
        covidRestrictions.setAdapter(covidAdaptor);

        //Images
        View picUploadButton = view.findViewById(R.id.uploadPhotoIcon);
        imagePlaceholder = view.findViewById(R.id.uploadPhotoImage);
        picUploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFileChooser();
            }
        });

        locationInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || keyEvent.getAction() == KeyEvent.ACTION_DOWN
                        || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER) {
                    geoLocate();
                }
                return false;

            }
        });

        createEvent = (View) view.findViewById(R.id.createEvent);
        createEvent.setOnClickListener(v -> {
            newEvent();
        });


        return view;
    }

    private void geoLocate() {
        Log.d(TAG, "geoLocate: geolocating");
        String searchString = locationInput.getText().toString();
        Geocoder geocoder = new Geocoder(getContext());
        List<Address> list = new ArrayList<>();
        try {
            list = geocoder.getFromLocationName(searchString, 1);
        } catch (IOException e) {
            Log.e(TAG, "geoLocate: IOException: " + e.getMessage());
        }
        if (list.size() > 0) {
            Address address = list.get(0);
            Log.d(TAG, "geoLocate: found a location: " + address.toString());
            Toast.makeText(getActivity(), address.toString(), Toast.LENGTH_SHORT).show();
            //addressLines(list), latitude, longitude
            //if not addressLines, Locality, thoroughfare
            geographicalLocationLiveData = new MutableLiveData<>(new EventGeographicalLocation(address.getAddressLine(0), address.getLatitude(), address.getLongitude()));
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                imagePlaceholder.setImageBitmap(bitmap);
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
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month, year);
    }

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = makeDateString(day, month, year);
                startDate.setText(date);
            }
        };
        Calendar cal = Calendar.getInstance();

        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;
        startDateEventDialog = new DatePickerDialog(getActivity(), style, dateSetListener, year, month, day);
    }

    private void openStartDate(View view) {
        startDateEventDialog.show();
    }

    private String makeDateString(int day, int month, int year) {
        return year + "-" + getMonthFormat(month) + "-" + day;
    }

    private String getMonthFormat(int month) {
        if (month == 1) {
            return "01";
        }
        if (month == 2) {
            return "02";
        }
        if (month == 3) {
            return "03";
        }
        if (month == 4) {
            return "04";
        }
        if (month == 5) {
            return "05";
        }
        if (month == 6) {
            return "06";
        }
        if (month == 7) {
            return "07";
        }
        if (month == 8) {
            return "08";
        }
        if (month == 9) {
            return "09";
        }
        if (month == 10) {
            return "10";
        }
        if (month == 11) {
            return "11";
        }
        if (month == 12) {
            return "12";
        }
        // Default should never happen.
        return "01";
    }

    private void newEvent() {

        String eventNameString = eventName.getText().toString().trim();
        String eventDescriptionString = eventDescription.getText().toString().trim();
        String locationString = locationInput.getText().toString().trim();
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

        if (locationString.isEmpty()) {
            locationInput.setError("Location is required!");
            locationInput.requestFocus();
            return;
        }

        eventGenre = genreSelector.getSelectedItem().toString();
        for (int i = 0; i < covidRestrictions.getCount(); i++) {
            if (covidRestrictions.isItemChecked(i)) {
                eventCovidRestrictionString += covidRestrictions.getItemAtPosition(i) + ", ";
            }
        }
        eventCovidRestrictionString = eventCovidRestrictionString.replaceAll(", $", "");

        Log.d("CreatingEventFragment", "1");
        progressBar3.setVisibility(View.VISIBLE);
        Event event = new Event(eventNameString, eventDescriptionString, locationString, startEventDateString, eventGenre, eventCovidRestrictionString);
        MutableLiveData<Event> eventMutableLiveData = new MutableLiveData<>();
        MutableLiveData<Uri> uriMutableLiveData = new MutableLiveData<>();
        eventMutableLiveData.setValue(event);
        uriMutableLiveData.setValue(filePath);
        Log.d("CreatingEventFragment", "2");

        createEventViewModel.setEvent(eventMutableLiveData);

        Log.d("CreatingEventFragment", "3");
        eventMutableLiveData.observe(getViewLifecycleOwner(), new Observer<Event>() {
            @Override
            public void onChanged(Event event) {
                if (event != null) {
//                    Log.d("CreatingEventFragment", event.getId());
                    Log.d("CreatingEventFragment", event.getName());
                    String imagePath = event.getId() + "/" + event.getId() + ".jpg";
                    createEventViewModel.setEventImage(imagePath, uriMutableLiveData);
//                    ImageRepository.getInstance().uploadImage(imagePath, uriMutableLiveData);
                    uriMutableLiveData.observe(getViewLifecycleOwner(), new Observer<Uri>() {
                        @Override
                        public void onChanged(Uri uri) {
                            if (uri != null) {
                                createEventViewModel.setGeoLocation(event.getId(), geographicalLocationLiveData);
                                geographicalLocationLiveData.observe(getViewLifecycleOwner(), new Observer<EventGeographicalLocation>() {
                                    @Override
                                    public void onChanged(EventGeographicalLocation eventGeographicalLocation) {
                                        if (eventGeographicalLocation != null) {
                                            Log.d(TAG, event.getName());
                                            Toast.makeText(getActivity(), "Event has been registered successfully!", Toast.LENGTH_LONG).show();
                                            progressBar3.setVisibility(View.GONE);

                                            Navigation.findNavController(getView()).navigate(R.id.action_creatingEventFragment_to_feedFragment);
                                        } else {
                                            Log.d(TAG, "it is null");
                                            Toast.makeText(getActivity(), "Failed to register! Try again!", Toast.LENGTH_LONG).show();
                                            progressBar3.setVisibility(View.GONE);
                                        }
                                    }
                                });
                            } else {
                                Log.d(TAG, "it is null");
                                Toast.makeText(getActivity(), "Failed to register! Try again!", Toast.LENGTH_LONG).show();
                                progressBar3.setVisibility(View.GONE);
                            }
                        }
                    });

                } else {
                    Log.d(TAG, "it is null");
                    Toast.makeText(getActivity(), "Failed to register! Try again!", Toast.LENGTH_LONG).show();
                    progressBar3.setVisibility(View.GONE);
                }
            }
        });
        Log.d("CreatingEventFragment", "4");
    }

}