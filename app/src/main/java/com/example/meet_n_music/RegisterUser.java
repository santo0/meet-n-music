package com.example.meet_n_music;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterUser extends AppCompatActivity implements View.OnClickListener{
    private TextView banner2;
    private EditText username2, email2, psswrd2, rPsswrd2;
    private ProgressBar progressBar2;
    private Button registerUser;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);
        mAuth = FirebaseAuth.getInstance();
        banner2 = (TextView) findViewById(R.id.banner2);
        banner2.setOnClickListener(this);
        username2 = (EditText) findViewById(R.id.username2);
        email2 = (EditText) findViewById(R.id.email2);
        psswrd2 = (EditText) findViewById(R.id.password2);
        rPsswrd2 = (EditText) findViewById(R.id.repeatPassword2);
        progressBar2 = (ProgressBar) findViewById(R.id.progressBar2);
        registerUser = (Button) findViewById(R.id.registerUser);
        registerUser.setOnClickListener(this);
    }


    private void registerUserFunction() {

        String emailString = email2.getText().toString().trim();
        String userString = username2.getText().toString().trim();
        String psswrdString = psswrd2.getText().toString().trim();
        String rPsswrdString = rPsswrd2.getText().toString().trim();

        if (userString.isEmpty()) {
            username2.setError("Username is required!");
            username2.requestFocus();
            return;
        }
        if (emailString.isEmpty()) {
            email2.setError("Email is required!");
            email2.requestFocus();
            return;
        }
        if (psswrdString.isEmpty()) {
            psswrd2.setError("Password is required!");
            psswrd2.requestFocus();
            return;
        }
        if (rPsswrdString.isEmpty()) {
            rPsswrd2.setError("Please repeat the password!");
            rPsswrd2.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(emailString).matches()) {
            email2.setError("Please provide a valid email!");
            email2.requestFocus();
            return;
        }
        if (!psswrdString.equals(rPsswrdString)) {
            rPsswrd2.setError("Passwords do not match!");
            rPsswrd2.requestFocus();
            return;
        }
        if (psswrdString.length() < 6) {
            psswrd2.setError("Password must have at least 6 characters!");
            psswrd2.requestFocus();
            return;
        }

        progressBar2.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(emailString, psswrdString).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    User user = new User(userString, emailString, psswrdString);
                    FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(RegisterUser.this, "User has been registered successfully!", Toast.LENGTH_LONG).show();
                                progressBar2.setVisibility(View.GONE);

                            } else {
                                Toast.makeText(RegisterUser.this, "Failed to register! Try again!", Toast.LENGTH_LONG).show();
                                progressBar2.setVisibility(View.GONE);
                            }
                        }
                    });
                } else {
                    Toast.makeText(RegisterUser.this, "Failed to register! Try again!", Toast.LENGTH_LONG).show();
                    progressBar2.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.banner2:
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.registerUser:
                registerUserFunction();
                break;
        }
    }
}
