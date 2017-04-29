package jcollado.pw.pimpam.model;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import jcollado.pw.pimpam.utils.Singleton;
import jcollado.pw.pimpam.utils.PrefKeys;

/**
 * Created by Yuki on 14/03/2017.
 */

public class Database {

    private static ArrayList<Comic> comics;
    private static ArrayList<Serie> series;

    private DatabaseReference myRef;

    public ValueEventListener eventListenerComics;

    public ValueEventListener eventListenerSeries;

    /*
        Constructors
     */

    public Database() {

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

        series = new ArrayList<>();
        eventListenerSeries = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                series = new ArrayList<>();

                for(DataSnapshot data : dataSnapshot.getChildren())
                    series.add(data.getValue(Serie.class));
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.i("yuki", "Failed to read value. ",  error.toException());
            }
        };
    }

    public Database(ArrayList<Serie> series, ArrayList<Comic> comics) {
        this.series = series;
        this.comics = comics;
    }

    /*
        Getters and Setters
     */
    public static ArrayList<Serie> getSeries() {
        return series;
    }

    public static ArrayList<Comic> getComics() {
        return comics;
    }

    public static void setComics(ArrayList<Comic> comics) {
        Database.comics = comics;
    }

    public static void setSeries(ArrayList<Serie> series) {
        Database.series = series;
    }

    /*
        Logic of the database
     */
    public void serieToDatabase(Serie serie){
        myRef.child(PrefKeys.SERIES.toString()).child(serie.getName()).setValue(serie);
    }

    public void comicToDatabase(Comic comic){
        myRef.child(PrefKeys.COMICS.toString()).child(comic.getName()).setValue(comic);
    }

    public List<String> getAllSeriesName(){
        ArrayList<String> names = new ArrayList<>();
        for(Serie s : series) names.add(s.getName());
        return names;
    }

    public Serie getSerieByName(String serieName){
        for(Serie s : series) {
            if(s.getName().equals(serieName)) return s;
        }
        return null;
    }

    /*
        Method for Connecting to the firebase
     */
    public void setConnections(){
        myRef = Singleton.getInstance().getFirebaseModule().getDatabaseReference();
        myRef.addValueEventListener(eventListenerComics);
        myRef.addValueEventListener(eventListenerSeries);
    }





}
