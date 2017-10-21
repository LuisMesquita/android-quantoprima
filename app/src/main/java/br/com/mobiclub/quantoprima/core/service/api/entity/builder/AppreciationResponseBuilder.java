package br.com.mobiclub.quantoprima.core.service.api.entity.builder;

import br.com.mobiclub.quantoprima.core.service.api.entity.AppreciationResponse;

public class AppreciationResponseBuilder {
    private Integer displayInApp;
    private Integer nsi;
    private Integer nps;
    private Integer locationId;
    private String comment;

    public AppreciationResponseBuilder setDisplayInApp(Integer displayInApp) {
        this.displayInApp = displayInApp;
        return this;
    }

    public AppreciationResponseBuilder setNsi(Integer nsi) {
        this.nsi = nsi;
        return this;
    }

    public AppreciationResponseBuilder setNps(Integer nps) {
        this.nps = nps;
        return this;
    }

    public AppreciationResponseBuilder setLocationId(Integer locationId) {
        this.locationId = locationId;
        return this;
    }

    public AppreciationResponseBuilder setComment(String comment) {
        this.comment = comment;
        return this;
    }

    public AppreciationResponse getResult() {
        return new AppreciationResponse(displayInApp, nsi, nps, locationId, comment);
    }
}