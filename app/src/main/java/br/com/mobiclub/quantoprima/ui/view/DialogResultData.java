package br.com.mobiclub.quantoprima.ui.view;

import java.io.Serializable;

import br.com.mobiclub.quantoprima.core.service.api.entity.EstablishmentLocation;

/**
 */
public interface DialogResultData extends Serializable {

    public EstablishmentLocation getLocation();

    public void setLocation(EstablishmentLocation location);

    public Integer getId();

    public String getFacebookPost();

}
