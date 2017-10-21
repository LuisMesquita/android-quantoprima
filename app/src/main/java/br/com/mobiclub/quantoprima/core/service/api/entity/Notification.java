
package br.com.mobiclub.quantoprima.core.service.api.entity;

import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.Date;

public class Notification implements Serializable, Comparable<Notification> {

    @SerializedName("Id")
    private Integer id;

    @SerializedName("Reference")
    private String reference;

    @SerializedName("Title")
    private String title;

    @SerializedName("Text")
    private String text;

    @SerializedName("Date")
    private Date date;

    @SerializedName("Establishment")
    private EstablishmentNotify establishment;

    @SerializedName("EstablishmentLocation")
    private EstablishmentLocationNotify establishmentLocation;

    @SerializedName("UserId")
    private Integer userId;

    @SerializedName("SendAt")
    private Date sendAt;

    @SerializedName("Type")
    private Integer type;

    @SerializedName("LogoURL")
    private String logoURL;

    @SerializedName("Name")
    private String name;

    @SerializedName("Read")
    private boolean read;

    @SerializedName("ReadAt")
    private Date readAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public EstablishmentNotify getEstablishment() {
        return establishment;
    }

    public void setEstablishment(EstablishmentNotify establishment) {
        this.establishment = establishment;
    }

    public EstablishmentLocationNotify getEstablishmentLocation() {
        return establishmentLocation;
    }

    public void setEstablishmentLocation(EstablishmentLocationNotify establishmentLocation) {
        this.establishmentLocation = establishmentLocation;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Date getSendAt() {
        return sendAt;
    }

    public void setSendAt(Date sendAt) {
        this.sendAt = sendAt;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getLogoURL() {
        return logoURL;
    }

    public void setLogoURL(String logoURL) {
        this.logoURL = logoURL;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public boolean getRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public Date getReadAt() {
        return readAt;
    }

    public void setReadAt(Date readAt) {
        this.readAt = readAt;
    }

    @Override
    public int compareTo(Notification another) {
//        if(this.getSendAt().before(another.getSendAt())) {
//            return 1;
//        } else if(this.getSendAt().after(another.getSendAt())) {
//            return -1;
//        }
        if (this.getRead() && !another.getRead()) {
            return 1;
        } else if (!this.getRead() && another.getRead()) {
            return -1;
        }
        return 0;
    }
}
