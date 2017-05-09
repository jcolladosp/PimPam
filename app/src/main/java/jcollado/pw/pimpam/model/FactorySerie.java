package jcollado.pw.pimpam.model;

import jcollado.pw.pimpam.utils.Singleton;

/**
 * Created by Yuki on 21/03/2017.
 */

public class FactorySerie {

    public static Serie createSerie(String name, int startYear, int endYear){
        Serie serie = new Serie(name, startYear, endYear);
        return serie;
    }

}
