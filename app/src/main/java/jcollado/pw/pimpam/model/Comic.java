package jcollado.pw.pimpam.model;

/**
 * Created by colla on 13/03/2017.
 */

public class Comic {

    private String name;
    private String editorial;
    private String imageURL;
    private int volumen;
    private int year;

    public Comic(){
        name = "";
        editorial = "";
        imageURL = "";
    }

    public Comic(String name, String editorial, String imageURL, int volumen, int year) {
        this.name = name;
        this.editorial = editorial;
        this.imageURL = imageURL;
        this.volumen = volumen;
        this.year = year;
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


}
