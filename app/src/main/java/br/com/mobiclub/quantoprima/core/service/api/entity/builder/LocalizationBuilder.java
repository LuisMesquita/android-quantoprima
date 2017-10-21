package br.com.mobiclub.quantoprima.core.service.api.entity.builder;

import br.com.mobiclub.quantoprima.core.service.api.entity.Localization;

public class LocalizationBuilder {
    private double latitude;
    private double longitude;

    public LocalizationBuilder setLatitude(double latitude) {
        this.latitude = latitude;
        return this;
    }

    public LocalizationBuilder setLongitude(double longitude) {
        this.longitude = longitude;
        return this;
    }

    public Localization createLocalization() {
        return new Localization(latitude, longitude);
    }
}