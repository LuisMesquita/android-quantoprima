
package br.com.mobiclub.quantoprima.core.service.api.entity;

import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;

public class EstablishmentNotify implements Serializable{

    @SerializedName("Id")
    private Integer id;

    @SerializedName("LogoUrl")
    private Object logoUrl;

    @SerializedName("Description")
    private Object description;

    @SerializedName("Email")
    private Object email;

    @SerializedName("Name")
    private Object name;

    @SerializedName("Hash")
    private Object hash;

    @SerializedName("Permission")
    private Object permission;

    @SerializedName("Document")
    private Object document;

    @SerializedName("Locations")
    private Object locations;

    @SerializedName("EstablishmentCategory")
    private Object establishmentCategory;

    @SerializedName("Store")
    private Object store;

    @SerializedName("Phone")
    private Object phone;

    @SerializedName("Enabled")
    private Boolean enabled;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Object getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(Object logoUrl) {
        this.logoUrl = logoUrl;
    }

    public Object getDescription() {
        return description;
    }

    public void setDescription(Object description) {
        this.description = description;
    }

    public Object getEmail() {
        return email;
    }

    public void setEmail(Object email) {
        this.email = email;
    }

    public Object getName() {
        return name;
    }

    public void setName(Object name) {
        this.name = name;
    }

    public Object getHash() {
        return hash;
    }

    public void setHash(Object hash) {
        this.hash = hash;
    }

    public Object getPermission() {
        return permission;
    }

    public void setPermission(Object permission) {
        this.permission = permission;
    }

    public Object getDocument() {
        return document;
    }

    public void setDocument(Object document) {
        this.document = document;
    }

    public Object getLocations() {
        return locations;
    }

    public void setLocations(Object locations) {
        this.locations = locations;
    }

    public Object getEstablishmentCategory() {
        return establishmentCategory;
    }

    public void setEstablishmentCategory(Object establishmentCategory) {
        this.establishmentCategory = establishmentCategory;
    }

    public Object getStore() {
        return store;
    }

    public void setStore(Object store) {
        this.store = store;
    }

    public Object getPhone() {
        return phone;
    }

    public void setPhone(Object phone) {
        this.phone = phone;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
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

}
