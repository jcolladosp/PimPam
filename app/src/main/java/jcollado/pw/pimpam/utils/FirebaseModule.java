package jcollado.pw.pimpam.utils;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;

import jcollado.pw.pimpam.R;

/**
 * Created by Yuki on 14/03/2017.
 */

public class FirebaseModule {

    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private StorageReference storageRef;
    private FirebaseAuth mAuth;
    private DatabaseReference comicReference, serieReference;

    public FirebaseModule(){
        storageRef = FirebaseStorage.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser() != null) {
            comicReference = database.getReference(Functions.getUniqueID()+"/comics");
            serieReference = database.getReference(Functions.getUniqueID()+"/series");
        }
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

    public DatabaseReference getComicReference(){
        return comicReference;
    }

    public DatabaseReference getSerieReference(){
        return serieReference;
    }

    public String uploadBitmap(Bitmap bitmap, String imagename, final BaseFragment fragment){
        fragment.onPreStartConnection(fragment.getString(R.string.loading));

        final String[] downloadURL = new String[1];
        StorageReference ImagesRef = storageRef.child("images/"+imagename);
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
        comicReference = database.getReference(Functions.getUniqueID()+"/comics");
        serieReference = database.getReference(Functions.getUniqueID()+"/series");
        Singleton.getInstance().getDatabase().setConnections();
    }
}
