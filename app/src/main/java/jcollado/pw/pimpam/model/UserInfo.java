package jcollado.pw.pimpam.model;

/**
 * Created by colla on 02/05/2017.
 */

public class UserInfo {
    private String name;
    private String mail;
    private String imageURL;

    public UserInfo(String name, String imageURL,String mail) {
        this.name = name;
        this.mail = mail;
        this.imageURL = imageURL;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }
}
