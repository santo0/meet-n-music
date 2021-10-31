package com.example.meet_n_music;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        register = findViewById(R.id.createAccount);
        register.setOnClickListener(l -> {
            setContentView(R.layout.register_layout);
        });
    }


}