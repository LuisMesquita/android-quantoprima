package br.com.mobiclub.quantoprima.ui.activity;

import br.com.mobiclub.quantoprima.core.service.api.entity.EstablishmentLocation;

/**
 */
public interface GainMobiListener {
    public boolean isConnected(boolean force);

    public void onGainedMobi(EstablishmentLocation location);
}
