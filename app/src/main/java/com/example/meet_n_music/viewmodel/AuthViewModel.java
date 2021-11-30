package com.example.meet_n_music.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.meet_n_music.model.User;
import com.example.meet_n_music.repository.AuthRepository;

public class AuthViewModel extends ViewModel {
    private AuthRepository authRepository;

    public AuthViewModel() {
        authRepository = AuthRepository.getAuthRepository();
    }

    public MutableLiveData<User> getCurrentUser() {
        return authRepository.getCurrentUser();
    }

    public void signIn(String email, String password) {
        authRepository.firebaseSignIn(email, password);
    }

    public MutableLiveData<User> signUp(String username, String interestedIn, String email, String password) {
        return authRepository.firebaseSignUp(username, interestedIn, email, password);
    }

    public void signOut() {
        authRepository.firebaseSignOut();
    }


}


