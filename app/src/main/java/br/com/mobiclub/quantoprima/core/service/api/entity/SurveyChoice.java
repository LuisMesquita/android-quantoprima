
package br.com.mobiclub.quantoprima.core.service.api.entity;

import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class SurveyChoice {

    @SerializedName("Id")
    private Integer id;
    @SerializedName("Index")
    private Integer index;
    @SerializedName("Text")
    private String text;
    @SerializedName("IdSurveyQuestion")
    private Integer idSurveyQuestion;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getIdSurveyQuestion() {
        return idSurveyQuestion;
    }

    public void setIdSurveyQuestion(Integer idSurveyQuestion) {
        this.idSurveyQuestion = idSurveyQuestion;
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
