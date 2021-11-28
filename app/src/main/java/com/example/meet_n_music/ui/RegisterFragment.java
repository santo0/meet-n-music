package com.example.meet_n_music.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.meet_n_music.R;
import com.example.meet_n_music.model.User;
import com.example.meet_n_music.viewmodel.AuthViewModel;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterFragment extends Fragment {

    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private TextView banner2;
    private EditText username2, email2, psswrd2, rPsswrd2;
    private ProgressBar progressBar2;
    private Button registerUser;
    private FirebaseAuth mAuth;
    private Spinner genreUser;

    @Override
    public void onResume() {
        super.onResume();
        getActivity().findViewById(R.id.bottom_navigation).setVisibility(View.GONE);
        getActivity().findViewById(R.id.appbar_top).setVisibility(View.GONE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        // Inflate the layout for this fragment
        mAuth = FirebaseAuth.getInstance();
        banner2 = (TextView) view.findViewById(R.id.banner2);
        banner2.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_registerFragment_to_startPageFragment);
        });
        username2 = (EditText) view.findViewById(R.id.username2);
        email2 = (EditText) view.findViewById(R.id.email2);
        psswrd2 = (EditText) view.findViewById(R.id.password2);
        rPsswrd2 = (EditText) view.findViewById(R.id.repeatPassword2);
        progressBar2 = (ProgressBar) view.findViewById(R.id.progressBar2);
        genreUser = (Spinner) view.findViewById(R.id.genreUser);
        ArrayAdapter<String> genreAdaptor = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.Genres));
        genreAdaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genreUser.setAdapter(genreAdaptor);
        registerUser = (Button) view.findViewById(R.id.registerUser);
        registerUser.setOnClickListener(v -> {
            registerUserFunction();
        });
        return view;
    }

    private void registerUserFunction() {

        String emailString = email2.getText().toString().trim();
        String userString = username2.getText().toString().trim();
        String psswrdString = psswrd2.getText().toString().trim();
        String rPsswrdString = rPsswrd2.getText().toString().trim();
        String genreUserString = genreUser.getSelectedItem().toString().trim();

        if (userString.isEmpty()) {
            username2.setError("Username is required!");
            username2.requestFocus();
            return;
        }
        if (emailString.isEmpty()) {
            email2.setError("Email is required!");
            email2.requestFocus();
            return;
        }
        if (psswrdString.isEmpty()) {
            psswrd2.setError("Password is required!");
            psswrd2.requestFocus();
            return;
        }
        if (rPsswrdString.isEmpty()) {
            rPsswrd2.setError("Please repeat the password!");
            rPsswrd2.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(emailString).matches()) {
            email2.setError("Please provide a valid email!");
            email2.requestFocus();
            return;
        }
        if (!psswrdString.equals(rPsswrdString)) {
            rPsswrd2.setError("Passwords do not match!");
            rPsswrd2.requestFocus();
            return;
        }
        if (psswrdString.length() < 6) {
            psswrd2.setError("Password must have at least 6 characters!");
            psswrd2.requestFocus();
            return;
        }

        progressBar2.setVisibility(View.VISIBLE);

        AuthViewModel authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        authViewModel.signUp(userString, genreUserString, emailString, psswrdString).observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if (user != null) {
                    Toast.makeText(getActivity(), "User has been registered successfully!", Toast.LENGTH_LONG).show();
                    progressBar2.setVisibility(View.GONE);
                    Navigation.findNavController(getView()).navigate(R.id.action_registerFragment_to_startPageFragment);
                } else {
                    Toast.makeText(getActivity(), "Failed to register! Try again!", Toast.LENGTH_LONG).show();
                    progressBar2.setVisibility(View.GONE);
                }
            }
        });
    }
}