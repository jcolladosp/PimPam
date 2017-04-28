package jcollado.pw.pimpam.model;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import jcollado.pw.pimpam.controller.Singleton;
import jcollado.pw.pimpam.utils.PrefKeys;

/**
 * Created by Yuki on 14/03/2017.
 */

public class Database {

    private static ArrayList<Comic> comics;
    private static ArrayList<Serie> series;

    private FirebaseDatabase fdatabase;

    private DatabaseReference myRef;

    public ValueEventListener eventListenerComics;

    public Database() {
        Singleton.getInstance().getFirebaseModule().setReference();
        fdatabase = Singleton.getInstance().getFirebaseModule().getDatabase();
        myRef = Singleton.getInstance().getFirebaseModule().getDatabaseReference();

        comics = new ArrayList<>();
        eventListenerComics = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                comics = new ArrayList<>();

                for(DataSnapshot data : dataSnapshot.getChildren())
                    comics.add(data.getValue(Comic.class));
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.i("yuki", "Failed to read value. ",  error.toException());
            }
        };
        myRef.addValueEventListener(eventListenerComics);
    }

    public Database(ArrayList<Serie> series, ArrayList<Comic> comics) {
        this.series = series;
        this.comics = comics;
    }

    public static ArrayList<Serie> getSeries() {
        return series;
    }

    public void setComics(ArrayList<Serie> comics) {
        this.series = comics;
    }


    public void addNewSerie(Serie serie){
        series.add(serie);
        myRef.child("test2").setValue(serie);
    }

    public void addNewComic(Comic comic){

        myRef.child(PrefKeys.COMICS.toString()).setValue(comic);
    }

    public List<String> getAllSeriesName(){
        ArrayList<String> names = new ArrayList<>();
        for(Serie s : series) names.add(s.getName());
        return names;
    }


}
