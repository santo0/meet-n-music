package com.example.meet_n_music;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class UserProfileFragment extends Fragment {

    public UserProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);
        TextView username = view.findViewById(R.id.username);
        ImageView profilePhoto = view.findViewById(R.id.profile_photo);
        TextView changePhoto = view.findViewById(R.id.change_photo);
        TextView profileEmail = view.findViewById(R.id.profile_email);
        TextView changeEmail = view.findViewById(R.id.change_email);
        TextView changePassword = view.findViewById(R.id.change_password);
        ImageView lSpanish = view.findViewById(R.id.spanish);
        ImageView lEnglish = view.findViewById(R.id.english);
        ImageView lDanish = view.findViewById(R.id.danish);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user == null) {
            throw new NullPointerException();
        }

        //Show username
        //TODO
        username.setText(user.toString());

        //Show user profile photo
        //TODO
        Glide.with(this)
                .load(user.getPhotoUrl())
                .override(100, 100)
                .into(profilePhoto);

        //Change user profile photo
        changePhoto.setOnClickListener(l -> {
            //TODO
        });

        //Show email
        //TODO
        profileEmail.setText(user.getEmail());

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
            //TODO
        });

        //Change language to English
        lEnglish.setOnClickListener(l -> {
            //TODO
        });

        //Change language to Danish
        lDanish.setOnClickListener(l -> {
            //TODO
        });

        return view;
    }
}