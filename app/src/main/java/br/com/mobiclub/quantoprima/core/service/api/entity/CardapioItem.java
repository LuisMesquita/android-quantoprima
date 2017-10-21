package br.com.mobiclub.quantoprima.core.service.api.entity;

import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.List;

import br.com.mobiclub.quantoprima.ui.view.DialogResultData;

public class CardapioItem implements Serializable, Entity, DialogResultData {

    @SerializedName("Image")
    private Image image;

    @SerializedName("Id")
    private Integer id;

    @SerializedName("Name")
    private String name;

    @SerializedName("Description")
    private String description;

    @SerializedName("AdditionalDescription")
    private String additionalDescription;

    @SerializedName("Prices")
    private List<Price> price;

    @SerializedName("LogoURL")
    private String logoURL;

    private EstablishmentLocation location;

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public Integer getId() {
        return id;
    }

    @Override
    public String getFacebookPost() {
        if(location != null)
            return String.format("Um %s da loja %s", name, location.getReference());
        else
            return String.format("Um %s", name);
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getAdditionalDescription() {
        return additionalDescription;
    }

    public void setAdditionalDescription(String additionalDescription) {
        this.additionalDescription = additionalDescription;
    }

    public List<Price> getPrice() {
        return price;
    }

    public void setPrice(List<Price> price) {
        this.price = price;
    }

    public String getLogoURL() {
        return logoURL;
    }

    public void setLogoURL(String logoURL) {
        this.logoURL = logoURL;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public boolean equals(Object other) {
        return EqualsBuilder.reflectionEquals(this, other);
    }

    @Override
    public EstablishmentLocation getLocation() {
        return location;
    }

    public void setLocation(EstablishmentLocation location) {
        this.location = location;
    }
}