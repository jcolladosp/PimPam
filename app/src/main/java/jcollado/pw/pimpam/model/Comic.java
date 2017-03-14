package jcollado.pw.pimpam.model;

/**
 * Created by colla on 13/03/2017.
 */

public class Comic {

    private String name;
    private String description;
    private String imageURL;

    public Comic(){
        name = "";
        description = "";
        imageURL = "";
    }

    public Comic(String name, String description, String imageURL) {
        this.name = name;
        this.description = description;
        this.imageURL = imageURL;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
