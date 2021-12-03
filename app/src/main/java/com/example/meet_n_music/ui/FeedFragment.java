package com.example.meet_n_music.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meet_n_music.utils.EventItemAction;
import com.example.meet_n_music.utils.EventListAdapter;
import com.example.meet_n_music.R;
import com.example.meet_n_music.model.Event;
import com.example.meet_n_music.model.User;
import com.example.meet_n_music.viewmodel.AuthViewModel;
import com.example.meet_n_music.viewmodel.FeedViewModel;

import java.util.ArrayList;


public class FeedFragment extends Fragment {

    private RecyclerView recyclerView;
    private EventListAdapter adapter;
    AuthViewModel authViewModel;
    FeedViewModel feedViewModel;

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
        getActivity().findViewById(R.id.btn_create_event).setVisibility(View.VISIBLE);
        ((MainActivity)getActivity()).unlockDrawerMenu();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_feed, container, false);
        Log.d("feedFragment", "1");
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        feedViewModel = new ViewModelProvider(this).get(FeedViewModel.class);
        //feedViewModel.init();

        User user = authViewModel.getCurrentUser().getValue();

        Log.d("feedFragment", "2");
        //showedEvents.setValue(feedViewModel.getEventsWithGenre(user.interestedIn));
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));

        adapter = new EventListAdapter(getContext(), new EventItemAction() {
            @Override
            public NavDirections navigate(String id) {
                FeedFragmentDirections.ActionFeedFragmentToViewEventFragment action = FeedFragmentDirections.actionFeedFragmentToViewEventFragment();
                action.setEventId(id);
                return action;
            }
        });
        Log.d("feedFragment", "3");
        MutableLiveData<ArrayList<Event>> eventsToShow = feedViewModel.getEventMutableLiveData();
        eventsToShow.observe(getViewLifecycleOwner(), new Observer<ArrayList<Event>>() {
            @Override
            public void onChanged(ArrayList<Event> events) {
                Log.d("ASK!", user.interestedIn);
                ArrayList<Event> eventsGenre = new ArrayList<>();
                if(events != null){
                    for (Event event : events) {
                        Log.d("ASK", event.getGenre());
                        Log.d("ASK", event.getName());
                        if (event.getGenre().equals(user.interestedIn)) {
                            Log.d("YES", event.getName());
                            eventsGenre.add(event);
                        }
                    }
                }

                adapter.setEvents(eventsGenre);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        });
        Log.d("feedFragment", "4");
        return view;
    }
}