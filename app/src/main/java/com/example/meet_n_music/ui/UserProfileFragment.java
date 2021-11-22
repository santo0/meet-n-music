package com.example.meet_n_music.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.meet_n_music.R;
import com.example.meet_n_music.model.User;
import com.example.meet_n_music.repository.AuthRepository;

public class UserProfileFragment extends Fragment {

    View view;

    TextView username;

    ImageView profilePhoto;
    TextView changePhoto;

    TextView profileEmail;
    TextView changeEmail;

    TextView changePassword;

    ImageView lSpanish;
    ImageView lEnglish;
    ImageView lDanish;

    int SELECT_IMAGE_CODE = 1;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_user_profile, container, false);

        username = view.findViewById(R.id.username);

        profilePhoto = view.findViewById(R.id.profile_photo);
        changePhoto = view.findViewById(R.id.change_photo);

        profileEmail = view.findViewById(R.id.profile_email);
        changeEmail = view.findViewById(R.id.change_email);

        changePassword = view.findViewById(R.id.change_password);

        lSpanish = view.findViewById(R.id.spanish);
        lEnglish = view.findViewById(R.id.english);
        lDanish = view.findViewById(R.id.danish);


        AuthRepository authRepository = AuthRepository.getAuthRepository();
        MutableLiveData<User> userMutableLiveData= authRepository.getCurrentUser();
        User user = userMutableLiveData.getValue();

        if (user == null) {
            throw new NullPointerException();
        }

        //Show username
        //TODO
        username.setText(user.username);

        //Show user profile photo
        //TODO
        /*
        Glide.with(this)
                .load(user.getPhotoUrl())
                .override(90, 90)
                .into(profilePhoto);*/

        //Change user profile photo
        changePhoto.setOnClickListener(l -> {
            //TODO
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Profile photo"), SELECT_IMAGE_CODE);
        });

        //Show email
        //TODO
        profileEmail.setText(user.email);

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            Uri uri = data.getData();
            profilePhoto.setImageURI(uri);
        }
    }
}