package br.com.mobiclub.quantoprima.config;


import br.com.mobiclub.quantoprima.config.module.MobiClubServiceModule;
import br.com.mobiclub.quantoprima.config.module.MockMobiClubServiceModule;

/**
 * Troca entre servico com mock ou servico real
 */
public enum ApiLevel {
    MOCK(new MockMobiClubServiceModule()),
    PRODUCTION(new MobiClubServiceModule());

    private final Object module;

    ApiLevel(Object module) {
        this.module = module;
    }

    public Object module() {
        return module;
    }
}
