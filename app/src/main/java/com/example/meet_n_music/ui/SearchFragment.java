package com.example.meet_n_music.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.meet_n_music.EventListAdapter;
import com.example.meet_n_music.R;
import com.example.meet_n_music.model.Event;
import com.example.meet_n_music.model.User;
import com.example.meet_n_music.viewmodel.AuthViewModel;
import com.example.meet_n_music.viewmodel.FeedViewModel;

import java.util.ArrayList;


public class SearchFragment extends Fragment {

    private Spinner genreSearcher;
    private Button searchButtonGenre;
    private RecyclerView recyclerViewGenre;
    private EventListAdapter adapterEventGenreList;
    FeedViewModel feedViewModel;

    public SearchFragment() {
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity)getActivity()).lockDrawerMenu();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().findViewById(R.id.bottom_navigation).setVisibility(View.VISIBLE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        genreSearcher = (Spinner) view.findViewById(R.id.genreSearch);
        ArrayAdapter<String> genreAdaptor = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.Genres));
        genreAdaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genreSearcher.setAdapter(genreAdaptor);
        feedViewModel = new ViewModelProvider(this).get(FeedViewModel.class);
        recyclerViewGenre = view.findViewById(R.id.recyclerViewGenre);
        recyclerViewGenre.setHasFixedSize(true);
        recyclerViewGenre.setLayoutManager(new LinearLayoutManager(getContext()));
        searchButtonGenre = (Button) view.findViewById(R.id.searchButtonGenre);
        searchButtonGenre.setOnClickListener(l -> {
            listEventGenre();
        });
        return view;
    }

    public void listEventGenre(){
        adapterEventGenreList = new EventListAdapter();
        String userSearchingFor = genreSearcher.getSelectedItem().toString().trim();
        MutableLiveData<ArrayList<Event>> eventsToShow = feedViewModel.getEventMutableLiveData();
        eventsToShow.observe(getViewLifecycleOwner(), new Observer<ArrayList<Event>>() {
            @Override
            public void onChanged(ArrayList<Event> events) {
                Log.d("ASK!", userSearchingFor);
                ArrayList<Event> eventsGenre = new ArrayList<>();
                for (Event event : events) {
                    Log.d("ASK", event.getGenre());
                    Log.d("ASK", event.getName());
                    if (event.getGenre().equals(userSearchingFor)) {
                        Log.d("YES", event.getName());
                        eventsGenre.add(event);
                    }
                }

                adapterEventGenreList.setEvents(eventsGenre);
                recyclerViewGenre.setAdapter(adapterEventGenreList);
                adapterEventGenreList.notifyDataSetChanged();
            }
        });
    }
}