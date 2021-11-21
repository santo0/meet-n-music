package com.example.meet_n_music;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.meet_n_music.model.Event;
import com.example.meet_n_music.repository.Repo;
import com.example.meet_n_music.viewmodel.EventViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class FeedFragment extends Fragment {

    private RecyclerView recyclerView;
    private EventListAdapter adapter;
    private EventViewModel eventViewModel;
    private Button syncBtn;
    private User loggedUser;

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




        TextView tvUname = view.findViewById(R.id.tvUname);
        User user = getLoggedUser(new OnGetDataListener() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                loggedUser = user;
                Log.d("debug", loggedUser.username);
                Log.d("debug", loggedUser.email);
                tvUname.setText(user.username);
            }

            @Override
            public void onStart() {
                Log.d("ONSTART", "Started");
            }

            @Override
            public void onFailure() {
                Log.d("onFailure", "Failed");
            }
        });



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


    public User getLoggedUser(OnGetDataListener listener) {
        FirebaseUser fbUser = FirebaseAuth.getInstance().getCurrentUser();
        if (fbUser != null) {
            listener.onStart();
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
            String uid = fbUser.getUid();
            Query query = reference.child("Users/" + uid);
            Log.d("debug", uid);
            ValueEventListener valueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Log.d("debug", snapshot.toString());
                    listener.onSuccess(snapshot);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    listener.onFailure();
                }
            };
            query.addListenerForSingleValueEvent(valueEventListener);
        } else {
            loggedUser = null;
        }
        return loggedUser;
    }

    public interface OnGetDataListener {
        //this is for callbacks
        void onSuccess(DataSnapshot dataSnapshot);
        void onStart();
        void onFailure();
    }
}