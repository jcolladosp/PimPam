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

    public String uploadBitmap(Bitmap bitmap, String imagename, final BaseFragment fragment, final BaseActivity activity){
        if(fragment!=null) fragment.onPreStartConnection();

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
                if(fragment!=null){  fragment.onImageUploaded(downloadURL[0]);}
                if (activity!=null){ activity.onImageUploaded(downloadURL[0]);}

        }
        });
        return downloadURL[0];
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
