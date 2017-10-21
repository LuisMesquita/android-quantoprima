package br.com.mobiclub.quantoprima.core.service.api.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 *
 */
public class EstablishmentWrapper {

    @SerializedName("Establishments")
    private List<Establishment> establishments;

    @SerializedName("NewNotifys")
    private Integer newNotifys;

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

    public List<Establishment> getEstablishments() {
        return establishments;
    }

    public Integer getNewNotifys() {
        return newNotifys;
    }

    public void setNewNotifys(Integer newNotifys) {
        this.newNotifys = newNotifys;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
