package com.example.meet_n_music;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.meet_n_music.model.Event;
import com.example.meet_n_music.repository.Repo;
import com.example.meet_n_music.viewmodel.EventViewModel;

import java.util.ArrayList;


public class EventListFragment extends Fragment {

    private RecyclerView recyclerView;
    private EventListAdapter adapter;
    private EventViewModel eventViewModel;
    private Button syncBtn;

    public EventListFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_event_list, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        eventViewModel = new ViewModelProvider(this).get(EventViewModel.class);
        eventViewModel.init();
        eventViewModel.getEvents().observe(getViewLifecycleOwner(), events -> adapter.notifyDataSetChanged());
        
        adapter = new EventListAdapter(eventViewModel.getEvents().getValue());
        recyclerView.setAdapter(adapter);

        syncBtn = view.findViewById(R.id.syncBtn);
        syncBtn.setOnClickListener(view1 -> Repo.getInstance().getEvents());

        return view;
    }
}