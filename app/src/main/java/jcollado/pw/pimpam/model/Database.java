package jcollado.pw.pimpam.model;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by Yuki on 14/03/2017.
 */

public class Database {

    private ArrayList<Comic> comics;
    private ArrayList<Comic> favorites;

    private static FirebaseDatabase fdatabase;

    private static DatabaseReference myRef, myRef2;

    public Database() {
        fdatabase = FirebaseDatabase.getInstance();
        myRef = fdatabase.getReference("comics");
        comics = new ArrayList<>();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                comics = new ArrayList<Comic>();
                Log.d("yuki", "running data change");
                for(DataSnapshot data : dataSnapshot.getChildren())
                    comics.add(data.getValue(Comic.class));
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.i("yuki", "Failed to read value. ",  error.toException());
            }
        });

        favorites = new ArrayList<>();
        myRef2 = fdatabase.getReference("favorites");
        myRef2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                favorites = new ArrayList<Comic>();
                for(DataSnapshot data : dataSnapshot.getChildren())
                    favorites.add(data.getValue(Comic.class));
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.i("yuki", "Failed to read value. ",  error.toException());
            }
        });

    }

    public Database(ArrayList<Comic> comics, ArrayList<Comic> favorites) {
        this.comics = comics;
        this.favorites = favorites;
    }

    public ArrayList<Comic> getComics() {
        return comics;
    }

    public void setComics(ArrayList<Comic> comics) {
        this.comics = comics;
    }

    public ArrayList<Comic> getFavorites() {
        return favorites;
    }

    public void setFavorites(ArrayList<Comic> favorites) {
        this.favorites = favorites;
    }

    public void addNewComic(Comic comic){
        comics.add(comic);
        myRef.push().setValue(comic);
    }

    public void addNewFavorite(Comic comic){
        favorites.add(comic);
    }

}
