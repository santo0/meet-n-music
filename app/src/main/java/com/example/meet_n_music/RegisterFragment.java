package com.example.meet_n_music;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

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
    private boolean check = false;

    @Override
    public void onResume() {
        super.onResume();
        getActivity().findViewById(R.id.bottom_navigation).setVisibility(View.GONE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        // Inflate the layout for this fragment
        mAuth = FirebaseAuth.getInstance();
        banner2 = (TextView) view.findViewById(R.id.banner2);
        banner2.setOnClickListener(v ->{
            Navigation.findNavController(v).navigate(R.id.action_registerFragment_to_startPageFragment);
        } );
        username2 = (EditText) view.findViewById(R.id.username2);
        email2 = (EditText) view.findViewById(R.id.email2);
        psswrd2 = (EditText) view.findViewById(R.id.password2);
        rPsswrd2 = (EditText) view.findViewById(R.id.repeatPassword2);
        progressBar2 = (ProgressBar) view.findViewById(R.id.progressBar2);
        registerUser = (Button) view.findViewById(R.id.registerUser);
        registerUser.setOnClickListener(v ->{
            registerUserFunction();
        } );
        return view;
    }

    private void registerUserFunction() {

        String emailString = email2.getText().toString().trim();
        String userString = username2.getText().toString().trim();
        String psswrdString = psswrd2.getText().toString().trim();
        String rPsswrdString = rPsswrd2.getText().toString().trim();

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
        mAuth.createUserWithEmailAndPassword(emailString, psswrdString).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    User user = new User(userString, emailString);
                    FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getActivity(), "User has been registered successfully!", Toast.LENGTH_LONG).show();
                                progressBar2.setVisibility(View.GONE);
                                Navigation.findNavController(getView()).navigate(R.id.action_registerFragment_to_startPageFragment);
                            } else {
                                Toast.makeText(getActivity(), "Failed to register! Try again!", Toast.LENGTH_LONG).show();
                                progressBar2.setVisibility(View.GONE);
                            }
                        }
                    });
                } else {
                    Toast.makeText(getActivity(), "Failed to register! Try again!", Toast.LENGTH_LONG).show();
                    progressBar2.setVisibility(View.GONE);
                }
            }
        });
    }
}