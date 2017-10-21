package br.com.mobiclub.quantoprima.core.service.api.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 *
 */
public class Extract {

    @SerializedName("Months")
    private List<ExtractDetails> extractDetails;

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

    public List<ExtractDetails> getExtractDetails() {
        return extractDetails;
    }

    public void setExtractDetails(List<ExtractDetails> extractDetails) {
        this.extractDetails = extractDetails;
    }
}
