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

    public FirebaseModule(){
        database = FirebaseDatabase.getInstance();
    }

    public void setDatabase(Database database){
        DatabaseReference myRef = this.database.getReference("");
        myRef.setValue(database);
    }
}
