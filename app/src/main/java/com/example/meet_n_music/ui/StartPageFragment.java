package com.example.meet_n_music.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.meet_n_music.R;
import com.example.meet_n_music.model.User;
import com.example.meet_n_music.viewmodel.AuthViewModel;
import com.google.firebase.auth.FirebaseAuth;


public class StartPageFragment extends Fragment {
    private static final String TAG = "StartPageFragment";

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
    AuthViewModel authViewModel;
    @Override
    public void onResume() {
        super.onResume();
        getActivity().findViewById(R.id.bottom_navigation).setVisibility(View.GONE);
        getActivity().findViewById(R.id.appbar_top).setVisibility(View.GONE);
        ((MainActivity)getActivity()).lockDrawerMenu();
        authViewModel.signOut();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_start_page, container, false);
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

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
            loginUser.setEnabled(false);
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


        authViewModel.signIn(emailString,passwordString).observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean) {
                    User user = authViewModel.getCurrentUser().getValue();
                    if(user != null){
                        Log.d(TAG, user.username);
                        Toast.makeText(getActivity(), "Login passed successfully!", Toast.LENGTH_SHORT).show();
                        progressBar1.setVisibility(View.GONE);
                        ((TextView)getActivity().findViewById(R.id.header_username)).setText("Logged as : " + user.username);
                        Navigation.findNavController(getView()).navigate(R.id.action_startPageFragment_to_feedFragment);
                    }else{
                        Toast.makeText(getActivity(), "Login failed! Check your credentials!", Toast.LENGTH_SHORT).show();
                        progressBar1.setVisibility(View.GONE);
                    }
                } else {
                    Toast.makeText(getActivity(), "Login failed! Check your credentials!", Toast.LENGTH_SHORT).show();
                    progressBar1.setVisibility(View.GONE);
                }
                loginUser.setEnabled(true);
            }
        });
    }
}