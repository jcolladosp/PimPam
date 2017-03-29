package jcollado.pw.pimpam.model;

import java.util.ArrayList;

/**
 * Created by Yuki on 29/03/2017.
 */

public class Serie {

    private String name;
    private int startYear;
    private int endYear;

    private ArrayList<Comic> volumenes;

    public Serie() {

    }

    public Serie(String name, int startYear, int endYear) {
        this.name = name;
        this.startYear = startYear;
        this.endYear = endYear;
        this.volumenes = volumenes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStartYear() {
        return startYear;
    }

    public void setStartYear(int startYear) {
        this.startYear = startYear;
    }

    public int getEndYear() {
        return endYear;
    }

    public void setEndYear(int endYear) {
        this.endYear = endYear;
    }

    public ArrayList<Comic> getVolumenes() {
        return volumenes;
    }

    public void setVolumenes(ArrayList<Comic> volumenes) {
        this.volumenes = volumenes;
    }
}
