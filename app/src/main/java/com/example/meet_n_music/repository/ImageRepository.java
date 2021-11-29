package com.example.meet_n_music.repository;


import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class ImageRepository {
    static private ImageRepository instance;


    public static ImageRepository getInstance() {
        if (instance == null) {
            instance = new ImageRepository();
        }
        return instance;
    }

    //path-> events/event_id/image_id
    public void uploadImage(String path, MutableLiveData<Uri> image) {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        Log.d("ImageRepository", "images/" + path);
        storageRef.child("images/" + path).putFile(image.getValue()).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.d("ImageRepository", "Image success");
                image.setValue(image.getValue());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("ImageRepository", "Image failure");
                image.setValue(null);
            }
        });

    }

}
