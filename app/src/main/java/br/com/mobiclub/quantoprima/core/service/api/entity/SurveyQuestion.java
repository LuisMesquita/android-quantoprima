
package br.com.mobiclub.quantoprima.core.service.api.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.List;

public class SurveyQuestion {

    @SerializedName("Id")
    private Integer id;
    @SerializedName("Type")
    private Integer type;
    @SerializedName("Question")
    private String question;
    @SerializedName("SurveyCategory")
    private SurveyCategory surveyCategory;
    @SerializedName("SurveyChoices")
    private List<SurveyChoice> surveyChoices = new ArrayList<SurveyChoice>();
    @SerializedName("IdToAnswer")
    private Integer idToAnswer;

    @Expose
    private int nota;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public SurveyCategory getSurveyCategory() {
        return surveyCategory;
    }

    public void setSurveyCategory(SurveyCategory surveyCategory) {
        this.surveyCategory = surveyCategory;
    }

    public List<SurveyChoice> getSurveyChoices() {
        return surveyChoices;
    }

    public void setSurveyChoices(List<SurveyChoice> surveyChoices) {
        this.surveyChoices = surveyChoices;
    }

    public Integer getIdToAnswer() {
        return idToAnswer;
    }

    public void setIdToAnswer(Integer idToAnswer) {
        this.idToAnswer = idToAnswer;
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

    public int getNota() {
        return nota;
    }

    public void setNota(int nota) {
        this.nota = nota;
    }
}
