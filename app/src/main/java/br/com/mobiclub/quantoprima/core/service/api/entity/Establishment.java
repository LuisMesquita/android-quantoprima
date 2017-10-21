package br.com.mobiclub.quantoprima.core.service.api.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("serial")
public class Establishment implements Serializable, Comparable<Establishment> {

    @SerializedName("Id")
    private int id;

    @SerializedName("Name")
    private String name;

    @SerializedName("LogoURL")
    private String logoUrl;

    @SerializedName("Locations")
    private List<EstablishmentLocation> locations;

    public Establishment() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public List<EstablishmentLocation> getLocations(){
        return this.locations;
    }

    public void setLocations(List<EstablishmentLocation> locations){
        this.locations = locations;
    }

    public String getFirstReference() {
        String reference = "";

        if (locations != null && locations.size() > 0) {
            reference = locations.get(0).getReference();
        }

        return reference;
    }

    public String getFormatReferences() {
        String formatReferences = "";

        if (locations != null) {
            if (locations.size() == 2) {
                formatReferences = this.locations.get(0).getReference() + " e outro local";
            } else if (locations.size() > 2) {
                formatReferences = this.locations.get(0).getReference() + " e outros " + (locations.size() - 1) + " locais";
            } else {
                formatReferences = this.getFirstReference();
            }
        }

        return formatReferences;
    }

    public double getMoreProximityLocation() {
        double minDistance = Double.MAX_VALUE;
        for (int i = 0; i < getLocations().size(); i++) {
            EstablishmentLocation l = getLocations().get(i);
            if(l.getDistance() < minDistance) {
                minDistance = l.getDistance();
            }
        }
        return minDistance;
    }

    public boolean isGroup() {
        return getLocations().size() > 1 ? true : false;
    }

    public EstablishmentLocation getLocation() {
        if(locations.size() == 1)
            return locations.get(0);
        return null;
    }

    public void setLocation(EstablishmentLocation location) {
        locations.clear();
        locations.add(location);
    }

    @Override
    public int compareTo(Establishment another) {

        EstablishmentLocation location = this.getLocation();
        EstablishmentLocation location1 = another.getLocation();
        if(location != null && location1 != null) {
            Double distance = location.getDistance();
            Double distance1 = location1.getDistance();

            if(another.getLocations().size() > 1) {
                distance1 = another.getMoreProximityLocation();
            }
            if(this.getLocations().size() > 1) {
                distance = getMoreProximityLocation();
            }

            if(distance != null && distance1 != null) {
                if(distance > distance1) {
                    return 1;
                } else if(distance < distance1) {
                    return -1;
                } else {
                    return 0;
                }
            }
        }
        return 0;
    }
}