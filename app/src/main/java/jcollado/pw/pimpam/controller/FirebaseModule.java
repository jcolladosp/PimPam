package jcollado.pw.pimpam.controller;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;

import jcollado.pw.pimpam.model.Comic;
import jcollado.pw.pimpam.model.Database;

/**
 * Created by Yuki on 14/03/2017.
 */

public class FirebaseModule {

    private FirebaseDatabase database;
    private StorageReference storageRef;

    public FirebaseModule(){
        database = FirebaseDatabase.getInstance();
        storageRef = FirebaseStorage.getInstance().getReference();
    }

    public void setDatabase(Database database){
        DatabaseReference myRef = this.database.getReference("");
        myRef.setValue(database);
    }

    public void uploadFile(String dir){
        Uri file = Uri.fromFile(new File(dir));
        StorageReference riversRef = storageRef.child("images/" + "1.jpg");

        riversRef.putFile(file)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                    }
                });
    }

    public void downloadFile(String name){
        try {
            File localFile = File.createTempFile("images", "jpg");
            storageRef.getFile(localFile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            // Successfully downloaded data to local file
                            // ...
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle failed download
                    // ...
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
