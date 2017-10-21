
package br.com.mobiclub.quantoprima.core.service.api.entity;

import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.List;

public class Survey {

    @SerializedName("HasAppreciation")
    private Boolean hasAppreciation;
    @SerializedName("Id")
    private Integer id;
    @SerializedName("SurveyQuestions")
    private List<SurveyQuestion> surveyQuestions = new ArrayList<SurveyQuestion>();

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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getHasAppreciation() {
        return hasAppreciation;
    }

    public void setHasAppreciation(Boolean hasAppreciation) {
        this.hasAppreciation = hasAppreciation;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<SurveyQuestion> getSurveyQuestions() {
        return surveyQuestions;
    }

    public void setSurveyQuestions(List<SurveyQuestion> surveyQuestions) {
        this.surveyQuestions = surveyQuestions;
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

}
