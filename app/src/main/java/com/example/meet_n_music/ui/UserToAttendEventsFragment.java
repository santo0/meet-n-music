package com.example.meet_n_music.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.meet_n_music.EventItemAction;
import com.example.meet_n_music.EventListAdapter;
import com.example.meet_n_music.R;
import com.example.meet_n_music.model.Event;
import com.example.meet_n_music.model.User;
import com.example.meet_n_music.repository.EventRepository;
import com.example.meet_n_music.viewmodel.AttendEventsViewModel;
import com.example.meet_n_music.viewmodel.AuthViewModel;
import com.example.meet_n_music.viewmodel.FeedViewModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;


public class UserToAttendEventsFragment extends Fragment {

    private static final String TAG = "UserToAttendEventsFragment";

    private RecyclerView recyclerView;
    private EventListAdapter adapter;
    private AuthViewModel authViewModel;

    public UserToAttendEventsFragment() {
        // Required empty public constructor
    }
    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity)getActivity()).unlockDrawerMenu();
        getActivity().findViewById(R.id.btn_create_event).setVisibility(View.GONE);
        //       ((MainActivity)getActivity()).lockDrawerMenu();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_user_to_attend_events, container, false);

        Log.d("feedFragment", "1");
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
//        feedViewModel.init();

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
                UserToAttendEventsFragmentDirections.ActionUserToAttendEventsFragmentToViewEventFragment action = UserToAttendEventsFragmentDirections.actionUserToAttendEventsFragmentToViewEventFragment();
                action.setEventId(id);
                return action;
            }
        });
        Log.d("feedFragment", "3");


        FirebaseDatabase.getInstance().getReference("Users").child(user.id).child("attendingEventsIds").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @SuppressWarnings("unchecked")
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                ArrayList<String> eventIds = new ArrayList<>();
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    eventIds.add(ds.getValue(String.class));
                }

                ArrayList<Event> attendingEvents = new ArrayList<>();
                for(Event event : EventRepository.getInstance().getAllEvents().getValue()){
                    if(eventIds.contains(event.getId())){
                        attendingEvents.add(event);
                    }
                }

                adapter.setEvents(attendingEvents);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();

            }
        });

        return rootView;
    }

}
