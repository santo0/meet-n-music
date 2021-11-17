package com.example.meet_n_music;

import android.database.DataSetObserver;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.meet_n_music.model.Event;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewEventFragmentArgs args = ViewEventFragmentArgs.fromBundle(getArguments());
        String eventId = args.getEventId();
        View view = inflater.inflate(R.layout.fragment_view_event, container, false);

        eventName = (TextView) view.findViewById(R.id.namePlaceholder);
        eventDescription = (TextView) view.findViewById(R.id.descriptionPlaceholder);
        eventDate = (TextView) view.findViewById(R.id.datePlaceHolder);
        eventLocation = (TextView) view.findViewById(R.id.locationPlaceholder);
        covidRestrictions = (ListView) view.findViewById(R.id.covidPlaceholder);

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
}