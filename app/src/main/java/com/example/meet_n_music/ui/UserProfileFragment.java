package com.example.meet_n_music.ui;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;

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

    Button changePassword;

    ImageView lSpanish;
    ImageView lEnglish;
    ImageView lDanish;

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

        lSpanish = (ImageView) view.findViewById(R.id.spanish);
        lEnglish = (ImageView) view.findViewById(R.id.english);
        lDanish = (ImageView) view.findViewById(R.id.danish);


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

        //Change language to Spanish
        lSpanish.setOnClickListener(l -> {
            Log.d("languages", "Spanish");
            Locale localization = new Locale("es", "ES");
            Locale.setDefault(localization);
            Configuration config = new Configuration();
            config.locale = localization;
            getActivity().getBaseContext().getResources().updateConfiguration(config,
                    getActivity().getBaseContext().getResources().getDisplayMetrics());
        });

        //Change language to English
        lEnglish.setOnClickListener(l -> {
            Locale localization = new Locale("en", "US");
            Locale.setDefault(localization);
            Configuration config = new Configuration();
            config.locale = localization;
            getActivity().getBaseContext().getResources().updateConfiguration(config,
                    getActivity().getBaseContext().getResources().getDisplayMetrics());
        });

        //Change language to Danish
        lDanish.setOnClickListener(l -> {
            Locale localization = new Locale("da", "DK");
            Locale.setDefault(localization);
            Configuration config = new Configuration();
            config.locale = localization;
            getActivity().getBaseContext().getResources().updateConfiguration(config,
                    getActivity().getBaseContext().getResources().getDisplayMetrics());
        });

        return view;
    }
}