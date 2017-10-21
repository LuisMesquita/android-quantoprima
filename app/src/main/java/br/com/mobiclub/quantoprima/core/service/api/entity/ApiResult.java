package br.com.mobiclub.quantoprima.core.service.api.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ApiResult implements Serializable {

    @SerializedName("HttpStatus")
    private Integer httpStatus;

    @SerializedName("Message")
    private String message;

    private int code;

    public Integer getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(Integer httpStatus) {
        this.httpStatus = httpStatus;
    }

    public ApiResult withHttpStatus(Integer httpStatus) {
        this.httpStatus = httpStatus;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ApiResult withMessage(String message) {
        this.message = message;
        return this;
    }

    public Boolean isSuccess() {
        return httpStatus == 200 ? true : false;
    }

    public int getCode() {
        return code;
    }
}