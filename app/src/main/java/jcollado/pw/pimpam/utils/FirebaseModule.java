package jcollado.pw.pimpam.utils;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;

import jcollado.pw.pimpam.R;
import jcollado.pw.pimpam.controller.AddComicFragment;
import jcollado.pw.pimpam.model.Database;

/**
 * Created by Yuki on 14/03/2017.
 */

public class FirebaseModule {

    private static final FirebaseModule ourInstance = new FirebaseModule();

    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private StorageReference storageRef;
    private FirebaseAuth mAuth;
    private DatabaseReference comicReference, serieReference;

    private FirebaseModule(){
        storageRef = FirebaseStorage.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser() != null) {
            setReferences();
        }
    }

    public static FirebaseModule getInstance(){
        return ourInstance;
    }

    public FirebaseDatabase getDatabase() {
        return database;
    }

    public StorageReference getStorageRef() {
        return storageRef;
    }

    public FirebaseAuth getmAuth() {
        return mAuth;
    }

    public FirebaseUser getCurrentUser(){
        return mAuth.getCurrentUser();
    }

    public DatabaseReference getComicReference(){
        return comicReference;
    }



    public DatabaseReference getSerieReference(){
        return serieReference;
    }

    public String uploadBitmap(Bitmap bitmap, String imagename, final AddComicFragment fragment){
        fragment.onPreStartConnection(fragment.getString(R.string.loading));

        final String[] downloadURL = new String[1];
        StorageReference ImagesRef = storageRef.child(PrefKeys.IMAGES.toString()).child(UserInfo.getUniqueID()).child(imagename);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        UploadTask uploadTask = ImagesRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.e("fire", exception.toString());
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            @SuppressWarnings("VisibleForTests")
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                downloadURL[0] = taskSnapshot.getDownloadUrl().toString();
                fragment.onConnectionFinished();
                fragment.uploadComic(downloadURL[0]);
                Toast.makeText(fragment.getActivity(), fragment.getString(R.string.comic_addded_succesfull), Toast.LENGTH_SHORT).show();

        }
        });
        return downloadURL[0];
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

    public void setConnectionDatabase(){
        setReferences();
        Database.getInstance().setConnections();
    }
    private void setReferences(){
        comicReference = database.getReference(mAuth.getCurrentUser().getUid()+"/" + PrefKeys.COMICS.toString());
        serieReference = database.getReference(mAuth.getCurrentUser().getUid()+"/" + PrefKeys.SERIES.toString());
    }
}
