package br.com.mobiclub.quantoprima.core.service.api.entity;

import com.google.gson.annotations.SerializedName;

/**
 *
 */
public class ScoreAnswerResponse {

    @SerializedName("ScoreAnswer")
    private Integer scoreAnswer;

    @SerializedName("SurveySelected")
    private SurveySelectedResponse selectedResponse;

    public Integer getScoreAnswer() {
        return scoreAnswer;
    }

    public void setScoreAnswer(Integer scoreAnswer) {
        this.scoreAnswer = scoreAnswer;
    }

    public SurveySelectedResponse getSelectedResponse() {
        return selectedResponse;
    }

    public void setSelectedResponse(SurveySelectedResponse selectedResponse) {
        this.selectedResponse = selectedResponse;
    }
}
