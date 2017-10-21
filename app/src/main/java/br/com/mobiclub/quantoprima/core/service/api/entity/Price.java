package br.com.mobiclub.quantoprima.core.service.api.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 */
public class Price implements Serializable{

    @SerializedName("Size")
    private String size;

    @SerializedName("Price")
    private Double price;

    public Price(String name, Double price) {
        this.size = name;
        this.price = price;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String name) {
        this.size = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

}
