package br.com.mobiclub.quantoprima.core.service.api.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import br.com.mobiclub.quantoprima.ui.view.DialogResultData;

/**
 *
 */
public class Gift implements Serializable, DialogResultData {

    @SerializedName("HttpStatus")
    private Integer httpStatus;

    @SerializedName("Message")
    private String message;

    @SerializedName("Description")
    private String description;

    @SerializedName("Title")
    private String title;

    @SerializedName("Id")
    private Integer id;

    @SerializedName("GiftId")
    private Integer giftId;

    @SerializedName("Expiration")
    private String expiration;

    @SerializedName("Image")
    private Image image;

    @SerializedName("Establishment")
    private Establishment establishment;

    private String expirationString;

    private EstablishmentLocation location;

    private String establishmentName;

    private String establishmentLogoURL;
    private Reward reward;

    public String toString() {
        return String.format("id = %d, title = %s, giftId = %d", id, title, giftId);
    }

    public Integer getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(Integer httpStatus) {
        this.httpStatus = httpStatus;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean isSuccess() {
        return httpStatus == 200;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getId() {
        return id;
    }

    @Override
    public String getFacebookPost() {
        if(establishmentName != null)
            return String.format("Acabei de ganhar um presente da loja %s", establishmentName);
        else if(location != null)
            return String.format("Acabei de ganhar um presente da loja %s", location.getReference());
        else
            return "";
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getGiftId() {
        return giftId;
    }

    public void setGiftId(Integer giftId) {
        this.giftId = giftId;
    }

    public String getExpiration() {
        return expiration;
    }

    public void setExpiration(String expiration) {
        this.expiration = expiration;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public Establishment getEstablishment() {
        return establishment;
    }

    public void setEstablishment(Establishment establishment) {
        this.establishment = establishment;
    }

    public void setExpirationString(String expirationString) {
        this.expirationString = expirationString;
    }

    public String getExpirationString() {
        return expirationString;
    }

    @Override
    public EstablishmentLocation getLocation() {
        return location;
    }

    @Override
    public void setLocation(EstablishmentLocation location) {
        this.location = location;
    }

    public String getEstablishmentName() {
        return establishmentName;
    }

    public void setEstablishmentName(String establishmentName) {
        this.establishmentName = establishmentName;
    }

    public String getEstablishmentLogoURL() {
        return establishmentLogoURL;
    }

    public void setEstablishmentLogoURL(String establishmentLogoURL) {
        this.establishmentLogoURL = establishmentLogoURL;
    }

    public void setReward(Reward reward) {
        this.reward = reward;
    }

    public Reward getReward() {
        return reward;
    }
}
