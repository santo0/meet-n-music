package com.example.meet_n_music;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.meet_n_music.model.Event;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;


public class CreatingEventFragment extends Fragment {

    public CreatingEventFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private Spinner genreSelector;
    private ListView covidRestrictions;
    private EditText eventName,  eventDescription, location;
    private View createEvent;
    private Button startDate;
    private DatePickerDialog startDateEventDialog;
    private ProgressBar progressBar3;
    private FirebaseDatabase db;
    private DatabaseReference reference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_creating_event, container, false);
        eventName = (EditText) view.findViewById(R.id.eventName);
        eventDescription = (EditText) view.findViewById(R.id.EventDescription);
        location = (EditText) view.findViewById(R.id.location);
        progressBar3 = (ProgressBar) view.findViewById(R.id.progressBar3);
        initDatePicker();
        startDate = (Button) view.findViewById(R.id.eventStartDate);
        startDate.setOnClickListener(v -> {
            openStartDate(v);
        });
        startDate.setText(getTodaysDate());
        genreSelector = (Spinner) view.findViewById(R.id.genreSelection);
        ArrayAdapter<String> genreAdaptor = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.Genres));
        genreAdaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genreSelector.setAdapter(genreAdaptor);
        covidRestrictions = (ListView) view.findViewById(R.id.CovidRestrictions);
        ArrayAdapter<String> covidAdaptor = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_multiple_choice, getResources().getStringArray(R.array.CovidRestrictions));
        covidRestrictions.setAdapter(covidAdaptor);
        //Images
        createEvent = (View) view.findViewById(R.id.createEvent);
        createEvent.setOnClickListener(v -> {
            newEvent();
        });
        return view;
    }

    private String getTodaysDate() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month, year);
    }

    private void initDatePicker(){
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
        if (month == 1){
            return "01";
        }
        if (month == 2){
            return "02";
        }
        if (month == 3){
            return "03";
        }
        if (month == 4){
            return "04";
        }
        if (month == 5){
            return "05";
        }
        if (month == 6){
            return "06";
        }
        if (month == 7){
            return "07";
        }
        if (month == 8){
            return "08";
        }
        if (month == 9){
            return "09";
        }
        if (month == 10){
            return "10";
        }
        if (month == 11){
            return "11";
        }
        if (month == 12){
            return "12";
        }
        // Default should never happen.
        return "01";
    }

    private void newEvent() {

        String eventNameString = eventName.getText().toString().trim();
        String eventDescriptionString = eventDescription.getText().toString().trim();
        String locationString = location.getText().toString().trim();
        String startEventDateString = startDate.getText().toString().trim();
        String eventGenre;
        String eventCovidRestrictionString = "";

        if(eventNameString.isEmpty()){
            eventName.setError("Event name is required!");
            eventName.requestFocus();
            return;
        }

        if(eventDescriptionString.isEmpty()){
            eventDescription.setError("Event description is required!");
            eventDescription.requestFocus();
            return;
        }

        if(locationString.isEmpty()){
            location.setError("Location is required!");
            location.requestFocus();
            return;
        }

        eventGenre = genreSelector.getSelectedItem().toString();
        for (int i=0; i<covidRestrictions.getCount(); i++){
            if (covidRestrictions.isItemChecked(i)){
                eventCovidRestrictionString += covidRestrictions.getItemAtPosition(i) + ", ";
            }
        }
        eventCovidRestrictionString = eventCovidRestrictionString.replaceAll(", $", "");
        /*Toast.makeText(getActivity(), eventGenre, Toast.LENGTH_SHORT).show();
        Toast.makeText(getActivity(), eventCovidRestrictionString, Toast.LENGTH_SHORT).show();
        Toast.makeText(getActivity(), startEventDateString, Toast.LENGTH_SHORT).show();*/

        //Guardar informació en un nou Event class (a la database) + tornar al feed.
        progressBar3.setVisibility(View.VISIBLE);
        db = FirebaseDatabase.getInstance();
        reference = db.getReference("Events");
        Event event = new Event(eventNameString, eventDescriptionString, locationString, startEventDateString, eventGenre, eventCovidRestrictionString);
        reference.push().setValue(event).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getActivity(), "Event has been registered successfully!", Toast.LENGTH_LONG).show();
                    progressBar3.setVisibility(View.GONE);
                    Navigation.findNavController(getView()).navigate(R.id.action_creatingEventFragment_to_userProfileFragment);
                } else {
                    Toast.makeText(getActivity(), "Failed to register! Try again!", Toast.LENGTH_LONG).show();
                    progressBar3.setVisibility(View.GONE);
                }
            }
        });

    }

}