package jcollado.pw.pimpam.model;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import jcollado.pw.pimpam.utils.Singleton;

/**
 * Created by Yuki on 14/03/2017.
 */

public class Database {

    private static ArrayList<Comic> comics;
    private static ArrayList<Serie> series;

    private DatabaseReference comicReference, serieReference;

    public ValueEventListener comicListener, serieListener;

    /*
        Constructors
     */

    public Database() {

        comics = new ArrayList<>();
        series = new ArrayList<>();
        comicListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                comics = new ArrayList<>();

                for(DataSnapshot data : dataSnapshot.getChildren()) {
                    comics.add(data.getValue(Comic.class));
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.i("yuki", "Failed to read value. ",  error.toException());
            }
        };
        serieListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                series = new ArrayList<>();

                for(DataSnapshot data : dataSnapshot.getChildren()) {
                    series.add(data.getValue(Serie.class));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

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
    public  ArrayList<Serie> getSeries() {
        return series;
    }

    public  ArrayList<Comic> getComics() {
        return comics;
    }

    public  void setComics(ArrayList<Comic> comics) {
        Database.comics = comics;
    }

    public  void setSeries(ArrayList<Serie> series) {
        Database.series = series;
    }

    /*
        Logic of the database
     */
    public void serieToDatabase(Serie serie){
        serieReference.child(serie.getName()).setValue(serie);
    }

    /*
    public void userToDatabase(UserInfo user){
        comicReference.child(PrefKeys.USERINFO.toString()).setValue(user);
    }*/

    public void comicToDatabase(Comic comic){
        comicReference.child(comic.getDisplayName()).setValue(comic);
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
        comicReference = Singleton.getInstance().getFirebaseModule().getComicReference();
        serieReference = Singleton.getInstance().getFirebaseModule().getSerieReference();
        comicReference.addValueEventListener(comicListener);
        serieReference.addValueEventListener(serieListener);
    }





}
