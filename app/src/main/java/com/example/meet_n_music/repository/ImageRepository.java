package com.example.meet_n_music.repository;


import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class ImageRepository {
    static private ImageRepository instance;
    static final long THREE_MEGABYTES = 3 * 1024 * 1024;
    static final String TAG = "ImageRepository";

    public static ImageRepository getInstance() {
        if (instance == null) {
            instance = new ImageRepository();
        }
        return instance;
    }

    //path-> events/event_id/image_id
    public MutableLiveData<Boolean> uploadImage(String path, MutableLiveData<Uri> image) {
        MutableLiveData<Boolean> uploadImageState = new MutableLiveData<>();
        Log.d(TAG, "Uploading image with path images/" + path);
        FirebaseStorage.getInstance().getReference().child("images/" + path).putFile(image.getValue()).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.d(TAG, "Image of path images/" + path  + " success on uploading");
                uploadImageState.setValue(true);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Image of path images/" + path + " failed on uploading");
                uploadImageState.setValue(false);
            }
        });
        return uploadImageState;
    }

    public MutableLiveData<Boolean> changeImage(String path, MutableLiveData<Uri> image){
        MutableLiveData<Boolean> changeImageState = new MutableLiveData<>();
        StorageReference storageRef = FirebaseStorage.getInstance().getReference("images/" + path);
        Log.d(TAG, "images/" + path);
        storageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d(TAG, "Image success on deleting");
                MutableLiveData<Boolean> uploadImageState = uploadImage(path, image);
                uploadImageState.observeForever(new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean aBoolean) {
                        if(aBoolean != null && aBoolean){
                            changeImageState.setValue(true);
                        }else {
                            changeImageState.setValue(false);
                        }
                        uploadImageState.removeObserver(this);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Couldn't delete, trying to upload anyway.");
                MutableLiveData<Boolean> uploadImageState = uploadImage(path, image);
                uploadImageState.observeForever(new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean aBoolean) {
                        if(aBoolean != null && aBoolean){
                            changeImageState.setValue(true);
                        }else {
                            changeImageState.setValue(false);
                        }
                        uploadImageState.removeObserver(this);
                    }
                });
            }
        });
        return changeImageState;
    }

    // String imagePath = event.getId() + "/" + event.getId() + ".jpg";
    public MutableLiveData<Uri> getImageUri(String path) {
        MutableLiveData<Uri> uriMutableLiveData = new MutableLiveData<>();
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        Log.d("ImageRepository", "images/" + path);
        storageRef.child("images/" + path).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                uriMutableLiveData.setValue(uri);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                uriMutableLiveData.setValue(null);
            }
        });

        return uriMutableLiveData;
    }
}
