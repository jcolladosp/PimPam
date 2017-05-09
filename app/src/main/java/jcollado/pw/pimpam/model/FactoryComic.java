package jcollado.pw.pimpam.model;

/**
 * Created by Yuki on 09/05/2017.
 */

public class FactoryComic {

    public static Comic createComic(String name, String editorial, String imageURL, String volumen, String year, Serie serie){
        Comic comic = new Comic(name,editorial, imageURL,volumen,year,serie);
        return comic;
    }

}

