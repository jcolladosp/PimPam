package jcollado.pw.pimpam.controller;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import jcollado.pw.pimpam.model.Comic;

/**
 * Created by Yuki on 14/03/2017.
 */

public class FirebaseModule {

    private FirebaseDatabase database;
    private String value;

    private FirebaseModule(){
        database = FirebaseDatabase.getInstance();
    }

    /*
        Path indicated by firebase database structure.
     */
    public String getData(String path){
        DatabaseReference myRef = database.getReference("path");
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
                System.out.println("Failed to read value. " + error.toException());
            }
        });
        //Clone value
        return value.substring(0);
    }


    public void setComic(String path, Comic c){
        DatabaseReference myRef = database.getReference("message");
        //myRef.setValue(c  something);
    }

    //To be erased
    public void setComic(String path) {
        DatabaseReference myRef = database.getReference("message");
        myRef.setValue("id:1");
    }

}
