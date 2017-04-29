package jcollado.pw.pimpam.model;

import com.google.firebase.database.Exclude;

/**
 * Created by colla on 13/03/2017.
 */

public class Comic {

    private String name;
    private String editorial;
    private String imageURL;
    private int volumen;
    private int year;
    private Serie serie;
    private String serieName;

    public Comic(){
        name = "";
        editorial = "";
        imageURL = "";
    }

    public Comic(String name, String editorial, String imageURL, int volumen, int year, Serie serie) {
        this.name = name;
        this.editorial = editorial;
        this.imageURL = imageURL;
        this.volumen = volumen;
        this.year = year;
        this.serie = serie;
        if(this.serie != null)
                this.serieName = serie.getName();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEditorial() {
        return editorial;
    }

    public void setEditorial(String editorial) {
        this.editorial = editorial;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    @Exclude
    public Serie getSerie() {
        return serie;
    }

    public void setSerie(Serie serie) {
        this.serie = serie;
        serieName = serie.getName();
    }

    public String getSerieName() {
        return serieName;
    }

    public void setSerieName(String serieName) {
        this.serieName = serieName;
    }

    public int getYear() {

        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getVolumen() {

        return volumen;
    }

    public void setVolumen(int volumen) {
        this.volumen = volumen;
    }
}
