package com.example.meet_n_music;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;


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
    private EditText eventName, eventData, eventDescription;
    private View createEvent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_creating_event, container, false);
        eventName = (EditText) view.findViewById(R.id.eventName);
        eventData = (EditText) view.findViewById(R.id.EventDate);
        eventDescription = (EditText) view.findViewById(R.id.EventDescription);
        genreSelector = (Spinner) view.findViewById(R.id.genreSelection);
        ArrayAdapter<String> genreAdaptor = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.Genres));
        genreAdaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genreSelector.setAdapter(genreAdaptor);
        covidRestrictions = (ListView) view.findViewById(R.id.CovidRestrictions);
        ArrayAdapter<String> covidAdaptor = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_multiple_choice, getResources().getStringArray(R.array.CovidRestrictions));
        covidRestrictions.setAdapter(covidAdaptor);
        //Ficar fotos
        createEvent = (View) view.findViewById(R.id.createEvent);
        createEvent.setOnClickListener(v -> {
            newEvent();
        });
        return view;
    }

    private void newEvent() {

        String eventNameString = eventName.getText().toString().trim();
        String eventDateString = eventData.getText().toString().trim();
        String eventDescriptionString = eventDescription.getText().toString().trim();
        String eventGenre;
        String eventCovidRestrictionString = "";

        if(eventNameString.isEmpty()){
            eventName.setError("Event name is required!");
            eventName.requestFocus();
            return;
        }

        if(eventDateString.isEmpty()){
            eventData.setError("Event date is required!");
            eventData.requestFocus();
            return;
        }

        if(eventDescriptionString.isEmpty()){
            eventDescription.setError("Event description is required!");
            eventDescription.requestFocus();
            return;
        }

        //Checking format data

        eventGenre = genreSelector.getSelectedItem().toString();
        for (int i=0; i<covidRestrictions.getCount(); i++){
            if (covidRestrictions.isItemChecked(i)){
                eventCovidRestrictionString += covidRestrictions.getItemAtPosition(i) + ", ";
            }
        }
        eventCovidRestrictionString = eventCovidRestrictionString.replaceAll(", $", "");

        Toast.makeText(getActivity(), eventGenre, Toast.LENGTH_SHORT).show();
        Toast.makeText(getActivity(), eventCovidRestrictionString, Toast.LENGTH_SHORT).show();
        //Guardar informació en un nou Event class (a la database) + tornar al feed.
    }

}