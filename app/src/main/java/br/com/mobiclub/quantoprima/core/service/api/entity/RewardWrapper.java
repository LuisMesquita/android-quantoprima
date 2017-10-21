
package br.com.mobiclub.quantoprima.core.service.api.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class RewardWrapper {

    @SerializedName("Rewards")

    private List<Reward> rewards = new ArrayList<Reward>();

    @SerializedName("HttpStatus")
    private Integer httpStatus;

    @SerializedName("Message")
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(Integer httpStatus) {
        this.httpStatus = httpStatus;
    }

    public List<Reward> getRewards() {
        return rewards;
    }

    public void setRewards(List<Reward> rewards) {
        this.rewards = rewards;
    }

}
