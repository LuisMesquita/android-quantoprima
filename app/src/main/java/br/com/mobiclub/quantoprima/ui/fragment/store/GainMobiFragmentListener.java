package br.com.mobiclub.quantoprima.ui.fragment.store;

import br.com.mobiclub.quantoprima.core.service.api.entity.EstablishmentLocation;

/**
 */
public interface GainMobiFragmentListener {

    /**
     * Chamado cliente deseja atualizar a quantidade de mobis do usuario
     * para o location
     *
     * @param location
     * @param mobisType
     * @param buyValue
     */
    public void updateLocationMobis(EstablishmentLocation location,
                                    Integer mobisType,
                                    Integer buyValue);
}