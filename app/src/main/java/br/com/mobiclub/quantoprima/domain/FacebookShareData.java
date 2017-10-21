package br.com.mobiclub.quantoprima.domain;

/**
 * Created by Thales Ferreira on 09/09/2014.
 */
public class FacebookShareData {

    private String picture;

    private String name;

    private String link;

    private String description;

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
