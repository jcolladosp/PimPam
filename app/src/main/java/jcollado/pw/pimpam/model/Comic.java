package jcollado.pw.pimpam.model;

import com.google.firebase.database.Exclude;

/**
 * Created by colla on 13/03/2017.
 */

public class Comic {

    private String name;
    private String editorial;
    private String imageURL;
    private String volumen;
    private String year;
    private Serie serie;
    private String serieName;


    private String displayName;

    public Comic(){
        name = "";
        editorial = "";
        imageURL = "";
    }

    public Comic(String name, String editorial, String imageURL, String volumen, String year, Serie serie) {
        this.name = name;

        this.editorial = editorial;
        this.imageURL = imageURL;
        this.volumen = volumen;
        this.year = year;
        this.serie = serie;
        if(this.serie != null) {

            this.serieName = serie.getName();
            this.displayName = serieName + " " + volumen;
        }
    }
    public String getDisplayName(){
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
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

    public String getYear() {

        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getVolumen() {

        return  volumen;
    }

    public void setVolumen(String volumen) {
        this.volumen = volumen;
    }
}
