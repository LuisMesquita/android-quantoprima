package br.com.mobiclub.quantoprima.core.service.api.entity;

import com.google.gson.annotations.SerializedName;

/**
 *
 */
public class LocationReward {

    @SerializedName("Id")
    private int id;

    @SerializedName("Title")
    private String title;

    @SerializedName("Description")
    private String description;

    @SerializedName("Price")
    private String price;

    @SerializedName("FBSuggestedComment")
    private String fbSuggestedComment;

    @SerializedName("Image")
    private Image image;

    private EstablishmentLocation location;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getFbSuggestedComment() {
        return fbSuggestedComment;
    }

    public void setFbSuggestedComment(String fbSuggestedComment) {
        this.fbSuggestedComment = fbSuggestedComment;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public EstablishmentLocation getLocation() {
        return location;
    }

    public void setLocation(EstablishmentLocation location) {
        this.location = location;
    }

    public boolean canBuy() {
        int priceNum = Integer.parseInt(price);
        if(location != null && location.getScore() >= priceNum) {
            return true;
        }
        return false;
    }
}
