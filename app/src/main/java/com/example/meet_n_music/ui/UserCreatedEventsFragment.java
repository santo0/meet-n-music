package com.example.meet_n_music.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meet_n_music.EventItemAction;
import com.example.meet_n_music.EventListAdapter;
import com.example.meet_n_music.R;
import com.example.meet_n_music.model.Event;
import com.example.meet_n_music.model.User;
import com.example.meet_n_music.repository.EventRepository;
import com.example.meet_n_music.viewmodel.AuthViewModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class UserCreatedEventsFragment extends Fragment {


    private RecyclerView recyclerView;
    private EventListAdapter adapter;
    private AuthViewModel authViewModel;

    public UserCreatedEventsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).unlockDrawerMenu();
        //        ((MainActivity)getActivity()).lockDrawerMenu();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_user_created_events, container, false);

        Log.d("feedFragment", "1");
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        User user = authViewModel.getCurrentUser().getValue();

        Log.d("feedFragment", "2");
        //showedEvents.setValue(feedViewModel.getEventsWithGenre(user.interestedIn));
        recyclerView = rootView.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));

        adapter = new EventListAdapter(getContext(), new EventItemAction() {
            @Override
            public NavDirections navigate(String id) {
                UserCreatedEventsFragmentDirections.ActionUserCreatedEventsFragmentToViewEventFragment action = UserCreatedEventsFragmentDirections.actionUserCreatedEventsFragmentToViewEventFragment();
                action.setEventId(id);
                return action;
            }
        });
        Log.d("feedFragment", "3");


        FirebaseDatabase.getInstance().getReference("Users").child(user.id).child("ownedEventsIds").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @SuppressWarnings("unchecked")
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                ArrayList<String> eventIds = new ArrayList<>();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    eventIds.add(ds.getValue(String.class));
                }

                ArrayList<Event> ownedEvents = new ArrayList<>();
                for (Event event : EventRepository.getInstance().getAllEvents().getValue()) {
                    if (eventIds.contains(event.getId())) {
                        ownedEvents.add(event);
                    }
                }

                adapter.setEvents(ownedEvents);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();

            }
        });

        return rootView;
    }
}