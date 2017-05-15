package jcollado.pw.pimpam.model;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;

/**
 * Created by Yuki on 29/03/2017.
 */

public class Serie {

    private String name;
    private String year;
    private String editorial;

    private ArrayList<String> volumenesName;

    private ArrayList<Comic> volumenes;

    public Serie(){
        volumenes = new ArrayList<>();
        volumenesName = new ArrayList<>();
        name = new String();
    }

    public Serie(String name, String year, String editorial) {
        this.name = name;
        this.year = year;
        this.editorial = editorial;
        volumenes = new ArrayList<>();
        volumenesName = new ArrayList<>();
    }

    public Serie(String name, int startYear, int endYear, ArrayList<String> volumenesName, ArrayList<Comic> volumenes) {
        this.name = name;
        this.year = year;
        this.editorial = editorial;
        this.volumenesName = volumenesName;
        this.volumenes = volumenes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getEditorial() {
        return editorial;
    }

    public void setEditorial(String editorial) {
        this.editorial = editorial;
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
