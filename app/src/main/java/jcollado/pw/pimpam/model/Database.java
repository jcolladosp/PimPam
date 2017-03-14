package jcollado.pw.pimpam.model;

import java.util.ArrayList;

/**
 * Created by Yuki on 14/03/2017.
 */

public class Database {

    private ArrayList<Comic> comics;
    private ArrayList<Comic> favorites;

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

    public Database() {
        comics = new ArrayList<>();
        favorites = new ArrayList<>();
    }
}
