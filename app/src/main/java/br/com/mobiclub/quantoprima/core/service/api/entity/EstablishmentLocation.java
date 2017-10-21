package br.com.mobiclub.quantoprima.core.service.api.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@SuppressWarnings("serial")
public class EstablishmentLocation implements Serializable, Entity {

    @SerializedName("Id")
    private Integer id;

    @SerializedName("EstablishmentId")
    private int establishmentId;

    @SerializedName("Reference")
    private String reference;

    @SerializedName("Distance")
    private Double distance;

    @SerializedName("Lat")
    private double lat;

    @SerializedName("Lon")
    private double lon;

    @SerializedName("Latitude")
    private double latitude;

    @SerializedName("Longitude")
    private double longitude;

    @SerializedName("Score")
    private int score;

    private Establishment establishment;

    @SerializedName("Blocked")
    private Boolean blocked;

    @SerializedName("HasReward")
    private Boolean hasReward;

    @SerializedName("LogoURL")
    private String logoUrl;

    @SerializedName("Survey")
    private int survey;

    @SerializedName("Menu")
    private int menu;

    @SerializedName("Shopping")
    private int shopping;
    public int plusScore= 0;

    public EstablishmentLocation(){}

    @Override
    public String toString() {
        return String.format("EstablishmentLocation[id=%d]", id);
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getReference() {
        return this.reference;
    }

    public Integer getId() {
        return this.id;
    }

    @Override
    public EstablishmentLocation getLocation() {
        return null;
    }

    public double getLat(){
        return this.lat;
    }

    public double getLon() {
        return this.lon;
    }

    public int getEstablishmentId() {
        return establishmentId;
    }

    public void setEstablishmentId(int establishmentId) {
        this.establishmentId = establishmentId;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Boolean getBlocked() {
        return blocked;
    }

    public void setBlocked(Boolean blocked) {
        this.blocked = blocked;
    }

    public Boolean getHasReward() {
        return hasReward;
    }

    public void setHasReward(Boolean hasReward) {
        this.hasReward = hasReward;
    }

    public Establishment getEstablishment() {
        return establishment;
    }

    public void setEstablishment(Establishment establishment) {
        this.establishment = establishment;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public int getSurvey() {
        return survey;
    }

    public void setSurvey(int survey) {
        this.survey = survey;
    }

    public int getMenu() {
        return menu;
    }

    public void setMenu(int menu) {
        this.menu = menu;
    }

    public int getShopping() {
        return shopping;
    }

    public void setShopping(int shopping) {
        this.shopping = shopping;
    }
}
