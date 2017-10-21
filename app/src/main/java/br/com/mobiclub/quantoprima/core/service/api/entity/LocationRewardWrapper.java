package br.com.mobiclub.quantoprima.core.service.api.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 *
 */
public class LocationRewardWrapper {

    @SerializedName("Rewards")
    public List<LocationReward> locationRewards;

    @SerializedName("HttpStatus")
    private Integer httpStatus;

    @SerializedName("Message")
    private String message;

    public Integer getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(Integer httpStatus) {
        this.httpStatus = httpStatus;
    }

    public List<LocationReward> getLocationRewards() {
        return locationRewards;
    }

    public void setLocationRewards(List<LocationReward> locationRewards) {
        this.locationRewards = locationRewards;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
