package br.com.mobiclub.quantoprima.core.service.api.entity;

import com.google.gson.annotations.SerializedName;

/**
 *
 */
public class AppreciationResponse {

    private class LocationResponse {

        @SerializedName("Id")
        private Integer id;

        private LocationResponse(Integer id) {
            this.id = id;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }
    }

    @SerializedName("DisplayInApp")
    private Integer displayInApp;

    @SerializedName("NSI")
    private Integer nsi;

    @SerializedName("NPS")
    private Integer nps;

    @SerializedName("EstablishmentLocation")
    private LocationResponse location;

    @SerializedName("Comment")
    private String comment;

    public AppreciationResponse(Integer displayInApp, Integer nsi, Integer nps, Integer locationId,
                                String comment) {
        this.displayInApp = displayInApp;
        this.nsi = nsi;
        this.nps = nps;
        if(comment == null)
            comment = "";
        this.comment = comment;
        this.location = new LocationResponse(locationId);
    }

    public Integer getDisplayInApp() {
        return displayInApp;
    }

    public void setDisplayInApp(Integer displayInApp) {
        this.displayInApp = displayInApp;
    }

    public Integer getNsi() {
        return nsi;
    }

    public void setNsi(Integer nsi) {
        this.nsi = nsi;
    }

    public Integer getNps() {
        return nps;
    }

    public void setNps(Integer nps) {
        this.nps = nps;
    }

    public LocationResponse getLocation() {
        return location;
    }

    public void setLocation(LocationResponse location) {
        this.location = location;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
