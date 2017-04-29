package jcollado.pw.pimpam.utils;

import jcollado.pw.pimpam.model.Database;

/**
 * Created by Yuki on 14/03/2017.
 */

public class Singleton {

    private final static Singleton ourInstance = new Singleton();

    private FirebaseModule firebaseModule;

    private  Database database;

    public static Singleton getInstance() {
        return ourInstance;
    }

    private Singleton() {
        firebaseModule = new FirebaseModule();
        database = new Database();
    }

    public FirebaseModule getFirebaseModule() {
        return firebaseModule;
    }

    public Database getDatabase(){ return database;}





}
