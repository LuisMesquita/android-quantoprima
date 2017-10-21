package br.com.mobiclub.quantoprima.ui.fragment.store;

import java.io.Serializable;

import br.com.mobiclub.quantoprima.domain.Language;

/**
 */
public class LanguageWrapper implements Serializable {

    private Language[] languages;

    public LanguageWrapper(Language[] languages) {
        this.languages = languages;
    }

    public Language[] getLanguages() {
        return languages;
    }

}
