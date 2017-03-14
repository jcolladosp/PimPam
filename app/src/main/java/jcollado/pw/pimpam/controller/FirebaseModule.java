package jcollado.pw.pimpam.controller;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import jcollado.pw.pimpam.model.Comic;
import jcollado.pw.pimpam.model.Database;

/**
 * Created by Yuki on 14/03/2017.
 */

public class FirebaseModule {

    private FirebaseDatabase database;
    private String value;
    private ArrayList<String> comics;

    public FirebaseModule(){
        database = FirebaseDatabase.getInstance();
    }

    /*
        Path indicated by firebase database structure.
     */
    public String getConcreteComic(String path){
        DatabaseReference myRef = database.getReference(path);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                value = dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.i("yuki", "Failed to read value. ",  error.toException());
            }
        });
        return value;
    }

    public ArrayList<String> getComics(String path){
        DatabaseReference myRef = database.getReference(path);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                comics = dataSnapshot.getValue(ArrayList.class);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.i("yuki", "Failed to read value. ",  error.toException());
            }
        });
        return comics;
    }


    public void setComic(String path, Comic c){
        DatabaseReference myRef = database.getReference(path);
        myRef.setValue(c);
    }

    public void setDatabase(Database database){
        DatabaseReference myRef = this.database.getReference("");
        myRef.setValue(database);
    }

}
