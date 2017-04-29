package jcollado.pw.pimpam.model;

import jcollado.pw.pimpam.utils.Singleton;

/**
 * Created by Yuki on 21/03/2017.
 */

public class Factory {

    public static Serie createSerie(String name, int startYear, int endYear){
        Serie serie = new Serie(name, startYear, endYear);
        return serie;
    }

    public static Comic createComic(String name, String editorial, String imageURL, int volumen, int year, Serie serie){
        Comic comic = new Comic(name,editorial, imageURL,volumen,year,serie);
        return comic;
    }

    public static Database createDatabase(){
        Database database = new Database();
        return database;
    }


}
