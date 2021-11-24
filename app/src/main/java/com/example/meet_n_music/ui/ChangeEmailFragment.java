package com.example.meet_n_music.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.meet_n_music.R;

public class ChangeEmailFragment extends Fragment {

    View view;

    EditText currentEmail;
    EditText newEmail;
    EditText confirmEmail;

    public ChangeEmailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity)getActivity()).lockDrawerMenu();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_change_email, container, false);

        currentEmail = view.findViewById(R.id.currentEmail);
        newEmail = view.findViewById(R.id.newEmail);
        confirmEmail = view.findViewById(R.id.confirmEmail);

        Button submitButton = view.findViewById(R.id.submitEmail);
        submitButton.setOnClickListener(l -> {
            //TODO
            if (currentEmail.equals("***User email***")) {
                if (newEmail.equals(confirmEmail)) {
                    //TODO
                    //Change email
                } else {
                    Toast.makeText(getContext(), "Both emails doesn't match", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getContext(), "The email doesn't match with your current email", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}