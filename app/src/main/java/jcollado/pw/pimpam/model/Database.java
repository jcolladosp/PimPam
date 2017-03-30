package jcollado.pw.pimpam.model;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yuki on 14/03/2017.
 */

public class Database {

    private static ArrayList<Serie> series;
    private ArrayList<Serie> favorites;

    private FirebaseDatabase fdatabase;

    private DatabaseReference myRef, myRef2;

    public ValueEventListener eventListenerComics, eventListenerFavorites;

    public Database() {
        fdatabase = FirebaseDatabase.getInstance();
        myRef = fdatabase.getReference("series");
//        addNewSerie(new Serie("asfda", 1, 1));
        series = new ArrayList<>();
        eventListenerComics = new ValueEventListener() {
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
        myRef.addValueEventListener(eventListenerComics);

    }

    public Database(ArrayList<Serie> series, ArrayList<Serie> favorites) {
        this.series = series;
        this.favorites = favorites;
    }

    public static ArrayList<Serie> getSeries() {
        return series;
    }

    public void setComics(ArrayList<Serie> comics) {
        this.series = comics;
    }

    public ArrayList<Serie> getFavorites() {
        return favorites;
    }

    public void setFavorites(ArrayList<Serie> favorites) {
        this.favorites = favorites;
    }

    public void addNewSerie(Serie serie){
        series.add(serie);
        myRef.child("test").setValue(serie);
    }

    public void addNewComic(Comic comic, Serie serie){
        serie.getVolumenes().add(comic);
        myRef.child("comic").setValue(comic);
    }

    public List<String> getAllSeriesName(){
        ArrayList<String> names = new ArrayList<>();
        for(Serie s : series) names.add(s.getName());
        return names;
    }

    public void addNewFavorite(Serie comic){
        favorites.add(comic);
    }

    public FirebaseDatabase getFdatabase() {
        return fdatabase;
    }

    public void setFdatabase(FirebaseDatabase fdatabase) {
        this.fdatabase = fdatabase;
    }

}
