package jcollado.pw.pimpam.controller;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import jcollado.pw.pimpam.model.Comic;
import jcollado.pw.pimpam.model.Database;
import jcollado.pw.pimpam.model.Factory;

/**
 * Created by Yuki on 14/03/2017.
 */

public class Singleton {

    private static final Singleton ourInstance = new Singleton();

    private  FirebaseModule firebaseModule;

    private  Database database;

    public static Singleton getInstance() {
        return ourInstance;
    }

    private Singleton() {

        firebaseModule = new FirebaseModule();
        database = new Database();
    }

    public  FirebaseModule getFirebaseModule() {
        return firebaseModule;
    }

    public  Database getDatabase(){ return database;}





}
