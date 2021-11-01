package com.example.meet_n_music;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class StartPageFragment extends Fragment {
    public StartPageFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private TextView createAccountListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_start_page, container, false);
        createAccountListener = (TextView) view.findViewById(R.id.createAccount);
        createAccountListener.setOnClickListener(v ->{
            Navigation.findNavController(v).navigate(R.id.action_startPageFragment_to_registerFragment);
        } );

        return view;
    }
}