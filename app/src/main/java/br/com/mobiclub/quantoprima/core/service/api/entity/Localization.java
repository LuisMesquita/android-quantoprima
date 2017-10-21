package br.com.mobiclub.quantoprima.core.service.api.entity;

public class Localization {

    private double latitude;
    private double longitude;

    public Localization(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return String.format("[lat=.2f, long=.2f.]", latitude, longitude);
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(final double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(final double longitude) {
        this.longitude = longitude;
    }
}
