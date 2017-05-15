package jcollado.pw.pimpam.model;


/**
 * Created by Yuki on 21/03/2017.
 */

public class FactorySerie {

    public static Serie createSerie(String name, String year, String editorial){
        Serie serie = new Serie(name, year, editorial);
        return serie;
    }

}
