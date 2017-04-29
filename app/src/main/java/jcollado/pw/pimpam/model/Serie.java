package jcollado.pw.pimpam.model;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;

/**
 * Created by Yuki on 29/03/2017.
 */

public class Serie {

    private String name;
    private int startYear;
    private int endYear;

    private ArrayList<String> volumenesName;

    private ArrayList<Comic> volumenes;

    public Serie(){
        volumenes = new ArrayList<>();
        volumenesName = new ArrayList<>();
        name = new String();
    }

    public Serie(String name, int startYear, int endYear) {
        this.name = name;
        this.startYear = startYear;
        this.endYear = endYear;
        volumenes = new ArrayList<>();
        volumenesName = new ArrayList<>();
    }

    public Serie(String name, int startYear, int endYear, ArrayList<String> volumenesName, ArrayList<Comic> volumenes) {
        this.name = name;
        this.startYear = startYear;
        this.endYear = endYear;
        this.volumenesName = volumenesName;
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

    @Exclude
    public ArrayList<Comic> getVolumenes() {
        return volumenes;
    }

    public void setVolumenes(ArrayList<Comic> volumenes) {
        this.volumenes = volumenes;
    }

    public ArrayList<String> getVolumenesName() {
        return volumenesName;
    }

    public void setVolumenesName(ArrayList<String> volumenesName) {
        this.volumenesName = volumenesName;
    }

    public void addComicToSerie(Comic c){
        volumenes.add(c);
        volumenesName.add(c.getName());
    }

    public void deleteComicOfSerie(Comic c){
        volumenes.remove(c);
        volumenes.remove(c.getName());
    }


}
