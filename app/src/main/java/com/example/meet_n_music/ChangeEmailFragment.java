package com.example.meet_n_music;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class ChangeEmailFragment extends Fragment {

    public ChangeEmailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_change_email, container, false);

        EditText currentEmail = view.findViewById(R.id.currentEmail);
        EditText newEmail = view.findViewById(R.id.newEmail);
        EditText confirmEmail = view.findViewById(R.id.confirmEmail);

        Button submitButton = view.findViewById(R.id.submitEmail);
        submitButton.setOnClickListener(l -> {
            //TODO
            //Check info and submit it
        });

        return view;
    }
}