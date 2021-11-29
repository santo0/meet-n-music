package com.example.meet_n_music.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.meet_n_music.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AuthRepository {
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private MutableLiveData<User> currentUser = new MutableLiveData<>();
    static private AuthRepository authRepository;

    public static AuthRepository getAuthRepository() {
        if (authRepository == null) {
            authRepository = new AuthRepository();
        }
        return authRepository;
    }

    public MutableLiveData<User> getCurrentUser() {
        return currentUser;
    }

    public void firebaseSignOut(){
        firebaseAuth.signOut();
        currentUser = new MutableLiveData<>();
    }

    public void firebaseSignIn(String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(authTask -> {
            if (authTask.isSuccessful()) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser != null) {
                    String uid = firebaseUser.getUid();

                    FirebaseDatabase.getInstance().getReference("Users").child(uid).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            User user = snapshot.getValue(User.class);
                            user.id = uid;
                            currentUser.setValue(user);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
            }
        });
    }

    public MutableLiveData<User> firebaseSignUp(String username, String interestedIn, String email, String password) {
        MutableLiveData<User> userMutableLiveData = new MutableLiveData<>();
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    User user = new User(username, email, interestedIn);
                    FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                userMutableLiveData.setValue(user);
                            }
                        }
                    });
                }
            }
        });
        return userMutableLiveData;
    }

    public MutableLiveData<List<String>> getCurrentUserAttendingEvents(){
        User user = currentUser.getValue();
        MutableLiveData<List<String>> attendingEventsLiveData = new MutableLiveData<>();
        if(currentUser != null){
            FirebaseDatabase.getInstance().getReference("Users").child(user.id).child("attendingEventsIds").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if(task.isSuccessful()){
                        DataSnapshot dataSnapshot = task.getResult();
                        ArrayList<String> eventIds = new ArrayList<>();
                        for(DataSnapshot ds: dataSnapshot.getChildren()){
                            eventIds.add(ds.getValue(String.class));
                        }
                        attendingEventsLiveData.setValue(eventIds);
                    }else{
                        attendingEventsLiveData.setValue(null);
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    attendingEventsLiveData.setValue(null);
                }
            });
        }
        return attendingEventsLiveData;
    }

    public MutableLiveData<Boolean> updateEmail(String currentPassword, String newEmail) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        MutableLiveData<Boolean> completed = new MutableLiveData<>();

        if (user == null || user.getEmail() == null) {
            throw new NullPointerException();
        }

        AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), currentPassword);

        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            user.updateEmail(newEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        completed.setValue(true);
                                    } else {
                                        completed.setValue(false);
                                    }
                                }
                            });
                        }
                    }
                });
        return completed;
    }

    public MutableLiveData<Boolean> updatePassword(String currentPassword, String newPassword) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        MutableLiveData<Boolean> completed = new MutableLiveData<>();

        if (user == null || user.getEmail() == null) {
            throw new NullPointerException();
        }

        AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), currentPassword);

        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            user.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        completed.setValue(true);
                                    } else {
                                        completed.setValue(false);
                                    }
                                }
                            });
                        }
                    }
                });
        return completed;
    }

}
