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

public class ChangePasswordFragment extends Fragment {

    View view;

    EditText currentPassword;
    EditText newPassword;
    EditText confirmPassword;

    public ChangePasswordFragment() {
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
        view = inflater.inflate(R.layout.fragment_change_password, container, false);

        currentPassword = view.findViewById(R.id.currentPassword);
        newPassword = view.findViewById(R.id.newPassword);
        confirmPassword = view.findViewById(R.id.confirmPassword);

        Button submitButton = view.findViewById(R.id.submitPassword);
        submitButton.setOnClickListener(l -> {
            //TODO
            if (currentPassword.equals("***User password***")) {
                if (newPassword.equals(confirmPassword)) {
                    //TODO
                    //Change password
                } else {
                    Toast.makeText(getContext(), "Both passwords doesn't match", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getContext(), "The password doesn't match with your current password", Toast.LENGTH_SHORT).show();
            }
        });


        return view;
    }
}