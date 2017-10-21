package br.com.mobiclub.quantoprima.core.service.api.entity;


import com.google.gson.annotations.SerializedName;

public class ApiError {
    private int code;
    private String error;

    @SerializedName("Message")
    private String message;

    @SerializedName("HttpStatus")
    private String httpStatus;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(String httpStatus) {
        this.httpStatus = httpStatus;
    }
}
