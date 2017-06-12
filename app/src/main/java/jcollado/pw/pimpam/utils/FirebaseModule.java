package jcollado.pw.pimpam.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

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

    public void uploadImageToFirebase(String url, String imagename, final UploadImageListener listener){

        StorageReference ImagesRef = storageRef.child(PrefKeys.IMAGES.toString()).child(UserInfo.getUniqueID()).child(imagename);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = BitmapFactory.decodeFile(url, options);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = ImagesRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.e("fire", exception.toString());
                listener.onUploadImageError();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            @SuppressWarnings("VisibleForTests")
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                listener.onUploadImageSuccess(taskSnapshot.getDownloadUrl().toString());

        }
        });

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
