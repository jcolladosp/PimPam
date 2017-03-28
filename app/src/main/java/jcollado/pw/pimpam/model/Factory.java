package jcollado.pw.pimpam.model;

/**
 * Created by Yuki on 21/03/2017.
 */

public class Factory {

    public static Comic createComic(String name, String description, int URL){
        Comic comic = new Comic(name, description, URL);
        return comic;
    }

    public static Database createDatabase(){
        Database database = new Database();
        return database;
    }


}
