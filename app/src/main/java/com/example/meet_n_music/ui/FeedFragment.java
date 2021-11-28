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
import android.widget.Button;
import android.widget.TextView;

import com.example.meet_n_music.EventListAdapter;
import com.example.meet_n_music.R;
import com.example.meet_n_music.model.Event;
import com.example.meet_n_music.model.User;
import com.example.meet_n_music.viewmodel.AuthViewModel;
import com.example.meet_n_music.viewmodel.FeedViewModel;

import java.util.ArrayList;


public class FeedFragment extends Fragment {

    private RecyclerView recyclerView;
    private EventListAdapter adapter;
    private Button syncBtn;
    AuthViewModel authViewModel;
    FeedViewModel feedViewModel;
//    private MutableLiveData<ArrayList<Event>> showedEvents = new MutableLiveData<>();

    public FeedFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().findViewById(R.id.bottom_navigation).setVisibility(View.VISIBLE);
        getActivity().findViewById(R.id.appbar_top).setVisibility(View.VISIBLE);
        getActivity().findViewById(R.id.appbar_top).findViewById(R.id.btn_return_back).setVisibility(View.GONE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_feed, container, false);

        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        feedViewModel = new ViewModelProvider(this).get(FeedViewModel.class);
        feedViewModel.init();

        TextView tvUname = view.findViewById(R.id.tvUname);


        User user = authViewModel.getCurrentUser().getValue();

        tvUname.setText(user.username);

        //showedEvents.setValue(feedViewModel.getEventsWithGenre(user.interestedIn));
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new EventListAdapter();

        MutableLiveData<ArrayList<Event>> eventsToShow = feedViewModel.getEventMutableLiveData();
        eventsToShow.observe(getViewLifecycleOwner(), new Observer<ArrayList<Event>>() {
            @Override
            public void onChanged(ArrayList<Event> events) {
                Log.d("ASK!", user.interestedIn);
                ArrayList<Event> eventsGenre = new ArrayList<>();
                for (Event event : events) {
                    Log.d("ASK", event.getGenre());
                    Log.d("ASK", event.getName());
                    if (event.getGenre().equals(user.interestedIn)) {
                        Log.d("YES", event.getName());
                        eventsGenre.add(event);
                    }
                }

                adapter.setEvents(eventsGenre);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        });
//        eventsToShow.observe(getViewLifecycleOwner(), events -> adapter.notifyDataSetChanged());

        // adapter = new EventListAdapter(eventsToShow.getValue());
        // recyclerView.setAdapter(adapter);

        syncBtn = view.findViewById(R.id.syncBtn);

        return view;
    }
}