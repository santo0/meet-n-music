package com.example.meet_n_music;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;

public class ViewEventFragment extends Fragment {


    public ViewEventFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {super.onCreate(savedInstanceState);}

    private TextView eventName, eventDescription, eventDate, eventLocation;
    private String eventId, eventCovid;
    private Button eventEdit;
    private ListView covidRestrictions;
    private DatabaseReference reference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_event, container, false);
        eventName = (TextView) view.findViewById(R.id.namePlaceholder);
        eventDescription = (TextView) view.findViewById(R.id.descriptionPlaceholder);
        eventDate = (TextView) view.findViewById(R.id.datePlaceHolder);
        eventLocation = (TextView) view.findViewById(R.id.locationPlaceholder);
        covidRestrictions = (ListView) view.findViewById(R.id.covidPlaceholder);
        reference = FirebaseDatabase.getInstance().getReference().child("Events");
        ArrayList<String> covidArray = getCovidRestrictions(eventCovid);
        return view;
    }

    public ArrayList<String> getCovidRestrictions(String eventCovid){
        return new ArrayList<String>(Arrays.asList(eventCovid.split(", ")));
    }
}