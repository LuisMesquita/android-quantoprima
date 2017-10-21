package br.com.mobiclub.quantoprima.core.service.api.entity;

import com.google.gson.annotations.SerializedName;

/**
 *
 */
public class SurveySelectedResponse {

    @SerializedName("Id")
    private Integer id;

    public SurveySelectedResponse(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
