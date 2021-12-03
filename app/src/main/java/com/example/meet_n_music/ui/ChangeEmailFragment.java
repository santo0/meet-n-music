package com.example.meet_n_music.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.meet_n_music.R;
import com.example.meet_n_music.model.User;
import com.example.meet_n_music.repository.AuthRepository;

public class ChangeEmailFragment extends Fragment {

    View view;

    EditText newEmail;
    EditText confirmEmail;
    EditText currentPassword;

    public ChangeEmailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().findViewById(R.id.btn_create_event).setVisibility(View.GONE);
        ((MainActivity)getActivity()).unlockDrawerMenu();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_change_email, container, false);

        newEmail = view.findViewById(R.id.newEmail);
        confirmEmail = view.findViewById(R.id.confirmEmail);
        currentPassword = view.findViewById(R.id.currentPassword);

        AuthRepository authRepository = AuthRepository.getAuthRepository();
        MutableLiveData<User> userMutableLiveData= authRepository.getCurrentUser();
        User user = userMutableLiveData.getValue();

        if (user == null) {
            throw new NullPointerException();
        }

        Button submitButton = view.findViewById(R.id.submitEmail);
        submitButton.setOnClickListener(l -> {
            if (newEmail.getText().toString().equals(confirmEmail.getText().toString())) {
                authRepository.updateEmail(currentPassword.getText().toString(), newEmail.getText().toString()).observe(getViewLifecycleOwner(), new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean aBoolean) {
                        if (aBoolean != null) {
                            if  (aBoolean) {
                                Toast.makeText(getContext(), "Email updated successfully", Toast.LENGTH_SHORT).show();
                                Navigation.findNavController(view).navigate(R.id.action_changeEmailFragment_to_userProfileFragment);
                            }
                            if (!aBoolean) {
                                Toast.makeText(getContext(), "Something was wrong while updating the email", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getContext(), "Something was wrong while updating the email", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } else {
                Toast.makeText(getContext(), "Both emails doesn't match", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}
