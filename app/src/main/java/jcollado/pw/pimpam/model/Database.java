package jcollado.pw.pimpam.model;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


import jcollado.pw.pimpam.R;
import jcollado.pw.pimpam.controller.CollectionFragment;
import jcollado.pw.pimpam.controller.ViewComicFragment;
import jcollado.pw.pimpam.utils.BaseFragment;
import jcollado.pw.pimpam.utils.FirebaseModule;

/**
 * Created by Yuki on 14/03/2017.
 */

public class Database {

    private static final Database ourInstance = new Database();

    private ArrayList<Comic> comics;
    private ArrayList<Serie> series;

    private DatabaseReference comicReference, serieReference;

    public ValueEventListener comicListener, serieListener;
    private CollectionFragment fragment;
    private boolean firstLoad;

    /*
        Constructors
     */

    private Database() {
        firstLoad = true;
        comics = new ArrayList<>();
        series = new ArrayList<>();
        comicListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                comics = new ArrayList<>();
                if(fragment != null && fragment.isAtached){
                    fragment.mostrarCargando();
                }
                for(DataSnapshot data : dataSnapshot.getChildren()) {
                    comics.add(data.getValue(Comic.class));
                }
                fragment.prepareComics();

                if(fragment != null && fragment.isAtached) {
                    fragment.prepareComics();
                    fragment.onConnectionFinished();
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

    /*
        Static getter
     */

    public static Database getInstance(){
        return ourInstance;
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
        this.comics = comics;
    }

    public  void setSeries(ArrayList<Serie> series) {
        this.series = series;
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

    public void comicToDatabase(Comic comic, final BaseFragment fragment){
        comicReference.child(comic.getDisplayName()).setValue(comic,new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError error, DatabaseReference ref) {
                if (error != null) {
                    Log.i("eror","Data could not be deleted. " + error.getMessage());
                } else {
                    fragment.comicUploaded();
                }

            }
        });
    }
    public void deleteComicFromDatabase(Comic comic, final ViewComicFragment view){
        view.mostrarCargando();
        comicReference.child(comic.getDisplayName()).removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError error, DatabaseReference ref) {
                if (error != null) {
                    Log.i("eror","Data could not be deleted. " + error.getMessage());
                } else {
                    view.comicDeleted();
                }

            }
        });
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
        comicReference = FirebaseModule.getInstance().getComicReference();
        serieReference = FirebaseModule.getInstance().getSerieReference();
        comicReference.addValueEventListener(comicListener);
        serieReference.addValueEventListener(serieListener);
    }

    public void setFragment(CollectionFragment fragment){
        this.fragment = fragment;
    }

    public boolean isFirstLoad() {
        return firstLoad;
    }

    public void setFirstLoad(boolean firstLoad) {
        this.firstLoad = firstLoad;
    }
}
