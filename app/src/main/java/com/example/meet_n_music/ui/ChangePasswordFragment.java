package com.example.meet_n_music.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.meet_n_music.R;
import com.example.meet_n_music.model.User;
import com.example.meet_n_music.repository.AuthRepository;

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

        AuthRepository authRepository = AuthRepository.getAuthRepository();
        MutableLiveData<User> userMutableLiveData= authRepository.getCurrentUser();
        User user = userMutableLiveData.getValue();

        if (user == null) {
            throw new NullPointerException();
        }

        Button submitButton = view.findViewById(R.id.submitPassword);
        submitButton.setOnClickListener(l -> {
            if (newPassword.toString().equals(confirmPassword.toString())) {
                authRepository.updatePassword(currentPassword.toString(), newPassword.toString()).observe(getViewLifecycleOwner(), new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean aBoolean) {
                        if (aBoolean != null) {
                            if (aBoolean) {
                                Toast.makeText(getContext(), "Password updated successfully", Toast.LENGTH_SHORT).show();
                            }
                            if (!aBoolean) {
                                Toast.makeText(getContext(), "Something was wrong while updating the password", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            throw new NullPointerException();
                        }
                    }
                });
            } else {
                Toast.makeText(getContext(), "Both passwords doesn't match", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}
