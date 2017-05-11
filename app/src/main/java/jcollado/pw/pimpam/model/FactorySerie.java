package jcollado.pw.pimpam.model;

/**
 * Created by Yuki on 21/03/2017.
 */

public class FactorySerie {

    public static Serie createSerie(String name, int startYear, int endYear){
        Serie serie = new Serie(name, startYear, endYear);
        return serie;
    }

}
