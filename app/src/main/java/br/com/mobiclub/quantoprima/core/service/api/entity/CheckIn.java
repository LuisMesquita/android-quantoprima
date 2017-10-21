package br.com.mobiclub.quantoprima.core.service.api.entity;

public class CheckIn {

    private Localization localization;
    private String name;
    private String objectId;

    public Localization getLocalization() {
        return localization;
    }

    public void setLocalization(final Localization localization) {
        this.localization = localization;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(final String objectId) {
        this.objectId = objectId;
    }
}
