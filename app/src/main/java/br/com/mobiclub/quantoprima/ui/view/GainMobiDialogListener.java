package br.com.mobiclub.quantoprima.ui.view;

import br.com.mobiclub.quantoprima.core.service.api.entity.EstablishmentLocation;

/**
 */
public interface GainMobiDialogListener extends DialogListener {
    public void onEvaluate(EstablishmentLocation location);

    public void onShare(DialogResultData data);

    public void onGoToReward();
}
