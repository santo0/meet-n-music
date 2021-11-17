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


public class StartPageFragment extends Fragment {
    public StartPageFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private TextView createAccountListener;
    private EditText email1, psswrd1;
    private ProgressBar progressBar1;
    private FirebaseAuth mAuth;
    private Button loginUser;

    @Override
    public void onResume() {
        super.onResume();
        getActivity().findViewById(R.id.bottom_navigation).setVisibility(View.GONE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_start_page, container, false);

        email1 = (EditText) view.findViewById(R.id.email);
        psswrd1 = (EditText) view.findViewById(R.id.password);
        progressBar1 = (ProgressBar) view.findViewById(R.id.progressBar);
        mAuth = FirebaseAuth.getInstance();
        createAccountListener = (TextView) view.findViewById(R.id.createAccount);
        createAccountListener.setOnClickListener(v ->{
            Navigation.findNavController(v).navigate(R.id.action_startPageFragment_to_registerFragment);
        } );
        loginUser = (Button) view.findViewById(R.id.loginUser);
        loginUser.setOnClickListener(v -> {
            loginUserFunction();
        } );
        return view;
    }

    private void loginUserFunction() {
        String emailString = email1.getText().toString().trim();
        String passwordString = psswrd1.getText().toString().trim();

        if(emailString.isEmpty()){
            email1.setError("Email is required!");
            email1.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(emailString).matches()){
            email1.setError("Please enter a valid email!");
            email1.requestFocus();
            return;
        }

        if(passwordString.isEmpty()){
            psswrd1.setError("Password is required!");
            psswrd1.requestFocus();
            return;
        }

        if(passwordString.length() < 6){
            psswrd1.setError("Password must have at least 6 characters!");
            psswrd1.requestFocus();
            return;
        }

        progressBar1.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(emailString, passwordString).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(getActivity(), "Login passed successfully!", Toast.LENGTH_SHORT).show();
                    progressBar1.setVisibility(View.GONE);
                    Navigation.findNavController(getView()).navigate(R.id.action_startPageFragment_to_feedFragment);
                }else{
                    Toast.makeText(getActivity(), "Login failed! Check your credentials!", Toast.LENGTH_SHORT).show();
                    progressBar1.setVisibility(View.GONE);
                }
            }
        });

    }
}