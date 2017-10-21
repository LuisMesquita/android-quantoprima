
package br.com.mobiclub.quantoprima.core.service.api.entity;

import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;

public class EstablishmentLocationNotify implements Serializable {

    @SerializedName("EstablishmentId")
    private Integer establishmentId;
    @SerializedName("HasDelivery")
    private Boolean hasDelivery;
    @SerializedName("Id")
    private Integer id;
    @SerializedName("Document")
    private Object document;
    @SerializedName("HasParking")
    private Boolean hasParking;
    @SerializedName("HasLiveMusic")
    private Boolean hasLiveMusic;
    @SerializedName("HasFumodromo")
    private Boolean hasFumodromo;
    @SerializedName("HasManobrista")
    private Boolean hasManobrista;
    @SerializedName("HasAirConditioner")
    private Boolean hasAirConditioner;
    @SerializedName("HasAddapted")
    private Boolean hasAddapted;
    @SerializedName("HasOutsideTables")
    private Boolean hasOutsideTables;
    @SerializedName("Latitude")
    private Float latitude;
    @SerializedName("Longitude")
    private Float longitude;
    @SerializedName("Reference")
    private Object reference;
    @SerializedName("Twitter")
    private Integer twitter;
    @SerializedName("Email")
    private Object email;
    @SerializedName("SuggestedText")
    private Object suggestedText;
    @SerializedName("FBPage")
    private Integer fBPage;
    @SerializedName("Site")
    private Object site;
    @SerializedName("Enabled")
    private Boolean enabled;
    @SerializedName("Payment")
    private Object payment;
    @SerializedName("OpenHours")
    private Object openHours;
    @SerializedName("Permission")
    private Object permission;
    @SerializedName("Phone")
    private Object phone;
    @SerializedName("Store")
    private Object store;
    @SerializedName("Shopping")
    private Object shopping;
    @SerializedName("Descriptions")
    private Object descriptions;
    @SerializedName("Rule")
    private Object rule;

    public Integer getEstablishmentId() {
        return establishmentId;
    }

    public void setEstablishmentId(Integer establishmentId) {
        this.establishmentId = establishmentId;
    }

    public Boolean getHasDelivery() {
        return hasDelivery;
    }

    public void setHasDelivery(Boolean hasDelivery) {
        this.hasDelivery = hasDelivery;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Object getDocument() {
        return document;
    }

    public void setDocument(Object document) {
        this.document = document;
    }

    public Boolean getHasParking() {
        return hasParking;
    }

    public void setHasParking(Boolean hasParking) {
        this.hasParking = hasParking;
    }

    public Boolean getHasLiveMusic() {
        return hasLiveMusic;
    }

    public void setHasLiveMusic(Boolean hasLiveMusic) {
        this.hasLiveMusic = hasLiveMusic;
    }

    public Boolean getHasFumodromo() {
        return hasFumodromo;
    }

    public void setHasFumodromo(Boolean hasFumodromo) {
        this.hasFumodromo = hasFumodromo;
    }

    public Boolean getHasManobrista() {
        return hasManobrista;
    }

    public void setHasManobrista(Boolean hasManobrista) {
        this.hasManobrista = hasManobrista;
    }

    public Boolean getHasAirConditioner() {
        return hasAirConditioner;
    }

    public void setHasAirConditioner(Boolean hasAirConditioner) {
        this.hasAirConditioner = hasAirConditioner;
    }

    public Boolean getHasAddapted() {
        return hasAddapted;
    }

    public void setHasAddapted(Boolean hasAddapted) {
        this.hasAddapted = hasAddapted;
    }

    public Boolean getHasOutsideTables() {
        return hasOutsideTables;
    }

    public void setHasOutsideTables(Boolean hasOutsideTables) {
        this.hasOutsideTables = hasOutsideTables;
    }

    public Float getLatitude() {
        return latitude;
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    public Float getLongitude() {
        return longitude;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }

    public Object getReference() {
        return reference;
    }

    public void setReference(Object reference) {
        this.reference = reference;
    }

    public Integer getTwitter() {
        return twitter;
    }

    public void setTwitter(Integer twitter) {
        this.twitter = twitter;
    }

    public Object getEmail() {
        return email;
    }

    public void setEmail(Object email) {
        this.email = email;
    }

    public Object getSuggestedText() {
        return suggestedText;
    }

    public void setSuggestedText(Object suggestedText) {
        this.suggestedText = suggestedText;
    }

    public Integer getFBPage() {
        return fBPage;
    }

    public void setFBPage(Integer fBPage) {
        this.fBPage = fBPage;
    }

    public Object getSite() {
        return site;
    }

    public void setSite(Object site) {
        this.site = site;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Object getPayment() {
        return payment;
    }

    public void setPayment(Object payment) {
        this.payment = payment;
    }

    public Object getOpenHours() {
        return openHours;
    }

    public void setOpenHours(Object openHours) {
        this.openHours = openHours;
    }

    public Object getPermission() {
        return permission;
    }

    public void setPermission(Object permission) {
        this.permission = permission;
    }

    public Object getPhone() {
        return phone;
    }

    public void setPhone(Object phone) {
        this.phone = phone;
    }

    public Object getStore() {
        return store;
    }

    public void setStore(Object store) {
        this.store = store;
    }

    public Object getShopping() {
        return shopping;
    }

    public void setShopping(Object shopping) {
        this.shopping = shopping;
    }

    public Object getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(Object descriptions) {
        this.descriptions = descriptions;
    }

    public Object getRule() {
        return rule;
    }

    public void setRule(Object rule) {
        this.rule = rule;
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
