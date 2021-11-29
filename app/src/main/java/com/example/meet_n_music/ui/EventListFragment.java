package com.example.meet_n_music.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.meet_n_music.EventListAdapter;
import com.example.meet_n_music.R;
import com.example.meet_n_music.repository.EventRepository;
import com.example.meet_n_music.viewmodel.FeedViewModel;


public class EventListFragment extends Fragment {

    private RecyclerView recyclerView;
    private EventListAdapter adapter;
    private FeedViewModel feedViewModel;
    private Button syncBtn;

    public EventListFragment() {
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_event_list, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        feedViewModel = new ViewModelProvider(this).get(FeedViewModel.class);
        feedViewModel.init();
        feedViewModel.getEventMutableLiveData().observe(getViewLifecycleOwner(), events -> adapter.notifyDataSetChanged());
        
        adapter = new EventListAdapter(getContext());
        adapter.setEvents(feedViewModel.getEventMutableLiveData().getValue());
        recyclerView.setAdapter(adapter);

        syncBtn = view.findViewById(R.id.syncBtn);
        //syncBtn.setOnClickListener(view1 -> EventRepository.getInstance().getEvents());

        return view;
    }
}