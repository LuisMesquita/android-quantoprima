package br.com.mobiclub.quantoprima.core.service.api.entity;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 *
 */
public class ExtractDetails {

    @SerializedName("DateReference")
    private Date reference;

    @SerializedName("MobiWon")
    private int won;

    @SerializedName("MobiLose")
    private int lose;

    @SerializedName("MobiUsed")
    private int used;

    @SerializedName("MobiWillLose")
    private int willLose;

    public Date getReference() {
        return reference;
    }

    public void setReference(Date reference) {
        this.reference = reference;
    }

    public int getWon() {
        return won;
    }

    public void setWon(int won) {
        this.won = won;
    }

    /**
     * Mobix expirados
     *
     * @return
     */
    public int getLose() {
        return lose;
    }

    public void setLose(int lose) {
        this.lose = lose;
    }

    public int getUsed() {
        return used;
    }

    public void setUsed(int used) {
        this.used = used;
    }

    public int getWillLose() {
        return willLose;
    }

    public void setWillLose(int willLose) {
        this.willLose = willLose;
    }
}
