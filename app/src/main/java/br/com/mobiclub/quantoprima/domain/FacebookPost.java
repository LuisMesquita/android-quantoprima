package br.com.mobiclub.quantoprima.domain;

import android.os.Bundle;

import java.io.Serializable;

import br.com.mobiclub.quantoprima.core.service.api.entity.User;

@SuppressWarnings("serial")
public class FacebookPost implements Serializable {

    private String postId;
    private String link;
    private String picture;
    private String name;
    private String caption;
    private String description;
    private String message;
    private String place;
    private User user;

    public FacebookPost() {
        this.message = "";
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

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

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPlace() {
        return this.place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public Bundle getParameters() {
        Bundle parameteres = new Bundle();

        if (getLink() != null) {
            parameteres.putString("link", this.getLink());
        }

        if (getPicture() != null) {
            parameteres.putString("picture", this.getPicture());
        }

        if (getName() != null) {
            parameteres.putString("name", this.getName());
        }

        if (getCaption() != null) {
            parameteres.putString("caption", this.getCaption());
        }

        if (getDescription() != null) {
            parameteres.putString("description", this.getDescription());
        }

        if (getMessage() != null) {
            parameteres.putString("message", this.getMessage());
        }

        if (getPlace() != null) {
            if (!getPlace().equals("-1")) {
                parameteres.putString("place", this.getPlace());
            }
        }

        return parameteres;
    }
}
