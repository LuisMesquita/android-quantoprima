
package br.com.mobiclub.quantoprima.core.service.api.entity;

import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.Date;

import br.com.mobiclub.quantoprima.ui.view.DialogResultData;

public class Reward implements Serializable, DialogResultData, Entity, Comparable<Reward> {

    private static final String REWARD_BUY_TYPE = "RewardBuy";
    private static final String REWARD_SHOT_TYPE = "RewardShot";
    private static final String REWARD_GIFT_TYPE = "RewardGift";

    public static final int REWARD_TYPE = 0;
    public static final int REWARD_BUY_EXPIRED_ID = 1;
    public static final Integer REWARD_GIFT_ID = 2;
    public static final int REWARD_SHOT_ID = 3;
    public static final int REWARD_REDEEMED = 4;

    @SerializedName("Id")
    private Integer id;

    @SerializedName("Type")
    private String type;

    @SerializedName("RewardAt")
    private String rewardAt;

    @SerializedName("ExpirationAt")
    private String expirationAt;

    @SerializedName("IdToRescue")
    private Integer idToRescue;

    @SerializedName("IdtoHex")
    private String idToHexa;

    @SerializedName("FBPostSuggested")
    private String fBPostSuggested;

    @SerializedName("Description")
    private String description;

    @SerializedName("Title")
    private String title;

    @SerializedName("EstablishmentName")
    private String establishmentName;

    @SerializedName("EstablishmentLogoURL")
    private String establishmentLogoURL;

    @SerializedName("EstablishmentFBPage")
    private Long establishmentFBPage;

    @SerializedName("LocationId")
    private Integer locationId;

    @SerializedName("Image")
    private Image image;

    @SerializedName("Expired")
    private Boolean expired;

    @SerializedName("Redeemed")
    private Boolean redeemend;

    @SerializedName("Responsed")
    private Boolean responsed;

    /**
     * hora em que foi aceito
     */
    private Date time;

    private EstablishmentLocation location;

    public Integer getId() {
        return id;
    }

    @Override
    public String getFacebookPost() {
        String fbPostSuggested = getFBPostSuggested();
        return fbPostSuggested;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRewardAt() {
        return rewardAt;
    }

    public void setRewardAt(String rewardAt) {
        this.rewardAt = rewardAt;
    }

    public String getExpirationAt() {
        return expirationAt;
    }

    public void setExpirationAt(String expirationAt) {
        this.expirationAt = expirationAt;
    }

    public Integer getIdToRescue() {
        return idToRescue;
    }

    public void setIdToRescue(Integer idToRescue) {
        this.idToRescue = idToRescue;
    }

    public String getIdToHexa() {
        return idToHexa;
    }

    public void setIdToHexa(String idToHexa) {
        this.idToHexa = idToHexa;
    }

    public String getFBPostSuggested() {
        return fBPostSuggested;
    }

    public void setFBPostSuggested(String fBPostSuggested) {
        this.fBPostSuggested = fBPostSuggested;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEstablishmentName() {
        return establishmentName;
    }

    public void setEstablishmentName(String establishmentName) {
        this.establishmentName = establishmentName;
    }

    public String getEstablishmentLogoURL() {
        return establishmentLogoURL;
    }

    public void setEstablishmentLogoURL(String establishmentLogoURL) {
        this.establishmentLogoURL = establishmentLogoURL;
    }

    public Long getEstablishmentFBPage() {
        return establishmentFBPage;
    }

    public void setEstablishmentFBPage(Long establishmentFBPage) {
        this.establishmentFBPage = establishmentFBPage;
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public Boolean getExpired() {
        return expired;
    }

    public void setExpired(Boolean expired) {
        this.expired = expired;
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

    public boolean isRewardBuy() {
        String type = getType();
        return type != null && type.equals(REWARD_BUY_TYPE);
    }

    public boolean isRewardShot() {
        String type = getType();
        return type != null && type.equals(REWARD_SHOT_TYPE);
    }

    public boolean isRewardGift() {
        String type = getType();
        return type != null && type.equals(REWARD_GIFT_TYPE);
    }

    public boolean isRewardExpired() {
        return getExpired() == true ? true : false;
    }
    public boolean isRewardRedeemed() {
        return getRedeemend() == true ? true : false;
    }
    public Boolean getRedeemend() {
        return redeemend;
    }

    public Integer getTypeId() {
        if (isRewardRedeemed()) {
            return REWARD_REDEEMED;
        } else {
            if (isRewardExpired()) {
                return REWARD_BUY_EXPIRED_ID;
            } else {
                return REWARD_TYPE;
            }
        }

        /*
        if (isRewardExpired() && !isRewardRedeemed()) {
            return REWARD_BUY_EXPIRED_ID;
        } else if (isRewardRedeemed()) {
            return REWARD_REDEEMED;
        }else if (isRewardBuy()) {
            return REWARD_TYPE;
        } else if (isRewardGift()) {
            return REWARD_TYPE;
        } else if (isRewardShot()) {
            return REWARD_TYPE;
        }
        return null;
        */
    }

    @Override
    public EstablishmentLocation getLocation() {
        return location;
    }

    @Override
    public void setLocation(EstablishmentLocation location) {
        this.location = location;
    }

    public Boolean getResponsed() {
        return responsed;
    }

    public void setResponsed(Boolean responsed) {
        this.responsed = responsed;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    @Override
    public int compareTo(Reward another) {
        if(another.getId() > this.getId()) {
            return 1;
        } else if(another.getId() < this.getId()) {
            return -1;
        }
        return 0;
    }

}
