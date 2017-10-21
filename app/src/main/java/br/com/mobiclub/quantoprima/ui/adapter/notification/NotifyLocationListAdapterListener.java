package br.com.mobiclub.quantoprima.ui.adapter.notification;

import br.com.mobiclub.quantoprima.core.service.api.entity.EstablishmentLocation;

public interface NotifyLocationListAdapterListener {

    public void onSwitched(EstablishmentLocation location, boolean isChecked);

}