package com.example.meet_n_music.ui;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.meet_n_music.R;
import com.example.meet_n_music.model.User;
import com.example.meet_n_music.repository.AuthRepository;

import java.util.Locale;

public class UserProfileFragment extends Fragment {

    View view;

    TextView username;

    TextView profileEmail;
    Button changeEmail;

    Button logoutBtn;

    Button changePassword;

    public UserProfileFragment() {
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
        getActivity().findViewById(R.id.btn_create_event).setVisibility(View.GONE);
        ((MainActivity)getActivity()).unlockDrawerMenu();
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_user_profile, container, false);

        username = view.findViewById(R.id.username);

        profileEmail = view.findViewById(R.id.profile_email);
        changeEmail = view.findViewById(R.id.change_email);

        changePassword = view.findViewById(R.id.change_password);
/*
        OnBackPressedCallback callback  = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Navigation.findNavController(view).navigate(R.id.action_userProfileFragment_to_feedFragment);

            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getActivity(), callback);
*/
        AuthRepository authRepository = AuthRepository.getAuthRepository();
        MutableLiveData<User> userMutableLiveData = authRepository.getCurrentUser();
        User user = userMutableLiveData.getValue();

        if (user == null) {
            throw new NullPointerException();
        }

        //Show username
        String userToShow = "Username: " + user.username;
        username.setText(userToShow);

        //Show email
        String emailToShow = "Email: " + user.email;
        profileEmail.setText(emailToShow);

        //Change user email
        changeEmail.setOnClickListener(l -> {
            Navigation.findNavController(getView()).navigate(R.id.action_userProfileFragment_to_changeEmailFragment);
        });

        //Change user password
        changePassword.setOnClickListener(l -> {
            Navigation.findNavController(getView()).navigate(R.id.action_userProfileFragment_to_changePasswordFragment);
        });

        logoutBtn = view.findViewById(R.id.logout);
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AuthRepository.getAuthRepository().firebaseSignOut();
                Navigation.findNavController(view).navigate(R.id.action_userProfileFragment_to_startPageFragment);
            }
        });

        return view;
    }
}