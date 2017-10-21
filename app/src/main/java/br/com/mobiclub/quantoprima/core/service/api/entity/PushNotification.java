
package br.com.mobiclub.quantoprima.core.service.api.entity;

import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class PushNotification {

    private static final String GIFT_TYPE = "gift";

    @SerializedName("alert")
    private String alert;

    @SerializedName("type")
    private String type;

    @SerializedName("object")
    private GiftId giftId;

    public String getAlert() {
        return alert;
    }

    public void setAlert(String alert) {
        this.alert = alert;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public GiftId getGiftId() {
        return giftId;
    }

    public void setGiftId(GiftId giftId) {
        this.giftId = giftId;
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
    public boolean equals(java.lang.Object other) {
        return EqualsBuilder.reflectionEquals(this, other);
    }

    public boolean isGift() {
        if(type == null)
            return false;
        return type.equals(GIFT_TYPE) ? true : false;
    }
}
