package br.com.mobiclub.quantoprima.core.service.api.entity;

import java.io.Serializable;

/**
 */
public interface Entity extends Serializable {

    public Integer getId();

    public EstablishmentLocation getLocation();
}

