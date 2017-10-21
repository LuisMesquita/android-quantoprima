package br.com.mobiclub.quantoprima.core.service.api.entity;

import com.google.gson.annotations.SerializedName;

/**
 *
 */
public class Phone {

    @SerializedName("DDI")
    private int DDI;

    @SerializedName("DDD")
    private int DDD;

    @SerializedName("PhoneNumber")
    private String phoneNumber;

    @SerializedName("Type")
    private int type;

    public int getDDI() {
        return DDI;
    }

    public void setDDI(int DDI) {
        this.DDI = DDI;
    }

    public int getDDD() {
        return DDD;
    }

    public void setDDD(int DDD) {
        this.DDD = DDD;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
