package br.com.mobiclub.quantoprima.domain;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 */
public class Language implements Serializable {

    @SerializedName("_id")
    private Long id;

    @SerializedName("_language")
    private String language;

    @SerializedName("_languageShort")
    private String languageShort;

    @SerializedName("_enabled")
    private Boolean enabled;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
       this.id = id;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getLanguageShort() {
        return languageShort;
    }

    public void setLanguageShort(String languageShort) {
        this.languageShort = languageShort;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

}
