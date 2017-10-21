
package br.com.mobiclub.quantoprima.core.service.api.entity;

import com.google.gson.annotations.SerializedName;

public class PushNotificationGift {

    private static final String GIFT_TYPE = "gift";

    @SerializedName("alert")
    private String alert;

    @SerializedName("title")
    private String title;

    @SerializedName("type")
    private String type;

    @SerializedName("Gift")
    private Gift gift;

    public static String getGiftType() {
        return GIFT_TYPE;
    }

    public String getAlert() {
        return alert;
    }

    public void setAlert(String alert) {
        this.alert = alert;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Gift getGift() {
        return gift;
    }

    public void setGift(Gift gift) {
        this.gift = gift;
    }
}
